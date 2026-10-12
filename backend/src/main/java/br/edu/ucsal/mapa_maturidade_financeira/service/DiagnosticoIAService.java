package br.edu.ucsal.mapa_maturidade_financeira.service;

import br.edu.ucsal.mapa_maturidade_financeira.dto.DiagnosticoResponse;
import br.edu.ucsal.mapa_maturidade_financeira.exception.IaIndisponivelException;
import br.edu.ucsal.mapa_maturidade_financeira.exception.LimiteDeUsoException;
import br.edu.ucsal.mapa_maturidade_financeira.model.*;
import br.edu.ucsal.mapa_maturidade_financeira.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DiagnosticoIAService {

    public static final String ORIGEM_IA = "IA";
    public static final String ORIGEM_REGRAS = "REGRAS";

    /** Intervalo mínimo entre dois diagnósticos do mesmo usuário (protege a cota da API). */
    private static final long INTERVALO_MINIMO_SEGUNDOS = 30;

    private final UsuarioRepository usuarioRepository;
    private final PerfilOnboardingRepository perfilRepository;
    private final ProgressoUsuarioRepository progressoRepository;
    private final RespostaUsuarioRepository respostaRepository;
    private final HistoricoSimulacaoRepository historicoSimulacaoRepository;
    private final DiagnosticoIARepository diagnosticoRepository;
    private final GeminiClientService geminiClient;

    public DiagnosticoIAService(
            UsuarioRepository usuarioRepository,
            PerfilOnboardingRepository perfilRepository,
            ProgressoUsuarioRepository progressoRepository,
            RespostaUsuarioRepository respostaRepository,
            HistoricoSimulacaoRepository historicoSimulacaoRepository,
            DiagnosticoIARepository diagnosticoRepository,
            GeminiClientService geminiClient
    ) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.progressoRepository = progressoRepository;
        this.respostaRepository = respostaRepository;
        this.historicoSimulacaoRepository = historicoSimulacaoRepository;
        this.diagnosticoRepository = diagnosticoRepository;
        this.geminiClient = geminiClient;
    }

    /**
     * Gera o diagnóstico. O chamador deve ter obtido a autorização explícita do usuário para o
     * envio de dados ao serviço de IA (validado no controller). O prompt NÃO contém nome nem e-mail.
     * Se a IA estiver indisponível, é gerado um texto de contingência por regras (origem REGRAS).
     */
    public DiagnosticoResponse gerarDiagnostico(String emailUsuario) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);

        PerfilOnboarding perfil = perfilRepository.findByUsuario(usuario)
                .orElseThrow(() -> new IllegalArgumentException("O usuário ainda não completou o questionário inicial de perfil."));

        List<DiagnosticoIA> anteriores = diagnosticoRepository.findByUsuarioOrderByDataGeracaoDesc(usuario);
        if (!anteriores.isEmpty()
                && anteriores.get(0).getDataGeracao().isAfter(LocalDateTime.now().minusSeconds(INTERVALO_MINIMO_SEGUNDOS))) {
            throw new LimiteDeUsoException("Aguarde alguns segundos antes de gerar um novo diagnóstico.");
        }

        List<String> modulosConcluidos = progressoRepository.findByUsuario(usuario).stream()
                .filter(ProgressoUsuario::isConcluido)
                .map(p -> p.getModulo().getTitulo())
                .collect(Collectors.toList());

        List<RespostaUsuario> respostas = respostaRepository.findByUsuario(usuario);
        long totalRespondidas = respostas.size();
        long totalAcertos = respostas.stream().filter(RespostaUsuario::isAcertou).count();

        Set<String> topicosComErro = respostas.stream()
                .filter(r -> !r.isAcertou())
                .map(r -> r.getPergunta().getModulo().getTitulo())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        List<HistoricoSimulacao> simulacoes = historicoSimulacaoRepository.findByUsuarioOrderByDataSimulacaoDesc(usuario);
        double notaMediaSimulador = simulacoes.stream()
                .mapToInt(HistoricoSimulacao::getPontuacao)
                .average()
                .orElse(0.0);

        String texto;
        String origem;
        try {
            String prompt = construirPrompt(perfil, modulosConcluidos, totalRespondidas, totalAcertos,
                    topicosComErro, simulacoes.size(), notaMediaSimulador);
            texto = geminiClient.gerarResposta(prompt);
            origem = ORIGEM_IA;
        } catch (IaIndisponivelException e) {
            texto = construirDiagnosticoPorRegras(perfil, modulosConcluidos, totalRespondidas, totalAcertos,
                    topicosComErro);
            origem = ORIGEM_REGRAS;
        }

        DiagnosticoIA diagnostico = new DiagnosticoIA(usuario, texto, LocalDateTime.now());
        diagnostico.setOrigem(origem);
        diagnosticoRepository.save(diagnostico);

        return new DiagnosticoResponse(diagnostico.getTextoDiagnostico(), diagnostico.getDataGeracao(),
                ORIGEM_IA.equals(origem));
    }

    public DiagnosticoResponse obterUltimoDiagnostico(String emailUsuario) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        List<DiagnosticoIA> lista = diagnosticoRepository.findByUsuarioOrderByDataGeracaoDesc(usuario);

        if (lista.isEmpty()) {
            throw new IllegalArgumentException("Nenhum diagnóstico foi gerado para este usuário ainda.");
        }

        return converter(lista.get(0));
    }

    public List<DiagnosticoResponse> listarHistorico(String emailUsuario) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        return diagnosticoRepository.findByUsuarioOrderByDataGeracaoDesc(usuario).stream()
                .map(this::converter)
                .collect(Collectors.toList());
    }

    private DiagnosticoResponse converter(DiagnosticoIA d) {
        boolean geradoPorIa = !ORIGEM_REGRAS.equals(d.getOrigem()); // nulo (registro antigo) = IA
        return new DiagnosticoResponse(d.getTextoDiagnostico(), d.getDataGeracao(), geradoPorIa);
    }

    private Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
    }

    private String construirPrompt(
            PerfilOnboarding perfil,
            List<String> modulosConcluidos,
            long totalRespondidas,
            long totalAcertos,
            Set<String> topicosComErro,
            int cenariosRealizados,
            double notaMediaSimulador
    ) {
        Locale ptBr = Locale.forLanguageTag("pt-BR");
        StringBuilder sb = new StringBuilder();
        sb.append("Você é um assistente pedagógico de uma plataforma educacional de educação financeira ")
          .append("para adolescentes e adultos. Escreva um diagnóstico de aprendizagem curto (até 180 palavras), ")
          .append("em português do Brasil, com tom acolhedor e linguagem simples.\n\n");

        sb.append("DADOS DO ESTUDANTE (não há nome nem qualquer identificação pessoal):\n");
        sb.append("- Perfil de maturidade: ")
          .append(PerfilOnboardingService.descricaoDoPerfil(perfil.getPerfilCalculado()))
          .append(" (pontuação no questionário inicial: ").append(perfil.getPontuacaoTotal()).append(" de 15)\n");
        sb.append("- Módulos concluídos: ")
          .append(modulosConcluidos.isEmpty() ? "nenhum ainda" : String.join("; ", modulosConcluidos)).append("\n");
        sb.append("- Quiz: ").append(totalAcertos).append(" acertos em ").append(totalRespondidas)
          .append(" questões respondidas\n");
        sb.append("- Tópicos em que houve erro no quiz: ")
          .append(topicosComErro.isEmpty() ? "nenhum registrado" : String.join("; ", topicosComErro)).append("\n");
        sb.append("- Simulador de decisões: ");
        if (cenariosRealizados == 0) {
            sb.append("nenhum cenário realizado ainda\n\n");
        } else {
            sb.append(cenariosRealizados).append(" decisões, nota média ")
              .append(String.format(ptBr, "%.1f", notaMediaSimulador)).append(" de 10\n\n");
        }

        sb.append("REGRAS OBRIGATÓRIAS:\n");
        sb.append("1. Use somente os dados acima. Não invente informações.\n");
        sb.append("2. Aponte o que o estudante já parece dominar e o que deve revisar, e sugira o próximo passo de estudo dentro da trilha da plataforma.\n");
        sb.append("3. NÃO recomende produtos financeiros, ativos, fundos, corretoras ou instituições, nem diga onde aplicar dinheiro. O conteúdo é exclusivamente educacional.\n");
        sb.append("4. Não peça nem mencione dados pessoais.");
        return sb.toString();
    }

    /** Diagnóstico de contingência, usado quando o serviço de IA não responde. Sem IA e sem recomendação de produtos. */
    private String construirDiagnosticoPorRegras(
            PerfilOnboarding perfil,
            List<String> modulosConcluidos,
            long totalRespondidas,
            long totalAcertos,
            Set<String> topicosComErro
    ) {
        StringBuilder sb = new StringBuilder();
        sb.append("Seu perfil de maturidade financeira é \"")
          .append(PerfilOnboardingService.descricaoDoPerfil(perfil.getPerfilCalculado()))
          .append("\" (").append(perfil.getPontuacaoTotal()).append(" de 15 pontos no questionário).\n");

        sb.append("Módulos concluídos: ")
          .append(modulosConcluidos.isEmpty() ? "nenhum ainda" : String.join(", ", modulosConcluidos)).append(".\n");

        if (totalRespondidas > 0) {
            sb.append("No quiz, você acertou ").append(totalAcertos).append(" de ").append(totalRespondidas)
              .append(" questões.\n");
        }
        if (!topicosComErro.isEmpty()) {
            sb.append("Vale revisar: ").append(String.join(", ", topicosComErro)).append(".\n");
        }

        String proximoPasso;
        if (PerfilOnboardingService.PERFIL_INVESTIDOR.equals(perfil.getPerfilCalculado())) {
            proximoPasso = "aprofundar os módulos da camada 3 e praticar no simulador de decisões.";
        } else if (PerfilOnboardingService.PERFIL_EM_DESENVOLVIMENTO.equals(perfil.getPerfilCalculado())) {
            proximoPasso = "consolidar a camada 2 (sistema financeiro) antes de avançar para a camada 3.";
        } else {
            proximoPasso = "concluir os módulos da camada 1 (organização financeira básica) antes de avançar.";
        }
        sb.append("Próximo passo sugerido: ").append(proximoPasso).append("\n");
        sb.append("Este conteúdo é educacional e não constitui recomendação de investimento.");
        return sb.toString();
    }
}

package br.edu.ucsal.mapa_maturidade_financeira.service;

import br.edu.ucsal.mapa_maturidade_financeira.dto.CenarioSimulacaoDTO;
import br.edu.ucsal.mapa_maturidade_financeira.dto.ExecutarSimulacaoRequest;
import br.edu.ucsal.mapa_maturidade_financeira.dto.OpcaoSimulacaoDTO;
import br.edu.ucsal.mapa_maturidade_financeira.dto.ResultadoSimulacaoResponse;
import br.edu.ucsal.mapa_maturidade_financeira.model.CenarioSimulacao;
import br.edu.ucsal.mapa_maturidade_financeira.model.HistoricoSimulacao;
import br.edu.ucsal.mapa_maturidade_financeira.model.OpcaoSimulacao;
import br.edu.ucsal.mapa_maturidade_financeira.model.Usuario;
import br.edu.ucsal.mapa_maturidade_financeira.repository.CenarioSimulacaoRepository;
import br.edu.ucsal.mapa_maturidade_financeira.repository.HistoricoSimulacaoRepository;
import br.edu.ucsal.mapa_maturidade_financeira.repository.OpcaoSimulacaoRepository;
import br.edu.ucsal.mapa_maturidade_financeira.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SimuladorService {

    private final CenarioSimulacaoRepository cenarioRepository;
    private final OpcaoSimulacaoRepository opcaoRepository;
    private final HistoricoSimulacaoRepository historicoRepository;
    private final UsuarioRepository usuarioRepository;

    public SimuladorService(
            CenarioSimulacaoRepository cenarioRepository,
            OpcaoSimulacaoRepository opcaoRepository,
            HistoricoSimulacaoRepository historicoRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.cenarioRepository = cenarioRepository;
        this.opcaoRepository = opcaoRepository;
        this.historicoRepository = historicoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<CenarioSimulacaoDTO> listarCenarios() {
        return cenarioRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public CenarioSimulacaoDTO buscarCenarioPorId(Long id) {
        CenarioSimulacao cenario = cenarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cenário de simulação não encontrado."));
        return converterParaDTO(cenario);
    }

    @Transactional
    public ResultadoSimulacaoResponse executarSimulacao(String emailUsuario, ExecutarSimulacaoRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        CenarioSimulacao cenario = cenarioRepository.findById(request.getCenarioId())
                .orElseThrow(() -> new IllegalArgumentException("Cenário de simulação não encontrado."));

        OpcaoSimulacao opcao = opcaoRepository.findById(request.getOpcaoId())
                .orElseThrow(() -> new IllegalArgumentException("Opção de decisão não encontrada."));

        if (!opcao.getCenario().getId().equals(cenario.getId())) {
            throw new IllegalArgumentException("A opção informada não pertence ao cenário selecionado.");
        }

        Double saldoInicial = cenario.getSaldoInicial();
        Double impacto = opcao.getImpactoFinanceiro();
        Double saldoFinal = saldoInicial + impacto;
        LocalDateTime agora = LocalDateTime.now();

        HistoricoSimulacao historico = new HistoricoSimulacao(
                usuario,
                cenario,
                opcao,
                saldoInicial,
                impacto,
                saldoFinal,
                opcao.getPontuacao(),
                opcao.getFeedbackEducativo(),
                agora
        );

        HistoricoSimulacao salvo = historicoRepository.save(historico);

        return new ResultadoSimulacaoResponse(
                salvo.getId(),
                cenario.getTitulo(),
                opcao.getDescricao(),
                saldoInicial,
                impacto,
                saldoFinal,
                opcao.getPontuacao(),
                opcao.getFeedbackEducativo(),
                agora
        );
    }

    public List<ResultadoSimulacaoResponse> listarHistoricoDoUsuario(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        return historicoRepository.findByUsuarioOrderByDataSimulacaoDesc(usuario).stream()
                .map(h -> new ResultadoSimulacaoResponse(
                        h.getId(),
                        h.getCenario().getTitulo(),
                        h.getOpcaoEscolhida().getDescricao(),
                        h.getSaldoInicial(),
                        h.getImpactoFinanceiro(),
                        h.getSaldoFinal(),
                        h.getPontuacao(),
                        h.getFeedbackRecebido(),
                        h.getDataSimulacao()
                ))
                .collect(Collectors.toList());
    }

    private CenarioSimulacaoDTO converterParaDTO(CenarioSimulacao cenario) {
        List<OpcaoSimulacaoDTO> opcoes = opcaoRepository.findByCenario(cenario).stream()
                .map(o -> new OpcaoSimulacaoDTO(o.getId(), o.getDescricao(), o.getImpactoFinanceiro()))
                .collect(Collectors.toList());

        return new CenarioSimulacaoDTO(
                cenario.getId(),
                cenario.getTitulo(),
                cenario.getDescricao(),
                cenario.getSaldoInicial(),
                cenario.getCategoria(),
                opcoes
        );
    }
}

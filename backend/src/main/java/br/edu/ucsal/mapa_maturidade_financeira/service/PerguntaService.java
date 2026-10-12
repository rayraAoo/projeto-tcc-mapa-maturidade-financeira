package br.edu.ucsal.mapa_maturidade_financeira.service;

import br.edu.ucsal.mapa_maturidade_financeira.dto.ResultadoQuizResponse;
import br.edu.ucsal.mapa_maturidade_financeira.model.Modulo;
import br.edu.ucsal.mapa_maturidade_financeira.model.Pergunta;
import br.edu.ucsal.mapa_maturidade_financeira.model.RespostaUsuario;
import br.edu.ucsal.mapa_maturidade_financeira.model.Usuario;
import br.edu.ucsal.mapa_maturidade_financeira.repository.ModuloRepository;
import br.edu.ucsal.mapa_maturidade_financeira.repository.PerguntaRepository;
import br.edu.ucsal.mapa_maturidade_financeira.repository.RespostaUsuarioRepository;
import br.edu.ucsal.mapa_maturidade_financeira.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerguntaService {

    private final PerguntaRepository perguntaRepository;
    private final ModuloRepository moduloRepository;
    private final RespostaUsuarioRepository respostaUsuarioRepository;
    private final UsuarioRepository usuarioRepository;

    public PerguntaService(
            PerguntaRepository perguntaRepository,
            ModuloRepository moduloRepository,
            RespostaUsuarioRepository respostaUsuarioRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.perguntaRepository = perguntaRepository;
        this.moduloRepository = moduloRepository;
        this.respostaUsuarioRepository = respostaUsuarioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Pergunta> listarPerguntasDoModulo(Long moduloId) {
        Modulo modulo = moduloRepository.findById(moduloId)
                .orElseThrow(() -> new IllegalArgumentException("Módulo não encontrado."));

        return perguntaRepository.findByModulo(modulo);
    }

    /**
     * Corrige a resposta e guarda apenas o resultado MAIS RECENTE de cada pergunta por usuário.
     * Assim, repetir a mesma pergunta não infla as estatísticas usadas no progresso e no diagnóstico.
     */
    public ResultadoQuizResponse corrigirResposta(String emailUsuario, Long perguntaId, String alternativaEscolhida) {

        if (alternativaEscolhida == null || !alternativaEscolhida.matches("(?i)[A-D]")) {
            throw new IllegalArgumentException("Responda com uma das alternativas: A, B, C ou D.");
        }

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        Pergunta pergunta = perguntaRepository.findById(perguntaId)
                .orElseThrow(() -> new IllegalArgumentException("Pergunta não encontrada."));

        boolean correta = pergunta.getAlternativaCorreta().equalsIgnoreCase(alternativaEscolhida);

        RespostaUsuario resposta = respostaUsuarioRepository
                .findFirstByUsuarioAndPerguntaOrderByIdDesc(usuario, pergunta)
                .orElse(new RespostaUsuario(usuario, pergunta, correta));
        resposta.setAcertou(correta);
        respostaUsuarioRepository.save(resposta);

        return new ResultadoQuizResponse(correta, pergunta.getAlternativaCorreta(), pergunta.getExplicacao());
    }
}

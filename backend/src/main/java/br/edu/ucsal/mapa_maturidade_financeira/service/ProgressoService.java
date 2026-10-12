package br.edu.ucsal.mapa_maturidade_financeira.service;

import br.edu.ucsal.mapa_maturidade_financeira.dto.ProgressoResponse;
import br.edu.ucsal.mapa_maturidade_financeira.model.HistoricoSimulacao;
import br.edu.ucsal.mapa_maturidade_financeira.model.Modulo;
import br.edu.ucsal.mapa_maturidade_financeira.model.ProgressoUsuario;
import br.edu.ucsal.mapa_maturidade_financeira.model.RespostaUsuario;
import br.edu.ucsal.mapa_maturidade_financeira.model.Usuario;
import br.edu.ucsal.mapa_maturidade_financeira.repository.HistoricoSimulacaoRepository;
import br.edu.ucsal.mapa_maturidade_financeira.repository.ProgressoUsuarioRepository;
import br.edu.ucsal.mapa_maturidade_financeira.repository.RespostaUsuarioRepository;
import br.edu.ucsal.mapa_maturidade_financeira.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Progresso e XP do usuário (RF11).
 *
 * O XP recompensa APRENDIZADO, nunca tempo de uso nem frequência de acesso:
 *  - 20 XP por módulo concluído da trilha;
 *  - 10 XP por pergunta de quiz acertada (conta uma vez por pergunta);
 *  - a melhor nota (0 a 10) obtida em cada cenário do simulador.
 */
@Service
public class ProgressoService {

    public static final int XP_POR_MODULO = 20;
    public static final int XP_POR_ACERTO = 10;

    private final UsuarioRepository usuarioRepository;
    private final ModuloService moduloService;
    private final ProgressoUsuarioRepository progressoRepository;
    private final RespostaUsuarioRepository respostaRepository;
    private final HistoricoSimulacaoRepository historicoSimulacaoRepository;

    public ProgressoService(
            UsuarioRepository usuarioRepository,
            ModuloService moduloService,
            ProgressoUsuarioRepository progressoRepository,
            RespostaUsuarioRepository respostaRepository,
            HistoricoSimulacaoRepository historicoSimulacaoRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.moduloService = moduloService;
        this.progressoRepository = progressoRepository;
        this.respostaRepository = respostaRepository;
        this.historicoSimulacaoRepository = historicoSimulacaoRepository;
    }

    @Transactional(readOnly = true)
    public ProgressoResponse obterProgresso(String emailUsuario) {

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        List<Modulo> trilha = moduloService.listarTrilhaDoUsuario(emailUsuario);
        Set<Long> idsDaTrilha = trilha.stream().map(Modulo::getId).collect(Collectors.toSet());

        int modulosConcluidos = (int) progressoRepository.findByUsuario(usuario).stream()
                .filter(ProgressoUsuario::isConcluido)
                .filter(p -> idsDaTrilha.contains(p.getModulo().getId()))
                .count();

        double percentual = trilha.isEmpty() ? 0.0 : (modulosConcluidos * 100.0) / trilha.size();

        int acertos = (int) respostaRepository.findByUsuario(usuario).stream()
                .filter(RespostaUsuario::isAcertou)
                .map(r -> r.getPergunta().getId())
                .distinct()
                .count();

        // Melhor nota por cenário: repetir o mesmo cenário não gera XP extra.
        Map<Long, Integer> melhorNotaPorCenario = new HashMap<>();
        for (HistoricoSimulacao h : historicoSimulacaoRepository.findByUsuarioOrderByDataSimulacaoDesc(usuario)) {
            melhorNotaPorCenario.merge(h.getCenario().getId(), h.getPontuacao(), Math::max);
        }
        int xpSimulador = melhorNotaPorCenario.values().stream().mapToInt(Integer::intValue).sum();

        int xp = modulosConcluidos * XP_POR_MODULO + acertos * XP_POR_ACERTO + xpSimulador;

        return new ProgressoResponse(percentual, modulosConcluidos, trilha.size(), xp);
    }
}

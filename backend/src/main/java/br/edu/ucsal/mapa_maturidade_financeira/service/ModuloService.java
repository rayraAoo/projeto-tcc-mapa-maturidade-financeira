package br.edu.ucsal.mapa_maturidade_financeira.service;

import br.edu.ucsal.mapa_maturidade_financeira.model.Modulo;
import br.edu.ucsal.mapa_maturidade_financeira.model.PerfilOnboarding;
import br.edu.ucsal.mapa_maturidade_financeira.model.ProgressoUsuario;
import br.edu.ucsal.mapa_maturidade_financeira.model.Usuario;
import br.edu.ucsal.mapa_maturidade_financeira.repository.ModuloRepository;
import br.edu.ucsal.mapa_maturidade_financeira.repository.PerfilOnboardingRepository;
import br.edu.ucsal.mapa_maturidade_financeira.repository.ProgressoUsuarioRepository;
import br.edu.ucsal.mapa_maturidade_financeira.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ModuloService {

    /** Base financeira (orçamento + reserva + dívida) igual ou abaixo disso é considerada fraca. */
    public static final int LIMITE_BASE_FRACA = 3;

    private final ModuloRepository moduloRepository;
    private final ProgressoUsuarioRepository progressoRepository;
    private final PerfilOnboardingRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;

    public ModuloService(
            ModuloRepository moduloRepository,
            ProgressoUsuarioRepository progressoRepository,
            PerfilOnboardingRepository perfilRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.moduloRepository = moduloRepository;
        this.progressoRepository = progressoRepository;
        this.perfilRepository = perfilRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Modulo> listarTrilhaDoUsuario(String emailUsuario) {
        return montarTrilha(buscarUsuario(emailUsuario));
    }

    public void marcarComoConcluido(String emailUsuario, Long moduloId) {

        Usuario usuario = buscarUsuario(emailUsuario);

        Modulo modulo = moduloRepository.findById(moduloId)
                .orElseThrow(() -> new IllegalArgumentException("Módulo não encontrado."));

        boolean naTrilha = montarTrilha(usuario).stream()
                .anyMatch(m -> m.getId().equals(modulo.getId()));
        if (!naTrilha) {
            throw new IllegalArgumentException("Este módulo não faz parte da sua trilha.");
        }

        ProgressoUsuario progresso = progressoRepository.findByUsuarioAndModulo(usuario, modulo)
                .orElse(new ProgressoUsuario(usuario, modulo, false));

        progresso.setConcluido(true);
        progressoRepository.save(progresso);
    }

    /** Percentual de módulos concluídos DENTRO da trilha do usuário (nunca passa de 100%). */
    public double calcularPercentualConcluido(String emailUsuario) {

        Usuario usuario = buscarUsuario(emailUsuario);

        List<Modulo> trilha = montarTrilha(usuario);
        if (trilha.isEmpty()) {
            return 0.0;
        }

        Set<Long> idsDaTrilha = trilha.stream().map(Modulo::getId).collect(Collectors.toSet());

        long concluidos = progressoRepository.findByUsuario(usuario).stream()
                .filter(ProgressoUsuario::isConcluido)
                .filter(p -> idsDaTrilha.contains(p.getModulo().getId()))
                .count();

        return (concluidos * 100.0) / trilha.size();
    }

    /** Ids dos módulos da trilha que o usuário já concluiu (o frontend usa para marcar o status de cada módulo). */
    public List<Long> listarIdsConcluidos(String emailUsuario) {

        Usuario usuario = buscarUsuario(emailUsuario);

        Set<Long> idsDaTrilha = montarTrilha(usuario).stream().map(Modulo::getId).collect(Collectors.toSet());

        return progressoRepository.findByUsuario(usuario).stream()
                .filter(ProgressoUsuario::isConcluido)
                .map(p -> p.getModulo().getId())
                .filter(idsDaTrilha::contains)
                .distinct()
                .collect(Collectors.toList());
    }

    private Usuario buscarUsuario(String emailUsuario) {
        return usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
    }

    private List<Modulo> montarTrilha(Usuario usuario) {

        Optional<PerfilOnboarding> perfilSalvo = perfilRepository.findByUsuario(usuario);

        // Se ainda não fez o questionário, usa o perfil mais básico.
        String perfil = perfilSalvo
                .map(PerfilOnboarding::getPerfilCalculado)
                .orElse(PerfilOnboardingService.PERFIL_INICIANTE);

        boolean baseFraca = perfilSalvo
                .map(PerfilOnboarding::getPontuacaoBase)
                .map(base -> base <= LIMITE_BASE_FRACA)
                .orElse(false);

        List<Modulo> camada1 = moduloRepository.findByCamadaOrderByOrdemAsc(1);
        List<Modulo> camada2 = moduloRepository.findByCamadaOrderByOrdemAsc(2);
        List<Modulo> camada3 = moduloRepository.findByCamadaOrderByOrdemAsc(3);

        int limiteCamada1;
        int limiteCamada2;
        int limiteCamada3;

        switch (perfil) {
            case PerfilOnboardingService.PERFIL_INVESTIDOR:
                // Quem já investe mas tem a base fraca (sem reserva, endividado...) precisa
                // reforçar a camada 1: a organização financeira antecede qualquer investimento.
                limiteCamada1 = baseFraca ? 3 : 0;
                limiteCamada2 = 1;
                limiteCamada3 = 5;
                break;
            case PerfilOnboardingService.PERFIL_EM_DESENVOLVIMENTO:
                limiteCamada1 = 1;
                limiteCamada2 = 2;
                limiteCamada3 = 3;
                break;
            default: // PERFIL_INICIANTE
                limiteCamada1 = 3;
                limiteCamada2 = 2;
                limiteCamada3 = 1;
        }

        return Stream.of(
                camada1.stream().limit(limiteCamada1),
                camada2.stream().limit(limiteCamada2),
                camada3.stream().limit(limiteCamada3)
        ).flatMap(s -> s).collect(Collectors.toList());
    }
}

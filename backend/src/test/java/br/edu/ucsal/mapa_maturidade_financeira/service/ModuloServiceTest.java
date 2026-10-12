package br.edu.ucsal.mapa_maturidade_financeira.service;

import br.edu.ucsal.mapa_maturidade_financeira.model.Modulo;
import br.edu.ucsal.mapa_maturidade_financeira.model.PerfilOnboarding;
import br.edu.ucsal.mapa_maturidade_financeira.model.ProgressoUsuario;
import br.edu.ucsal.mapa_maturidade_financeira.model.Usuario;
import br.edu.ucsal.mapa_maturidade_financeira.repository.ModuloRepository;
import br.edu.ucsal.mapa_maturidade_financeira.repository.PerfilOnboardingRepository;
import br.edu.ucsal.mapa_maturidade_financeira.repository.ProgressoUsuarioRepository;
import br.edu.ucsal.mapa_maturidade_financeira.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModuloServiceTest {

    @Mock
    private ModuloRepository moduloRepository;

    @Mock
    private ProgressoUsuarioRepository progressoRepository;

    @Mock
    private PerfilOnboardingRepository perfilRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ModuloService moduloService;

    private Usuario usuario;
    private List<Modulo> modulosCamada1;
    private List<Modulo> modulosCamada2;
    private List<Modulo> modulosCamada3;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("Alice", "alice@teste.com", "hash123", LocalDate.now().minusYears(20));

        modulosCamada1 = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Modulo m = new Modulo("C1-M" + i, "Conteudo", 1, i);
            ReflectionTestUtils.setField(m, "id", (long) i);
            modulosCamada1.add(m);
        }

        modulosCamada2 = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            Modulo m = new Modulo("C2-M" + i, "Conteudo", 2, i);
            ReflectionTestUtils.setField(m, "id", (long) (10 + i));
            modulosCamada2.add(m);
        }

        modulosCamada3 = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Modulo m = new Modulo("C3-M" + i, "Conteudo", 3, i);
            ReflectionTestUtils.setField(m, "id", (long) (20 + i));
            modulosCamada3.add(m);
        }
    }

    private void mockCamadas() {
        when(moduloRepository.findByCamadaOrderByOrdemAsc(1)).thenReturn(modulosCamada1);
        when(moduloRepository.findByCamadaOrderByOrdemAsc(2)).thenReturn(modulosCamada2);
        when(moduloRepository.findByCamadaOrderByOrdemAsc(3)).thenReturn(modulosCamada3);
    }

    private PerfilOnboarding perfilIniciante() {
        return new PerfilOnboarding(usuario, PerfilOnboardingService.PERFIL_INICIANTE, 2);
    }

    @Test
    void deveListarTrilhaParaPerfilIniciante() {
        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));
        when(perfilRepository.findByUsuario(usuario)).thenReturn(Optional.of(perfilIniciante()));
        mockCamadas();

        List<Modulo> trilha = moduloService.listarTrilhaDoUsuario("alice@teste.com");

        // Iniciante: 3 da camada 1 + 2 da camada 2 + 1 da camada 3 = 6
        assertEquals(6, trilha.size());
        assertEquals("C1-M1", trilha.get(0).getTitulo());
    }

    @Test
    void deveListarTrilhaParaPerfilEmDesenvolvimento() {
        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));
        PerfilOnboarding perfil = new PerfilOnboarding(usuario, PerfilOnboardingService.PERFIL_EM_DESENVOLVIMENTO, 6);
        when(perfilRepository.findByUsuario(usuario)).thenReturn(Optional.of(perfil));
        mockCamadas();

        List<Modulo> trilha = moduloService.listarTrilhaDoUsuario("alice@teste.com");

        // Em desenvolvimento: 1 da camada 1 + 2 da camada 2 + 3 da camada 3 = 6
        assertEquals(6, trilha.size());
    }

    @Test
    void deveListarTrilhaParaPerfilInvestidorComBaseSolida() {
        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));
        PerfilOnboarding perfil = new PerfilOnboarding(usuario, PerfilOnboardingService.PERFIL_INVESTIDOR, 12);
        perfil.setPontuacaoBase(7);
        when(perfilRepository.findByUsuario(usuario)).thenReturn(Optional.of(perfil));
        mockCamadas();

        List<Modulo> trilha = moduloService.listarTrilhaDoUsuario("alice@teste.com");

        // Investidor: 0 da camada 1 + 1 da camada 2 + 5 da camada 3 = 6
        assertEquals(6, trilha.size());
        assertEquals(2, trilha.get(0).getCamada());
    }

    @Test
    void deveReforcarCamada1ParaInvestidorComBaseFraca() {
        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));
        PerfilOnboarding perfil = new PerfilOnboarding(usuario, PerfilOnboardingService.PERFIL_INVESTIDOR, 8);
        perfil.setPontuacaoBase(2); // investe, mas tem a base financeira fraca
        when(perfilRepository.findByUsuario(usuario)).thenReturn(Optional.of(perfil));
        mockCamadas();

        List<Modulo> trilha = moduloService.listarTrilhaDoUsuario("alice@teste.com");

        // 3 da camada 1 (reforço) + 1 da camada 2 + 5 da camada 3 = 9
        assertEquals(9, trilha.size());
        assertEquals(1, trilha.get(0).getCamada());
    }

    @Test
    void deveMarcarModuloDaTrilhaComoConcluidoComSucesso() {
        Modulo modulo = modulosCamada1.get(0);
        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));
        when(moduloRepository.findById(1L)).thenReturn(Optional.of(modulo));
        when(perfilRepository.findByUsuario(usuario)).thenReturn(Optional.of(perfilIniciante()));
        mockCamadas();
        when(progressoRepository.findByUsuarioAndModulo(usuario, modulo)).thenReturn(Optional.empty());

        moduloService.marcarComoConcluido("alice@teste.com", 1L);

        verify(progressoRepository, times(1)).save(any(ProgressoUsuario.class));
    }

    @Test
    void deveRecusarConclusaoDeModuloForaDaTrilha() {
        Modulo foraDaTrilha = modulosCamada3.get(4); // iniciante só vê 1 módulo da camada 3
        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));
        when(moduloRepository.findById(25L)).thenReturn(Optional.of(foraDaTrilha));
        when(perfilRepository.findByUsuario(usuario)).thenReturn(Optional.of(perfilIniciante()));
        mockCamadas();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> moduloService.marcarComoConcluido("alice@teste.com", 25L));

        assertTrue(ex.getMessage().contains("não faz parte da sua trilha"));
        verify(progressoRepository, never()).save(any(ProgressoUsuario.class));
    }

    @Test
    void deveCalcularPercentualConcluidoCorretamente() {
        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));
        when(perfilRepository.findByUsuario(usuario)).thenReturn(Optional.of(perfilIniciante()));
        mockCamadas();

        // 6 módulos na trilha, marcando 3 como concluídos
        ProgressoUsuario p1 = new ProgressoUsuario(usuario, modulosCamada1.get(0), true);
        ProgressoUsuario p2 = new ProgressoUsuario(usuario, modulosCamada1.get(1), true);
        ProgressoUsuario p3 = new ProgressoUsuario(usuario, modulosCamada1.get(2), true);
        when(progressoRepository.findByUsuario(usuario)).thenReturn(List.of(p1, p2, p3));

        double percentual = moduloService.calcularPercentualConcluido("alice@teste.com");

        // 3 de 6 = 50.0%
        assertEquals(50.0, percentual, 0.01);
    }

    @Test
    void naoDeveContarModulosForaDaTrilhaNoPercentual() {
        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));
        when(perfilRepository.findByUsuario(usuario)).thenReturn(Optional.of(perfilIniciante()));
        mockCamadas();

        ProgressoUsuario dentro1 = new ProgressoUsuario(usuario, modulosCamada1.get(0), true);
        ProgressoUsuario dentro2 = new ProgressoUsuario(usuario, modulosCamada1.get(1), true);
        ProgressoUsuario dentro3 = new ProgressoUsuario(usuario, modulosCamada1.get(2), true);
        ProgressoUsuario fora = new ProgressoUsuario(usuario, modulosCamada3.get(4), true); // fora da trilha
        when(progressoRepository.findByUsuario(usuario)).thenReturn(List.of(dentro1, dentro2, dentro3, fora));

        double percentual = moduloService.calcularPercentualConcluido("alice@teste.com");

        assertEquals(50.0, percentual, 0.01);
        assertTrue(percentual <= 100.0);
    }

    @Test
    void deveRetornarZeroQuandoNaoHaModulosNaTrilha() {
        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));

        assertEquals(0.0, moduloService.calcularPercentualConcluido("alice@teste.com"), 0.01);
    }

    @Test
    void deveListarApenasIdsConcluidosQueEstaoNaTrilha() {
        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));
        when(perfilRepository.findByUsuario(usuario)).thenReturn(Optional.of(perfilIniciante()));
        mockCamadas();

        ProgressoUsuario concluido = new ProgressoUsuario(usuario, modulosCamada1.get(0), true);
        ProgressoUsuario pendente = new ProgressoUsuario(usuario, modulosCamada1.get(1), false);
        ProgressoUsuario fora = new ProgressoUsuario(usuario, modulosCamada3.get(4), true); // fora da trilha
        when(progressoRepository.findByUsuario(usuario)).thenReturn(List.of(concluido, pendente, fora));

        List<Long> ids = moduloService.listarIdsConcluidos("alice@teste.com");

        assertEquals(List.of(1L), ids);
    }
}

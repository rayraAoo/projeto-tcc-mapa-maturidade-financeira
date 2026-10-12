package br.edu.ucsal.mapa_maturidade_financeira.service;

import br.edu.ucsal.mapa_maturidade_financeira.dto.QuestionarioRequest;
import br.edu.ucsal.mapa_maturidade_financeira.model.PerfilOnboarding;
import br.edu.ucsal.mapa_maturidade_financeira.model.Usuario;
import br.edu.ucsal.mapa_maturidade_financeira.repository.PerfilOnboardingRepository;
import br.edu.ucsal.mapa_maturidade_financeira.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PerfilOnboardingServiceTest {

    @Mock
    private PerfilOnboardingRepository perfilRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private PerfilOnboardingService perfilService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("Alice", "alice@teste.com", "senha123", LocalDate.now().minusYears(22));
    }

    @Test
    void deveClassificarComoInicianteQuandoBaseFinanceiraForBaixa() {
        QuestionarioRequest request = new QuestionarioRequest();
        request.setRespostaOrcamento("A"); // 0
        request.setRespostaReservaEmergencia("A"); // 0
        request.setRespostaDivida("A"); // 0
        request.setRespostaConhecimentoInvestimento("A"); // 0
        request.setRespostaExperienciaInvestimento("A"); // 0

        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));
        when(perfilRepository.findByUsuario(usuario)).thenReturn(Optional.empty());
        when(perfilRepository.save(any(PerfilOnboarding.class))).thenAnswer(i -> i.getArgument(0));

        PerfilOnboarding perfil = perfilService.calcularEsalvar("alice@teste.com", request);

        assertNotNull(perfil);
        assertEquals(PerfilOnboardingService.PERFIL_INICIANTE, perfil.getPerfilCalculado());
        assertEquals(0, perfil.getPontuacaoTotal());
    }

    @Test
    void deveClassificarComoEmDesenvolvimentoQuandoTiverBaseMasSemInvestir() {
        QuestionarioRequest request = new QuestionarioRequest();
        request.setRespostaOrcamento("C"); // 2
        request.setRespostaReservaEmergencia("C"); // 2
        request.setRespostaDivida("C"); // 2 (base = 6 > 3)
        request.setRespostaConhecimentoInvestimento("B"); // 1
        request.setRespostaExperienciaInvestimento("A"); // 0 (sem exp prática)

        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));
        when(perfilRepository.findByUsuario(usuario)).thenReturn(Optional.empty());
        when(perfilRepository.save(any(PerfilOnboarding.class))).thenAnswer(i -> i.getArgument(0));

        PerfilOnboarding perfil = perfilService.calcularEsalvar("alice@teste.com", request);

        assertNotNull(perfil);
        assertEquals(PerfilOnboardingService.PERFIL_EM_DESENVOLVIMENTO, perfil.getPerfilCalculado());
    }

    @Test
    void deveClassificarComoInvestidorQuandoJaPossuirExperienciaPratica() {
        QuestionarioRequest request = new QuestionarioRequest();
        request.setRespostaOrcamento("B"); // 1
        request.setRespostaReservaEmergencia("B"); // 1
        request.setRespostaDivida("B"); // 1
        request.setRespostaConhecimentoInvestimento("C"); // 2
        request.setRespostaExperienciaInvestimento("C"); // 2 (experiência C = Investidor direto)

        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));
        when(perfilRepository.findByUsuario(usuario)).thenReturn(Optional.empty());
        when(perfilRepository.save(any(PerfilOnboarding.class))).thenAnswer(i -> i.getArgument(0));

        PerfilOnboarding perfil = perfilService.calcularEsalvar("alice@teste.com", request);

        assertNotNull(perfil);
        assertEquals(PerfilOnboardingService.PERFIL_INVESTIDOR, perfil.getPerfilCalculado());
    }

    @Test
    void deveGuardarAPontuacaoDaBaseFinanceira() {
        QuestionarioRequest request = new QuestionarioRequest();
        request.setRespostaOrcamento("C"); // 2
        request.setRespostaReservaEmergencia("C"); // 2
        request.setRespostaDivida("C"); // 2
        request.setRespostaConhecimentoInvestimento("B"); // 1
        request.setRespostaExperienciaInvestimento("A"); // 0

        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));
        when(perfilRepository.findByUsuario(usuario)).thenReturn(Optional.empty());
        when(perfilRepository.save(any(PerfilOnboarding.class))).thenAnswer(i -> i.getArgument(0));

        PerfilOnboarding perfil = perfilService.calcularEsalvar("alice@teste.com", request);

        assertEquals(6, perfil.getPontuacaoBase());
        assertEquals(7, perfil.getPontuacaoTotal());
    }

    @Test
    void deveManterInvestidorMasRegistrarBaseFracaQuandoJaInvesteSemBase() {
        QuestionarioRequest request = new QuestionarioRequest();
        request.setRespostaOrcamento("A");
        request.setRespostaReservaEmergencia("A");
        request.setRespostaDivida("A");
        request.setRespostaConhecimentoInvestimento("C");
        request.setRespostaExperienciaInvestimento("D");

        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));
        when(perfilRepository.findByUsuario(usuario)).thenReturn(Optional.empty());
        when(perfilRepository.save(any(PerfilOnboarding.class))).thenAnswer(i -> i.getArgument(0));

        PerfilOnboarding perfil = perfilService.calcularEsalvar("alice@teste.com", request);

        assertEquals(PerfilOnboardingService.PERFIL_INVESTIDOR, perfil.getPerfilCalculado());
        assertEquals(0, perfil.getPontuacaoBase()); // o ModuloService usa isso para reforçar a camada 1
    }

    @Test
    void deveRecusarAlternativaInvalidaNoQuestionario() {
        QuestionarioRequest request = new QuestionarioRequest();
        request.setRespostaOrcamento("Z");
        request.setRespostaReservaEmergencia("A");
        request.setRespostaDivida("A");
        request.setRespostaConhecimentoInvestimento("A");
        request.setRespostaExperienciaInvestimento("A");

        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));

        assertThrows(IllegalArgumentException.class,
                () -> perfilService.calcularEsalvar("alice@teste.com", request));

        verify(perfilRepository, never()).save(any(PerfilOnboarding.class));
    }

    @Test
    void deveDescreverOsPerfisComNomesProfissionais() {
        assertEquals("Iniciante", PerfilOnboardingService.descricaoDoPerfil(PerfilOnboardingService.PERFIL_INICIANTE));
        assertEquals("Em desenvolvimento",
                PerfilOnboardingService.descricaoDoPerfil(PerfilOnboardingService.PERFIL_EM_DESENVOLVIMENTO));
        assertEquals("Investidor", PerfilOnboardingService.descricaoDoPerfil(PerfilOnboardingService.PERFIL_INVESTIDOR));
    }
}

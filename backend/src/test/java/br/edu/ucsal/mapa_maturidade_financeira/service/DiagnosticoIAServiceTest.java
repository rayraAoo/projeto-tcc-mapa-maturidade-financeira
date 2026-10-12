package br.edu.ucsal.mapa_maturidade_financeira.service;

import br.edu.ucsal.mapa_maturidade_financeira.dto.DiagnosticoResponse;
import br.edu.ucsal.mapa_maturidade_financeira.exception.IaIndisponivelException;
import br.edu.ucsal.mapa_maturidade_financeira.exception.LimiteDeUsoException;
import br.edu.ucsal.mapa_maturidade_financeira.model.*;
import br.edu.ucsal.mapa_maturidade_financeira.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiagnosticoIAServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PerfilOnboardingRepository perfilRepository;

    @Mock
    private ProgressoUsuarioRepository progressoRepository;

    @Mock
    private RespostaUsuarioRepository respostaRepository;

    @Mock
    private HistoricoSimulacaoRepository historicoSimulacaoRepository;

    @Mock
    private DiagnosticoIARepository diagnosticoRepository;

    @Mock
    private GeminiClientService geminiClient;

    @InjectMocks
    private DiagnosticoIAService diagnosticoService;

    private Usuario usuario;
    private PerfilOnboarding perfil;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("Alice", "alice@teste.com", "senha123", LocalDate.now().minusYears(21));
        perfil = new PerfilOnboarding(usuario, "INICIANTE", 4);
    }

    private void prepararUsuarioComPerfil() {
        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));
        when(perfilRepository.findByUsuario(usuario)).thenReturn(Optional.of(perfil));
    }

    @Test
    void deveGerarDiagnosticoComSucessoMarcandoComoGeradoPorIa() {
        prepararUsuarioComPerfil();
        when(geminiClient.gerarResposta(anyString())).thenReturn("Você está iniciando bem a sua jornada.");
        when(diagnosticoRepository.save(any(DiagnosticoIA.class))).thenAnswer(i -> i.getArgument(0));

        DiagnosticoResponse response = diagnosticoService.gerarDiagnostico("alice@teste.com");

        assertNotNull(response);
        assertTrue(response.getTexto().contains("iniciando bem"));
        assertNotNull(response.getDataGeracao());
        assertTrue(response.isGeradoPorIa());
        assertTrue(response.getAviso().contains("inteligência artificial"));
        verify(diagnosticoRepository, times(1)).save(any(DiagnosticoIA.class));
    }

    @Test
    void oPromptNaoDeveConterNomeNemEmailDoUsuario() {
        prepararUsuarioComPerfil();
        when(geminiClient.gerarResposta(anyString())).thenReturn("ok");
        when(diagnosticoRepository.save(any(DiagnosticoIA.class))).thenAnswer(i -> i.getArgument(0));

        diagnosticoService.gerarDiagnostico("alice@teste.com");

        ArgumentCaptor<String> prompt = ArgumentCaptor.forClass(String.class);
        verify(geminiClient).gerarResposta(prompt.capture());
        assertFalse(prompt.getValue().contains("Alice"), "O nome não pode ser enviado ao serviço de IA");
        assertFalse(prompt.getValue().contains("alice@teste.com"), "O e-mail não pode ser enviado ao serviço de IA");
        assertTrue(prompt.getValue().contains("NÃO recomende produtos financeiros"));
    }

    @Test
    void deveUsarDiagnosticoDeContingenciaQuandoAIaEstiverIndisponivel() {
        prepararUsuarioComPerfil();
        when(geminiClient.gerarResposta(anyString())).thenThrow(new IaIndisponivelException("indisponível"));
        when(diagnosticoRepository.save(any(DiagnosticoIA.class))).thenAnswer(i -> i.getArgument(0));

        DiagnosticoResponse response = diagnosticoService.gerarDiagnostico("alice@teste.com");

        assertFalse(response.isGeradoPorIa());
        assertTrue(response.getTexto().contains("Iniciante"));
        assertTrue(response.getTexto().contains("não constitui recomendação de investimento"));

        ArgumentCaptor<DiagnosticoIA> salvo = ArgumentCaptor.forClass(DiagnosticoIA.class);
        verify(diagnosticoRepository).save(salvo.capture());
        assertEquals(DiagnosticoIAService.ORIGEM_REGRAS, salvo.getValue().getOrigem());
    }

    @Test
    void deveLimitarGeracoesSeguidasDoMesmoUsuario() {
        prepararUsuarioComPerfil();
        DiagnosticoIA recente = new DiagnosticoIA(usuario, "texto", LocalDateTime.now());
        when(diagnosticoRepository.findByUsuarioOrderByDataGeracaoDesc(usuario)).thenReturn(List.of(recente));

        assertThrows(LimiteDeUsoException.class, () -> diagnosticoService.gerarDiagnostico("alice@teste.com"));

        verify(geminiClient, never()).gerarResposta(anyString());
    }

    @Test
    void deveLancarExcecaoSeUsuarioNaoPossuirPerfilAoGerarDiagnostico() {
        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));
        when(perfilRepository.findByUsuario(usuario)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> diagnosticoService.gerarDiagnostico("alice@teste.com"));

        assertTrue(ex.getMessage().contains("ainda não completou o questionário"));
        verify(geminiClient, never()).gerarResposta(anyString());
    }

    @Test
    void deveObterUltimoDiagnosticoComSucesso() {
        DiagnosticoIA diag = new DiagnosticoIA(usuario, "Diagnóstico recente", LocalDateTime.now());
        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));
        when(diagnosticoRepository.findByUsuarioOrderByDataGeracaoDesc(usuario)).thenReturn(List.of(diag));

        DiagnosticoResponse response = diagnosticoService.obterUltimoDiagnostico("alice@teste.com");

        assertNotNull(response);
        assertEquals("Diagnóstico recente", response.getTexto());
        assertTrue(response.isGeradoPorIa(), "Registro sem origem (antigo) é tratado como gerado por IA");
    }

    @Test
    void deveIdentificarNoHistoricoOsDiagnosticosDeContingencia() {
        DiagnosticoIA porRegras = new DiagnosticoIA(usuario, "Por regras", LocalDateTime.now());
        porRegras.setOrigem(DiagnosticoIAService.ORIGEM_REGRAS);
        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));
        when(diagnosticoRepository.findByUsuarioOrderByDataGeracaoDesc(usuario)).thenReturn(List.of(porRegras));

        List<DiagnosticoResponse> historico = diagnosticoService.listarHistorico("alice@teste.com");

        assertEquals(1, historico.size());
        assertFalse(historico.get(0).isGeradoPorIa());
    }
}

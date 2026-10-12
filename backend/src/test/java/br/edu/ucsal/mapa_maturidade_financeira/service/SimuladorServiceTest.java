package br.edu.ucsal.mapa_maturidade_financeira.service;

import br.edu.ucsal.mapa_maturidade_financeira.dto.CenarioSimulacaoDTO;
import br.edu.ucsal.mapa_maturidade_financeira.dto.ExecutarSimulacaoRequest;
import br.edu.ucsal.mapa_maturidade_financeira.dto.ResultadoSimulacaoResponse;
import br.edu.ucsal.mapa_maturidade_financeira.model.CenarioSimulacao;
import br.edu.ucsal.mapa_maturidade_financeira.model.HistoricoSimulacao;
import br.edu.ucsal.mapa_maturidade_financeira.model.OpcaoSimulacao;
import br.edu.ucsal.mapa_maturidade_financeira.model.Usuario;
import br.edu.ucsal.mapa_maturidade_financeira.repository.CenarioSimulacaoRepository;
import br.edu.ucsal.mapa_maturidade_financeira.repository.HistoricoSimulacaoRepository;
import br.edu.ucsal.mapa_maturidade_financeira.repository.OpcaoSimulacaoRepository;
import br.edu.ucsal.mapa_maturidade_financeira.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimuladorServiceTest {

    @Mock
    private CenarioSimulacaoRepository cenarioRepository;

    @Mock
    private OpcaoSimulacaoRepository opcaoRepository;

    @Mock
    private HistoricoSimulacaoRepository historicoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private SimuladorService simuladorService;

    private Usuario usuario;
    private CenarioSimulacao cenario;
    private OpcaoSimulacao opcao;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("Alice", "alice@teste.com", "hash123", LocalDate.now().minusYears(20));
        ReflectionTestUtils.setField(usuario, "id", 1L);

        cenario = new CenarioSimulacao("Primeiro Salário", "Descrição do cenário", 1200.0, "ORCAMENTO");
        ReflectionTestUtils.setField(cenario, "id", 10L);

        opcao = new OpcaoSimulacao(cenario, "Guardar 20%", -960.0, 10, "Excelente decisão");
        ReflectionTestUtils.setField(opcao, "id", 100L);
    }

    @Test
    void deveListarCenariosComSucesso() {
        when(cenarioRepository.findAll()).thenReturn(List.of(cenario));
        when(opcaoRepository.findByCenario(cenario)).thenReturn(List.of(opcao));

        List<CenarioSimulacaoDTO> resultado = simuladorService.listarCenarios();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Primeiro Salário", resultado.get(0).getTitulo());
        assertEquals(1, resultado.get(0).getOpcoes().size());
    }

    @Test
    void deveBuscarCenarioPorIdComSucesso() {
        when(cenarioRepository.findById(10L)).thenReturn(Optional.of(cenario));
        when(opcaoRepository.findByCenario(cenario)).thenReturn(List.of(opcao));

        CenarioSimulacaoDTO dto = simuladorService.buscarCenarioPorId(10L);

        assertNotNull(dto);
        assertEquals(10L, dto.getId());
        assertEquals("Primeiro Salário", dto.getTitulo());
    }

    @Test
    void deveLancarExcecaoAoBuscarCenarioInexistente() {
        when(cenarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            simuladorService.buscarCenarioPorId(99L);
        });
    }

    @Test
    void deveExecutarSimulacaoComSucessoECalcularSaldoCorreto() {
        ExecutarSimulacaoRequest request = new ExecutarSimulacaoRequest(10L, 100L);

        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));
        when(cenarioRepository.findById(10L)).thenReturn(Optional.of(cenario));
        when(opcaoRepository.findById(100L)).thenReturn(Optional.of(opcao));
        when(historicoRepository.save(any(HistoricoSimulacao.class))).thenAnswer(inv -> {
            HistoricoSimulacao h = inv.getArgument(0);
            ReflectionTestUtils.setField(h, "id", 500L);
            return h;
        });

        ResultadoSimulacaoResponse response = simuladorService.executarSimulacao("alice@teste.com", request);

        assertNotNull(response);
        assertEquals(500L, response.getHistoricoId());
        assertEquals(1200.0, response.getSaldoInicial());
        assertEquals(-960.0, response.getImpactoFinanceiro());
        assertEquals(240.0, response.getSaldoFinal());
        assertEquals(10, response.getPontuacao());
        assertEquals("Excelente decisão", response.getFeedbackEducativo());

        verify(historicoRepository, times(1)).save(any(HistoricoSimulacao.class));
    }

    @Test
    void deveFalharSeOpcaoNaoPertencerAoCenario() {
        CenarioSimulacao outroCenario = new CenarioSimulacao("Outro", "Desc", 500.0, "RESERVA");
        ReflectionTestUtils.setField(outroCenario, "id", 20L);
        OpcaoSimulacao opcaoInvalida = new OpcaoSimulacao(outroCenario, "Outra opcao", -100.0, 5, "Feedback");
        ReflectionTestUtils.setField(opcaoInvalida, "id", 200L);

        ExecutarSimulacaoRequest request = new ExecutarSimulacaoRequest(10L, 200L);

        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));
        when(cenarioRepository.findById(10L)).thenReturn(Optional.of(cenario));
        when(opcaoRepository.findById(200L)).thenReturn(Optional.of(opcaoInvalida));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            simuladorService.executarSimulacao("alice@teste.com", request);
        });

        assertTrue(ex.getMessage().contains("não pertence"));
        verify(historicoRepository, never()).save(any(HistoricoSimulacao.class));
    }

    @Test
    void deveListarHistoricoDoUsuario() {
        HistoricoSimulacao historico = new HistoricoSimulacao(
                usuario, cenario, opcao, 1200.0, -960.0, 240.0, 10, "Excelente decisão", LocalDateTime.now()
        );
        ReflectionTestUtils.setField(historico, "id", 1L);

        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));
        when(historicoRepository.findByUsuarioOrderByDataSimulacaoDesc(usuario)).thenReturn(List.of(historico));

        List<ResultadoSimulacaoResponse> lista = simuladorService.listarHistoricoDoUsuario("alice@teste.com");

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals(240.0, lista.get(0).getSaldoFinal());
    }
}

package br.edu.ucsal.mapa_maturidade_financeira.service;

import br.edu.ucsal.mapa_maturidade_financeira.dto.ProgressoResponse;
import br.edu.ucsal.mapa_maturidade_financeira.model.*;
import br.edu.ucsal.mapa_maturidade_financeira.repository.HistoricoSimulacaoRepository;
import br.edu.ucsal.mapa_maturidade_financeira.repository.ProgressoUsuarioRepository;
import br.edu.ucsal.mapa_maturidade_financeira.repository.RespostaUsuarioRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProgressoServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ModuloService moduloService;

    @Mock
    private ProgressoUsuarioRepository progressoRepository;

    @Mock
    private RespostaUsuarioRepository respostaRepository;

    @Mock
    private HistoricoSimulacaoRepository historicoSimulacaoRepository;

    @InjectMocks
    private ProgressoService progressoService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("Alice", "alice@teste.com", "hash", LocalDate.now().minusYears(20));
    }

    private Modulo modulo(long id) {
        Modulo m = new Modulo("M" + id, "conteudo", 1, (int) id);
        ReflectionTestUtils.setField(m, "id", id);
        return m;
    }

    private Pergunta pergunta(long id, Modulo modulo) {
        Pergunta p = new Pergunta(modulo, "Pergunta " + id, "a", "b", "c", "d", "A", "explicação");
        ReflectionTestUtils.setField(p, "id", id);
        return p;
    }

    private CenarioSimulacao cenario(long id) {
        CenarioSimulacao c = new CenarioSimulacao("Cenário " + id, "descrição", 100.0, "ORCAMENTO");
        ReflectionTestUtils.setField(c, "id", id);
        return c;
    }

    private HistoricoSimulacao simulacao(CenarioSimulacao cenario, int nota) {
        return new HistoricoSimulacao(usuario, cenario, null, 100.0, -10.0, 90.0, nota, "feedback",
                LocalDateTime.now());
    }

    @Test
    void deveCalcularXpPorAprendizadoSemContarRepeticoes() {
        Modulo m1 = modulo(1);
        Modulo m2 = modulo(2);
        Modulo m3 = modulo(3);
        Modulo foraDaTrilha = modulo(99);

        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));
        when(moduloService.listarTrilhaDoUsuario("alice@teste.com")).thenReturn(List.of(m1, m2, m3));

        // 2 concluídos dentro da trilha (+ 1 fora da trilha, que não conta)
        when(progressoRepository.findByUsuario(usuario)).thenReturn(List.of(
                new ProgressoUsuario(usuario, m1, true),
                new ProgressoUsuario(usuario, m2, true),
                new ProgressoUsuario(usuario, m3, false),
                new ProgressoUsuario(usuario, foraDaTrilha, true)));

        // Pergunta 10 acertada duas vezes (conta uma), pergunta 11 errada, pergunta 12 acertada => 2 acertos
        Pergunta p10 = pergunta(10, m1);
        Pergunta p11 = pergunta(11, m1);
        Pergunta p12 = pergunta(12, m2);
        when(respostaRepository.findByUsuario(usuario)).thenReturn(List.of(
                new RespostaUsuario(usuario, p10, true),
                new RespostaUsuario(usuario, p10, true),
                new RespostaUsuario(usuario, p11, false),
                new RespostaUsuario(usuario, p12, true)));

        // Cenário 100: notas 4 e 9 (vale a melhor = 9); cenário 101: nota 6 => 15
        CenarioSimulacao c100 = cenario(100);
        CenarioSimulacao c101 = cenario(101);
        when(historicoSimulacaoRepository.findByUsuarioOrderByDataSimulacaoDesc(usuario)).thenReturn(List.of(
                simulacao(c100, 4), simulacao(c100, 9), simulacao(c101, 6)));

        ProgressoResponse progresso = progressoService.obterProgresso("alice@teste.com");

        assertEquals(2, progresso.getModulosConcluidos());
        assertEquals(3, progresso.getTotalModulos());
        assertEquals(66.67, progresso.getPercentualConcluido(), 0.01);
        // 2 módulos * 20 + 2 acertos * 10 + 15 de simulador = 75
        assertEquals(75, progresso.getXp());
    }

    @Test
    void deveRetornarZeroQuandoNaoHaAtividade() {
        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));
        when(moduloService.listarTrilhaDoUsuario("alice@teste.com")).thenReturn(List.of());

        ProgressoResponse progresso = progressoService.obterProgresso("alice@teste.com");

        assertEquals(0.0, progresso.getPercentualConcluido(), 0.01);
        assertEquals(0, progresso.getXp());
    }
}

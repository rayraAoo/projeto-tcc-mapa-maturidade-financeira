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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PerguntaServiceTest {

    @Mock
    private PerguntaRepository perguntaRepository;

    @Mock
    private ModuloRepository moduloRepository;

    @Mock
    private RespostaUsuarioRepository respostaUsuarioRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private PerguntaService perguntaService;

    private Usuario usuario;
    private Modulo modulo;
    private Pergunta pergunta;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("Alice", "alice@teste.com", "hash123", LocalDate.now().minusYears(20));
        modulo = new Modulo("Orçamento", "Conteúdo sobre orçamento", 1, 1);
        ReflectionTestUtils.setField(modulo, "id", 1L);

        pergunta = new Pergunta(modulo, "Qual a regra 50-30-20?", "50% necessidades", "50% supérfluos", "50% lazer", "50% dívidas", "A", "Explicação A");
        ReflectionTestUtils.setField(pergunta, "id", 10L);
    }

    @Test
    void deveListarPerguntasDoModuloComSucesso() {
        when(moduloRepository.findById(1L)).thenReturn(Optional.of(modulo));
        when(perguntaRepository.findByModulo(modulo)).thenReturn(List.of(pergunta));

        List<Pergunta> resultado = perguntaService.listarPerguntasDoModulo(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Qual a regra 50-30-20?", resultado.get(0).getEnunciado());
    }

    @Test
    void deveFalharAoListarPerguntasDeModuloInexistente() {
        when(moduloRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            perguntaService.listarPerguntasDoModulo(99L);
        });
    }

    @Test
    void deveCorrigirRespostaCorretaComSucesso() {
        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));
        when(perguntaRepository.findById(10L)).thenReturn(Optional.of(pergunta));

        ResultadoQuizResponse response = perguntaService.corrigirResposta("alice@teste.com", 10L, "A");

        assertNotNull(response);
        assertTrue(response.isCorreta());
        assertEquals("A", response.getAlternativaCorreta());
        assertEquals("Explicação A", response.getExplicacao());
        verify(respostaUsuarioRepository, times(1)).save(any(RespostaUsuario.class));
    }

    @Test
    void deveCorrigirRespostaIncorretaComSucesso() {
        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));
        when(perguntaRepository.findById(10L)).thenReturn(Optional.of(pergunta));

        ResultadoQuizResponse response = perguntaService.corrigirResposta("alice@teste.com", 10L, "B");

        assertNotNull(response);
        assertFalse(response.isCorreta());
        assertEquals("A", response.getAlternativaCorreta());
        verify(respostaUsuarioRepository, times(1)).save(any(RespostaUsuario.class));
    }

    @Test
    void deveAtualizarRespostaExistenteEmVezDeCriarOutra() {
        RespostaUsuario existente = new RespostaUsuario(usuario, pergunta, false);
        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));
        when(perguntaRepository.findById(10L)).thenReturn(Optional.of(pergunta));
        when(respostaUsuarioRepository.findFirstByUsuarioAndPerguntaOrderByIdDesc(usuario, pergunta))
                .thenReturn(Optional.of(existente));

        ResultadoQuizResponse response = perguntaService.corrigirResposta("alice@teste.com", 10L, "A");

        assertTrue(response.isCorreta());
        assertTrue(existente.isAcertou(), "O registro existente deve refletir a tentativa mais recente");
        verify(respostaUsuarioRepository, times(1)).save(existente);
    }

    @Test
    void deveRejeitarAlternativaInvalida() {
        assertThrows(IllegalArgumentException.class,
                () -> perguntaService.corrigirResposta("alice@teste.com", 10L, "Z"));

        verifyNoInteractions(respostaUsuarioRepository);
    }
}

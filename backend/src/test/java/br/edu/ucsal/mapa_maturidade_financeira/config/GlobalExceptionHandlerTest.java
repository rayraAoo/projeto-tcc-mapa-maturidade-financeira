package br.edu.ucsal.mapa_maturidade_financeira.config;

import br.edu.ucsal.mapa_maturidade_financeira.exception.CredenciaisInvalidasException;
import br.edu.ucsal.mapa_maturidade_financeira.exception.LimiteDeUsoException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void erroInesperadoNaoDeveVazarDetalhesInternosAoCliente() {
        ResponseEntity<Map<String, Object>> resposta =
                handler.handleGenerico(new RuntimeException("senha do banco: abc123 / chave: SEGREDO"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resposta.getStatusCode());
        String mensagem = String.valueOf(resposta.getBody().get("mensagem"));
        assertFalse(mensagem.contains("SEGREDO"));
        assertFalse(mensagem.contains("abc123"));
    }

    @Test
    void credenciaisInvalidasDevolvem401() {
        ResponseEntity<Map<String, Object>> resposta =
                handler.handleCredenciais(new CredenciaisInvalidasException("E-mail ou senha inválidos."));

        assertEquals(HttpStatus.UNAUTHORIZED, resposta.getStatusCode());
        assertEquals("E-mail ou senha inválidos.", resposta.getBody().get("mensagem"));
    }

    @Test
    void regraDeNegocioDevolve400ComFormatoPadrao() {
        ResponseEntity<Map<String, Object>> resposta =
                handler.handleIllegalArgument(new IllegalArgumentException("Módulo não encontrado."));

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        assertEquals(400, resposta.getBody().get("status"));
        assertEquals("Módulo não encontrado.", resposta.getBody().get("mensagem"));
        assertNotNull(resposta.getBody().get("timestamp"));
    }

    @Test
    void limiteDeUsoDevolve429() {
        ResponseEntity<Map<String, Object>> resposta =
                handler.handleLimite(new LimiteDeUsoException("Aguarde."));

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, resposta.getStatusCode());
    }
}

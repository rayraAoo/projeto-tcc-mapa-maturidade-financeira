package br.edu.ucsal.mapa_maturidade_financeira.service;

import br.edu.ucsal.mapa_maturidade_financeira.exception.IaIndisponivelException;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class GeminiClientServiceTest {

    @Test
    void semChaveConfiguradaDeveFalharComMensagemGenericaSemChamarARede() {
        GeminiClientService cliente = new GeminiClientService();
        ReflectionTestUtils.setField(cliente, "apiKey", "");

        IaIndisponivelException ex = assertThrows(IaIndisponivelException.class,
                () -> cliente.gerarResposta("qualquer prompt"));

        assertFalse(ex.getMessage().toLowerCase().contains("key"));
        assertFalse(ex.getMessage().contains("http"));
    }
}

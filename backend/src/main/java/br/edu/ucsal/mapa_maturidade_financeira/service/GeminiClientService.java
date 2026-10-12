package br.edu.ucsal.mapa_maturidade_financeira.service;

import br.edu.ucsal.mapa_maturidade_financeira.exception.IaIndisponivelException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * Cliente da API do Gemini.
 *
 * Segurança:
 *  - a chave vai no cabeçalho x-goog-api-key (nunca na URL, que costuma aparecer em mensagens de erro);
 *  - qualquer falha vira IaIndisponivelException com mensagem genérica; o detalhe técnico
 *    (sem URL e sem chave) fica apenas no log do servidor;
 *  - há limite de tempo de conexão e de leitura, para a requisição nunca ficar presa.
 */
@Service
public class GeminiClientService {

    private static final Logger log = LoggerFactory.getLogger(GeminiClientService.class);
    private static final String MENSAGEM_INDISPONIVEL = "O serviço de IA está temporariamente indisponível.";

    @Value("${gemini.api.key:}")
    private String apiKey;

    // Modelos do Gemini são aposentados com frequência. Troque pela variável GEMINI_MODEL,
    // sem alterar o código (veja a lista atual em ai.google.dev/gemini-api/docs/models).
    @Value("${gemini.api.model:gemini-3.5-flash}")
    private String modelName;

    @Value("${gemini.api.base-url:https://generativelanguage.googleapis.com/v1beta/models}")
    private String baseUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GeminiClientService() {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(httpClient);
        factory.setReadTimeout(Duration.ofSeconds(30));
        this.restTemplate = new RestTemplate(factory);
    }

    public String gerarResposta(String prompt) {
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("gemini.api.key não configurada (variável GEMINI_API_KEY).");
            throw new IaIndisponivelException(MENSAGEM_INDISPONIVEL);
        }

        String url = baseUrl + "/" + modelName + ":generateContent";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", apiKey);

        // Estrutura esperada pela API: { "contents": [ { "parts": [ { "text": "..." } ] } ] }
        Map<String, Object> corpo = Map.of(
                "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt))))
        );

        try {
            ResponseEntity<String> resposta =
                    restTemplate.postForEntity(url, new HttpEntity<>(corpo, headers), String.class);

            JsonNode texto = objectMapper.readTree(resposta.getBody())
                    .path("candidates").path(0)
                    .path("content").path("parts").path(0)
                    .path("text");

            if (texto.isMissingNode() || texto.asText().isBlank()) {
                log.warn("Resposta do Gemini sem texto utilizável (modelo {}).", modelName);
                throw new IaIndisponivelException(MENSAGEM_INDISPONIVEL);
            }
            return texto.asText();

        } catch (IaIndisponivelException e) {
            throw e;
        } catch (RestClientResponseException e) {
            // Ex.: 404 = modelo aposentado; 401/403 = chave inválida; 429 = cota excedida.
            log.warn("Falha na API do Gemini: HTTP {} (modelo {}).", e.getStatusCode().value(), modelName);
            throw new IaIndisponivelException(MENSAGEM_INDISPONIVEL);
        } catch (Exception e) {
            log.warn("Falha ao chamar o Gemini ({}).", e.getClass().getSimpleName());
            throw new IaIndisponivelException(MENSAGEM_INDISPONIVEL);
        }
    }
}

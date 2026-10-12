package br.edu.ucsal.mapa_maturidade_financeira.config;

import br.edu.ucsal.mapa_maturidade_financeira.exception.CredenciaisInvalidasException;
import br.edu.ucsal.mapa_maturidade_financeira.exception.LimiteDeUsoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Único ponto de tratamento de erros da API.
 * Todas as respostas de erro seguem o mesmo formato JSON: timestamp, status, mensagem (e erros, na validação).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<Map<String, Object>> resposta(HttpStatus status, String mensagem) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("mensagem", mensagem);
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<Map<String, Object>> handleCredenciais(CredenciaisInvalidasException ex) {
        return resposta(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return resposta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(LimiteDeUsoException.class)
    public ResponseEntity<Map<String, Object>> handleLimite(LimiteDeUsoException ex) {
        return resposta(HttpStatus.TOO_MANY_REQUESTS, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            erros.put(error.getField(), error.getDefaultMessage());
        }
        ResponseEntity<Map<String, Object>> resposta =
                resposta(HttpStatus.BAD_REQUEST, "Erro de validação nos campos informados.");
        resposta.getBody().put("erros", erros);
        return resposta;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleJsonInvalido(HttpMessageNotReadableException ex) {
        return resposta(HttpStatus.BAD_REQUEST, "Corpo da requisição ausente ou em formato inválido.");
    }

    /** Qualquer erro inesperado: registra o detalhe no log e devolve uma mensagem genérica ao cliente. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenerico(Exception ex) {
        log.error("Erro inesperado", ex);
        return resposta(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro interno. Tente novamente mais tarde.");
    }
}

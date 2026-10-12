package br.edu.ucsal.mapa_maturidade_financeira.exception;

/**
 * Login com e-mail ou senha incorretos.
 * Estende IllegalArgumentException para manter compatibilidade com o restante do código,
 * mas é tratada separadamente pelo GlobalExceptionHandler (HTTP 401).
 */
public class CredenciaisInvalidasException extends IllegalArgumentException {

    public CredenciaisInvalidasException(String mensagem) {
        super(mensagem);
    }
}

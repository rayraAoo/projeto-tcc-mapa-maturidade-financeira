package br.edu.ucsal.mapa_maturidade_financeira.exception;

/** Usuário excedeu o limite de uso de um recurso (HTTP 429). */
public class LimiteDeUsoException extends RuntimeException {

    public LimiteDeUsoException(String mensagem) {
        super(mensagem);
    }
}

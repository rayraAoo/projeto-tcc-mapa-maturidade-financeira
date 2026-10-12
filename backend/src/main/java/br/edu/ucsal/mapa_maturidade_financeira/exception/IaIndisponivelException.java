package br.edu.ucsal.mapa_maturidade_financeira.exception;

/**
 * O serviço externo de IA não respondeu ou não está configurado.
 * A mensagem é genérica de propósito: nunca deve conter URL, chave ou detalhes internos.
 */
public class IaIndisponivelException extends RuntimeException {

    public IaIndisponivelException(String mensagem) {
        super(mensagem);
    }
}

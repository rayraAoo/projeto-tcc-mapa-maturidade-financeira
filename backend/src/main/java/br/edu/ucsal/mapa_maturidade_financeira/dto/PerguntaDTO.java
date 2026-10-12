package br.edu.ucsal.mapa_maturidade_financeira.dto;

import br.edu.ucsal.mapa_maturidade_financeira.model.Pergunta;

/**
 * Pergunta como o aluno a vê ANTES de responder.
 * Não contém a alternativa correta nem a explicação: elas só voltam na correção (ResultadoQuizResponse).
 */
public class PerguntaDTO {

    private final Long id;
    private final Long moduloId;
    private final String enunciado;
    private final String alternativaA;
    private final String alternativaB;
    private final String alternativaC;
    private final String alternativaD;

    public PerguntaDTO(Long id, Long moduloId, String enunciado,
                       String alternativaA, String alternativaB, String alternativaC, String alternativaD) {
        this.id = id;
        this.moduloId = moduloId;
        this.enunciado = enunciado;
        this.alternativaA = alternativaA;
        this.alternativaB = alternativaB;
        this.alternativaC = alternativaC;
        this.alternativaD = alternativaD;
    }

    public static PerguntaDTO de(Pergunta p) {
        return new PerguntaDTO(
                p.getId(),
                p.getModulo().getId(),
                p.getEnunciado(),
                p.getAlternativaA(),
                p.getAlternativaB(),
                p.getAlternativaC(),
                p.getAlternativaD()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getModuloId() {
        return moduloId;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public String getAlternativaA() {
        return alternativaA;
    }

    public String getAlternativaB() {
        return alternativaB;
    }

    public String getAlternativaC() {
        return alternativaC;
    }

    public String getAlternativaD() {
        return alternativaD;
    }
}

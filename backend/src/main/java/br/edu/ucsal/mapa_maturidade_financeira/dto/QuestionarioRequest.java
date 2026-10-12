package br.edu.ucsal.mapa_maturidade_financeira.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class QuestionarioRequest {

    private static final String LETRA = "^[A-Da-d]$";
    private static final String MSG = "Responda com uma das alternativas: A, B, C ou D.";

    @NotBlank(message = MSG)
    @Pattern(regexp = LETRA, message = MSG)
    private String respostaOrcamento;

    @NotBlank(message = MSG)
    @Pattern(regexp = LETRA, message = MSG)
    private String respostaReservaEmergencia;

    @NotBlank(message = MSG)
    @Pattern(regexp = LETRA, message = MSG)
    private String respostaDivida;

    @NotBlank(message = MSG)
    @Pattern(regexp = LETRA, message = MSG)
    private String respostaConhecimentoInvestimento;

    @NotBlank(message = MSG)
    @Pattern(regexp = LETRA, message = MSG)
    private String respostaExperienciaInvestimento;

    public String getRespostaOrcamento() {
        return respostaOrcamento;
    }

    public void setRespostaOrcamento(String respostaOrcamento) {
        this.respostaOrcamento = respostaOrcamento;
    }

    public String getRespostaReservaEmergencia() {
        return respostaReservaEmergencia;
    }

    public void setRespostaReservaEmergencia(String respostaReservaEmergencia) {
        this.respostaReservaEmergencia = respostaReservaEmergencia;
    }

    public String getRespostaDivida() {
        return respostaDivida;
    }

    public void setRespostaDivida(String respostaDivida) {
        this.respostaDivida = respostaDivida;
    }

    public String getRespostaConhecimentoInvestimento() {
        return respostaConhecimentoInvestimento;
    }

    public void setRespostaConhecimentoInvestimento(String respostaConhecimentoInvestimento) {
        this.respostaConhecimentoInvestimento = respostaConhecimentoInvestimento;
    }

    public String getRespostaExperienciaInvestimento() {
        return respostaExperienciaInvestimento;
    }

    public void setRespostaExperienciaInvestimento(String respostaExperienciaInvestimento) {
        this.respostaExperienciaInvestimento = respostaExperienciaInvestimento;
    }
}

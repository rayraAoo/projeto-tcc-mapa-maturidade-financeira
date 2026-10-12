package br.edu.ucsal.mapa_maturidade_financeira.dto;

import java.time.LocalDateTime;

public class ResultadoSimulacaoResponse {

    private Long historicoId;
    private String tituloCenario;
    private String opcaoEscolhida;
    private Double saldoInicial;
    private Double impactoFinanceiro;
    private Double saldoFinal;
    private Integer pontuacao;
    private String feedbackEducativo;
    private LocalDateTime dataSimulacao;

    public ResultadoSimulacaoResponse() {
    }

    public ResultadoSimulacaoResponse(Long historicoId, String tituloCenario, String opcaoEscolhida,
                                       Double saldoInicial, Double impactoFinanceiro, Double saldoFinal,
                                       Integer pontuacao, String feedbackEducativo, LocalDateTime dataSimulacao) {
        this.historicoId = historicoId;
        this.tituloCenario = tituloCenario;
        this.opcaoEscolhida = opcaoEscolhida;
        this.saldoInicial = saldoInicial;
        this.impactoFinanceiro = impactoFinanceiro;
        this.saldoFinal = saldoFinal;
        this.pontuacao = pontuacao;
        this.feedbackEducativo = feedbackEducativo;
        this.dataSimulacao = dataSimulacao;
    }

    public Long getHistoricoId() {
        return historicoId;
    }

    public void setHistoricoId(Long historicoId) {
        this.historicoId = historicoId;
    }

    public String getTituloCenario() {
        return tituloCenario;
    }

    public void setTituloCenario(String tituloCenario) {
        this.tituloCenario = tituloCenario;
    }

    public String getOpcaoEscolhida() {
        return opcaoEscolhida;
    }

    public void setOpcaoEscolhida(String opcaoEscolhida) {
        this.opcaoEscolhida = opcaoEscolhida;
    }

    public Double getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(Double saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public Double getImpactoFinanceiro() {
        return impactoFinanceiro;
    }

    public void setImpactoFinanceiro(Double impactoFinanceiro) {
        this.impactoFinanceiro = impactoFinanceiro;
    }

    public Double getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(Double saldoFinal) {
        this.saldoFinal = saldoFinal;
    }

    public Integer getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(Integer pontuacao) {
        this.pontuacao = pontuacao;
    }

    public String getFeedbackEducativo() {
        return feedbackEducativo;
    }

    public void setFeedbackEducativo(String feedbackEducativo) {
        this.feedbackEducativo = feedbackEducativo;
    }

    public LocalDateTime getDataSimulacao() {
        return dataSimulacao;
    }

    public void setDataSimulacao(LocalDateTime dataSimulacao) {
        this.dataSimulacao = dataSimulacao;
    }
}

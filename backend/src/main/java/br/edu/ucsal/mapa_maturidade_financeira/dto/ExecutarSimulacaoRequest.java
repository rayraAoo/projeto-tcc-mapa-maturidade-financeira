package br.edu.ucsal.mapa_maturidade_financeira.dto;

import jakarta.validation.constraints.NotNull;

public class ExecutarSimulacaoRequest {

    @NotNull(message = "Informe o cenário.")
    private Long cenarioId;
    @NotNull(message = "Informe a opção escolhida.")
    private Long opcaoId;

    public ExecutarSimulacaoRequest() {
    }

    public ExecutarSimulacaoRequest(Long cenarioId, Long opcaoId) {
        this.cenarioId = cenarioId;
        this.opcaoId = opcaoId;
    }

    public Long getCenarioId() {
        return cenarioId;
    }

    public void setCenarioId(Long cenarioId) {
        this.cenarioId = cenarioId;
    }

    public Long getOpcaoId() {
        return opcaoId;
    }

    public void setOpcaoId(Long opcaoId) {
        this.opcaoId = opcaoId;
    }
}

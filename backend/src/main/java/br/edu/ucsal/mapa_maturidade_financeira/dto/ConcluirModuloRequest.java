package br.edu.ucsal.mapa_maturidade_financeira.dto;

import jakarta.validation.constraints.NotNull;

public class ConcluirModuloRequest {

    @NotNull(message = "Informe o módulo.")
    private Long moduloId;

    public Long getModuloId() {
        return moduloId;
    }

    public void setModuloId(Long moduloId) {
        this.moduloId = moduloId;
    }
}

package br.edu.ucsal.mapa_maturidade_financeira.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

/** Autorização explícita, a cada uso, para enviar dados de aprendizagem (sem nome) ao serviço de IA. */
public class GerarDiagnosticoRequest {

    @NotNull(message = "É necessário autorizar o envio dos dados de aprendizagem ao serviço de IA.")
    @AssertTrue(message = "É necessário autorizar o envio dos dados de aprendizagem ao serviço de IA.")
    private Boolean autorizaEnvioIa;

    public Boolean getAutorizaEnvioIa() {
        return autorizaEnvioIa;
    }

    public void setAutorizaEnvioIa(Boolean autorizaEnvioIa) {
        this.autorizaEnvioIa = autorizaEnvioIa;
    }
}

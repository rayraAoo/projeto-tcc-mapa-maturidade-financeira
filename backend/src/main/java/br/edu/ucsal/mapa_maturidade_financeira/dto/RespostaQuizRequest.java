package br.edu.ucsal.mapa_maturidade_financeira.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class RespostaQuizRequest {

    @NotNull(message = "Informe a pergunta.")
    private Long perguntaId;

    @NotBlank(message = "Informe a alternativa escolhida.")
    @Pattern(regexp = "^[A-Da-d]$", message = "Responda com uma das alternativas: A, B, C ou D.")
    private String alternativaEscolhida; // "A", "B", "C" ou "D"

    public Long getPerguntaId() {
        return perguntaId;
    }

    public void setPerguntaId(Long perguntaId) {
        this.perguntaId = perguntaId;
    }

    public String getAlternativaEscolhida() {
        return alternativaEscolhida;
    }

    public void setAlternativaEscolhida(String alternativaEscolhida) {
        this.alternativaEscolhida = alternativaEscolhida;
    }
}

package br.edu.ucsal.mapa_maturidade_financeira.dto;

import java.time.LocalDateTime;

public class DiagnosticoResponse {

    private static final String AVISO_IA =
            "Texto gerado por inteligência artificial. Conteúdo educacional, sem recomendação de investimento.";
    private static final String AVISO_REGRAS =
            "Texto gerado automaticamente por regras da plataforma (sem IA). "
            + "Conteúdo educacional, sem recomendação de investimento.";

    private final String texto;
    private final LocalDateTime dataGeracao;
    private final boolean geradoPorIa;
    private final String aviso;

    public DiagnosticoResponse(String texto, LocalDateTime dataGeracao, boolean geradoPorIa) {
        this.texto = texto;
        this.dataGeracao = dataGeracao;
        this.geradoPorIa = geradoPorIa;
        this.aviso = geradoPorIa ? AVISO_IA : AVISO_REGRAS;
    }

    public String getTexto() {
        return texto;
    }

    public LocalDateTime getDataGeracao() {
        return dataGeracao;
    }

    public boolean isGeradoPorIa() {
        return geradoPorIa;
    }

    public String getAviso() {
        return aviso;
    }
}

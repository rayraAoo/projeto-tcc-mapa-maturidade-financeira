package br.edu.ucsal.mapa_maturidade_financeira.dto;

import java.util.List;

/** Informações públicas (sem login): faixa etária indicada, texto de ciência e orientações aos responsáveis. */
public class TermosResponse {

    private final String faixaEtariaIndicada;
    private final int idadeMinima;
    private final int maioridade;
    private final String versaoTermoCiencia;
    private final String textoCiencia;
    private final List<String> informacoesResponsaveis;

    public TermosResponse(String faixaEtariaIndicada, int idadeMinima, int maioridade,
                          String versaoTermoCiencia, String textoCiencia, List<String> informacoesResponsaveis) {
        this.faixaEtariaIndicada = faixaEtariaIndicada;
        this.idadeMinima = idadeMinima;
        this.maioridade = maioridade;
        this.versaoTermoCiencia = versaoTermoCiencia;
        this.textoCiencia = textoCiencia;
        this.informacoesResponsaveis = informacoesResponsaveis;
    }

    public String getFaixaEtariaIndicada() {
        return faixaEtariaIndicada;
    }

    public int getIdadeMinima() {
        return idadeMinima;
    }

    public int getMaioridade() {
        return maioridade;
    }

    public String getVersaoTermoCiencia() {
        return versaoTermoCiencia;
    }

    public String getTextoCiencia() {
        return textoCiencia;
    }

    public List<String> getInformacoesResponsaveis() {
        return informacoesResponsaveis;
    }
}

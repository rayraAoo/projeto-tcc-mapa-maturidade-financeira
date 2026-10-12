package br.edu.ucsal.mapa_maturidade_financeira.dto;

public class OpcaoSimulacaoDTO {

    private Long id;
    private String descricao;
    private Double impactoFinanceiro;

    public OpcaoSimulacaoDTO() {
    }

    public OpcaoSimulacaoDTO(Long id, String descricao, Double impactoFinanceiro) {
        this.id = id;
        this.descricao = descricao;
        this.impactoFinanceiro = impactoFinanceiro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getImpactoFinanceiro() {
        return impactoFinanceiro;
    }

    public void setImpactoFinanceiro(Double impactoFinanceiro) {
        this.impactoFinanceiro = impactoFinanceiro;
    }
}

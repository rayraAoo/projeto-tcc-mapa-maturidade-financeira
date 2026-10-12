package br.edu.ucsal.mapa_maturidade_financeira.dto;

import java.util.List;

public class CenarioSimulacaoDTO {

    private Long id;
    private String titulo;
    private String descricao;
    private Double saldoInicial;
    private String categoria;
    private List<OpcaoSimulacaoDTO> opcoes;

    public CenarioSimulacaoDTO() {
    }

    public CenarioSimulacaoDTO(Long id, String titulo, String descricao, Double saldoInicial, String categoria, List<OpcaoSimulacaoDTO> opcoes) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.saldoInicial = saldoInicial;
        this.categoria = categoria;
        this.opcoes = opcoes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(Double saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public List<OpcaoSimulacaoDTO> getOpcoes() {
        return opcoes;
    }

    public void setOpcoes(List<OpcaoSimulacaoDTO> opcoes) {
        this.opcoes = opcoes;
    }
}

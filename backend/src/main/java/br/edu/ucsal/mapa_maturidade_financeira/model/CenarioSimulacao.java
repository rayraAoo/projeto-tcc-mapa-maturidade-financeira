package br.edu.ucsal.mapa_maturidade_financeira.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cenario_simulacao")
public class CenarioSimulacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "saldo_inicial", nullable = false)
    private Double saldoInicial;

    @Column(nullable = false)
    private String categoria;

    public CenarioSimulacao() {
    }

    public CenarioSimulacao(String titulo, String descricao, Double saldoInicial, String categoria) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.saldoInicial = saldoInicial;
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
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
}

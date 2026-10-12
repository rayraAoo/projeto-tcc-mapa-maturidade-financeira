package br.edu.ucsal.mapa_maturidade_financeira.model;

import jakarta.persistence.*;

@Entity
@Table(name = "modulo")
public class Modulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String conteudo;

    @Column(nullable = false)
    private Integer camada; // 1 = educação financeira geral, 2 = sistema financeiro, 3 = investimento

    @Column(nullable = false)
    private Integer ordem; // ordem de exibição dentro da camada

    public Modulo() {
    }

    public Modulo(String titulo, String conteudo, Integer camada, Integer ordem) {
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.camada = camada;
        this.ordem = ordem;
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

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Integer getCamada() {
        return camada;
    }

    public void setCamada(Integer camada) {
        this.camada = camada;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }
}

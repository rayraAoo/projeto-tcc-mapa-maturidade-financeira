package br.edu.ucsal.mapa_maturidade_financeira.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pergunta")
public class Pergunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "modulo_id", nullable = false)
    private Modulo modulo;

    @Column(nullable = false)
    private String enunciado;

    @Column(nullable = false)
    private String alternativaA;

    @Column(nullable = false)
    private String alternativaB;

    @Column(nullable = false)
    private String alternativaC;

    @Column(nullable = false)
    private String alternativaD;

    @Column(name = "alternativa_correta", nullable = false)
    private String alternativaCorreta; // "A", "B", "C" ou "D"

    @Column(nullable = false, columnDefinition = "TEXT")
    private String explicacao;

    public Pergunta() {
    }

    public Pergunta(Modulo modulo, String enunciado, String alternativaA, String alternativaB, String alternativaC, String alternativaD, String alternativaCorreta, String explicacao) {
        this.modulo = modulo;
        this.enunciado = enunciado;
        this.alternativaA = alternativaA;
        this.alternativaB = alternativaB;
        this.alternativaC = alternativaC;
        this.alternativaD = alternativaD;
        this.alternativaCorreta = alternativaCorreta;
        this.explicacao = explicacao;
    }

    public Long getId() {
        return id;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public String getAlternativaA() {
        return alternativaA;
    }

    public void setAlternativaA(String alternativaA) {
        this.alternativaA = alternativaA;
    }

    public String getAlternativaB() {
        return alternativaB;
    }

    public void setAlternativaB(String alternativaB) {
        this.alternativaB = alternativaB;
    }

    public String getAlternativaC() {
        return alternativaC;
    }

    public void setAlternativaC(String alternativaC) {
        this.alternativaC = alternativaC;
    }

    public String getAlternativaD() {
        return alternativaD;
    }

    public void setAlternativaD(String alternativaD) {
        this.alternativaD = alternativaD;
    }

    public String getAlternativaCorreta() {
        return alternativaCorreta;
    }

    public void setAlternativaCorreta(String alternativaCorreta) {
        this.alternativaCorreta = alternativaCorreta;
    }

    public String getExplicacao() {
        return explicacao;
    }

    public void setExplicacao(String explicacao) {
        this.explicacao = explicacao;
    }
}

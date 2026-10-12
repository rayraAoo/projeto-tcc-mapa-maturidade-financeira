package br.edu.ucsal.mapa_maturidade_financeira.model;

import jakarta.persistence.*;

@Entity
@Table(name = "opcao_simulacao")
public class OpcaoSimulacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cenario_id", nullable = false)
    private CenarioSimulacao cenario;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "impacto_financeiro", nullable = false)
    private Double impactoFinanceiro;

    @Column(nullable = false)
    private Integer pontuacao;

    @Column(name = "feedback_educativo", nullable = false, columnDefinition = "TEXT")
    private String feedbackEducativo;

    public OpcaoSimulacao() {
    }

    public OpcaoSimulacao(CenarioSimulacao cenario, String descricao, Double impactoFinanceiro, Integer pontuacao, String feedbackEducativo) {
        this.cenario = cenario;
        this.descricao = descricao;
        this.impactoFinanceiro = impactoFinanceiro;
        this.pontuacao = pontuacao;
        this.feedbackEducativo = feedbackEducativo;
    }

    public Long getId() {
        return id;
    }

    public CenarioSimulacao getCenario() {
        return cenario;
    }

    public void setCenario(CenarioSimulacao cenario) {
        this.cenario = cenario;
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

    public Integer getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(Integer pontuacao) {
        this.pontuacao = pontuacao;
    }

    public String getFeedbackEducativo() {
        return feedbackEducativo;
    }

    public void setFeedbackEducativo(String feedbackEducativo) {
        this.feedbackEducativo = feedbackEducativo;
    }
}

package br.edu.ucsal.mapa_maturidade_financeira.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historico_simulacao")
public class HistoricoSimulacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cenario_id", nullable = false)
    private CenarioSimulacao cenario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opcao_id", nullable = false)
    private OpcaoSimulacao opcaoEscolhida;

    @Column(name = "saldo_inicial", nullable = false)
    private Double saldoInicial;

    @Column(name = "impacto_financeiro", nullable = false)
    private Double impactoFinanceiro;

    @Column(name = "saldo_final", nullable = false)
    private Double saldoFinal;

    @Column(nullable = false)
    private Integer pontuacao;

    @Column(name = "feedback_recebido", nullable = false, columnDefinition = "TEXT")
    private String feedbackRecebido;

    @Column(name = "data_simulacao", nullable = false)
    private LocalDateTime dataSimulacao;

    public HistoricoSimulacao() {
    }

    public HistoricoSimulacao(Usuario usuario, CenarioSimulacao cenario, OpcaoSimulacao opcaoEscolhida,
                              Double saldoInicial, Double impactoFinanceiro, Double saldoFinal,
                              Integer pontuacao, String feedbackRecebido, LocalDateTime dataSimulacao) {
        this.usuario = usuario;
        this.cenario = cenario;
        this.opcaoEscolhida = opcaoEscolhida;
        this.saldoInicial = saldoInicial;
        this.impactoFinanceiro = impactoFinanceiro;
        this.saldoFinal = saldoFinal;
        this.pontuacao = pontuacao;
        this.feedbackRecebido = feedbackRecebido;
        this.dataSimulacao = dataSimulacao;
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public CenarioSimulacao getCenario() {
        return cenario;
    }

    public void setCenario(CenarioSimulacao cenario) {
        this.cenario = cenario;
    }

    public OpcaoSimulacao getOpcaoEscolhida() {
        return opcaoEscolhida;
    }

    public void setOpcaoEscolhida(OpcaoSimulacao opcaoEscolhida) {
        this.opcaoEscolhida = opcaoEscolhida;
    }

    public Double getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(Double saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public Double getImpactoFinanceiro() {
        return impactoFinanceiro;
    }

    public void setImpactoFinanceiro(Double impactoFinanceiro) {
        this.impactoFinanceiro = impactoFinanceiro;
    }

    public Double getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(Double saldoFinal) {
        this.saldoFinal = saldoFinal;
    }

    public Integer getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(Integer pontuacao) {
        this.pontuacao = pontuacao;
    }

    public String getFeedbackRecebido() {
        return feedbackRecebido;
    }

    public void setFeedbackRecebido(String feedbackRecebido) {
        this.feedbackRecebido = feedbackRecebido;
    }

    public LocalDateTime getDataSimulacao() {
        return dataSimulacao;
    }

    public void setDataSimulacao(LocalDateTime dataSimulacao) {
        this.dataSimulacao = dataSimulacao;
    }
}

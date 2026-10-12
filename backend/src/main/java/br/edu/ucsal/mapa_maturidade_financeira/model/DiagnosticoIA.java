package br.edu.ucsal.mapa_maturidade_financeira.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "diagnostico_ia")
public class DiagnosticoIA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String textoDiagnostico;

    @Column(name = "data_geracao", nullable = false)
    private LocalDateTime dataGeracao;

    // "IA" (gerado pelo modelo) ou "REGRAS" (texto de contingência). Nulo em registros antigos = IA.
    @Column(name = "origem")
    private String origem;

    public DiagnosticoIA() {
    }

    public DiagnosticoIA(Usuario usuario, String textoDiagnostico, LocalDateTime dataGeracao) {
        this.usuario = usuario;
        this.textoDiagnostico = textoDiagnostico;
        this.dataGeracao = dataGeracao;
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

    public String getTextoDiagnostico() {
        return textoDiagnostico;
    }

    public void setTextoDiagnostico(String textoDiagnostico) {
        this.textoDiagnostico = textoDiagnostico;
    }

    public LocalDateTime getDataGeracao() {
        return dataGeracao;
    }

    public void setDataGeracao(LocalDateTime dataGeracao) {
        this.dataGeracao = dataGeracao;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }
}

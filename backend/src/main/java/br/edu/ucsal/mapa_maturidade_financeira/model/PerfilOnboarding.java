package br.edu.ucsal.mapa_maturidade_financeira.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "perfil_onboarding")
public class PerfilOnboarding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "perfil_calculado", nullable = false)
    private String perfilCalculado;

    @Column(name = "pontuacao_total", nullable = false)
    private Integer pontuacaoTotal;

    // Soma de orçamento + reserva + dívida (0 a 9). Nulo em registros antigos.
    @Column(name = "pontuacao_base")
    private Integer pontuacaoBase;

    public PerfilOnboarding() {
    }

    public PerfilOnboarding(Usuario usuario, String perfilCalculado, Integer pontuacaoTotal) {
        this.usuario = usuario;
        this.perfilCalculado = perfilCalculado;
        this.pontuacaoTotal = pontuacaoTotal;
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

    public String getPerfilCalculado() {
        return perfilCalculado;
    }

    public void setPerfilCalculado(String perfilCalculado) {
        this.perfilCalculado = perfilCalculado;
    }

    public Integer getPontuacaoTotal() {
        return pontuacaoTotal;
    }

    public void setPontuacaoTotal(Integer pontuacaoTotal) {
        this.pontuacaoTotal = pontuacaoTotal;
    }

    public Integer getPontuacaoBase() {
        return pontuacaoBase;
    }

    public void setPontuacaoBase(Integer pontuacaoBase) {
        this.pontuacaoBase = pontuacaoBase;
    }
}

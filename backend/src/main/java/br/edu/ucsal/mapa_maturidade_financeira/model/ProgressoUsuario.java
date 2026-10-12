package br.edu.ucsal.mapa_maturidade_financeira.model;

import jakarta.persistence.*;

@Entity
@Table(name = "progresso_usuario")
public class ProgressoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "modulo_id", nullable = false)
    private Modulo modulo;

    @Column(nullable = false)
    private boolean concluido;

    public ProgressoUsuario() {
    }

    public ProgressoUsuario(Usuario usuario, Modulo modulo, boolean concluido) {
        this.usuario = usuario;
        this.modulo = modulo;
        this.concluido = concluido;
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

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public boolean isConcluido() {
        return concluido;
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }
}

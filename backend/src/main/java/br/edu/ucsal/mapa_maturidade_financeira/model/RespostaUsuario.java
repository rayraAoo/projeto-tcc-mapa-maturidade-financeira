package br.edu.ucsal.mapa_maturidade_financeira.model;

import jakarta.persistence.*;

@Entity
@Table(name = "resposta_usuario")
public class RespostaUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "pergunta_id", nullable = false)
    private Pergunta pergunta;

    @Column(nullable = false)
    private boolean acertou;

    public RespostaUsuario() {
    }

    public RespostaUsuario(Usuario usuario, Pergunta pergunta, boolean acertou) {
        this.usuario = usuario;
        this.pergunta = pergunta;
        this.acertou = acertou;
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

    public Pergunta getPergunta() {
        return pergunta;
    }

    public void setPergunta(Pergunta pergunta) {
        this.pergunta = pergunta;
    }

    public boolean isAcertou() {
        return acertou;
    }

    public void setAcertou(boolean acertou) {
        this.acertou = acertou;
    }
}

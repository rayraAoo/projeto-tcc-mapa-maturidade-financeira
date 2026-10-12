package br.edu.ucsal.mapa_maturidade_financeira.dto;

import br.edu.ucsal.mapa_maturidade_financeira.model.Usuario;

/** Dados do usuário que podem sair pela API. Nunca inclui a senha nem seu hash. */
public class UsuarioResponse {

    private final Long id;
    private final String nome;
    private final String email;

    public UsuarioResponse(Long id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public static UsuarioResponse de(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }
}

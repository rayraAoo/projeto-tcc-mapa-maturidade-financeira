package br.edu.ucsal.mapa_maturidade_financeira.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore // o hash da senha nunca deve sair em nenhuma resposta da API
    @Column(nullable = false)
    private String senha;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    // Declaração de ciência dos responsáveis (somente para menores de 18 anos).
    // Colunas opcionais: usuários maiores de idade ficam com esses campos nulos.
    @Column(name = "ciencia_responsavel")
    private Boolean cienciaResponsavel;

    @Column(name = "data_ciencia_responsavel")
    private LocalDateTime dataCienciaResponsavel;

    @Column(name = "versao_termo_ciencia")
    private String versaoTermoCiencia;

    public Usuario() {
    }

    public Usuario(String nome, String email, String senha, LocalDate dataNascimento) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Boolean getCienciaResponsavel() {
        return cienciaResponsavel;
    }

    public void setCienciaResponsavel(Boolean cienciaResponsavel) {
        this.cienciaResponsavel = cienciaResponsavel;
    }

    public LocalDateTime getDataCienciaResponsavel() {
        return dataCienciaResponsavel;
    }

    public void setDataCienciaResponsavel(LocalDateTime dataCienciaResponsavel) {
        this.dataCienciaResponsavel = dataCienciaResponsavel;
    }

    public String getVersaoTermoCiencia() {
        return versaoTermoCiencia;
    }

    public void setVersaoTermoCiencia(String versaoTermoCiencia) {
        this.versaoTermoCiencia = versaoTermoCiencia;
    }
}

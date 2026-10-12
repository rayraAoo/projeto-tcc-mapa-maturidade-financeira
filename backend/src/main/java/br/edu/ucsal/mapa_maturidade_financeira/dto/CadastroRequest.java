package br.edu.ucsal.mapa_maturidade_financeira.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class CadastroRequest {

    @NotBlank(message = "Informe o nome.")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
    private String nome;

    @NotBlank(message = "Informe o e-mail.")
    @Email(message = "E-mail inválido.")
    @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres.")
    private String email;

    @NotBlank(message = "Informe a senha.")
    @Size(min = 8, max = 72, message = "A senha deve ter entre 8 e 72 caracteres.")
    private String senha;

    @NotNull(message = "Informe a data de nascimento.")
    @Past(message = "A data de nascimento deve estar no passado.")
    private LocalDate dataNascimento;

    /**
     * Obrigatório (true) apenas para menores de 18 anos: declaração de que os pais ou
     * responsáveis estão cientes do uso da plataforma. Para maiores de idade é ignorado.
     */
    private Boolean confirmaCienciaResponsavel;

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

    public Boolean getConfirmaCienciaResponsavel() {
        return confirmaCienciaResponsavel;
    }

    public void setConfirmaCienciaResponsavel(Boolean confirmaCienciaResponsavel) {
        this.confirmaCienciaResponsavel = confirmaCienciaResponsavel;
    }
}

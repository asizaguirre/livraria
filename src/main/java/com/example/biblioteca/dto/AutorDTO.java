package com.example.biblioteca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AutorDTO {

    private Integer codAu;

    @NotBlank(message = "O nome do autor é obrigatório.")
    @Size(max = 40, message = "O nome do autor deve ter no máximo 40 caracteres.")
    private String nome;

    // Getters and Setters
    public Integer getCodAu() {
        return codAu;
    }

    public void setCodAu(Integer codAu) {
        this.codAu = codAu;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}

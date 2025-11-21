package com.example.biblioteca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AssuntoDTO {

    private Integer codAs;

    @NotBlank(message = "A descrição do assunto é obrigatória.")
    @Size(max = 20, message = "A descrição do assunto deve ter no máximo 20 caracteres.")
    private String descricao;

    // Getters and Setters
    public Integer getCodAs() {
        return codAs;
    }

    public void setCodAs(Integer codAs) {
        this.codAs = codAs;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}

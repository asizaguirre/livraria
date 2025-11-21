package com.example.biblioteca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssuntoDTO {

    private Long id;

    @NotBlank(message = "A descrição do assunto é obrigatória")
    @Size(max = 100, message = "A descrição do assunto não pode exceder 100 caracteres")
    private String descricao;
}
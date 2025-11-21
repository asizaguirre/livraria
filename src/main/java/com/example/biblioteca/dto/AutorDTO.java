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
public class AutorDTO {

    private Long id;

    @NotBlank(message = "O nome do autor é obrigatório")
    @Size(max = 100, message = "O nome do autor não pode exceder 100 caracteres")
    private String nome;
}
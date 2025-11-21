package com.example.biblioteca.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LivroDTO {

    private Long id;

    @NotBlank(message = "O título do livro é obrigatório")
    @Size(max = 200, message = "O título do livro não pode exceder 200 caracteres")
    private String titulo;

    @NotBlank(message = "A editora do livro é obrigatória")
    @Size(max = 100, message = "A editora do livro não pode exceder 100 caracteres")
    private String editora;

    @Size(max = 50, message = "A edição do livro não pode exceder 50 caracteres")
    private String edicao;

    @Pattern(regexp = "\\d{4}", message = "O ano de publicação deve ter 4 dígitos")
    private String anoPublicacao;

    @NotNull(message = "O valor do livro é obrigatório")
    @DecimalMin(value = "0.00", inclusive = true, message = "O valor do livro deve ser igual ou maior que 0.00")
    private BigDecimal valor;

    private Set<Long> autoresIds;

    private Set<Long> assuntosIds;
}
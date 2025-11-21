package com.example.biblioteca.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioLivroDTO {
    private String autorNome;
    private String tituloLivro;
    private String editoraLivro;
    private String edicaoLivro;
    private String anoPublicacaoLivro;
    private BigDecimal valorLivro;
    private String assuntosLivro; // Concatenated string of subjects
}
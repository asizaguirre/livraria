package com.example.biblioteca.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public class LivroDTO {

    private Integer codL;

    @NotBlank(message = "O título do livro é obrigatório.")
    @Size(max = 40, message = "O título do livro deve ter no máximo 40 caracteres.")
    private String titulo;

    @NotBlank(message = "A editora do livro é obrigatória.")
    @Size(max = 40, message = "A editora do livro deve ter no máximo 40 caracteres.")
    private String editora;

    @NotNull(message = "A edição do livro é obrigatória.")
    private Integer edicao;

    @NotBlank(message = "O ano de publicação é obrigatório.")
    @Pattern(regexp = "\\d{4}", message = "O ano de publicação deve estar no formato AAAA.")
    private String anoPublicacao;

    @NotNull(message = "O valor do livro é obrigatório.")
    @DecimalMin(value = "0.01", message = "O valor do livro deve ser maior que zero.")
    private BigDecimal valor;

    @NotEmpty(message = "O livro deve ter pelo menos um autor.")
    private List<Integer> autoresIds;

    @NotEmpty(message = "O livro deve ter pelo menos um assunto.")
    private List<Integer> assuntosIds;

    // Getters and Setters

    public Integer getCodL() {
        return codL;
    }

    public void setCodL(Integer codL) {
        this.codL = codL;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public Integer getEdicao() {
        return edicao;
    }

    public void setEdicao(Integer edicao) {
        this.edicao = edicao;
    }

    public String getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(String anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public List<Integer> getAutoresIds() {
        return autoresIds;
    }

    public void setAutoresIds(List<Integer> autoresIds) {
        this.autoresIds = autoresIds;
    }

    public List<Integer> getAssuntosIds() {
        return assuntosIds;
    }

    public void setAssuntosIds(List<Integer> assuntosIds) {
        this.assuntosIds = assuntosIds;
    }
}

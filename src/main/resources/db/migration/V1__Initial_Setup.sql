-- V1__Initial_Setup.sql
CREATE TABLE autores (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

CREATE TABLE assuntos (
    id BIGSERIAL PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL
);

CREATE TABLE livros (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    editora VARCHAR(100) NOT NULL,
    edicao VARCHAR(50),
    ano_publicacao VARCHAR(4),
    valor NUMERIC(10, 2) NOT NULL
);

CREATE TABLE livro_autor (
    livro_id BIGINT NOT NULL,
    autor_id BIGINT NOT NULL,
    PRIMARY KEY (livro_id, autor_id),
    FOREIGN KEY (livro_id) REFERENCES livros(id) ON DELETE CASCADE,
    FOREIGN KEY (autor_id) REFERENCES autores(id) ON DELETE CASCADE
);

CREATE TABLE livro_assunto (
    livro_id BIGINT NOT NULL,
    assunto_id BIGINT NOT NULL,
    PRIMARY KEY (livro_id, assunto_id),
    FOREIGN KEY (livro_id) REFERENCES livros(id) ON DELETE CASCADE,
    FOREIGN KEY (assunto_id) REFERENCES assuntos(id) ON DELETE CASCADE
);

-- View para relatórios
CREATE OR REPLACE VIEW vw_relatorio_livros_por_autor AS
SELECT
    a.nome AS autor_nome,
    l.titulo AS titulo_livro,
    l.editora AS editora_livro,
    l.edicao AS edicao_livro,
    l.ano_publicacao AS ano_publicacao_livro,
    l.valor AS valor_livro,
    STRING_AGG(ass.descricao, ', ') AS assuntos_livro
FROM autores a
JOIN livro_autor la ON a.id = la.autor_id
JOIN livros l ON l.id = la.livro_id
LEFT JOIN livro_assunto las ON l.id = las.livro_id
LEFT JOIN assuntos ass ON ass.id = las.assunto_id
GROUP BY a.nome, l.titulo, l.editora, l.edicao, l.ano_publicacao, l.valor
ORDER BY a.nome, l.titulo;

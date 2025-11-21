package com.example.biblioteca.mapper;

import com.example.biblioteca.domain.Assunto;
import com.example.biblioteca.domain.Autor;
import com.example.biblioteca.domain.Livro;
import com.example.biblioteca.dto.LivroDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface LivroMapper {

    LivroMapper INSTANCE = Mappers.getMapper(LivroMapper.class);

    @Mapping(target = "autoresIds", source = "autores", qualifiedByName = "autoresToAutorIds")
    @Mapping(target = "assuntosIds", source = "assuntos", qualifiedByName = "assuntosToAssuntoIds")
    LivroDTO toDto(Livro livro);

    @Mapping(target = "autores", ignore = true) // Handled by service
    @Mapping(target = "assuntos", ignore = true) // Handled by service
    Livro toEntity(LivroDTO livroDTO);

    @Named("autoresToAutorIds")
    default Set<Long> autoresToAutorIds(Set<Autor> autores) {
        return autores.stream()
                .map(Autor::getId)
                .collect(Collectors.toSet());
    }

    @Named("assuntosToAssuntoIds")
    default Set<Long> assuntosToAssuntoIds(Set<Assunto> assuntos) {
        return assuntos.stream()
                .map(Assunto::getId)
                .collect(Collectors.toSet());
    }
}
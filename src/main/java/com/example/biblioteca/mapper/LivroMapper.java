package com.example.biblioteca.mapper;

import com.example.biblioteca.domain.Assunto;
import com.example.biblioteca.domain.Autor;
import com.example.biblioteca.domain.Livro;
import com.example.biblioteca.dto.LivroDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface LivroMapper {

    LivroMapper INSTANCE = Mappers.getMapper(LivroMapper.class);

    @Mapping(target = "autores", ignore = true)
    @Mapping(target = "assuntos", ignore = true)
    Livro toEntity(LivroDTO livroDTO);

    @Mapping(source = "autores", target = "autoresIds", qualifiedByName = "autoresToIds")
    @Mapping(source = "assuntos", target = "assuntosIds", qualifiedByName = "assuntosToIds")
    LivroDTO toDTO(Livro livro);

    @Named("autoresToIds")
    default List<Integer> autoresToIds(Set<Autor> autores) {
        if (autores == null) {
            return null;
        }
        return autores.stream()
                .map(Autor::getCodAu)
                .collect(Collectors.toList());
    }

    @Named("assuntosToIds")
    default List<Integer> assuntosToIds(Set<Assunto> assuntos) {
        if (assuntos == null) {
            return null;
        }
        return assuntos.stream()
                .map(Assunto::getCodAs)
                .collect(Collectors.toList());
    }
}

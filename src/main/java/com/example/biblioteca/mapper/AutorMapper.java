package com.example.biblioteca.mapper;

import com.example.biblioteca.domain.Autor;
import com.example.biblioteca.dto.AutorDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AutorMapper {

    AutorMapper INSTANCE = Mappers.getMapper(AutorMapper.class);

    Autor toEntity(AutorDTO autorDTO);

    AutorDTO toDTO(Autor autor);
}

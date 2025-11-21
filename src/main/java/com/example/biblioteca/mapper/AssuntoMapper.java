package com.example.biblioteca.mapper;

import com.example.biblioteca.domain.Assunto;
import com.example.biblioteca.dto.AssuntoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AssuntoMapper {

    AssuntoMapper INSTANCE = Mappers.getMapper(AssuntoMapper.class);

    AssuntoDTO toDto(Assunto assunto);

    Assunto toEntity(AssuntoDTO assuntoDTO);
}
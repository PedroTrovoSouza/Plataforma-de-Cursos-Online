package com.cursos_online.matricula_service.mapper;

import com.cursos_online.matricula_service.dto.MatriculaResponseDTO;
import com.cursos_online.matricula_service.dto.MatriulaRequestDTO;
import com.cursos_online.matricula_service.entity.Matricula;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MapperMatricula {

    Matricula toEntity(MatriulaRequestDTO matriulaRequestDTO);
    MatriulaRequestDTO toMatriculaRequestDTO(Matricula entity);
    MatriculaResponseDTO toMatriculaResponse(Matricula entity);
}

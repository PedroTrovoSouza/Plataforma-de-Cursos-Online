package com.cursos_online.matricula_service.mapper;

import com.cursos_online.matricula_service.dto.MatriculaResponseDTO;
import com.cursos_online.matricula_service.dto.MatriculaRequestDTO;
import com.cursos_online.matricula_service.entity.Matricula;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MapperMatricula {

    Matricula toEntity(MatriculaRequestDTO matriculaRequestDTO);
    MatriculaRequestDTO toMatriculaRequestDTO(Matricula entity);
    MatriculaResponseDTO toMatriculaResponse(Matricula entity);
}

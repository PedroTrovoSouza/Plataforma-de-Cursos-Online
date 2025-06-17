package com.cursos_online.matricula_service.service;

import com.cursos.dto.curso.CursoResponseDto;
import com.cursos.dto.curso.CursoResponseMatricula;
import com.cursos_online.matricula_service.dto.AlunoDTO;
import com.cursos_online.matricula_service.dto.MatriculaResponseDTO;
import com.cursos_online.matricula_service.dto.MatriculaRequestDTO;
import com.cursos_online.matricula_service.dto.UsuarioDTO;
import com.cursos_online.matricula_service.entity.Matricula;
import com.cursos_online.matricula_service.mapper.MapperMatricula;
import com.cursos_online.matricula_service.repository.MatriculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private MapperMatricula mapper;

    private final WebClient webCurso;
    private final WebClient webUsuario;

    @Autowired
    public MatriculaService(WebClient.Builder webClientBuilder) {
        this.webCurso = webClientBuilder.baseUrl("http://localhost:8081").build();
        this.webUsuario = webClientBuilder.baseUrl("http://localhost:8082").build();
    }

    public MatriculaResponseDTO cadastrarMatricula(String nomeCurso, String emailUsuario) {
        try {
            if (nomeCurso == null || nomeCurso.isBlank()) {
                throw new RuntimeException("Curso não pode estar em branco!");
            }

            CursoResponseMatricula cursoResponse = webCurso.get()
                    .uri("/cursos/buscar-por-nome/{nome}", nomeCurso)
                    .retrieve()
                    .bodyToMono(CursoResponseMatricula.class)
                    .block();

            Long idCurso = cursoResponse.getId();

            String emailEncoded = UriUtils.encodePathSegment(emailUsuario, StandardCharsets.UTF_8);

            UsuarioDTO usuarioResponse = webUsuario.get()
                    .uri("/usuario/email/{email}", emailUsuario)
                    .retrieve()
                    .bodyToMono(UsuarioDTO.class)
                    .block();

            MatriculaRequestDTO dto = new MatriculaRequestDTO();
            dto.setIdCurso(idCurso);
            dto.setIdUsuario(usuarioResponse.getId());

            Matricula novaMatricula = mapper.toEntity(dto);
            novaMatricula.setDataMatricula(LocalDate.now());
            novaMatricula.setStatus("ATIVA");

            Matricula salva = matriculaRepository.save(novaMatricula);

            MatriculaResponseDTO response = mapper.toMatriculaResponse(salva);
            response.setNomeCurso(cursoResponse.getTitulo());
            response.setData_matricula(LocalDate.now());
            return response;

        } catch (WebClientResponseException.NotFound e) {
            throw new RuntimeException("Curso ou usuário não encontrado.");
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Erro na comunicação: " + e.getMessage());
        }
    }

    public List<MatriculaResponseDTO> buscarMatricula() {
        List<Matricula> listaDeMatriculas = matriculaRepository.findAll();
        if (listaDeMatriculas.isEmpty()) {
            throw new RuntimeException("Nenhuma matrícula foi encontrada");
        }

        return listaDeMatriculas.stream()
                .map(mapper::toMatriculaResponse)
                .collect(Collectors.toList());
    }

    public List<UsuarioDTO> buscarUsuariosMatriculados(long idCurso) {

        System.out.println("ID do curso recebido: " + idCurso);

        List<UsuarioDTO> todosUsuarios = webUsuario.get()
                .uri("/usuario/listarInterno")
                .retrieve()
                .bodyToFlux(UsuarioDTO.class)
                .collectList()
                .block();

        List<Long> idsMatriculados = matriculaRepository.buscarIdsUsuariosPorCurso(idCurso);

        System.out.println("IDS Matriculados: " + idsMatriculados);
        System.out.println("Usuários disponíveis: " + todosUsuarios);

        return todosUsuarios.stream()
                .filter(usuario -> idsMatriculados.contains(usuario.getId()))
                .collect(Collectors.toList());
    }

    public void cancelarMatricula(long idMatricula) {
        Optional<Matricula> matriculaEncontrada = matriculaRepository.findById(idMatricula);
        if (matriculaEncontrada.isEmpty()) {
            throw new RuntimeException("Matrícula não encontrada");
        }
        matriculaRepository.delete(matriculaEncontrada.get());
    }
}

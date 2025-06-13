package com.cursos_online.matricula_service.service;

import com.cursos_online.matricula_service.dto.AlunoDTO;
import com.cursos_online.matricula_service.dto.MatriculaResponseDTO;
import com.cursos_online.matricula_service.dto.MatriulaRequestDTO;
import com.cursos_online.matricula_service.entity.Matricula;
import com.cursos_online.matricula_service.mapper.MapperMatricula;
import com.cursos_online.matricula_service.repository.MatriculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MatriculaService {
    
    @Autowired
    MatriculaRepository matriculaRepository;

    @Autowired
    MapperMatricula mapper;

    private final WebClient webCurso;
    private final WebClient webUsuario;

    @Autowired
    public MatriculaService(WebClient.Builder webClientBuilder){
        this.webCurso = webClientBuilder.baseUrl("http://localhost:8081").build();
        this.webUsuario = webClientBuilder.baseUrl("http://localhost:8082").build();
    }

    public MatriculaResponseDTO cadastrarMatricula(String nomeCurso, String emailUsuario) {
        try {
            if (nomeCurso == null || nomeCurso.isBlank()) {
                throw new RuntimeException("Curso não pode estar em branco!");
            }

            Long idCurso = webCurso.get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("http")
                            .host("localhost")
                            .port(8081)
                            .path("/curso/nome")
                            .queryParam("nome", nomeCurso)
                            .build())
                    .retrieve()
                    .bodyToMono(Long.class)
                    .block();

            Long idUsuario = webUsuario.get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("http")
                            .host("localhost")
                            .port(8082)
                            .path("/usuarios/email")
                            .queryParam("email", emailUsuario)
                            .build())
                    .retrieve()
                    .bodyToMono(Long.class)
                    .block();

            MatriulaRequestDTO dto = new MatriulaRequestDTO();
            dto.setIdCurso(idCurso);
            dto.setIdUsuario(idUsuario);

            Matricula novaMatricula = mapper.toEntity(dto);
            novaMatricula.setDataMatricula(LocalDate.now());
            novaMatricula.setStatus("ATIVA");

            Matricula salva = matriculaRepository.save(novaMatricula);
            return mapper.toMatriculaResponse(salva);

        } catch (WebClientResponseException.NotFound e) {
            throw new RuntimeException("Curso ou usuário não encontrado.");
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Erro na comunicação: " + e.getMessage());
        }
    }


    public List<MatriculaResponseDTO> buscarMatricula() {
        List<Matricula> listaDeMatriculas = matriculaRepository.findAll();

        if (listaDeMatriculas.isEmpty()){
            throw new RuntimeException("Nenhuma matricula foi encotrada");
        }

        return listaDeMatriculas.stream()
                .map(mapper::toMatriculaResponse)
                .collect(Collectors.toList());
    }

    public List<AlunoDTO> buscarAlunosMatriculados(long idCurso) {
        List<AlunoDTO> listaAluno = matriculaRepository.buscarAlunosPorCurso(idCurso);
        if(listaAluno.isEmpty()){
            throw new RuntimeException("Nenhum aluno matriculado nesse curso");
        }

        return listaAluno;
    }

    public void cancelarMatricula(long idMatricula){
        Optional<Matricula> matriculaEncontrada = matriculaRepository.findById(idMatricula);

        if (matriculaEncontrada.isEmpty()){
            throw new RuntimeException("Matricula não encontrada");
        }

        matriculaRepository.delete(matriculaEncontrada.get());
    }
}

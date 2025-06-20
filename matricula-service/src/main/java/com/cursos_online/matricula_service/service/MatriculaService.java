package com.cursos_online.matricula_service.service;

import com.cursos.dto.curso.CursoResponseMatricula;
import com.cursos_online.matricula_service.dto.MatriculaResponseDTO;
import com.cursos_online.matricula_service.dto.MatriculaRequestDTO;
import com.cursos_online.matricula_service.dto.UsuarioDTO;
import com.cursos_online.matricula_service.entity.Matricula;
import com.cursos_online.matricula_service.mapper.MapperMatricula;
import com.cursos_online.matricula_service.messaging.producer.MatriculaProducer;
import com.cursos_online.matricula_service.repository.MatriculaRepository;
import com.cursos_online.usuario_service.dto.UsuarioEmailEventoDto;
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
    private MatriculaRepository matriculaRepository;

    @Autowired
    private MapperMatricula mapper;

    @Autowired
    private MatriculaProducer matriculaProducer;

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

            System.out.println(emailUsuario);

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
            response.setDataMatricula(LocalDate.now());

            UsuarioEmailEventoDto eventoDto = new UsuarioEmailEventoDto(usuarioResponse.getNome(), emailUsuario);
            matriculaProducer.enviarEventoMatriculaRealizada(eventoDto);

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

        return listaDeMatriculas.stream().map(m -> {
            MatriculaResponseDTO dto = mapper.toMatriculaResponse(m);

            CursoResponseMatricula curso = webCurso.get()
                    .uri("/cursos/id/{id}", m.getIdCurso())
                    .retrieve()
                    .bodyToMono(CursoResponseMatricula.class)
                    .block();

            if (curso != null) {
                dto.setNomeCurso(curso.getTitulo());
            }

            return dto;
        }).collect(Collectors.toList());
    }

    public List<UsuarioDTO> buscarUsuariosMatriculados(long idCurso) {

        List<UsuarioDTO> todosUsuarios = webUsuario.get()
                .uri("/usuario/listarInterno")
                .retrieve()
                .bodyToFlux(UsuarioDTO.class)
                .collectList()
                .block();

        List<Long> idsMatriculados = matriculaRepository.buscarIdsUsuariosPorCurso(idCurso);

        return todosUsuarios.stream()
                .filter(usuario -> idsMatriculados.contains(usuario.getId()))
                .collect(Collectors.toList());
    }

    public Matricula buscarDadosMatricula(long id){
        Optional<Matricula> matriculaEncontrada = matriculaRepository.findById(id);

        if (matriculaEncontrada.isEmpty()){
            throw new RuntimeException("Nenhuma matricula encontrada com esse id");
        }

        return matriculaEncontrada.get();
    }

    public void cancelarMatricula(long idMatricula) {
        Optional<Matricula> matriculaEncontrada = matriculaRepository.findById(idMatricula);
        if (matriculaEncontrada.isEmpty()) {
            throw new RuntimeException("Matrícula não encontrada");
        }

        matriculaRepository.delete(matriculaEncontrada.get());
    }
}

package com.cursos_online.conteudo_service.service;

import com.cursos_online.conteudo_service.dto.AtualizarConteudoDTO;
import com.cursos_online.conteudo_service.dto.CadastrarConteudoDTO;
import com.cursos_online.conteudo_service.dto.CursoDTO;
import com.cursos_online.conteudo_service.entity.Conteudo;
import com.cursos_online.conteudo_service.repository.ConteudoRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Service
public class ConteudoService {

    private final ConteudoRepository conteudoRepository;
    private final WebClient webClient;
    private final S3Client s3Client;

    public ConteudoService(ConteudoRepository conteudoRepository, S3Client s3Client) {
        this.conteudoRepository = conteudoRepository;
        this.webClient = WebClient.create("http://localhost:8081");
        this.s3Client = s3Client;
    }

    public Conteudo salvar(CadastrarConteudoDTO dto, MultipartFile file) throws IOException {

        String bucketName = "curso";
        String nomeDoArquivo = file.getOriginalFilename();

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket("meu-bucketon")
                        .key(nomeDoArquivo)
                        .contentType(file.getContentType())
                        .build(),
                software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes())
        );

        String urlConteudo = "http://localhost:4566/" + bucketName + "/" + nomeDoArquivo;

        Conteudo conteudo = new Conteudo();
        conteudo.setTitulo(dto.titulo());
        conteudo.setUrlConteudo(urlConteudo);
        conteudo.setCursoId(dto.cursoId());

        return conteudoRepository.save(conteudo);
    }


    public byte[] baixarArquivo(String bucket, String nome) {
        try {
            System.out.println("Tentando baixar: bucket=" + bucket + ", key=" + nome);

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(nome)
                    .build();

            ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(getObjectRequest);
            System.out.println("Arquivo encontrado. Tamanho: " + objectBytes.asByteArray().length);

            return objectBytes.asByteArray();

        } catch (Exception e) {
            System.err.println("Erro ao baixar arquivo: " + e.getMessage());
            e.printStackTrace();
            return new byte[0]; // ou lançar exceção personalizada
        }
    }



    public MediaType detectarTipoDeArquivo(String nome) {
        if (nome.endsWith(".png")) return MediaType.IMAGE_PNG;
        if (nome.endsWith(".jpg") || nome.endsWith(".jpeg")) return MediaType.IMAGE_JPEG;
        if (nome.endsWith(".mp4")) return MediaType.valueOf("video/mp4");
        return MediaType.APPLICATION_OCTET_STREAM;
    }

    public List<Conteudo> listarTodos() {
        return conteudoRepository.findAll();
    }

    public Conteudo buscarPorId(Long id) {
        return conteudoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conteúdo não encontrado com ID: " + id));
    }

    public List<Conteudo> buscarPorCursoId(Long cursoId) {
        CursoDTO cursoDTO = webClient.get()
                .uri("/cursos/id/{id}", cursoId)
                .retrieve()
                .bodyToMono(CursoDTO.class)
                .block();

        if (cursoDTO == null) {
            throw new RuntimeException("Curso não encontrado");
        }
        return conteudoRepository.findByCursoId(cursoId);
    }

    public Conteudo atualizar(Long id, AtualizarConteudoDTO dto) {
        Conteudo conteudo = buscarPorId(id);
        conteudo.setTitulo(dto.titulo());
        return conteudoRepository.save(conteudo);
    }

    public void excluir(Long id) {
        conteudoRepository.deleteById(id);
    }
}
package com.exemplo.gateway.controller;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class GatewayController {

    private final WebClient webClient;

    public GatewayController() {
        this.webClient = WebClient.builder().build();
    }

    @RequestMapping("/{service}/**")
    public Mono<ResponseEntity<byte[]>> proxy(
            @PathVariable String service,
            @RequestHeader HttpHeaders headers,
            @RequestParam(required = false) MultiValueMap<String, String> queryParams,
            ServerHttpRequest request) {

        String url = switch (service) {
            case "conteudos" -> "http://localhost:8083";
            case "cursos" -> "http://localhost:8081";
            case "matriculas" -> "http://localhost:8080";
            case "usuarios" -> "http://localhost:8082";
            case "certificados" -> "http://localhost:8084";
            default -> null;
        };

        if (url == null) {
            return Mono.just(ResponseEntity.badRequest().build());
        }

        String originalPath = request.getURI().getRawPath();
        String prefixToStrip = "/api/" + service;
        String path = originalPath.substring(prefixToStrip.length());
        String uri = url + path;

        if (queryParams != null && !queryParams.isEmpty()) {
            uri += "?" + queryParams.toSingleValueMap().entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .reduce((a, b) -> a + "&" + b)
                    .orElse("");
        }

        String finalUri = uri;
        return request.getBody().collectList().flatMap(dataBuffers -> {
            DataBuffer joined = new DefaultDataBufferFactory().join(dataBuffers);
            byte[] bodyBytes = new byte[joined.readableByteCount()];
            joined.read(bodyBytes);
            DataBufferUtils.release(joined); // libera o buffer

            return webClient.method(request.getMethod())
                    .uri(finalUri)
                    .headers(h -> {
                        h.addAll(headers);
                        h.remove(HttpHeaders.TRANSFER_ENCODING);
                    })
                    .contentType(headers.getContentType() != null ? headers.getContentType() : MediaType.APPLICATION_OCTET_STREAM)
                    .bodyValue(bodyBytes)
                    .retrieve()
                    .toEntity(byte[].class);
        });
    }

}
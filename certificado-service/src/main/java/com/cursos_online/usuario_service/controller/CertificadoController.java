package com.cursos_online.usuario_service.controller;

import com.cursos_online.usuario_service.dto.CertificadoDto;
import com.cursos_online.usuario_service.service.CertificadoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/certificado")
public class CertificadoController {

    private CertificadoService service;

    public CertificadoController(CertificadoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CertificadoDto> cadastrar(@RequestBody CertificadoDto dto) {
        CertificadoDto response = service.cadastrarCertificado(dto);

        if (response == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(response);
    }
}

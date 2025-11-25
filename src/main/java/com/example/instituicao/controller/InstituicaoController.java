package com.example.instituicao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.instituicao.dto.EscolaRequest;
import com.example.instituicao.model.Instituicao;
import com.example.instituicao.model.UnidadesEscolares;
import com.example.instituicao.service.InstituicaoService;

@RestController
@RequestMapping("/api/instituicoes")
@CrossOrigin(origins = "http://localhost:3000")
public class InstituicaoController {

    @Autowired
    private InstituicaoService instituicaoService;
@PostMapping("/{instituicaoId}/escolas")
    public ResponseEntity<Instituicao> adicionarEscola(
            @PathVariable String instituicaoId,
            @RequestBody EscolaRequest request) {

        Instituicao instituicaoAtualizada = instituicaoService.adicionarEscola(instituicaoId, request);
        return ResponseEntity.ok(instituicaoAtualizada);
    }
    @PostMapping
    public ResponseEntity<Instituicao> criarInstituicao(@RequestBody Instituicao instituicao) {
        Instituicao nova = instituicaoService.criar(instituicao);
        return ResponseEntity.ok(nova);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarInstituicao(@PathVariable String id) {
        instituicaoService.deletar(id);
        return ResponseEntity.noContent().build(); // Retorna 204 (Sem Conteúdo)
    }

    @GetMapping("/{id}")
    public ResponseEntity<Instituicao> buscarInstituicao(@PathVariable String id) {
        return ResponseEntity.ok(instituicaoService.buscarPorId(id));
    }
    
 
    @GetMapping
    public ResponseEntity<List<Instituicao>> listarInstituicoes() {
        return ResponseEntity.ok(instituicaoService.listarTodas());
    }

    @GetMapping("/{id}/escolas")
    public ResponseEntity<List<UnidadesEscolares>> listarEscolasDaInstituicao(@PathVariable String id) {
        return ResponseEntity.ok(instituicaoService.listarEscolas(id));
    }
    @GetMapping("/download/pdf")
    public ResponseEntity<byte[]> downloadRelatorio() {
        
        byte[] pdfBytes = instituicaoService.criarPdfDasInstituicoes(instituicaoService.listarTodas());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"relatorio_instituicoes.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
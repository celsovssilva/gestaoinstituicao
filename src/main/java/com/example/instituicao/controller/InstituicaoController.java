package com.example.instituicao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.instituicao.model.Escola;
import com.example.instituicao.model.Instituicao;
import com.example.instituicao.service.InstituicaoService;

@RestController
@RequestMapping("/api/instituicoes")
@CrossOrigin(origins = "http://localhost:3000")
public class InstituicaoController {

    @Autowired
    private InstituicaoService instituicaoService;

    // Admin: Criar Instituição
    @PostMapping
    public ResponseEntity<Instituicao> criarInstituicao(@RequestBody Instituicao instituicao) {
        Instituicao nova = instituicaoService.criar(instituicao);
        return ResponseEntity.ok(nova);
    }

    // Admin: Deletar Instituição
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarInstituicao(@PathVariable String id) {
        instituicaoService.deletar(id);
        return ResponseEntity.noContent().build(); // Retorna 204 (Sem Conteúdo)
    }

    // Admin: Acessar dados de UMA instituição
    @GetMapping("/{id}")
    public ResponseEntity<Instituicao> buscarInstituicao(@PathVariable String id) {
        return ResponseEntity.ok(instituicaoService.buscarPorId(id));
    }
    
    // Admin: "Extrair dados reais" (Listar todas)
    @GetMapping
    public ResponseEntity<List<Instituicao>> listarInstituicoes() {
        return ResponseEntity.ok(instituicaoService.listarTodas());
    }

    // Admin: Ver escolas DENTRO de uma instituição
    @GetMapping("/{id}/escolas")
    public ResponseEntity<List<Escola>> listarEscolasDaInstituicao(@PathVariable String id) {
        return ResponseEntity.ok(instituicaoService.listarEscolas(id));
    }
}
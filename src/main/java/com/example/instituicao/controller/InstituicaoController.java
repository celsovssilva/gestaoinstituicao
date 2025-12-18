package com.example.instituicao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.instituicao.dto.ComunicadoRequest;
import com.example.instituicao.dto.EscolaRequest;
import com.example.instituicao.model.Comunicado;
import com.example.instituicao.model.Instituicao;
import com.example.instituicao.model.UnidadesEscolares;
import com.example.instituicao.model.Usuarios;
import com.example.instituicao.repository.UsuariosRepository;
import com.example.instituicao.service.InstituicaoService;

@RestController
@RequestMapping("/api/instituicoes")
@CrossOrigin(origins = "http://localhost:3000")
public class InstituicaoController {

    @Autowired
    private InstituicaoService instituicaoService;

    @Autowired
    private UsuariosRepository usuarioRepository;

   
 @PostMapping("/{instituicaoId}/gestores")
public ResponseEntity<Usuarios> cadastrarGestor(
        @PathVariable String instituicaoId,
        @RequestBody Usuarios gestor
) {
    try {
        System.out.println("🚀 DADOS RECEBIDOS NO BACKEND:");
        System.out.println("Instituição ID: " + instituicaoId);
        System.out.println("Gestor Nome: " + gestor.getNome());
        System.out.println("Gestor Email: " + gestor.getEmail());
        System.out.println("Gestor Senha: " + gestor.getSenha_hash());
        
        Usuarios novoGestor = instituicaoService.criarGestor(instituicaoId, gestor);
        
        System.out.println("✅ GESTOR SALVO: " + novoGestor.getId());
        return new ResponseEntity<>(novoGestor, HttpStatus.CREATED);
        
    } catch (RuntimeException e) {
        System.out.println("❌ ERRO NO BACKEND: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}

    
    
    @PutMapping("/{instituicaoId}/gestores/{gestorId}")
    public ResponseEntity<Usuarios> atualizarGestor(
            @PathVariable String instituicaoId, 
            @PathVariable String gestorId,
            @RequestBody Usuarios dadosAtualizados
    ) {
        try {
            Usuarios gestorAtualizado = instituicaoService.atualizarGestor(instituicaoId, gestorId, dadosAtualizados);
            return ResponseEntity.ok(gestorAtualizado);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{instituicaoId}/gestores/{gestorId}")
    public ResponseEntity<Void> deletarGestor(@PathVariable String gestorId) {
        try {
            instituicaoService.deletarGestor(gestorId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

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
        return ResponseEntity.noContent().build();
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

    @GetMapping("/{instituicaoId}/gestores")
    public List<Usuarios> listarGestoresPorInstituicao(@PathVariable String instituicaoId) {
   
    return usuarioRepository.findByInstituicaoIdAndPapel(instituicaoId, "GESTOR");
}

@GetMapping("download/pdf")
public ResponseEntity<byte[]> downloadPdf(@RequestParam(value = "t", required = false) String timestamp) {
    byte[] pdf = instituicaoService.gerarPdfInstituicoes();
    String nomeArquivo = "instituicoes.pdf";
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDisposition(ContentDisposition
        .attachment()
        .filename(nomeArquivo)
        .build());
 
    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
    headers.add("Pragma", "no-cache");
    headers.add("Expires", "0");
    headers.add("X-Content-Type-Options", "nosniff");
    
    return ResponseEntity.ok()
            .headers(headers)
            .body(pdf);
}
    @PostMapping("/{instituicaoId}/comunicados")
    public Comunicado enviarComunicado( @PathVariable String instituicaoId,
        @RequestBody ComunicadoRequest comunicadoDTO) {
        
        
        return instituicaoService.enviarComunicado(instituicaoId, comunicadoDTO);
    }
    
}
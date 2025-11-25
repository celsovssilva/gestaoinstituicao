package com.example.instituicao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.instituicao.dto.LoginRequest;
import com.example.instituicao.model.Instituicao;
import com.example.instituicao.service.InstituicaoService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private InstituicaoService instituicaoService;
    @PostMapping("/login")
    public ResponseEntity<?> loginInstituicao(@RequestBody LoginRequest loginRequest) {
        try {
          
            Instituicao instituicaoLogada = instituicaoService.autenticarInstituicao(
                    loginRequest.getNome(), 
                    loginRequest.getSenha());
            
         
            return ResponseEntity.ok(instituicaoLogada); 

        } catch (RuntimeException e) {
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    
}

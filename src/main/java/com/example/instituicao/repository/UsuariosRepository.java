package com.example.instituicao.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.instituicao.model.Usuarios;

public interface UsuariosRepository extends MongoRepository<Usuarios, String> {
    
    
    Optional<Usuarios> findByEmail(String email);
}
package com.example.instituicao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.instituicao.model.Usuarios;

public interface UsuariosRepository extends MongoRepository<Usuarios, String> {
    
    List<Usuarios> findByInstituicaoIdAndPapel(String instituicaoId, String papel);
    Optional<Usuarios> findByEmail(String email);
}
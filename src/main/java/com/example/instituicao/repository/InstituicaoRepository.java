package com.example.instituicao.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.instituicao.model.Instituicao;

public interface InstituicaoRepository extends MongoRepository<Instituicao, String> {

    Optional<Instituicao> findByNome(String nome);
    
}

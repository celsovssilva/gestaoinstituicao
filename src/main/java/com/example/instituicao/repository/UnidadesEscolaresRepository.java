package com.example.instituicao.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.instituicao.model.UnidadesEscolares;

public interface UnidadesEscolaresRepository extends MongoRepository<UnidadesEscolares, String> {
    
}

package com.example.instituicao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.instituicao.model.Escola;
import com.example.instituicao.model.Instituicao;
import com.example.instituicao.repository.InstituicaoRepository;

@Service
public class InstituicaoService {

    @Autowired
    private InstituicaoRepository instituicaoRepository;

    // Criar Instituição
    public Instituicao criar(Instituicao instituicao) {
        // Você pode adicionar lógicas aqui (ex: validar CNPJ)
        return instituicaoRepository.save(instituicao);
    }

    // Deletar Instituição
    public void deletar(String id) {
        instituicaoRepository.deleteById(id);
    }

    // Acessar dados (todas)
    public List<Instituicao> listarTodas() {
        return instituicaoRepository.findAll();
    }

    // Acessar dados (uma específica)
    public Instituicao buscarPorId(String id) {
        return instituicaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instituição não encontrada!"));
    }

    // Ver escolas de uma instituição
    public List<Escola> listarEscolas(String idInstituicao) {
        Instituicao instituicao = buscarPorId(idInstituicao);
        return instituicao.getEscolas();
    }
}

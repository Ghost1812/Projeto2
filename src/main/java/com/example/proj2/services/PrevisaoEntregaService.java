package com.example.proj2.services;

import com.example.proj2.models.PrevisaoEntrega;
import com.example.proj2.repositories.PrevisaoEntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrevisaoEntregaService {

    private final PrevisaoEntregaRepository previsaoEntregaRepository;

    @Autowired
    public PrevisaoEntregaService(PrevisaoEntregaRepository previsaoEntregaRepository) {
        this.previsaoEntregaRepository = previsaoEntregaRepository;
    }

    public List<PrevisaoEntrega> findAll() {
        return previsaoEntregaRepository.findAll();
    }

    public Optional<PrevisaoEntrega> findById(Integer id) {
        return previsaoEntregaRepository.findById(id);
    }

    public PrevisaoEntrega save(PrevisaoEntrega previsaoEntrega) {
        return previsaoEntregaRepository.save(previsaoEntrega);
    }

    public void deleteById(Integer id) {
        previsaoEntregaRepository.deleteById(id);
    }
}

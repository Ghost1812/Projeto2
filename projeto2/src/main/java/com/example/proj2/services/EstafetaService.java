package com.example.proj2.services;

import com.example.proj2.models.Estafeta;
import com.example.proj2.repositories.EstafetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstafetaService {

    private final EstafetaRepository estafetaRepository;

    @Autowired
    public EstafetaService(EstafetaRepository estafetaRepository) {
        this.estafetaRepository = estafetaRepository;
    }

    public List<Estafeta> findAll() {
        return estafetaRepository.findAll();
    }

    public Optional<Estafeta> findById(Integer id) {
        return estafetaRepository.findById(id);
    }

    public Estafeta save(Estafeta estafeta) {
        return estafetaRepository.save(estafeta);
    }

    public void deleteById(Integer id) {
        estafetaRepository.deleteById(id);
    }
}

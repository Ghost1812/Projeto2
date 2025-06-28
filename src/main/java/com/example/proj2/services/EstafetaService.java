package com.example.proj2.services;

import com.example.proj2.models.Estafeta;
import com.example.proj2.repositories.EstafetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstafetaService {

    @Autowired
    private EstafetaRepository estafetaRepository;

    public List<Estafeta> findAll() {
        return estafetaRepository.findAll();
    }

    public Estafeta findById(Integer id) {
        return estafetaRepository.findById(id).orElse(null);
    }

    public Estafeta save(Estafeta estafeta) {
        return estafetaRepository.save(estafeta);
    }

    public void deleteById(Integer id) {
        estafetaRepository.deleteById(id);
    }
}

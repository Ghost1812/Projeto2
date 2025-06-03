package com.example.proj2.services;

import com.example.proj2.models.Entrega;
import com.example.proj2.repositories.EntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EntregaService {

    private final EntregaRepository entregaRepository;

    @Autowired
    public EntregaService(EntregaRepository entregaRepository) {
        this.entregaRepository = entregaRepository;
    }

    public List<Entrega> findAll() {
        return entregaRepository.findAll();
    }

    public Optional<Entrega> findById(Integer id) {
        return entregaRepository.findById(id);
    }

    public Entrega save(Entrega entrega) {
        return entregaRepository.save(entrega);
    }

    public void deleteById(Integer id) {
        entregaRepository.deleteById(id);
    }
}

package com.example.proj2.services;

import com.example.proj2.models.Entrega;
import com.example.proj2.repositories.EntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntregaService {

    @Autowired
    private EntregaRepository entregaRepository;

    public List<Entrega> findAll() {
        return entregaRepository.findAll();
    }

    public Entrega findById(Integer id) {
        return entregaRepository.findById(id).orElse(null);
    }

    public Entrega save(Entrega entrega) {
        return entregaRepository.save(entrega);
    }

    public void deleteById(Integer id) {
        entregaRepository.deleteById(id);
    }
}

package com.example.proj2.services;

import com.example.proj2.models.Encomenda;
import com.example.proj2.repositories.EncomendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EncomendaService {

    private final EncomendaRepository encomendaRepository;

    @Autowired
    public EncomendaService(EncomendaRepository encomendaRepository) {
        this.encomendaRepository = encomendaRepository;
    }

    public List<Encomenda> findAll() {
        return encomendaRepository.findAll();
    }

    public Optional<Encomenda> findById(Integer id) {
        return encomendaRepository.findById(id);
    }

    @Transactional
    public Encomenda save(Encomenda encomenda) {
        if (encomenda.getId() == null) {
            return encomendaRepository.save(encomenda);
        } else {
            return encomendaRepository.saveAndFlush(encomenda);
        }
    }

    public void deleteById(Integer id) {
        encomendaRepository.deleteById(id);
    }
}
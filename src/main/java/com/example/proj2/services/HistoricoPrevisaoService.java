package com.example.proj2.services;

import com.example.proj2.models.Encomenda;
import com.example.proj2.repositories.EncomendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HistoricoPrevisaoService {

    private final EncomendaRepository encomendaRepository;

    @Autowired
    public HistoricoPrevisaoService(EncomendaRepository encomendaRepository) {
        this.encomendaRepository = encomendaRepository;
    }

    @Transactional
    public Encomenda save(Encomenda encomenda) {
        if (encomenda.getId() == null) {
            return encomendaRepository.save(encomenda);
        } else {
            return encomendaRepository.saveAndFlush(encomenda);
        }
    }
}
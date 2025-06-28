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

    public List<Encomenda> searchByNumeroRastreio(String numeroRastreio) {
        return encomendaRepository.findByNumeroRastreioContainingIgnoreCase(numeroRastreio);
    }

    public List<Encomenda> findByEstadoIntegridade(String estadoIntegridade) {
        return encomendaRepository.findByEstadoIntegridade(estadoIntegridade);
    }

    public List<Encomenda> findByEstadoEntrega(String estadoEntrega) {
        return encomendaRepository.findByEstadoEntrega(estadoEntrega);
    }

    // Método de conveniência para compatibilidade com controllers existentes
    public List<Encomenda> findByEstado(String estado) {
        // Tenta primeiro por estado de entrega, depois por estado de integridade
        List<Encomenda> result = encomendaRepository.findByEstadoEntrega(estado);
        if (result.isEmpty()) {
            result = encomendaRepository.findByEstadoIntegridade(estado);
        }
        return result;
    }
}
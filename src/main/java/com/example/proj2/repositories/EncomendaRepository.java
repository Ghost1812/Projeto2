package com.example.proj2.repositories;

import com.example.proj2.models.Encomenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EncomendaRepository extends JpaRepository<Encomenda, Integer> {
    List<Encomenda> findByNumeroRastreioContainingIgnoreCase(String numeroRastreio);
    List<Encomenda> findByEstadoIntegridade(String estadoIntegridade);
    List<Encomenda> findByEstadoEntrega(String estadoEntrega);
}

package com.example.proj2.repositories;

import com.example.proj2.models.PrevisaoEntrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrevisaoEntregaRepository extends JpaRepository<PrevisaoEntrega, Integer> {
}

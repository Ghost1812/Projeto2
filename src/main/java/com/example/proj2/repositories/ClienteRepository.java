package com.example.proj2.repositories;

import com.example.proj2.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    // findAll(), findById(), save(), delete() já vêm prontos aqui
}

package com.example.proj2.repositories;

import com.example.proj2.models.Rececionista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RececionistaRepository extends JpaRepository<Rececionista, Integer> {
}

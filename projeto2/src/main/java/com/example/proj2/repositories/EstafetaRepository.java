package com.example.proj2.repositories;

import com.example.proj2.models.Estafeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstafetaRepository extends JpaRepository<Estafeta, Integer> {
}

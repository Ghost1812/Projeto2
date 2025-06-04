package com.example.proj2.repositories;

import com.example.proj2.models.OperadorTriagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperadorTriagemRepository extends JpaRepository<OperadorTriagem, Integer> {
    // Nenhum método adicional é necessário agora
}

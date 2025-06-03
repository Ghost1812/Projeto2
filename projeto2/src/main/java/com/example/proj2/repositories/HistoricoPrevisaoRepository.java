package com.example.proj2.repositories;

import com.example.proj2.models.HistoricoPrevisao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoPrevisaoRepository extends JpaRepository<HistoricoPrevisao, Integer> {
}

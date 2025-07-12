package com.example.proj2.repositories;

import com.example.proj2.models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    @Query("SELECT f FROM Feedback f LEFT JOIN FETCH f.idEncomenda LEFT JOIN FETCH f.idCliente")
    List<Feedback> findAllWithEncomendaAndCliente();

    List<Feedback> findByOpinioesContainingIgnoreCase(String opinioes);

    List<Feedback> findByNotadeservicoContainingIgnoreCase(String notadeservico);

    List<Feedback> findByReclamacaoContainingIgnoreCase(String reclamacao);

    List<Feedback> findByQuestionarioContainingIgnoreCase(String questionario);
}

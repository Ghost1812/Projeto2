package com.example.proj2.repositories;

import com.example.proj2.models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    List<Feedback> findByComentarioContainingIgnoreCase(String comentario);
    List<Feedback> findByStatus(String status);
}

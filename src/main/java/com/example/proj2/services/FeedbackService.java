package com.example.proj2.services;

import com.example.proj2.models.Feedback;
import com.example.proj2.repositories.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public List<Feedback> findAll() {
        return feedbackRepository.findAll();
    }

    public Feedback findById(Integer id) {
        return feedbackRepository.findById(id).orElse(null);
    }

    public Feedback save(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public void deleteById(Integer id) {
        feedbackRepository.deleteById(id);
    }

    public List<Feedback> searchByComentario(String comentario) {
        return feedbackRepository.findByComentarioContainingIgnoreCase(comentario);
    }

    public List<Feedback> findByStatus(String status) {
        return feedbackRepository.findByStatus(status);
    }
}

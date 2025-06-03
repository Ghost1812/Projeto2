package com.example.proj2.services;

import com.example.proj2.models.Feedback;
import com.example.proj2.repositories.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public List<Feedback> findAll() {
        return feedbackRepository.findAll();
    }

    public Optional<Feedback> findById(Integer id) {
        return feedbackRepository.findById(id);
    }

    public Feedback save(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public void deleteById(Integer id) {
        feedbackRepository.deleteById(id);
    }
}

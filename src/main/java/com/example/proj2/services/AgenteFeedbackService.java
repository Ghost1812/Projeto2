package com.example.proj2.services;

import com.example.proj2.models.AgenteFeedback;
import com.example.proj2.repositories.AgenteFeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgenteFeedbackService {

    private final AgenteFeedbackRepository agenteFeedbackRepository;

    @Autowired
    public AgenteFeedbackService(AgenteFeedbackRepository agenteFeedbackRepository) {
        this.agenteFeedbackRepository = agenteFeedbackRepository;
    }

    public List<AgenteFeedback> findAll() {
        return agenteFeedbackRepository.findAll();
    }

    public Optional<AgenteFeedback> findById(Integer id) {
        return agenteFeedbackRepository.findById(id);
    }

    public AgenteFeedback save(AgenteFeedback agenteFeedback) {
        return agenteFeedbackRepository.save(agenteFeedback);
    }

    public void deleteById(Integer id) {
        agenteFeedbackRepository.deleteById(id);
    }
}

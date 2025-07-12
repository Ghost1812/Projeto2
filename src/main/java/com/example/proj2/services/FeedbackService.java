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

    public List<Feedback> findAllWithEncomendaAndCliente() {
        return feedbackRepository.findAllWithEncomendaAndCliente();
    }

    public Feedback save(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public void deleteById(Integer id) {
        feedbackRepository.deleteById(id);
    }

    // Pesquisa por texto na reclamação
    public List<Feedback> searchByReclamacao(String termo) {
        return feedbackRepository.findByReclamacaoContainingIgnoreCase(termo);
    }

    // Pesquisa por texto nas opiniões
    public List<Feedback> searchByOpinioes(String termo) {
        return feedbackRepository.findByOpinioesContainingIgnoreCase(termo);
    }

    // Filtrar por nota de serviço
    public List<Feedback> searchByNota(String nota) {
        return feedbackRepository.findByNotadeservicoContainingIgnoreCase(nota);
    }

    // Filtrar por questionário
    public List<Feedback> searchByQuestionario(String texto) {
        return feedbackRepository.findByQuestionarioContainingIgnoreCase(texto);
    }
}

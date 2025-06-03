package com.example.proj2.services;

import com.example.proj2.models.OperadorTriagem;
import com.example.proj2.repositories.OperadorTriagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OperadorTriagemService {

    private final OperadorTriagemRepository operadorTriagemRepository;

    @Autowired
    public OperadorTriagemService(OperadorTriagemRepository operadorTriagemRepository) {
        this.operadorTriagemRepository = operadorTriagemRepository;
    }

    public List<OperadorTriagem> findAll() {
        return operadorTriagemRepository.findAll();
    }

    public Optional<OperadorTriagem> findById(Integer id) {
        return operadorTriagemRepository.findById(id);
    }

    public OperadorTriagem save(OperadorTriagem operadorTriagem) {
        return operadorTriagemRepository.save(operadorTriagem);
    }

    public void deleteById(Integer id) {
        operadorTriagemRepository.deleteById(id);
    }
}

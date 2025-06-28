package com.example.proj2.services;

import com.example.proj2.models.OperadorTriagem;
import com.example.proj2.repositories.OperadorTriagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperadorTriagemService {

    @Autowired
    private OperadorTriagemRepository operadorTriagemRepository;

    public List<OperadorTriagem> findAll() {
        return operadorTriagemRepository.findAll();
    }

    public OperadorTriagem findById(Integer id) {
        return operadorTriagemRepository.findById(id).orElse(null);
    }

    public OperadorTriagem save(OperadorTriagem operadorTriagem) {
        return operadorTriagemRepository.save(operadorTriagem);
    }

    public void deleteById(Integer id) {
        operadorTriagemRepository.deleteById(id);
    }
}

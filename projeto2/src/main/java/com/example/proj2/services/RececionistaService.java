package com.example.proj2.services;

import com.example.proj2.models.Rececionista;
import com.example.proj2.repositories.RececionistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RececionistaService {

    private final RececionistaRepository rececionistaRepository;

    @Autowired
    public RececionistaService(RececionistaRepository rececionistaRepository) {
        this.rececionistaRepository = rececionistaRepository;
    }

    public List<Rececionista> findAll() {
        return rececionistaRepository.findAll();
    }

    public Optional<Rececionista> findById(Integer id) {
        return rececionistaRepository.findById(id);
    }

    public Rececionista save(Rececionista rececionista) {
        return rececionistaRepository.save(rececionista);
    }

    public void deleteById(Integer id) {
        rececionistaRepository.deleteById(id);
    }
}

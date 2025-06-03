package com.example.proj2.services;

import com.example.proj2.models.Veiculo;
import com.example.proj2.repositories.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;

    @Autowired
    public VeiculoService(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    public List<Veiculo> findAll() {
        return veiculoRepository.findAll();
    }

    public Optional<Veiculo> findById(Integer id) {
        return veiculoRepository.findById(id);
    }

    public Veiculo save(Veiculo veiculo) {
        return veiculoRepository.save(veiculo);
    }

    public void deleteById(Integer id) {
        veiculoRepository.deleteById(id);
    }
}

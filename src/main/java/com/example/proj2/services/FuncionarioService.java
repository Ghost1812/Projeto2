package com.example.proj2.services;

import com.example.proj2.models.Funcionario;
import com.example.proj2.repositories.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    public List<Funcionario> findAll() {
        return funcionarioRepository.findAll();
    }

    public Funcionario findById(Integer id) {
        return funcionarioRepository.findById(id).orElse(null);
    }

    public Funcionario findByEmail(String email) {
        return funcionarioRepository.findByEmail(email).orElse(null);
    }

    public Funcionario login(String email, String password) {
        return funcionarioRepository.findByEmailAndPassword(email, password).orElse(null);
    }

    public Funcionario save(Funcionario funcionario) {
        return funcionarioRepository.save(funcionario);
    }

    public void deleteById(Integer id) {
        funcionarioRepository.deleteById(id);
    }

    public List<Funcionario> searchByNome(String nome) {
        return funcionarioRepository.findByNomeContainingIgnoreCase(nome);
    }

    public void criarFuncionario(String email, String senha, String cargo, String nome) {
        Funcionario funcionario = new Funcionario();
        funcionario.setEmail(email);
        funcionario.setPassword(senha);
        funcionario.setCargo(cargo);
        funcionario.setNome(nome);
        funcionarioRepository.save(funcionario);
    }
}

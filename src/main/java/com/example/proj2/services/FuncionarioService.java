package com.example.proj2.services;

import com.example.proj2.models.Funcionario;
import com.example.proj2.repositories.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    public Funcionario findByEmail(String email) {
        return funcionarioRepository.findByEmail(email).orElse(null);
    }

    public Funcionario login(String email, String password) {
        return funcionarioRepository.findByEmailAndPassword(email, password).orElse(null);
    }

    public void criarFuncionario(String email, String senha, String cargo, String nome) {
    }
}

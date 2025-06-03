package com.example.proj2.controllers;

import com.example.proj2.models.Funcionario;
import com.example.proj2.models.LoginDTO;
import com.example.proj2.repositories.FuncionarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        Optional<Funcionario> funcionarioOptional = funcionarioRepository.findByEmailAndPassword(
                loginDTO.getEmail(), loginDTO.getPassword()
        );

        if (funcionarioOptional.isPresent()) {
            Funcionario funcionario = funcionarioOptional.get();
            return ResponseEntity.ok("Login bem-sucedido: " + funcionario.getCargo());
        } else {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }
}

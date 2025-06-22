package com.example.proj2.controllers;

import com.example.proj2.models.Funcionario;
import com.example.proj2.models.LoginDTO;
import com.example.proj2.repositories.FuncionarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController // Indica que esta classe é um controlador REST (responde com JSON/XML em vez de páginas HTML)
@RequestMapping("/api/auth") // Todos os endpoints desta classe começam com "/api/auth"
public class AuthController {

    @Autowired // Injeta automaticamente uma instância do repositório
    private FuncionarioRepository funcionarioRepository;

    // Endpoint POST para login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        // Procura um funcionário com o email e password fornecidos
        Optional<Funcionario> funcionarioOptional = funcionarioRepository.findByEmailAndPassword(
                loginDTO.getEmail(), loginDTO.getPassword()
        );

        // Se encontrar, retorna status 200 e o cargo do funcionário
        if (funcionarioOptional.isPresent()) {
            Funcionario funcionario = funcionarioOptional.get();
            return ResponseEntity.ok("Login bem-sucedido: " + funcionario.getCargo());
        } else {
            // Se não encontrar, retorna status 401 (não autorizado) com mensagem de erro
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }
}

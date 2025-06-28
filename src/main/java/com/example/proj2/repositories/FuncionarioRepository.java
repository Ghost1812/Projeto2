package com.example.proj2.repositories;

import com.example.proj2.models.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {
    Optional<Funcionario> findByEmail(String email);
    Optional<Funcionario> findByEmailAndPassword(String email, String password);
    List<Funcionario> findByNomeContainingIgnoreCase(String nome);
}

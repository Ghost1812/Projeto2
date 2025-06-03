package com.example;

import com.example.proj2.services.FuncionarioService;
import com.example.ui.LoginFrame;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.swing.*;

@SpringBootApplication
public class Projeto2Application {

    public static void main(String[] args) {
        var context = SpringApplication.run(Projeto2Application.class, args);

        SwingUtilities.invokeLater(() -> {
            FuncionarioService funcionarioService = context.getBean(FuncionarioService.class);
            new LoginFrame(funcionarioService);// <-- agora sim, passa os dois
        });
    }
}

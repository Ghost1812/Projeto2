package com.example.ui;


import com.example.SpringContext;
import com.example.proj2.services.FuncionarioService;
import org.springframework.context.ApplicationContext;

public class MainSwing {

    public static void main(String[] args) {
        ApplicationContext context = SpringContext.getContext();
        System.setProperty("java.awt.headless", "false");

        FuncionarioService funcionarioService = context.getBean(FuncionarioService.class);

        new LoginFrame(funcionarioService);
        // <-- passa os dois
    }
}

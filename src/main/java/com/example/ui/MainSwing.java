package com.example.ui;

import com.example.SpringContext;
import com.example.proj2.services.FuncionarioService;
import org.springframework.context.ApplicationContext;

public class MainSwing {

    public static void main(String[] args) {
        ApplicationContext context = SpringContext.getContext();

        FuncionarioService funcionarioService = context.getBean(FuncionarioService.class);

        new LoginFrame(funcionarioService);
        // <-- passa os dois
    }
}

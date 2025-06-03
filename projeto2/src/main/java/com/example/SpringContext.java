package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

public class SpringContext {

    private static final ApplicationContext context = SpringApplication.run(Projeto2Application.class);

    public static ApplicationContext getContext() {
        return context;
    }
}

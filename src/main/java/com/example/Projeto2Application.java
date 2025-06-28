package com.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Projeto2Application extends Application {

    private ConfigurableApplicationContext springContext;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        springContext = SpringApplication.run(Projeto2Application.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carregar o FXML de login
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        
        // Configurar o Spring para injetar dependências no controller
        loader.setControllerFactory(springContext::getBean);
        
        Parent root = loader.load();
        
        // Configurar a cena
        Scene scene = new Scene(root);
        primaryStage.setTitle("PackBee - Login");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        
        // Definir ícone
        try {
            primaryStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/images/img.png")));
        } catch (Exception e) {
            System.err.println("Erro ao definir ícone: " + e.getMessage());
        }
        
        primaryStage.show();
    }

    @Override
    public void stop() {
        springContext.close();
        Platform.exit();
    }
}

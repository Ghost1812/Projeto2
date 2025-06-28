package com.example.proj2.controllers;

import com.example.proj2.models.Funcionario;
import com.example.proj2.models.UtilizadorAtual;
import com.example.proj2.services.FuncionarioService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private ImageView logoImageView;

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private ApplicationContext applicationContext;

    @FXML
    public void initialize() {
        // Carregar logo
        loadLogo();
        
        // Configurar eventos de tecla
        setupKeyEvents();
    }

    private void loadLogo() {
        try {
            InputStream logoStream = getClass().getClassLoader().getResourceAsStream("images/img.png");
            if (logoStream != null) {
                Image logo = new Image(logoStream);
                logoImageView.setImage(logo);
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar logo: " + e.getMessage());
        }
    }

    private void setupKeyEvents() {
        // Permitir login com Enter
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                handleLogin();
            }
        });
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String senha = passwordField.getText().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            showAlert("Erro", "Por favor, preencha todos os campos.", Alert.AlertType.ERROR);
            return;
        }

        // Fazer login com o serviço
        Funcionario funcionario = funcionarioService.login(email, senha);

        if (funcionario != null) {
            // Guardar dados do utilizador autenticado
            UtilizadorAtual.email = funcionario.getEmail();
            UtilizadorAtual.tipo = "admin".equalsIgnoreCase(funcionario.getCargo()) ? "admin" : "normal";

            showAlert("Sucesso", "Bem-vindo, " + funcionario.getNome(), Alert.AlertType.INFORMATION);

            // Fechar janela de login
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();

            // Abrir menu principal JavaFX
            openMainMenu(funcionario);
        } else {
            showAlert("Erro", "Email ou senha incorretos.", Alert.AlertType.ERROR);
            passwordField.clear();
        }
    }

    private void openMainMenu(Funcionario funcionario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main-menu.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            
            Parent root = loader.load();
            
            // Obter o controller e definir o funcionário
            MainMenuController controller = loader.getController();
            controller.setFuncionario(funcionario);
            
            Stage stage = new Stage();
            stage.setTitle("PackBee - Menu Principal");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            
            // Definir ícone
            try {
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/img.png")));
            } catch (Exception e) {
                System.err.println("Erro ao definir ícone: " + e.getMessage());
            }
            
            stage.show();
        } catch (Exception e) {
            showAlert("Erro", "Erro ao abrir menu principal: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 
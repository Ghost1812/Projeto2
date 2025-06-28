package com.example.proj2.controllers;

import com.example.proj2.models.Funcionario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MainMenuController {

    @FXML
    private Label welcomeLabel;

    @Autowired
    private ApplicationContext applicationContext;

    private Funcionario funcionario;

    @FXML
    public void initialize() {
        // Este método será chamado quando o FXML for carregado
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
        if (welcomeLabel != null) {
            welcomeLabel.setText("Bem-vindo, " + funcionario.getNome());
        }
    }

    @FXML
    private void openCreateClient() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/cliente.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestão de Clientes");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            showAlert("Erro", "Erro ao abrir tela de gestão de clientes: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void openListClients() {
        // A mesma tela de gestão de clientes serve para listar
        openCreateClient();
    }

    @FXML
    private void openCreateOrder() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/encomenda.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestão de Encomendas");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            showAlert("Erro", "Erro ao abrir tela de gestão de encomendas: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void openListOrders() {
        // A mesma tela de gestão de encomendas serve para listar
        openCreateOrder();
    }

    @FXML
    private void openSorting() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/triagem.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Triagem de Encomendas");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            showAlert("Erro", "Erro ao abrir tela de triagem: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void openDelivery() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/entrega.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Entrega de Encomendas");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            showAlert("Erro", "Erro ao abrir tela de entrega: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void openFuncionarios() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/funcionario.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestão de Funcionários");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            showAlert("Erro", "Erro ao abrir tela de gestão de funcionários: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void openFeedback() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/feedback.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestão de Feedback");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            showAlert("Erro", "Erro ao abrir tela de feedback: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Saída");
        alert.setHeaderText(null);
        alert.setContentText("Tem certeza que deseja sair?");

        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                // Fechar todas as janelas e voltar ao login
                Stage stage = (Stage) welcomeLabel.getScene().getWindow();
                stage.close();
                
                // Aqui você pode adicionar lógica para voltar à tela de login
                // ou simplesmente fechar a aplicação
                System.exit(0);
            }
        });
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 
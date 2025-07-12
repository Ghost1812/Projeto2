package com.example.proj2.controllers;

import com.example.proj2.models.Funcionario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MainMenuController {

    @FXML
    private Label welcomeLabel;

    @FXML private Button btnClientes;
    @FXML private Button btnEncomendas;
    @FXML private Button btnFuncionarios;
    @FXML private Button btnTriagem;
    @FXML private Button btnEntrega;
    @FXML private Button btnFeedback;

    @Autowired
    private ApplicationContext applicationContext;

    private Funcionario funcionario;

    @FXML
    public void initialize() {
        // Se o funcionario já estiver definido, aplicar lógica de visibilidade
        if (funcionario != null) {
            aplicarVisibilidadePorCargo();
        }
    }

    private void aplicarVisibilidadePorCargo() {
        if (welcomeLabel != null && funcionario != null) {
            welcomeLabel.setText("Bem-vindo, " + funcionario.getNome() + " (" + funcionario.getCargo() + ")");
        }
        String cargo = funcionario != null ? funcionario.getCargo().toLowerCase().replace("-", "_") : "";
        System.out.println("[DEBUG] Cargo normalizado: '" + cargo + "'");
        if (btnClientes != null) btnClientes.setVisible(false);
        if (btnEncomendas != null) btnEncomendas.setVisible(false);
        if (btnFuncionarios != null) btnFuncionarios.setVisible(false);
        if (btnTriagem != null) btnTriagem.setVisible(false);
        if (btnEntrega != null) btnEntrega.setVisible(false);
        if (btnFeedback != null) btnFeedback.setVisible(false);
        switch (cargo) {
            case "admin":
                if (btnClientes != null) btnClientes.setVisible(true);
                if (btnEncomendas != null) btnEncomendas.setVisible(true);
                if (btnFuncionarios != null) btnFuncionarios.setVisible(true);
                if (btnTriagem != null) btnTriagem.setVisible(true);
                if (btnEntrega != null) btnEntrega.setVisible(true);
                if (btnFeedback != null) btnFeedback.setVisible(true);
                break;
            case "rececionista":
                if (btnClientes != null) btnClientes.setVisible(true);
                if (btnEncomendas != null) btnEncomendas.setVisible(true);
                break;
            case "operador":
                if (btnTriagem != null) btnTriagem.setVisible(true);
                break;
            case "estafeta":
                if (btnEntrega != null) btnEntrega.setVisible(true);
                break;
            case "agente":
                if (btnFeedback != null) btnFeedback.setVisible(true);
                break;
        }
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
        aplicarVisibilidadePorCargo();
    }

    public void refreshMenu() {
        aplicarVisibilidadePorCargo();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
            showAlert("Erro", "Erro ao abrir tela de feedback: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleLogout() {
        // Fecha o menu principal e volta ao login
        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        stage.close();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();
            Stage loginStage = new Stage();
            loginStage.setTitle("Login - PackBee");
            loginStage.setScene(new Scene(root));
            loginStage.setMaximized(true);
            loginStage.show();
        } catch (Exception e) {
            showAlert("Erro", "Erro ao voltar ao login: " + e.getMessage(), Alert.AlertType.ERROR);
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
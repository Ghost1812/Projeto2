package com.example.proj2.controllers;

import com.example.proj2.models.Funcionario;
import com.example.proj2.services.FuncionarioService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FuncionarioController {

    @FXML private TextField nomeField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> cargoComboBox;
    
    @FXML private TableView<Funcionario> funcionariosTable;
    @FXML private TableColumn<Funcionario, String> nomeColumn;
    @FXML private TableColumn<Funcionario, String> emailColumn;
    @FXML private TableColumn<Funcionario, String> cargoColumn;
    
    @FXML private TextField searchField;
    @FXML private Button createButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button clearButton;

    @Autowired
    private FuncionarioService funcionarioService;

    private ObservableList<Funcionario> funcionariosList;
    private Funcionario selectedFuncionario;

    @FXML
    public void initialize() {
        setupComboBoxes();
        setupTable();
        loadFuncionarios();
        setupEventHandlers();
        disableButtons();
    }

    private void setupComboBoxes() {
        // Cargos disponíveis
        cargoComboBox.setItems(FXCollections.observableArrayList(
            "admin", "rececionista", "operador_triagem", "estafeta", "agente_feedback"
        ));
        cargoComboBox.setValue("rececionista");
    }

    private void setupTable() {
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        cargoColumn.setCellValueFactory(new PropertyValueFactory<>("cargo"));

        funcionariosList = FXCollections.observableArrayList();
        funcionariosTable.setItems(funcionariosList);

        // Seleção de linha
        funcionariosTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                selectedFuncionario = newValue;
                if (newValue != null) {
                    loadFuncionarioToForm(newValue);
                    enableButtons();
                } else {
                    clearForm();
                    disableButtons();
                }
            }
        );
    }

    private void setupEventHandlers() {
        // Busca em tempo real
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                loadFuncionarios();
            } else {
                searchFuncionarios(newValue);
            }
        });
    }

    private void loadFuncionarios() {
        List<Funcionario> funcionarios = funcionarioService.findAll();
        funcionariosList.clear();
        funcionariosList.addAll(funcionarios);
    }

    private void searchFuncionarios(String searchTerm) {
        List<Funcionario> funcionarios = funcionarioService.searchByNome(searchTerm);
        funcionariosList.clear();
        funcionariosList.addAll(funcionarios);
    }

    @FXML
    private void handleCreate() {
        if (validateForm()) {
            try {
                Funcionario funcionario = new Funcionario();
                funcionario.setNome(nomeField.getText().trim());
                funcionario.setEmail(emailField.getText().trim());
                funcionario.setPassword(passwordField.getText().trim());
                funcionario.setCargo(cargoComboBox.getValue());

                funcionarioService.save(funcionario);
                showAlert("Sucesso", "Funcionário criado com sucesso!", Alert.AlertType.INFORMATION);
                clearForm();
                loadFuncionarios();
            } catch (Exception e) {
                showAlert("Erro", "Erro ao criar funcionário: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedFuncionario == null) {
            showAlert("Erro", "Selecione um funcionário para atualizar", Alert.AlertType.ERROR);
            return;
        }

        if (validateForm()) {
            try {
                selectedFuncionario.setNome(nomeField.getText().trim());
                selectedFuncionario.setEmail(emailField.getText().trim());
                
                // Só atualiza a password se foi fornecida
                if (!passwordField.getText().trim().isEmpty()) {
                    selectedFuncionario.setPassword(passwordField.getText().trim());
                }
                
                selectedFuncionario.setCargo(cargoComboBox.getValue());

                funcionarioService.save(selectedFuncionario);
                showAlert("Sucesso", "Funcionário atualizado com sucesso!", Alert.AlertType.INFORMATION);
                clearForm();
                loadFuncionarios();
            } catch (Exception e) {
                showAlert("Erro", "Erro ao atualizar funcionário: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedFuncionario == null) {
            showAlert("Erro", "Selecione um funcionário para eliminar", Alert.AlertType.ERROR);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminação");
        alert.setHeaderText(null);
        alert.setContentText("Tem certeza que deseja eliminar o funcionário " + selectedFuncionario.getNome() + "?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    funcionarioService.deleteById(selectedFuncionario.getId());
                    showAlert("Sucesso", "Funcionário eliminado com sucesso!", Alert.AlertType.INFORMATION);
                    clearForm();
                    loadFuncionarios();
                } catch (Exception e) {
                    showAlert("Erro", "Erro ao eliminar funcionário: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    @FXML
    private void handleClear() {
        clearForm();
        funcionariosTable.getSelectionModel().clearSelection();
    }

    private void loadFuncionarioToForm(Funcionario funcionario) {
        nomeField.setText(funcionario.getNome());
        emailField.setText(funcionario.getEmail());
        passwordField.clear(); // Não mostra a password por segurança
        cargoComboBox.setValue(funcionario.getCargo());
    }

    private void clearForm() {
        nomeField.clear();
        emailField.clear();
        passwordField.clear();
        cargoComboBox.setValue("rececionista");
        selectedFuncionario = null;
    }

    private boolean validateForm() {
        if (nomeField.getText().trim().isEmpty()) {
            showAlert("Erro", "Nome é obrigatório", Alert.AlertType.ERROR);
            return false;
        }
        if (emailField.getText().trim().isEmpty()) {
            showAlert("Erro", "Email é obrigatório", Alert.AlertType.ERROR);
            return false;
        }
        if (selectedFuncionario == null && passwordField.getText().trim().isEmpty()) {
            showAlert("Erro", "Password é obrigatória para novos funcionários", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private void enableButtons() {
        updateButton.setDisable(false);
        deleteButton.setDisable(false);
    }

    private void disableButtons() {
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 
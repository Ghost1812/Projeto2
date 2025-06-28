package com.example.proj2.controllers;

import com.example.proj2.models.Cliente;
import com.example.proj2.services.ClienteService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClienteController {

    @FXML private TextField nomeField;
    @FXML private TextField emailField;
    @FXML private TextField telefoneField;
    @FXML private TextField moradaField;
    @FXML private TextField cidadeField;
    @FXML private TextField paisField;
    
    @FXML private TableView<Cliente> clientesTable;
    @FXML private TableColumn<Cliente, String> nomeColumn;
    @FXML private TableColumn<Cliente, String> emailColumn;
    @FXML private TableColumn<Cliente, String> telefoneColumn;
    @FXML private TableColumn<Cliente, String> moradaColumn;
    
    @FXML private TextField searchField;
    @FXML private Button createButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button clearButton;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ApplicationContext applicationContext;

    private ObservableList<Cliente> clientesList;
    private Cliente selectedCliente;

    @FXML
    public void initialize() {
        setupTable();
        loadClientes();
        setupEventHandlers();
        disableButtons();
    }

    private void setupTable() {
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        telefoneColumn.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        moradaColumn.setCellValueFactory(new PropertyValueFactory<>("morada"));

        clientesList = FXCollections.observableArrayList();
        clientesTable.setItems(clientesList);

        // Seleção de linha
        clientesTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                selectedCliente = newValue;
                if (newValue != null) {
                    loadClienteToForm(newValue);
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
                loadClientes();
            } else {
                searchClientes(newValue);
            }
        });
    }

    private void loadClientes() {
        List<Cliente> clientes = clienteService.findAll();
        clientesList.clear();
        clientesList.addAll(clientes);
    }

    private void searchClientes(String searchTerm) {
        List<Cliente> clientes = clienteService.searchByNome(searchTerm);
        clientesList.clear();
        clientesList.addAll(clientes);
    }

    @FXML
    private void handleCreate() {
        if (validateForm()) {
            try {
                Cliente cliente = new Cliente();
                cliente.setNome(nomeField.getText().trim());
                cliente.setEmail(emailField.getText().trim());
                cliente.setTelefone(telefoneField.getText().trim());
                cliente.setMorada(moradaField.getText().trim());
                cliente.setCidade(cidadeField.getText().trim());
                cliente.setPais(paisField.getText().trim());

                clienteService.save(cliente);
                showAlert("Sucesso", "Cliente criado com sucesso!", Alert.AlertType.INFORMATION);
                clearForm();
                loadClientes();
            } catch (Exception e) {
                showAlert("Erro", "Erro ao criar cliente: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedCliente == null) {
            showAlert("Erro", "Selecione um cliente para atualizar", Alert.AlertType.ERROR);
            return;
        }

        if (validateForm()) {
            try {
                selectedCliente.setNome(nomeField.getText().trim());
                selectedCliente.setEmail(emailField.getText().trim());
                selectedCliente.setTelefone(telefoneField.getText().trim());
                selectedCliente.setMorada(moradaField.getText().trim());
                selectedCliente.setCidade(cidadeField.getText().trim());
                selectedCliente.setPais(paisField.getText().trim());

                clienteService.save(selectedCliente);
                showAlert("Sucesso", "Cliente atualizado com sucesso!", Alert.AlertType.INFORMATION);
                clearForm();
                loadClientes();
            } catch (Exception e) {
                showAlert("Erro", "Erro ao atualizar cliente: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedCliente == null) {
            showAlert("Erro", "Selecione um cliente para eliminar", Alert.AlertType.ERROR);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminação");
        alert.setHeaderText(null);
        alert.setContentText("Tem certeza que deseja eliminar o cliente " + selectedCliente.getNome() + "?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    clienteService.deleteById(selectedCliente.getId());
                    showAlert("Sucesso", "Cliente eliminado com sucesso!", Alert.AlertType.INFORMATION);
                    clearForm();
                    loadClientes();
                } catch (Exception e) {
                    showAlert("Erro", "Erro ao eliminar cliente: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    @FXML
    private void handleClear() {
        clearForm();
        selectedCliente = null;
        clientesTable.getSelectionModel().clearSelection();
        disableButtons();
    }

    private void loadClienteToForm(Cliente cliente) {
        nomeField.setText(cliente.getNome());
        emailField.setText(cliente.getEmail());
        telefoneField.setText(cliente.getTelefone());
        moradaField.setText(cliente.getMorada());
        cidadeField.setText(cliente.getCidade());
        paisField.setText(cliente.getPais());
    }

    private void clearForm() {
        nomeField.clear();
        emailField.clear();
        telefoneField.clear();
        moradaField.clear();
        cidadeField.clear();
        paisField.clear();
    }

    private boolean validateForm() {
        if (nomeField.getText().trim().isEmpty()) {
            showAlert("Erro", "Nome é obrigatório", Alert.AlertType.ERROR);
            return false;
        }
        if (telefoneField.getText().trim().isEmpty()) {
            showAlert("Erro", "Telefone é obrigatório", Alert.AlertType.ERROR);
            return false;
        }
        if (emailField.getText().trim().isEmpty()) {
            showAlert("Erro", "Email é obrigatório", Alert.AlertType.ERROR);
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
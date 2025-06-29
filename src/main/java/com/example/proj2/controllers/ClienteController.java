package com.example.proj2.controllers;

import com.example.proj2.models.Cliente;
import com.example.proj2.services.ClienteService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClienteController {

    @FXML private TextField nomeField;
    @FXML private TextField codpostalField;
    @FXML private TextField numeroPortaField;
    @FXML private TextField ruaField;
    @FXML private TextField contactoField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @FXML private TableView<Cliente> clientesTable;
    @FXML private TableColumn<Cliente, String> nomeColumn;
    @FXML private TableColumn<Cliente, String> codpostalColumn;
    @FXML private TableColumn<Cliente, Integer> numeroPortaColumn;
    @FXML private TableColumn<Cliente, String> ruaColumn;
    @FXML private TableColumn<Cliente, String> contactoColumn;
    @FXML private TableColumn<Cliente, String> emailColumn;

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
        codpostalColumn.setCellValueFactory(new PropertyValueFactory<>("codpostal"));
        numeroPortaColumn.setCellValueFactory(new PropertyValueFactory<>("numeroPorta"));
        ruaColumn.setCellValueFactory(new PropertyValueFactory<>("rua"));
        contactoColumn.setCellValueFactory(new PropertyValueFactory<>("contacto"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        clientesList = FXCollections.observableArrayList();
        clientesTable.setItems(clientesList);

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
                cliente.setCodpostal(codpostalField.getText().trim());
                cliente.setNumeroPorta(Integer.parseInt(numeroPortaField.getText().trim()));
                cliente.setRua(ruaField.getText().trim());
                cliente.setContacto(contactoField.getText().trim());
                cliente.setEmail(emailField.getText().trim());
                cliente.setPassword(passwordField.getText().trim());

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
                selectedCliente.setCodpostal(codpostalField.getText().trim());
                selectedCliente.setNumeroPorta(Integer.parseInt(numeroPortaField.getText().trim()));
                selectedCliente.setRua(ruaField.getText().trim());
                selectedCliente.setContacto(contactoField.getText().trim());
                selectedCliente.setEmail(emailField.getText().trim());
                selectedCliente.setPassword(passwordField.getText().trim());

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
        codpostalField.setText(cliente.getCodpostal());
        numeroPortaField.setText(String.valueOf(cliente.getNumeroPorta()));
        ruaField.setText(cliente.getRua());
        contactoField.setText(cliente.getContacto());
        emailField.setText(cliente.getEmail());
        passwordField.setText(cliente.getPassword());
    }

    private void clearForm() {
        nomeField.clear();
        codpostalField.clear();
        numeroPortaField.clear();
        ruaField.clear();
        contactoField.clear();
        emailField.clear();
        passwordField.clear();
    }

    private boolean validateForm() {
        if (nomeField.getText().trim().isEmpty()
                || codpostalField.getText().trim().isEmpty()
                || numeroPortaField.getText().trim().isEmpty()
                || ruaField.getText().trim().isEmpty()) {
            showAlert("Erro", "Preencha todos os campos obrigatórios!", Alert.AlertType.ERROR);
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

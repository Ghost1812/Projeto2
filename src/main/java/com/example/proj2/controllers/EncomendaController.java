package com.example.proj2.controllers;

import com.example.proj2.models.Cliente;
import com.example.proj2.models.Encomenda;
import com.example.proj2.models.Rececionista;
import com.example.proj2.services.ClienteService;
import com.example.proj2.services.EncomendaService;
import com.example.proj2.services.RececionistaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import javafx.scene.image.ImageView;
import com.example.proj2.ui.CustomDialog;

@Component
public class EncomendaController {

    @FXML private TextField numeroRastreioField;
    @FXML private ComboBox<Cliente> clienteComboBox;
    @FXML private TextField pesoField;
    @FXML private ComboBox<String> estadoIntegridadeComboBox;
    @FXML private ComboBox<String> estadoEntregaComboBox;
    
    @FXML private TableView<Encomenda> encomendasTable;
    @FXML private TableColumn<Encomenda, String> numeroRastreioColumn;
    @FXML private TableColumn<Encomenda, String> clienteColumn;
    @FXML private TableColumn<Encomenda, String> pesoColumn;
    @FXML private TableColumn<Encomenda, String> estadoIntegridadeColumn;
    @FXML private TableColumn<Encomenda, String> estadoEntregaColumn;
    
    @FXML private TextField searchField;
    @FXML private Button createButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button clearButton;
    @FXML private ImageView logoImageView;

    @Autowired
    private EncomendaService encomendaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private RececionistaService rececionistaService;

    private ObservableList<Encomenda> encomendasList;
    private ObservableList<Cliente> clientesList;
    private Encomenda selectedEncomenda;

    @FXML
    public void initialize() {
        if (logoImageView != null) {
            try {
                java.io.InputStream logoStream = getClass().getClassLoader().getResourceAsStream("images/img.png");
                if (logoStream != null) {
                    javafx.scene.image.Image logo = new javafx.scene.image.Image(logoStream);
                    logoImageView.setImage(logo);
                }
            } catch (Exception e) {
                System.err.println("Erro ao carregar logo: " + e.getMessage());
            }
        }
        setupComboBoxes();
        setupTable();
        loadEncomendas();
        loadClientes();
        setupEventHandlers();
        disableButtons();
    }

    private void setupComboBoxes() {
        // Estados de integridade
        estadoIntegridadeComboBox.setItems(FXCollections.observableArrayList(
            "Bom", "Regular", "Danificado"
        ));
        estadoIntegridadeComboBox.setValue("Bom");

        // Estados de entrega
        estadoEntregaComboBox.setItems(FXCollections.observableArrayList(
            "Pendente", "Em Processamento", "Em Trânsito", "Entregue", "Cancelado"
        ));
        estadoEntregaComboBox.setValue("Pendente");

        // Clientes
        clientesList = FXCollections.observableArrayList();
        clienteComboBox.setItems(clientesList);
    }

    private void setupTable() {
        numeroRastreioColumn.setCellValueFactory(new PropertyValueFactory<>("numeroRastreio"));
        clienteColumn.setCellValueFactory(cellData -> {
            Cliente cliente = cellData.getValue().getIdCliente();
            return new javafx.beans.property.SimpleStringProperty(
                cliente != null ? cliente.getNome() : "N/A"
            );
        });
        pesoColumn.setCellValueFactory(cellData -> {
            Double peso = cellData.getValue().getPeso();
            return new javafx.beans.property.SimpleStringProperty(
                peso != null ? peso.toString() + " kg" : "N/A"
            );
        });
        estadoIntegridadeColumn.setCellValueFactory(new PropertyValueFactory<>("estadoIntegridade"));
        estadoEntregaColumn.setCellValueFactory(new PropertyValueFactory<>("estadoEntrega"));

        encomendasList = FXCollections.observableArrayList();
        encomendasTable.setItems(encomendasList);

        // Seleção de linha
        encomendasTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                selectedEncomenda = newValue;
                if (newValue != null) {
                    loadEncomendaToForm(newValue);
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
                loadEncomendas();
            } else {
                searchEncomendas(newValue);
            }
        });
    }

    private void loadEncomendas() {
        List<Encomenda> encomendas = encomendaService.findAll();
        encomendasList.clear();
        encomendasList.addAll(encomendas);
    }

    private void loadClientes() {
        List<Cliente> clientes = clienteService.findAll();
        clientesList.clear();
        clientesList.addAll(clientes);
    }

    private void searchEncomendas(String searchTerm) {
        List<Encomenda> encomendas = encomendaService.searchByNumeroRastreio(searchTerm);
        encomendasList.clear();
        encomendasList.addAll(encomendas);
    }

    @FXML
    private void handleCreate() {
        if (validateForm()) {
            try {
                Encomenda encomenda = new Encomenda();
                encomenda.setNumeroRastreio(numeroRastreioField.getText().trim());
                encomenda.setIdCliente(clienteComboBox.getValue());
                encomenda.setPeso(Double.parseDouble(pesoField.getText().trim()));
                encomenda.setEstadoIntegridade(estadoIntegridadeComboBox.getValue());
                encomenda.setEstadoEntrega(estadoEntregaComboBox.getValue());
                
                // Definir um rececionista padrão
                List<Rececionista> rececionistas = rececionistaService.findAll();
                if (!rececionistas.isEmpty()) {
                    encomenda.setIdRececionista(rececionistas.get(0));
                } else {
                    // Se não há rececionistas, criar um padrão
                    Rececionista rececionistaPadrao = new Rececionista();
                    rececionistaPadrao.setNome("Rececionista Padrão");
                    rececionistaPadrao.setEmail("rececionista@empresa.com");
                    rececionistaPadrao.setContacto("123456789");
                    rececionistaPadrao = rececionistaService.save(rececionistaPadrao);
                    encomenda.setIdRececionista(rececionistaPadrao);
                }

                encomendaService.save(encomenda);
                showAlert("Sucesso", "Encomenda criada com sucesso!", Alert.AlertType.INFORMATION);
                clearForm();
                loadEncomendas();
            } catch (NumberFormatException e) {
                showAlert("Erro", "Peso deve ser um número válido", Alert.AlertType.ERROR);
            } catch (Exception e) {
                showAlert("Erro", "Erro ao criar encomenda: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace(); // Para ver o erro completo no terminal
            }
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedEncomenda == null) {
            showAlert("Erro", "Selecione uma encomenda para atualizar", Alert.AlertType.ERROR);
            return;
        }

        if (validateForm()) {
            try {
                selectedEncomenda.setNumeroRastreio(numeroRastreioField.getText().trim());
                selectedEncomenda.setIdCliente(clienteComboBox.getValue());
                selectedEncomenda.setPeso(Double.parseDouble(pesoField.getText().trim()));
                selectedEncomenda.setEstadoIntegridade(estadoIntegridadeComboBox.getValue());
                selectedEncomenda.setEstadoEntrega(estadoEntregaComboBox.getValue());

                encomendaService.save(selectedEncomenda);
                showAlert("Sucesso", "Encomenda atualizada com sucesso!", Alert.AlertType.INFORMATION);
                clearForm();
                loadEncomendas();
            } catch (NumberFormatException e) {
                showAlert("Erro", "Peso deve ser um número válido", Alert.AlertType.ERROR);
            } catch (Exception e) {
                showAlert("Erro", "Erro ao atualizar encomenda: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace(); // Para ver o erro completo no terminal
            }
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedEncomenda == null) {
            showAlert("Erro", "Selecione uma encomenda para eliminar", Alert.AlertType.ERROR);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminação");
        alert.setHeaderText(null);
        alert.setContentText("Tem certeza que deseja eliminar a encomenda " + selectedEncomenda.getNumeroRastreio() + "?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    encomendaService.deleteById(selectedEncomenda.getId());
                    showAlert("Sucesso", "Encomenda eliminada com sucesso!", Alert.AlertType.INFORMATION);
                    clearForm();
                    loadEncomendas();
                } catch (Exception e) {
                    showAlert("Erro", "Erro ao eliminar encomenda: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    @FXML
    private void handleClear() {
        clearForm();
        selectedEncomenda = null;
        encomendasTable.getSelectionModel().clearSelection();
        disableButtons();
    }

    private void loadEncomendaToForm(Encomenda encomenda) {
        numeroRastreioField.setText(encomenda.getNumeroRastreio());
        clienteComboBox.setValue(encomenda.getIdCliente());
        pesoField.setText(encomenda.getPeso().toString());
        estadoIntegridadeComboBox.setValue(encomenda.getEstadoIntegridade());
        estadoEntregaComboBox.setValue(encomenda.getEstadoEntrega());
    }

    private void clearForm() {
        numeroRastreioField.clear();
        clienteComboBox.setValue(null);
        pesoField.clear();
        estadoIntegridadeComboBox.setValue("Bom");
        estadoEntregaComboBox.setValue("Pendente");
    }

    private boolean validateForm() {
        if (numeroRastreioField.getText().trim().isEmpty()) {
            showAlert("Erro", "Número de rastreio é obrigatório", Alert.AlertType.ERROR);
            return false;
        }
        if (clienteComboBox.getValue() == null) {
            showAlert("Erro", "Cliente é obrigatório", Alert.AlertType.ERROR);
            return false;
        }
        if (pesoField.getText().trim().isEmpty()) {
            showAlert("Erro", "Peso é obrigatório", Alert.AlertType.ERROR);
            return false;
        }
        try {
            Double.parseDouble(pesoField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert("Erro", "Peso deve ser um número válido", Alert.AlertType.ERROR);
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
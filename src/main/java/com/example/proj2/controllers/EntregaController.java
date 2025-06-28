package com.example.proj2.controllers;

import com.example.proj2.models.Cliente;
import com.example.proj2.models.Encomenda;
import com.example.proj2.models.Entrega;
import com.example.proj2.models.Estafeta;
import com.example.proj2.models.Veiculo;
import com.example.proj2.services.EncomendaService;
import com.example.proj2.services.EntregaService;
import com.example.proj2.services.EstafetaService;
import com.example.proj2.services.VeiculoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EntregaController {

    @FXML private TableView<Encomenda> encomendasTable;
    @FXML private TableColumn<Encomenda, String> numeroRastreioColumn;
    @FXML private TableColumn<Encomenda, String> clienteColumn;
    @FXML private TableColumn<Encomenda, String> enderecoColumn;
    @FXML private TableColumn<Encomenda, String> estadoColumn;
    
    @FXML private ComboBox<Estafeta> estafetaComboBox;
    @FXML private ComboBox<Veiculo> veiculoComboBox;
    @FXML private ComboBox<String> statusEntregaComboBox;
    @FXML private TextArea observacoesField;
    
    @FXML private Button atribuirButton;
    @FXML private Button iniciarEntregaButton;
    @FXML private Button finalizarEntregaButton;
    @FXML private Button limparButton;
    
    @FXML private TextField searchField;
    @FXML private ComboBox<String> filterStatusComboBox;

    @Autowired
    private EncomendaService encomendaService;

    @Autowired
    private EntregaService entregaService;

    @Autowired
    private EstafetaService estafetaService;

    @Autowired
    private VeiculoService veiculoService;

    private ObservableList<Encomenda> encomendasList;
    private ObservableList<Estafeta> estafetasList;
    private ObservableList<Veiculo> veiculosList;
    private Encomenda selectedEncomenda;

    @FXML
    public void initialize() {
        setupComboBoxes();
        setupTable();
        loadEncomendas();
        loadEstafetas();
        loadVeiculos();
        setupEventHandlers();
        disableButtons();
    }

    private void setupComboBoxes() {
        // Status de entrega
        statusEntregaComboBox.setItems(FXCollections.observableArrayList(
            "Pendente", "Em Rota", "Entregue", "Cancelado"
        ));
        statusEntregaComboBox.setValue("Pendente");

        // Filtro por status
        filterStatusComboBox.setItems(FXCollections.observableArrayList(
            "Todos", "Pendente", "Em Rota", "Entregue", "Cancelado"
        ));
        filterStatusComboBox.setValue("Todos");

        // Estafetas
        estafetasList = FXCollections.observableArrayList();
        estafetaComboBox.setItems(estafetasList);

        // Veículos
        veiculosList = FXCollections.observableArrayList();
        veiculoComboBox.setItems(veiculosList);
    }

    private void setupTable() {
        numeroRastreioColumn.setCellValueFactory(new PropertyValueFactory<>("numeroRastreio"));
        clienteColumn.setCellValueFactory(cellData -> {
            Cliente cliente = cellData.getValue().getIdCliente();
            return new javafx.beans.property.SimpleStringProperty(
                cliente != null ? cliente.getNome() : "N/A"
            );
        });
        enderecoColumn.setCellValueFactory(cellData -> {
            Cliente cliente = cellData.getValue().getIdCliente();
            if (cliente != null && cliente.getMorada() != null) {
                return new javafx.beans.property.SimpleStringProperty(
                    cliente.getMorada() + ", " + cliente.getCidade()
                );
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estadoEntrega"));

        encomendasList = FXCollections.observableArrayList();
        encomendasTable.setItems(encomendasList);

        // Seleção de linha
        encomendasTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                selectedEncomenda = newValue;
                if (newValue != null) {
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

        // Filtro por status
        filterStatusComboBox.setOnAction(event -> {
            String filter = filterStatusComboBox.getValue();
            if ("Todos".equals(filter)) {
                loadEncomendas();
            } else {
                filterEncomendasByStatus(filter);
            }
        });
    }

    private void loadEncomendas() {
        List<Encomenda> encomendas = encomendaService.findByEstado("Pronta para Entrega");
        encomendasList.clear();
        encomendasList.addAll(encomendas);
    }

    private void loadEstafetas() {
        List<Estafeta> estafetas = estafetaService.findAll();
        estafetasList.clear();
        estafetasList.addAll(estafetas);
    }

    private void loadVeiculos() {
        List<Veiculo> veiculos = veiculoService.findAll();
        veiculosList.clear();
        veiculosList.addAll(veiculos);
    }

    private void searchEncomendas(String searchTerm) {
        List<Encomenda> encomendas = encomendaService.searchByNumeroRastreio(searchTerm);
        encomendasList.clear();
        encomendasList.addAll(encomendas);
    }

    private void filterEncomendasByStatus(String status) {
        List<Encomenda> encomendas = encomendaService.findByEstado(status);
        encomendasList.clear();
        encomendasList.addAll(encomendas);
    }

    @FXML
    private void handleAtribuir() {
        if (selectedEncomenda == null) {
            showAlert("Erro", "Selecione uma encomenda para atribuir", Alert.AlertType.ERROR);
            return;
        }

        if (estafetaComboBox.getValue() == null) {
            showAlert("Erro", "Selecione um estafeta", Alert.AlertType.ERROR);
            return;
        }

        if (veiculoComboBox.getValue() == null) {
            showAlert("Erro", "Selecione um veículo", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Criar nova entrega
            Entrega entrega = new Entrega();
            entrega.setStatus("Pendente");
            
            // Definir destino baseado no cliente
            Cliente cliente = selectedEncomenda.getIdCliente();
            if (cliente != null) {
                String destino = cliente.getMorada() + ", " + cliente.getCidade() + ", " + cliente.getPais();
                entrega.setDestino(destino);
            }

            entregaService.save(entrega);

            // Atualizar estado da encomenda
            selectedEncomenda.setEstadoEntrega("Em Rota");
            encomendaService.save(selectedEncomenda);

            showAlert("Sucesso", "Encomenda atribuída com sucesso!", Alert.AlertType.INFORMATION);
            clearForm();
            loadEncomendas();
        } catch (Exception e) {
            showAlert("Erro", "Erro ao atribuir encomenda: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleIniciarEntrega() {
        if (selectedEncomenda == null) {
            showAlert("Erro", "Selecione uma encomenda para iniciar entrega", Alert.AlertType.ERROR);
            return;
        }

        try {
            selectedEncomenda.setEstadoEntrega("Em Rota");
            encomendaService.save(selectedEncomenda);

            showAlert("Sucesso", "Entrega iniciada com sucesso!", Alert.AlertType.INFORMATION);
            clearForm();
            loadEncomendas();
        } catch (Exception e) {
            showAlert("Erro", "Erro ao iniciar entrega: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleFinalizarEntrega() {
        if (selectedEncomenda == null) {
            showAlert("Erro", "Selecione uma encomenda para finalizar", Alert.AlertType.ERROR);
            return;
        }

        if (statusEntregaComboBox.getValue() == null) {
            showAlert("Erro", "Selecione o status da entrega", Alert.AlertType.ERROR);
            return;
        }

        try {
            String status = statusEntregaComboBox.getValue();
            selectedEncomenda.setEstadoEntrega(status);

            encomendaService.save(selectedEncomenda);

            showAlert("Sucesso", "Entrega finalizada com sucesso!", Alert.AlertType.INFORMATION);
            clearForm();
            loadEncomendas();
        } catch (Exception e) {
            showAlert("Erro", "Erro ao finalizar entrega: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleLimpar() {
        clearForm();
        selectedEncomenda = null;
        encomendasTable.getSelectionModel().clearSelection();
        disableButtons();
    }

    private void clearForm() {
        estafetaComboBox.setValue(null);
        veiculoComboBox.setValue(null);
        statusEntregaComboBox.setValue("Pendente");
        observacoesField.clear();
    }

    private void enableButtons() {
        atribuirButton.setDisable(false);
        iniciarEntregaButton.setDisable(false);
        finalizarEntregaButton.setDisable(false);
    }

    private void disableButtons() {
        atribuirButton.setDisable(true);
        iniciarEntregaButton.setDisable(true);
        finalizarEntregaButton.setDisable(true);
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 
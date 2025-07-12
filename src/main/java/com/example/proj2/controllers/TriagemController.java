package com.example.proj2.controllers;

import com.example.proj2.models.Cliente;
import com.example.proj2.models.Encomenda;
import com.example.proj2.services.EncomendaService;
import com.example.proj2.services.OperadorTriagemService;
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
public class TriagemController {

    @FXML private TableView<Encomenda> encomendasTable;
    @FXML private TableColumn<Encomenda, String> numeroRastreioColumn;
    @FXML private TableColumn<Encomenda, String> clienteColumn;
    @FXML private TableColumn<Encomenda, String> pesoColumn;
    @FXML private TableColumn<Encomenda, String> estadoIntegridadeColumn;
    @FXML private TableColumn<Encomenda, String> estadoEntregaColumn;
    
    @FXML private ComboBox<String> estadoIntegridadeComboBox;
    @FXML private ComboBox<String> estadoEntregaComboBox;
    @FXML private TextArea observacoesField;
    
    @FXML private Button processarButton;
    @FXML private Button finalizarButton;
    @FXML private Button limparButton;
    
    @FXML private TextField searchField;
    @FXML private ComboBox<String> filterEstadoComboBox;
    @FXML private ImageView logoImageView;

    @Autowired
    private EncomendaService encomendaService;

    @Autowired
    private OperadorTriagemService operadorTriagemService;

    private ObservableList<Encomenda> encomendasList;
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
            "Pendente", "Em Triagem", "Triagem Concluída", "Pronta para Entrega", "Problema Detectado"
        ));
        estadoEntregaComboBox.setValue("Em Triagem");

        // Filtro por estado
        filterEstadoComboBox.setItems(FXCollections.observableArrayList(
            "Todos", "Pendente", "Em Triagem", "Triagem Concluída", "Pronta para Entrega", "Problema Detectado"
        ));
        filterEstadoComboBox.setValue("Todos");
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

        // Filtro por estado
        filterEstadoComboBox.setOnAction(event -> {
            String filter = filterEstadoComboBox.getValue();
            if ("Todos".equals(filter)) {
                loadEncomendas();
            } else {
                filterEncomendasByEstado(filter);
            }
        });
    }

    private void loadEncomendas() {
        String filter = filterEstadoComboBox != null ? filterEstadoComboBox.getValue() : null;
        List<Encomenda> encomendas;
        if (filter == null || filter.equals("Todos")) {
            encomendas = encomendaService.findAll();
        } else {
            encomendas = encomendaService.findByEstado(filter);
        }
        encomendasList.clear();
        encomendasList.addAll(encomendas);
    }

    private void searchEncomendas(String searchTerm) {
        List<Encomenda> encomendas = encomendaService.searchByNumeroRastreio(searchTerm);
        encomendasList.clear();
        encomendasList.addAll(encomendas);
    }

    private void filterEncomendasByEstado(String estado) {
        List<Encomenda> encomendas = encomendaService.findByEstado(estado);
        encomendasList.clear();
        encomendasList.addAll(encomendas);
    }

    @FXML
    private void handleProcessar() {
        if (selectedEncomenda == null) {
            CustomDialog.show("Erro", "Selecione uma encomenda para processar", CustomDialog.DialogType.ERROR);
            return;
        }

        try {
            selectedEncomenda.setEstadoIntegridade(estadoIntegridadeComboBox.getValue());
            selectedEncomenda.setEstadoEntrega(estadoEntregaComboBox.getValue());

            encomendaService.save(selectedEncomenda);
            CustomDialog.show("Sucesso", "Encomenda processada com sucesso!", CustomDialog.DialogType.SUCCESS);
            clearForm();
            loadEncomendas();
        } catch (Exception e) {
            CustomDialog.show("Erro", "Erro ao processar encomenda: " + e.getMessage(), CustomDialog.DialogType.ERROR);
        }
    }

    @FXML
    private void handleFinalizar() {
        if (selectedEncomenda == null) {
            CustomDialog.show("Erro", "Selecione uma encomenda para finalizar", CustomDialog.DialogType.ERROR);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Finalização");
        alert.setHeaderText(null);
        alert.setContentText("Tem certeza que deseja finalizar a triagem da encomenda " + selectedEncomenda.getNumeroRastreio() + "?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    selectedEncomenda.setEstadoEntrega("Pronta para Entrega");
                    encomendaService.save(selectedEncomenda);
                    CustomDialog.show("Sucesso", "Triagem finalizada com sucesso!", CustomDialog.DialogType.SUCCESS);
                    clearForm();
                    loadEncomendas();
                } catch (Exception e) {
                    CustomDialog.show("Erro", "Erro ao finalizar triagem: " + e.getMessage(), CustomDialog.DialogType.ERROR);
                }
            }
        });
    }

    @FXML
    private void handleLimpar() {
        clearForm();
        selectedEncomenda = null;
        encomendasTable.getSelectionModel().clearSelection();
        disableButtons();
    }

    private void loadEncomendaToForm(Encomenda encomenda) {
        estadoIntegridadeComboBox.setValue(encomenda.getEstadoIntegridade());
        estadoEntregaComboBox.setValue(encomenda.getEstadoEntrega());
        observacoesField.clear();
    }

    private void clearForm() {
        estadoIntegridadeComboBox.setValue("Bom");
        estadoEntregaComboBox.setValue("Em Triagem");
        observacoesField.clear();
    }

    private void enableButtons() {
        processarButton.setDisable(false);
        finalizarButton.setDisable(false);
    }

    private void disableButtons() {
        processarButton.setDisable(true);
        finalizarButton.setDisable(true);
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 
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
import javafx.scene.image.ImageView;
import com.example.proj2.ui.CustomDialog;

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
    @FXML private ImageView logoImageView;

    @Autowired private EncomendaService encomendaService;
    @Autowired private EntregaService entregaService;
    @Autowired private EstafetaService estafetaService;
    @Autowired private VeiculoService veiculoService;

    private ObservableList<Encomenda> encomendasList;
    private ObservableList<Estafeta> estafetasList;
    private ObservableList<Veiculo> veiculosList;
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
        loadEstafetas();
        loadVeiculos();
        setupEventHandlers();
        disableButtons();
    }

    private void setupComboBoxes() {
        statusEntregaComboBox.setItems(FXCollections.observableArrayList("Pendente", "Em Rota", "Entregue", "Cancelado"));
        statusEntregaComboBox.setValue("Pendente");

        filterStatusComboBox.setItems(FXCollections.observableArrayList("Todos", "Pendente", "Em Rota", "Entregue", "Cancelado"));
        filterStatusComboBox.setValue("Todos");

        estafetasList = FXCollections.observableArrayList();
        estafetaComboBox.setItems(estafetasList);

        veiculosList = FXCollections.observableArrayList();
        veiculoComboBox.setItems(veiculosList);
    }

    private void setupTable() {
        numeroRastreioColumn.setCellValueFactory(new PropertyValueFactory<>("numeroRastreio"));

        clienteColumn.setCellValueFactory(cellData -> {
            Cliente cliente = cellData.getValue().getIdCliente();
            return new javafx.beans.property.SimpleStringProperty(cliente != null ? cliente.getNome() : "N/A");
        });

        enderecoColumn.setCellValueFactory(cellData -> {
            Cliente cliente = cellData.getValue().getIdCliente();
            if (cliente != null) {
                String endereco = cliente.getRua() + ", " + cliente.getNumeroPorta() + ", " + cliente.getCodpostal();
                return new javafx.beans.property.SimpleStringProperty(endereco);
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });


        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estadoEntrega"));

        encomendasList = FXCollections.observableArrayList();
        encomendasTable.setItems(encomendasList);

        encomendasTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedEncomenda = newVal;
            if (newVal != null) {
                enableButtons();
            } else {
                clearForm();
                disableButtons();
            }
        });
    }

    private void setupEventHandlers() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty()) {
                loadEncomendas();
            } else {
                searchEncomendas(newVal);
            }
        });

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
        String filter = filterStatusComboBox != null ? filterStatusComboBox.getValue() : null;
        List<Encomenda> encomendas;
        if (filter == null || filter.equals("Todos")) {
            encomendas = encomendaService.findAll();
        } else {
            encomendas = encomendaService.findByEstado(filter);
        }
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
            CustomDialog.show("Erro", "Selecione uma encomenda para atribuir", CustomDialog.DialogType.ERROR);
            return;
        }

        if (estafetaComboBox.getValue() == null) {
            CustomDialog.show("Erro", "Selecione um estafeta", CustomDialog.DialogType.ERROR);
            return;
        }

        if (veiculoComboBox.getValue() == null) {
            CustomDialog.show("Erro", "Selecione um veículo", CustomDialog.DialogType.ERROR);
            return;
        }

        try {
            Entrega entrega = new Entrega();

            Cliente cliente = selectedEncomenda.getIdCliente();
            if (cliente != null) {
                String destino = cliente.getRua() + ", " + cliente.getCodpostal();
                entrega.setDestino(destino);
            }

            entregaService.save(entrega);

            selectedEncomenda.setEstadoEntrega("Em Rota");
            encomendaService.save(selectedEncomenda);

            CustomDialog.show("Sucesso", "Encomenda atribuída com sucesso!", CustomDialog.DialogType.SUCCESS);
            clearForm();
            loadEncomendas();
        } catch (Exception e) {
            CustomDialog.show("Erro", "Erro ao atribuir encomenda: " + e.getMessage(), CustomDialog.DialogType.ERROR);
        }
    }

    @FXML
    private void handleIniciarEntrega() {
        if (selectedEncomenda == null) {
            CustomDialog.show("Erro", "Selecione uma encomenda para iniciar entrega", CustomDialog.DialogType.ERROR);
            return;
        }

        try {
            selectedEncomenda.setEstadoEntrega("Em Rota");
            encomendaService.save(selectedEncomenda);

            CustomDialog.show("Sucesso", "Entrega iniciada com sucesso!", CustomDialog.DialogType.SUCCESS);
            clearForm();
            loadEncomendas();
        } catch (Exception e) {
            CustomDialog.show("Erro", "Erro ao iniciar entrega: " + e.getMessage(), CustomDialog.DialogType.ERROR);
        }
    }

    @FXML
    private void handleFinalizarEntrega() {
        if (selectedEncomenda == null) {
            CustomDialog.show("Erro", "Selecione uma encomenda para finalizar", CustomDialog.DialogType.ERROR);
            return;
        }

        try {
            selectedEncomenda.setEstadoEntrega("Entregue");
            encomendaService.save(selectedEncomenda);

            CustomDialog.show("Sucesso", "Entrega finalizada com sucesso!", CustomDialog.DialogType.SUCCESS);
            clearForm();
            loadEncomendas();
        } catch (Exception e) {
            CustomDialog.show("Erro", "Erro ao finalizar entrega: " + e.getMessage(), CustomDialog.DialogType.ERROR);
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

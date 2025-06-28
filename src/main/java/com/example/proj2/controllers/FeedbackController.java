package com.example.proj2.controllers;

import com.example.proj2.models.Cliente;
import com.example.proj2.models.Feedback;
import com.example.proj2.services.ClienteService;
import com.example.proj2.services.FeedbackService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FeedbackController {

    @FXML private TableView<Feedback> feedbackTable;
    @FXML private TableColumn<Feedback, String> clienteColumn;
    @FXML private TableColumn<Feedback, String> comentarioColumn;
    @FXML private TableColumn<Feedback, String> statusColumn;
    
    @FXML private ComboBox<Cliente> clienteComboBox;
    @FXML private TextArea comentarioField;
    @FXML private ComboBox<String> statusComboBox;
    
    @FXML private Button createButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button clearButton;
    
    @FXML private TextField searchField;
    @FXML private ComboBox<String> filterStatusComboBox;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private ClienteService clienteService;

    private ObservableList<Feedback> feedbackList;
    private ObservableList<Cliente> clientesList;
    private Feedback selectedFeedback;

    @FXML
    public void initialize() {
        setupComboBoxes();
        setupTable();
        loadFeedback();
        loadClientes();
        setupEventHandlers();
        disableButtons();
    }

    private void setupComboBoxes() {
        // Status
        statusComboBox.setItems(FXCollections.observableArrayList(
            "Pendente", "Em Análise", "Resolvido", "Fechado"
        ));
        statusComboBox.setValue("Pendente");

        // Filtro por status
        filterStatusComboBox.setItems(FXCollections.observableArrayList(
            "Todos", "Pendente", "Em Análise", "Resolvido", "Fechado"
        ));
        filterStatusComboBox.setValue("Todos");

        // Clientes
        clientesList = FXCollections.observableArrayList();
        clienteComboBox.setItems(clientesList);
    }

    private void setupTable() {
        clienteColumn.setCellValueFactory(cellData -> {
            Cliente cliente = cellData.getValue().getIdCliente();
            return new javafx.beans.property.SimpleStringProperty(
                cliente != null ? cliente.getNome() : "N/A"
            );
        });
        comentarioColumn.setCellValueFactory(new PropertyValueFactory<>("comentario"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        feedbackList = FXCollections.observableArrayList();
        feedbackTable.setItems(feedbackList);

        // Seleção de linha
        feedbackTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                selectedFeedback = newValue;
                if (newValue != null) {
                    loadFeedbackToForm(newValue);
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
                loadFeedback();
            } else {
                searchFeedback(newValue);
            }
        });

        // Filtro por status
        filterStatusComboBox.setOnAction(event -> {
            String filter = filterStatusComboBox.getValue();
            if ("Todos".equals(filter)) {
                loadFeedback();
            } else {
                filterFeedbackByStatus(filter);
            }
        });
    }

    private void loadFeedback() {
        List<Feedback> feedbacks = feedbackService.findAll();
        feedbackList.clear();
        feedbackList.addAll(feedbacks);
    }

    private void loadClientes() {
        List<Cliente> clientes = clienteService.findAll();
        clientesList.clear();
        clientesList.addAll(clientes);
    }

    private void searchFeedback(String searchTerm) {
        List<Feedback> feedbacks = feedbackService.searchByComentario(searchTerm);
        feedbackList.clear();
        feedbackList.addAll(feedbacks);
    }

    private void filterFeedbackByStatus(String status) {
        List<Feedback> feedbacks = feedbackService.findByStatus(status);
        feedbackList.clear();
        feedbackList.addAll(feedbacks);
    }

    @FXML
    private void handleCreate() {
        if (validateForm()) {
            try {
                Feedback feedback = new Feedback();
                feedback.setIdCliente(clienteComboBox.getValue());
                feedback.setComentario(comentarioField.getText().trim());
                feedback.setStatus(statusComboBox.getValue());

                feedbackService.save(feedback);
                showAlert("Sucesso", "Feedback criado com sucesso!", Alert.AlertType.INFORMATION);
                clearForm();
                loadFeedback();
            } catch (Exception e) {
                showAlert("Erro", "Erro ao criar feedback: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedFeedback == null) {
            showAlert("Erro", "Selecione um feedback para atualizar", Alert.AlertType.ERROR);
            return;
        }

        if (validateForm()) {
            try {
                selectedFeedback.setIdCliente(clienteComboBox.getValue());
                selectedFeedback.setComentario(comentarioField.getText().trim());
                selectedFeedback.setStatus(statusComboBox.getValue());

                feedbackService.save(selectedFeedback);
                showAlert("Sucesso", "Feedback atualizado com sucesso!", Alert.AlertType.INFORMATION);
                clearForm();
                loadFeedback();
            } catch (Exception e) {
                showAlert("Erro", "Erro ao atualizar feedback: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedFeedback == null) {
            showAlert("Erro", "Selecione um feedback para eliminar", Alert.AlertType.ERROR);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminação");
        alert.setHeaderText(null);
        alert.setContentText("Tem certeza que deseja eliminar este feedback?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    feedbackService.deleteById(selectedFeedback.getId());
                    showAlert("Sucesso", "Feedback eliminado com sucesso!", Alert.AlertType.INFORMATION);
                    clearForm();
                    loadFeedback();
                } catch (Exception e) {
                    showAlert("Erro", "Erro ao eliminar feedback: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    @FXML
    private void handleClear() {
        clearForm();
        selectedFeedback = null;
        feedbackTable.getSelectionModel().clearSelection();
        disableButtons();
    }

    private void loadFeedbackToForm(Feedback feedback) {
        clienteComboBox.setValue(feedback.getIdCliente());
        comentarioField.setText(feedback.getComentario());
        statusComboBox.setValue(feedback.getStatus());
    }

    private void clearForm() {
        clienteComboBox.setValue(null);
        comentarioField.clear();
        statusComboBox.setValue("Pendente");
    }

    private boolean validateForm() {
        if (clienteComboBox.getValue() == null) {
            showAlert("Erro", "Cliente é obrigatório", Alert.AlertType.ERROR);
            return false;
        }
        if (comentarioField.getText().trim().isEmpty()) {
            showAlert("Erro", "Comentário é obrigatório", Alert.AlertType.ERROR);
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
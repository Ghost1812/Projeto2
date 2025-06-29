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
    @FXML private TableColumn<Feedback, String> reclamacaoColumn;
    @FXML private TableColumn<Feedback, String> notaServicoColumn;
    @FXML private TableColumn<Feedback, String> opinioesColumn;
    @FXML private TableColumn<Feedback, String> questionarioColumn;

    @FXML private ComboBox<Cliente> clienteComboBox;
    @FXML private TextArea reclamacaoField;
    @FXML private TextArea notadeservicoField;
    @FXML private TextArea opinioesField;
    @FXML private TextArea questionarioField;

    @FXML private Button createButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button clearButton;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private ClienteService clienteService;

    private ObservableList<Feedback> feedbackList;
    private ObservableList<Cliente> clientesList;
    private Feedback selectedFeedback;

    @FXML
    public void initialize() {
        setupTable();
        loadFeedback();
        loadClientes();
        disableButtons();
    }

    private void setupTable() {
        clienteColumn.setCellValueFactory(cellData -> {
            Cliente cliente = cellData.getValue().getIdCliente();
            return new javafx.beans.property.SimpleStringProperty(
                    cliente != null ? cliente.getNome() : "N/A"
            );
        });

        reclamacaoColumn.setCellValueFactory(new PropertyValueFactory<>("reclamacao"));
        notaServicoColumn.setCellValueFactory(new PropertyValueFactory<>("notadeservico"));
        opinioesColumn.setCellValueFactory(new PropertyValueFactory<>("opinioes"));
        questionarioColumn.setCellValueFactory(new PropertyValueFactory<>("questionario"));

        feedbackList = FXCollections.observableArrayList();
        feedbackTable.setItems(feedbackList);

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

    private void loadFeedback() {
        List<Feedback> feedbacks = feedbackService.findAll();
        feedbackList.clear();
        feedbackList.addAll(feedbacks);
    }

    private void loadClientes() {
        List<Cliente> clientes = clienteService.findAll();
        clientesList = FXCollections.observableArrayList(clientes);
        clienteComboBox.setItems(clientesList);
    }

    @FXML
    private void handleCreate() {
        if (validateForm()) {
            try {
                Feedback feedback = new Feedback();
                feedback.setIdCliente(clienteComboBox.getValue());
                feedback.setReclamacao(reclamacaoField.getText().trim());
                feedback.setNotadeservico(notadeservicoField.getText().trim());
                feedback.setOpinioes(opinioesField.getText().trim());
                feedback.setQuestionario(questionarioField.getText().trim());

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
                selectedFeedback.setReclamacao(reclamacaoField.getText().trim());
                selectedFeedback.setNotadeservico(notadeservicoField.getText().trim());
                selectedFeedback.setOpinioes(opinioesField.getText().trim());
                selectedFeedback.setQuestionario(questionarioField.getText().trim());

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
        reclamacaoField.setText(feedback.getReclamacao());
        notadeservicoField.setText(feedback.getNotadeservico());
        opinioesField.setText(feedback.getOpinioes());
        questionarioField.setText(feedback.getQuestionario());
    }

    private void clearForm() {
        clienteComboBox.setValue(null);
        reclamacaoField.clear();
        notadeservicoField.clear();
        opinioesField.clear();
        questionarioField.clear();
    }

    private boolean validateForm() {
        if (clienteComboBox.getValue() == null) {
            showAlert("Erro", "Cliente é obrigatório", Alert.AlertType.ERROR);
            return false;
        }
        if (reclamacaoField.getText().trim().isEmpty()) {
            showAlert("Erro", "Reclamação é obrigatória", Alert.AlertType.ERROR);
            return false;
        }
        if (notadeservicoField.getText().trim().isEmpty()) {
            showAlert("Erro", "Nota de serviço é obrigatória", Alert.AlertType.ERROR);
            return false;
        }
        if (opinioesField.getText().trim().isEmpty()) {
            showAlert("Erro", "Opiniões são obrigatórias", Alert.AlertType.ERROR);
            return false;
        }
        if (questionarioField.getText().trim().isEmpty()) {
            showAlert("Erro", "Questionário é obrigatório", Alert.AlertType.ERROR);
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

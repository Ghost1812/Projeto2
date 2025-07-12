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
import javafx.scene.image.ImageView;
import com.example.proj2.ui.CustomDialog;

@Component
public class FeedbackController {

    @FXML private TableView<Feedback> feedbackTable;
    @FXML private TableColumn<Feedback, String> encomendaColumn;
    @FXML private TableColumn<Feedback, String> clienteColumn;
    @FXML private TableColumn<Feedback, String> questionarioColumn;
    @FXML private TableColumn<Feedback, String> notadeservicoColumn;
    @FXML private TableColumn<Feedback, String> opinioesColumn;
    @FXML private TableColumn<Feedback, String> reclamacaoColumn;
    @FXML private ImageView logoImageView;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private ClienteService clienteService;

    private ObservableList<Feedback> feedbackList;

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
        setupTable();
        loadFeedback();
    }

    private void setupTable() {
        encomendaColumn.setCellValueFactory(cellData -> {
            Feedback fb = cellData.getValue();
            if (fb.getIdEncomenda() != null) {
                return new javafx.beans.property.SimpleStringProperty(fb.getIdEncomenda().getNumeroRastreio());
            }
            return new javafx.beans.property.SimpleStringProperty("-");
        });
        clienteColumn.setCellValueFactory(cellData -> {
            Cliente cliente = cellData.getValue().getIdCliente();
            return new javafx.beans.property.SimpleStringProperty(
                    cliente != null ? cliente.getNome() : "N/A"
            );
        });
        questionarioColumn.setCellValueFactory(cellData -> {
            String raw = cellData.getValue().getQuestionario();
            if (raw != null && !raw.isEmpty()) {
                String[] respostas = raw.split(";");
                return new javafx.beans.property.SimpleStringProperty(String.join(", ", respostas));
            }
            return new javafx.beans.property.SimpleStringProperty("-");
        });
        notadeservicoColumn.setCellValueFactory(new PropertyValueFactory<>("notadeservico"));
        opinioesColumn.setCellValueFactory(new PropertyValueFactory<>("opinioes"));
        reclamacaoColumn.setCellValueFactory(new PropertyValueFactory<>("reclamacao"));

        feedbackList = FXCollections.observableArrayList();
        feedbackTable.setItems(feedbackList);
    }

    private void loadFeedback() {
        List<Feedback> feedbacks = feedbackService.findAllWithEncomendaAndCliente();
        // Filtrar só os que têm encomenda associada
        feedbacks.removeIf(fb -> fb.getIdEncomenda() == null);
        feedbackList.clear();
        feedbackList.addAll(feedbacks);
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

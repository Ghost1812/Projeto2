package com.example.proj2.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomDialog {
    public enum DialogType { INFO, SUCCESS, ERROR }

    public static void show(String title, String message, DialogType type) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UTILITY); // Parecido com Alert padrão
        dialog.setTitle(title);

        // Logo
        ImageView logo = new ImageView(new Image(CustomDialog.class.getResourceAsStream("/images/img.png")));
        logo.setFitHeight(32);
        logo.setFitWidth(32);
        logo.setPreserveRatio(true);

        // Título
        Text titleText = new Text(title);
        titleText.setFont(Font.font("Segoe UI", 16));
        titleText.setStyle("-fx-font-weight: bold;");

        // Mensagem
        Text msgText = new Text(message);
        msgText.setFont(Font.font("Segoe UI", 14));
        msgText.setWrappingWidth(320);

        // Topo: logo + título
        HBox top = new HBox(10, logo, titleText);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(10, 10, 0, 10));

        // Mensagem
        VBox msgBox = new VBox(msgText);
        msgBox.setAlignment(Pos.CENTER_LEFT);
        msgBox.setPadding(new Insets(10, 10, 0, 52)); // alinhado com o texto do título

        // Botão OK
        Button okBtn = new Button("OK");
        okBtn.setFont(Font.font("Segoe UI", 13));
        okBtn.setOnAction(e -> dialog.close());
        HBox btnBox = new HBox(okBtn);
        btnBox.setAlignment(Pos.CENTER_RIGHT);
        btnBox.setPadding(new Insets(10, 10, 10, 10));

        VBox root = new VBox(top, msgBox, btnBox);
        root.setStyle("-fx-background-color: #fff; -fx-border-color: #cfcfcf; -fx-border-width: 1;");
        root.setMinWidth(380);

        Scene scene = new Scene(root);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
} 
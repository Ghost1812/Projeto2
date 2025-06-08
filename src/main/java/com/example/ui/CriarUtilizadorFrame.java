package com.example.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class CriarUtilizadorFrame extends JanelaBase {

    private final Color PACKBEE_COLOR = new Color(230, 180, 60);
    private final Color BACKGROUND_COLOR = Color.WHITE;
    private final Color TEXT_COLOR = new Color(51, 51, 51);

    public CriarUtilizadorFrame() {
        setTitle("Criar Novo Utilizador");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("Novo Utilizador");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(titleLabel);

        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JTextField nomeField = createStyledTextField();
        JTextField emailField = createStyledTextField();
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        JTextField cargoField = createStyledTextField();

        formPanel.add(createLabeledField("Nome completo:", nomeField));
        formPanel.add(createLabeledField("Email:", emailField));
        formPanel.add(createLabeledField("Palavra-passe:", passwordField));
        formPanel.add(createLabeledField("Cargo:", cargoField));

        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton criarContaButton = new JButton("Criar conta");
        criarContaButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        criarContaButton.setBackground(PACKBEE_COLOR);
        criarContaButton.setForeground(Color.WHITE);
        criarContaButton.setFont(new Font("Arial", Font.PLAIN, 16));
        criarContaButton.setFocusPainted(false);
        criarContaButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        criarContaButton.setPreferredSize(new Dimension(180, 40));

        criarContaButton.addActionListener(e -> {
            String nome = nomeField.getText().trim();
            String email = emailField.getText().trim();
            String senha = new String(passwordField.getPassword()).trim();
            String cargo = cargoField.getText().trim();

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || cargo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor preencha todos os campos!");
            } else {
                criarContaNoBanco(nome, email, senha, cargo);
            }
        });

        formPanel.add(criarContaButton);

        add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createLabeledField(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        return panel;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        return field;
    }

    private void criarContaNoBanco(String nome, String email, String senha, String cargo) {
        String url = "jdbc:postgresql://localhost:5433/Projeto2";
        String usuario = "postgres";
        String senhaDb = "trepeteiro00"; // <-- confirma que aqui está certo!

        String sql = "INSERT INTO funcionario (nome, email, password, cargo) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, usuario, senhaDb);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setString(3, senha);
            stmt.setString(4, cargo);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(this, "Utilizador criado com sucesso!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao criar utilizador.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

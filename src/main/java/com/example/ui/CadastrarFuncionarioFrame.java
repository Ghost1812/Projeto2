package com.example.ui;

import com.example.proj2.services.FuncionarioService;

import javax.swing.*;
import java.awt.*;

public class CadastrarFuncionarioFrame extends JFrame {

    public CadastrarFuncionarioFrame(FuncionarioService funcionarioService) {
        setTitle("Cadastrar Novo Funcionário");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new GridBagLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(400, 500));

        JLabel titulo = new JLabel("Novo Funcionário");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));

        JTextField nomeField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField cargoField = new JTextField();

        stylizeField(nomeField, "Nome");
        stylizeField(emailField, "Email");
        stylizeField(passwordField, "Palavra-passe");
        stylizeField(cargoField, "Cargo");

        JButton cadastrar = new JButton("Cadastrar");
        cadastrar.setFont(new Font("SansSerif", Font.BOLD, 16));
        cadastrar.setBackground(new Color(60, 180, 90));
        cadastrar.setForeground(Color.WHITE);
        cadastrar.setFocusPainted(false);
        cadastrar.setMaximumSize(new Dimension(200, 40));
        cadastrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        cadastrar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        cadastrar.addActionListener(e -> {
            String nome = nomeField.getText().trim();
            String email = emailField.getText().trim();
            String senha = new String(passwordField.getPassword()).trim();
            String cargo = cargoField.getText().trim();

            funcionarioService.criarFuncionario(email, senha, cargo, nome);
            JOptionPane.showMessageDialog(this, "Funcionário cadastrado com sucesso!");
            dispose();
        });

        panel.add(titulo);
        panel.add(nomeField);
        panel.add(emailField);
        panel.add(passwordField);
        panel.add(cargoField);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(cadastrar);

        add(panel);
        setVisible(true);
    }

    private void stylizeField(JTextField field, String placeholder) {
        field.setMaximumSize(new Dimension(400, 40));
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createTitledBorder(placeholder));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}

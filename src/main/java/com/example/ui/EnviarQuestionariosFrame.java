package com.example.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class EnviarQuestionariosFrame extends JFrame {
    public EnviarQuestionariosFrame() {
        setTitle("Enviar Questionário ao Cliente");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Questionário de Satisfacção", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        contentPane.add(titulo, BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(20, 40, 20, 40));

        form.add(new JLabel("Cliente:"));
        JComboBox<String> clientesCombo = new JComboBox<>(new String[]{"Cliente A", "Cliente B", "Cliente C"});
        form.add(clientesCombo);

        form.add(Box.createRigidArea(new Dimension(0, 10)));
        form.add(new JLabel("A encomenda chegou dentro do prazo?"));
        JComboBox<String> prazoCombo = new JComboBox<>(new String[]{"Sim", "Não"});
        form.add(prazoCombo);

        form.add(Box.createRigidArea(new Dimension(0, 10)));
        form.add(new JLabel("A embalagem chegou em boas condições?"));
        JComboBox<String> embalagemCombo = new JComboBox<>(new String[]{"Sim", "Não"});
        form.add(embalagemCombo);

        form.add(Box.createRigidArea(new Dimension(0, 10)));
        form.add(new JLabel("Avaliação geral (0-5):"));
        JTextField avaliacaoField = new JTextField();
        form.add(avaliacaoField);

        form.add(Box.createRigidArea(new Dimension(0, 10)));
        form.add(new JLabel("Comentários adicionais:"));
        JTextArea comentarios = new JTextArea(4, 20);
        form.add(new JScrollPane(comentarios));

        contentPane.add(form, BorderLayout.CENTER);

        JButton enviarBtn = new JButton("Enviar Questionário");
        enviarBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Questionário enviado com sucesso."));
        contentPane.add(enviarBtn, BorderLayout.SOUTH);

        setContentPane(contentPane);
        setVisible(true);
    }
}
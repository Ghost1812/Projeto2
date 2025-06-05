package com.example.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class FeedbackFrame extends JFrame {

    private final Color PACKBEE_COLOR = new Color(230, 180, 60);
    private final Color BACKGROUND_COLOR = Color.WHITE;

    public FeedbackFrame() {
        setTitle("Gestão de Feedback");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());

        add(criarHeader(), BorderLayout.NORTH);
        add(criarCentro(), BorderLayout.CENTER);
        add(criarFooter(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel criarHeader() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 20, 0, 0));

        JLabel logo = new JLabel();
        try {
            var iconURL = getClass().getClassLoader().getResource("images/img.png");
            if (iconURL != null) {
                logo.setIcon(new ImageIcon(new ImageIcon(iconURL).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
            }
        } catch (Exception ignored) {}

        JLabel titulo = new JLabel("PACKBEE");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(70, 50, 20));
        titulo.setBorder(new EmptyBorder(0, 10, 0, 0));
        headerPanel.add(logo);
        headerPanel.add(titulo);

        return headerPanel;
    }

    private JPanel criarCentro() {
        JPanel centro = new JPanel();
        centro.setBackground(BACKGROUND_COLOR);
        centro.setBorder(new EmptyBorder(30, 20, 20, 20));
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        JButton visualizarBtn = criarBotao("Visualizar Respostas de Questionários");
        JButton enviarBtn = criarBotao("Enviar Questionário aos Clientes");

        visualizarBtn.addActionListener(e -> visualizarRespostas());
        enviarBtn.addActionListener(e -> enviarQuestionarios());

        centro.add(visualizarBtn);
        centro.add(Box.createVerticalStrut(20));
        centro.add(enviarBtn);

        return centro;
    }

    private JPanel criarFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(BACKGROUND_COLOR);
        footer.setBorder(new EmptyBorder(10, 20, 20, 20));

        JButton fecharBtn = new JButton("Fechar");
        estilizarBotao(fecharBtn);
        fecharBtn.addActionListener(e -> dispose());

        footer.add(fecharBtn);
        return footer;
    }

    private JButton criarBotao(String texto) {
        JButton botao = new JButton(texto);
        estilizarBotao(botao);
        botao.setPreferredSize(new Dimension(300, 40));
        return botao;
    }

    private void estilizarBotao(JButton botao) {
        botao.setFont(new Font("Arial", Font.PLAIN, 14));
        botao.setBackground(PACKBEE_COLOR);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void visualizarRespostas() {
        JOptionPane.showMessageDialog(this, "Visualização das respostas dos questionários (em construção).");
    }

    private void enviarQuestionarios() {
        JOptionPane.showMessageDialog(this, "Questionários enviados com sucesso para todos os clientes.");
    }
}

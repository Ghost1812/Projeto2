package com.example.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VisualizarQuestionariosFrame extends JFrame {
    private JTable tabela;

    public VisualizarQuestionariosFrame() {
        setTitle("Respostas dos Questionários");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Respostas de Clientes", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        contentPane.add(titulo, BorderLayout.NORTH);

        String[] colunas = {"Cliente", "Dentro do Prazo", "Embalagem Ok", "Avaliação", "Comentários"};
        Object[][] dados = {
                {"Cliente A", "Sim", "Sim", "5", "Tudo perfeito."},
                {"Cliente B", "Não", "Sim", "3", "Chegou atrasada."},
                {"Cliente C", "Sim", "Não", "4", "Caixa estava danificada."}
        };

        tabela = new JTable(new DefaultTableModel(dados, colunas));
        JScrollPane scrollPane = new JScrollPane(tabela);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JButton fecharBtn = new JButton("Fechar");
        fecharBtn.addActionListener(e -> dispose());
        contentPane.add(fecharBtn, BorderLayout.SOUTH);

        setContentPane(contentPane);
        setVisible(true);
    }
}
package com.example.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ListarEncomendasFrame extends JFrame {
    private JTable tabela;
    private final Color PACKBEE_COLOR = new Color(230, 180, 60);
    private final Color BACKGROUND_COLOR = Color.WHITE;
    private final Color TEXT_COLOR = new Color(51, 51, 51);
    private final Color HEADER_BG_COLOR = new Color(240, 240, 240);

    public ListarEncomendasFrame() {
        setTitle("Lista de Encomendas");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(0, 20, 10, 20));

        JLabel titleLabel = new JLabel("Encomendas Cadastradas");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(new EmptyBorder(5, 0, 15, 0));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        carregarTabela();
        JScrollPane scrollPane = new JScrollPane(tabela);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton fecharButton = createStyledButton("Fechar");
        fecharButton.addActionListener(e -> dispose());
        buttonPanel.add(fecharButton);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(15, 20, 15, 0));

        ImageIcon logoIcon = null;
        try {
            var logoURL = getClass().getClassLoader().getResource("images/img.png");
            if (logoURL != null) {
                logoIcon = new ImageIcon(new ImageIcon(logoURL).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar logo: " + e.getMessage());
        }

        JLabel logoLabel = new JLabel("PACKBEE", logoIcon, JLabel.LEFT);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 22));
        logoLabel.setForeground(new Color(70, 50, 20));
        logoLabel.setIconTextGap(10);
        panel.add(logoLabel);

        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.setBackground(PACKBEE_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(150, 35));
        return btn;
    }

    private void carregarTabela() {
        String[] colunas = {"ID", "Peso (kg)", "ID Cliente", "ID Rececionista", "ID Operador", "Estado Integridade", "Estado Entrega"};

        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Projeto2", "postgres", "trepeteiro00");
             PreparedStatement stmt = conn.prepareStatement("SELECT id_encomenda, peso, id_cliente, id_rececionista, id_operador, estado_integridade, estado_entrega FROM encomenda");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id_encomenda"),
                        rs.getDouble("peso"),
                        rs.getInt("id_cliente"),
                        rs.getInt("id_rececionista"),
                        rs.getInt("id_operador"),
                        rs.getString("estado_integridade") != null ? rs.getString("estado_integridade") : "N/A",
                        rs.getString("estado_entrega") != null ? rs.getString("estado_entrega") : "N/A"
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar encomendas: " + e.getMessage());
        }

        tabela = new JTable(model);
        JTableHeader header = tabela.getTableHeader();
        header.setBackground(HEADER_BG_COLOR);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setForeground(TEXT_COLOR);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        tabela.setRowHeight(30);
        tabela.setFont(new Font("Arial", Font.PLAIN, 14));
        tabela.setShowGrid(true);
        tabela.setGridColor(new Color(230, 230, 230));
        tabela.setRowSelectionAllowed(true);
        tabela.setSelectionBackground(new Color(245, 245, 220));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }
}

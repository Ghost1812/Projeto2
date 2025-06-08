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

public class ListarFuncionariosFrame extends JanelaBase {

    private JTable tabela;
    private final Color PACKBEE_COLOR = new Color(230, 180, 60);
    private final Color BACKGROUND_COLOR = Color.WHITE;
    private final Color TEXT_COLOR = new Color(51, 51, 51);
    private final Color HEADER_BG_COLOR = new Color(240, 240, 240);

    public ListarFuncionariosFrame() {
        setTitle("Lista de Funcionários");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(0, 20, 10, 20));

        JLabel titleLabel = new JLabel("Funcionários Cadastrados");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(new EmptyBorder(5, 0, 15, 0));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        carregarTabela();
        JScrollPane scrollPane = new JScrollPane(tabela);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JButton fecharButton = createStyledButton("Fechar");
        fecharButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
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
                logoIcon = new ImageIcon(logoURL);
                Image img = logoIcon.getImage();
                Image newImg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                logoIcon = new ImageIcon(newImg);
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
        try {
            String url = "jdbc:postgresql://localhost:5433/Projeto2";
            String usuario = "postgres";
            String senhaDb = "trepeteiro00"; // <- confirma aqui!

            Connection conn = DriverManager.getConnection(url, usuario, senhaDb);

            String sql = "SELECT id, nome, email, cargo FROM funcionario";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            String[] colunas = {"ID", "Nome", "Email", "Cargo"};
            DefaultTableModel model = new DefaultTableModel(colunas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("cargo")
                });
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
            tabela.setSelectionBackground(new Color(245, 245, 220));

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
            tabela.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Cargo

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar funcionários: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

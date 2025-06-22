// Define o pacote da classe
package com.example.ui;

// Importações necessárias para interface gráfica
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

// Importações para conexão com banco de dados PostgreSQL via JDBC
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// Classe que representa a janela de listagem de encomendas
public class ListarEncomendasFrame extends JanelaBase {

    private JTable tabela; // Tabela onde os dados das encomendas serão exibidos

    // Cores usadas na interface
    private final Color PACKBEE_COLOR = new Color(230, 180, 60);
    private final Color BACKGROUND_COLOR = Color.WHITE;
    private final Color TEXT_COLOR = new Color(51, 51, 51);
    private final Color HEADER_BG_COLOR = new Color(240, 240, 240);

    // Construtor da janela
    public ListarEncomendasFrame() {
        setTitle("Lista de Encomendas");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(10, 10)); // Layout com espaçamento

        // Cabeçalho com logotipo
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Painel de conteúdo principal
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(0, 20, 10, 20)); // Margens internas

        // Título da seção
        JLabel titleLabel = new JLabel("Encomendas Cadastradas");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(new EmptyBorder(5, 0, 15, 0));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        // Carrega os dados da tabela e adiciona à interface
        carregarTabela();
        JScrollPane scrollPane = new JScrollPane(tabela);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Painel de botões no rodapé
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton fecharButton = createStyledButton("Fechar");
        fecharButton.addActionListener(e -> dispose());
        buttonPanel.add(fecharButton);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true); // Exibe a janela
    }

    // Cria o painel do cabeçalho com o logo e nome da empresa
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(15, 20, 15, 0));

        ImageIcon logoIcon = null;
        try {
            var logoURL = getClass().getClassLoader().getResource("images/img.png");
            if (logoURL != null) {
                logoIcon = new ImageIcon(new ImageIcon(logoURL)
                        .getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
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

    // Cria um botão com estilo padronizado da aplicação
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

    // Método responsável por buscar os dados do banco e configurar a tabela
    private void carregarTabela() {
        // Define os nomes das colunas
        String[] colunas = {
                "ID", "Peso (kg)", "ID Cliente", "ID Rececionista",
                "ID Operador", "Estado Integridade", "Estado Entrega"
        };

        // Modelo da tabela com células não editáveis
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Conexão JDBC com o banco de dados PostgreSQL
        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5433/Projeto2",
                "postgres",
                "trepeteiro00");

             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT id_encomenda, peso, id_cliente, id_rececionista, id_operador, estado_integridade, estado_entrega FROM encomenda");

             ResultSet rs = stmt.executeQuery()) {

            // Percorre os resultados e adiciona cada linha ao modelo
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

        // Cria a tabela e aplica o modelo
        tabela = new JTable(model);

        // Estiliza o cabeçalho da tabela
        JTableHeader header = tabela.getTableHeader();
        header.setBackground(HEADER_BG_COLOR);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setForeground(TEXT_COLOR);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        // Estiliza as linhas da tabela
        tabela.setRowHeight(30);
        tabela.setFont(new Font("Arial", Font.PLAIN, 14));
        tabela.setShowGrid(true);
        tabela.setGridColor(new Color(230, 230, 230));
        tabela.setRowSelectionAllowed(true);
        tabela.setSelectionBackground(new Color(245, 245, 220));

        // Centraliza o conteúdo de todas as colunas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }
}

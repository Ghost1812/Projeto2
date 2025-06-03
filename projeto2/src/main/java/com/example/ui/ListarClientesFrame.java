package com.example.ui;

import com.example.proj2.models.Cliente;
import com.example.proj2.repositories.ClienteRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class ListarClientesFrame extends JFrame {

    private final ClienteRepository clienteRepository;
    private JTable tabela;
    private final Color PACKBEE_COLOR = new Color(230, 180, 60);
    private final Color BACKGROUND_COLOR = Color.WHITE;
    private final Color TEXT_COLOR = new Color(51, 51, 51);
    private final Color HEADER_BG_COLOR = new Color(240, 240, 240);

    public ListarClientesFrame(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;

        setTitle("Lista de Clientes");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(10, 10));

        // Adiciona painel com logo no topo
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Adiciona a tabela dentro de um painel
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(0, 20, 10, 20));

        // Título da seção
        JLabel titleLabel = new JLabel("Clientes Cadastrados");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(new EmptyBorder(5, 0, 15, 0));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        // Carrega os dados na tabela
        carregarTabela();
        JScrollPane scrollPane = new JScrollPane(tabela);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Adiciona botões de ação na parte inferior
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton adicionarButton = createStyledButton("Adicionar Cliente");
        adicionarButton.addActionListener(e -> abrirAdicionarCliente());

        JButton editarButton = createStyledButton("Editar Selecionado");
        editarButton.addActionListener(e -> editarClienteSelecionado());

        JButton fecharButton = createStyledButton("Fechar");
        fecharButton.addActionListener(e -> dispose());

        buttonPanel.add(adicionarButton);
        buttonPanel.add(editarButton);
        buttonPanel.add(fecharButton);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(15, 20, 15, 0));

        // Tenta carregar o ícone do logo
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
            List<Cliente> clientes = clienteRepository.findAll();
            String[] colunas = {"ID", "Nome", "Email", "Contacto", "Rua", "Número Porta", "Código Postal"};

            // Usar DefaultTableModel para melhor controle da tabela
            DefaultTableModel model = new DefaultTableModel(colunas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;  // Impede edição direta na tabela
                }
            };

            for (Cliente c : clientes) {
                model.addRow(new Object[] {
                        c.getId() != null ? c.getId().toString() : "N/A",
                        c.getNome() != null ? c.getNome() : "N/A",
                        c.getEmail() != null ? c.getEmail() : "N/A",
                        c.getContacto() != null ? c.getContacto().toString() : "N/A",
                        c.getRua() != null ? c.getRua() : "N/A",
                        c.getNumeroPorta() != null ? c.getNumeroPorta().toString() : "N/A",
                        c.getCodpostal() != null ? c.getCodpostal() : "N/A"
                });
            }

            tabela = new JTable(model);

            // Estilizar cabeçalho
            JTableHeader header = tabela.getTableHeader();
            header.setBackground(HEADER_BG_COLOR);
            header.setFont(new Font("Arial", Font.BOLD, 14));
            header.setForeground(TEXT_COLOR);
            header.setPreferredSize(new Dimension(header.getWidth(), 35));

            // Estilizar células
            tabela.setRowHeight(30);
            tabela.setFont(new Font("Arial", Font.PLAIN, 14));
            tabela.setShowGrid(true);
            tabela.setGridColor(new Color(230, 230, 230));
            tabela.setRowSelectionAllowed(true);
            tabela.setColumnSelectionAllowed(false);
            tabela.setSelectionBackground(new Color(245, 245, 220));

            // Centralizar conteúdo de algumas colunas
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

            // Aplicar renderizador para colunas específicas
            tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
            tabela.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // Número Porta

            // Ajustar largura das colunas
            tabela.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
            tabela.getColumnModel().getColumn(1).setPreferredWidth(150); // Nome
            tabela.getColumnModel().getColumn(2).setPreferredWidth(150); // Email
            tabela.getColumnModel().getColumn(3).setPreferredWidth(100); // Contacto
            tabela.getColumnModel().getColumn(4).setPreferredWidth(180); // Rua
            tabela.getColumnModel().getColumn(5).setPreferredWidth(100); // Número Porta
            tabela.getColumnModel().getColumn(6).setPreferredWidth(100); // Código Postal

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void abrirAdicionarCliente() {
        // Implementação para abrir tela de adicionar cliente
        JOptionPane.showMessageDialog(this, "Funcionalidade para adicionar cliente");
    }

    private void editarClienteSelecionado() {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow >= 0) {
            String clienteId = (String) tabela.getValueAt(selectedRow, 0);
            // Implementação para editar cliente
            JOptionPane.showMessageDialog(this, "Editar cliente ID: " + clienteId);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Selecione um cliente para editar",
                    "Nenhum cliente selecionado",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
package com.example.ui;

import com.example.proj2.models.Cliente;
import com.example.proj2.models.Funcionario;
import com.example.proj2.repositories.ClienteRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

public class CriarEncomendaFrame extends JanelaBase {

    private final ClienteRepository clienteRepository;
    private final Funcionario funcionario;
    private JComboBox<Cliente> comboBoxClientes;
    private JTextField pesoField;

    private final Color PACKBEE_COLOR = new Color(230, 180, 60);
    private final Color HOVER_COLOR = new Color(240, 200, 80);
    private final Color BACKGROUND_COLOR = Color.WHITE;
    private final Color TEXT_COLOR = new Color(51, 51, 51);
    private final Color FIELD_BG_COLOR = new Color(245, 245, 245);
    private final Color BORDER_COLOR = new Color(220, 220, 220);

    public CriarEncomendaFrame(ClienteRepository clienteRepository, Funcionario funcionario) {
        this.clienteRepository = clienteRepository;
        this.funcionario = funcionario;

        setTitle("Criar Nova Encomenda");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(15, 20, 5, 20));

        ImageIcon logoIcon = null;
        try {
            var logoURL = getClass().getClassLoader().getResource("images/img.png");
            if (logoURL != null) {
                logoIcon = new ImageIcon(logoURL);
                Image img = logoIcon.getImage();
                Image newImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                logoIcon = new ImageIcon(newImg);
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar logo: " + e.getMessage());
        }

        JLabel tituloLabel = new JLabel("Criar Encomenda", logoIcon, JLabel.CENTER);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 22));
        tituloLabel.setForeground(TEXT_COLOR);
        tituloLabel.setIconTextGap(10);
        panel.add(tituloLabel);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(15, 25, 15, 25));
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        JLabel clienteLabel = new JLabel("Cliente");
        clienteLabel.setFont(new Font("Arial", Font.BOLD, 14));
        clienteLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(clienteLabel, gbc);

        comboBoxClientes = new JComboBox<>();
        comboBoxClientes.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBoxClientes.setBackground(FIELD_BG_COLOR);
        comboBoxClientes.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        comboBoxClientes.setPreferredSize(new Dimension(300, 35));
        carregarClientes();
        gbc.gridy = 1;
        panel.add(comboBoxClientes, gbc);

        JLabel pesoLabel = new JLabel("Peso (kg)");
        pesoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        pesoLabel.setForeground(TEXT_COLOR);
        gbc.gridy = 2;
        panel.add(pesoLabel, gbc);

        pesoField = new JTextField();
        pesoField.setFont(new Font("Arial", Font.PLAIN, 14));
        pesoField.setPreferredSize(new Dimension(300, 35));
        pesoField.setBackground(FIELD_BG_COLOR);
        pesoField.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        gbc.gridy = 3;
        panel.add(pesoField, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(5, 20, 20, 20));

        JButton cadastrarButton = createStyledButton("Cadastrar Encomenda");
        cadastrarButton.setPreferredSize(new Dimension(200, 40));
        cadastrarButton.setFont(new Font("Arial", Font.BOLD, 14));
        cadastrarButton.addActionListener(e -> cadastrarEncomenda());

        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.setPreferredSize(new Dimension(120, 40));
        cancelarButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelarButton.setBackground(new Color(245, 245, 245));
        cancelarButton.setForeground(TEXT_COLOR);
        cancelarButton.setBorderPainted(false);
        cancelarButton.setFocusPainted(false);
        cancelarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelarButton.addActionListener(e -> dispose());

        panel.add(cadastrarButton);
        panel.add(Box.createRigidArea(new Dimension(15, 0)));
        panel.add(cancelarButton);

        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.setBackground(PACKBEE_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(PACKBEE_COLOR);
            }
        });

        return btn;
    }

    private void carregarClientes() {
        comboBoxClientes.removeAllItems();
        List<Cliente> clientes = clienteRepository.findAll();
        for (Cliente cliente : clientes) {
            comboBoxClientes.addItem(cliente);
        }
    }

    public void atualizarClientesDepoisDeCriar() {
        carregarClientes();
        comboBoxClientes.setSelectedIndex(comboBoxClientes.getItemCount() - 1);
    }

    private void cadastrarEncomenda() {
        Cliente clienteSelecionado = (Cliente) comboBoxClientes.getSelectedItem();
        String pesoTexto = pesoField.getText().trim();

        if (clienteSelecionado == null || pesoTexto.isEmpty()) {
            mostrarMensagemErro("Por favor selecione um cliente e insira o peso!");
            return;
        }

        try {
            double peso = Double.parseDouble(pesoTexto);

            String sql = "INSERT INTO encomenda (peso, id_cliente, id_rececionista, estado_integridade, estado_entrega) " +
                    "VALUES (?, ?, ?, NULL, 'Recebido')";

            try (Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5433/Projeto2",
                    "postgres",
                    "trepeteiro00"
            );
                 PreparedStatement stmt = conn.prepareStatement(sql)
            ) {
                stmt.setDouble(1, peso);
                stmt.setInt(2, clienteSelecionado.getId().intValue());
                stmt.setInt(3, funcionario.getId().intValue());

                stmt.executeUpdate();
                mostrarMensagemSucesso("Encomenda criada com sucesso!");
                dispose();
            }

        } catch (NumberFormatException e) {
            mostrarMensagemErro("Peso inválido. Insira um número!");
        } catch (Exception e) {
            mostrarMensagemErro("Erro ao cadastrar encomenda: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarMensagemSucesso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarMensagemErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}

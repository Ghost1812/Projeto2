package com.example.ui;

import com.example.proj2.models.Cliente;
import com.example.proj2.repositories.ClienteRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CriarClienteFrame extends JFrame {

    private final CriarEncomendaFrame parentFrame;
    private final ClienteRepository clienteRepository;
    private final Color PACKBEE_COLOR = new Color(230, 180, 60);
    private final Color BACKGROUND_COLOR = Color.WHITE;
    private final Color TEXT_COLOR = new Color(51, 51, 51);

    public CriarClienteFrame(CriarEncomendaFrame parentFrame, ClienteRepository clienteRepository) {
        this.parentFrame = parentFrame;
        this.clienteRepository = clienteRepository;

        setTitle("Criar Novo Cliente");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("Novo Cliente");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(titleLabel);

        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JTextField nomeField = createStyledTextField();
        JTextField codpostalField = createStyledTextField();
        JTextField numeroPortaField = createStyledTextField();
        JTextField ruaField = createStyledTextField();
        JTextField contactoField = createStyledTextField();
        JTextField emailField = createStyledTextField();

        formPanel.add(createLabeledField("Nome:", nomeField));
        formPanel.add(createLabeledField("Código Postal:", codpostalField));
        formPanel.add(createLabeledField("Número Porta:", numeroPortaField));
        formPanel.add(createLabeledField("Rua:", ruaField));
        formPanel.add(createLabeledField("Contacto:", contactoField));
        formPanel.add(createLabeledField("Email:", emailField));

        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton criarButton = new JButton("Criar Cliente");
        criarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        criarButton.setBackground(PACKBEE_COLOR);
        criarButton.setForeground(Color.WHITE);
        criarButton.setFont(new Font("Arial", Font.PLAIN, 16));
        criarButton.setFocusPainted(false);
        criarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        criarButton.setPreferredSize(new Dimension(180, 40));

        criarButton.addActionListener(e -> {
            String nome = nomeField.getText().trim();
            String codpostal = codpostalField.getText().trim();
            String numeroPorta = numeroPortaField.getText().trim();
            String rua = ruaField.getText().trim();
            String contacto = contactoField.getText().trim();
            String email = emailField.getText().trim();

            if (nome.isEmpty() || codpostal.isEmpty() || numeroPorta.isEmpty() || rua.isEmpty() || contacto.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
                return;
            }

            try {
                Cliente novoCliente = new Cliente();
                novoCliente.setNome(nome);
                novoCliente.setCodpostal(codpostal);
                novoCliente.setNumeroPorta(Integer.parseInt(numeroPorta));
                novoCliente.setRua(rua);
                novoCliente.setContacto(contacto);
                novoCliente.setEmail(email);

                clienteRepository.save(novoCliente);

                JOptionPane.showMessageDialog(this, "Cliente criado com sucesso!");

                parentFrame.atualizarClientesDepoisDeCriar();
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao criar cliente: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        formPanel.add(criarButton);

        add(formPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createLabeledField(String labelText, JTextField field) {
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
}

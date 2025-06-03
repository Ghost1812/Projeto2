package com.example.ui;

import com.example.proj2.models.Funcionario;
import com.example.proj2.models.UtilizadorAtual;
import com.example.proj2.repositories.ClienteRepository;
import com.example.proj2.repositories.EncomendaRepository;
import com.example.proj2.repositories.RececionistaRepository;
import com.example.proj2.services.FuncionarioService;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class LoginFrame extends JFrame {

    private final FuncionarioService funcionarioService;

    public LoginFrame(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;

        setTitle("PackBee - Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new GridBagLayout());

        try {
            URL iconURL = getClass().getClassLoader().getResource("images/img.png");
            if (iconURL != null) {
                Image icon = new ImageIcon(iconURL).getImage();
                setIconImage(icon);
            }
        } catch (Exception e) {
            System.err.println("Erro ao definir ícone: " + e.getMessage());
        }

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logo = new JLabel();
        try {
            URL resourceUrl = getClass().getClassLoader().getResource("images/img.png");
            if (resourceUrl != null) {
                ImageIcon icon = new ImageIcon(resourceUrl);
                Image image = icon.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH);
                logo.setIcon(new ImageIcon(image));
                logo.setAlignmentX(Component.CENTER_ALIGNMENT);
                logo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar imagem: " + e.getMessage());
        }

        JLabel titulo = new JLabel("LOGIN");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setBackground(Color.WHITE);
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setMaximumSize(new Dimension(400, 200));
        fieldsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JTextField emailField = new JTextField();
        stylizeField(emailField);

        JLabel passwordLabel = new JLabel("Palavra-passe");
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JPasswordField passwordField = new JPasswordField();
        stylizeField(passwordField);

        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        fieldsPanel.add(emailLabel);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        fieldsPanel.add(emailField);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        fieldsPanel.add(passwordLabel);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        fieldsPanel.add(passwordField);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton loginBtn = new JButton("Entrar");
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginBtn.setBackground(new Color(230, 180, 60));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(400, 40));
        loginBtn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        loginBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String senha = new String(passwordField.getPassword()).trim();

            Funcionario funcionario = funcionarioService.login(email, senha);
            if (funcionario != null) {
                JOptionPane.showMessageDialog(this, "Bem-vindo, " + funcionario.getNome());

                // Guardar quem está logado
                UtilizadorAtual.email = funcionario.getEmail(); // pega o email do objeto funcionario
                if ("admin".equalsIgnoreCase(funcionario.getCargo())) {
                    UtilizadorAtual.tipo = "admin";
                } else {
                    UtilizadorAtual.tipo = "normal";
                }

                dispose();

                // Agora abre diretamente o menu principal
                var context = com.example.SpringContext.getContext();
                EncomendaRepository encomendaRepository = context.getBean(EncomendaRepository.class);
                ClienteRepository clienteRepository = context.getBean(ClienteRepository.class);
                RececionistaRepository rececionistaRepository = context.getBean(RececionistaRepository.class);

                new MainMenuJava(funcionario, encomendaRepository, clienteRepository, rececionistaRepository);
            }


        });

        panel.add(logo);
        panel.add(titulo);
        panel.add(fieldsPanel);
        panel.add(loginBtn);

        add(panel);
        setVisible(true);
    }

    private void stylizeField(JTextField field) {
        field.setPreferredSize(new Dimension(400, 40));
        field.setMaximumSize(new Dimension(400, 40));
        field.setMinimumSize(new Dimension(400, 40));
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
    }
}

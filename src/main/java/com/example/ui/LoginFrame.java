// Pacote da interface gráfica
package com.example.ui;

// Importações de classes de modelo, repositórios e serviços
import com.example.proj2.models.Funcionario;
import com.example.proj2.models.UtilizadorAtual;
import com.example.proj2.repositories.ClienteRepository;
import com.example.proj2.repositories.EncomendaRepository;
import com.example.proj2.repositories.RececionistaRepository;
import com.example.proj2.services.FuncionarioService;

// Importações da biblioteca Swing e AWT
import javax.swing.*;
import java.awt.*;
import java.net.URL;

// Classe LoginFrame: tela de login do sistema
public class LoginFrame extends JFrame {

    // Serviço que faz a autenticação de usuários
    private final FuncionarioService funcionarioService;

    // Construtor que recebe o serviço de funcionário
    public LoginFrame(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;

        // Configuração da janela
        setTitle("PackBee - Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Tela cheia
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Fecha o app ao sair
        getContentPane().setBackground(Color.WHITE);
        setLayout(new GridBagLayout()); // Centraliza os componentes

        // Tenta definir o ícone da janela
        try {
            URL iconURL = getClass().getClassLoader().getResource("images/img.png");
            if (iconURL != null) {
                Image icon = new ImageIcon(iconURL).getImage();
                setIconImage(icon);
            }
        } catch (Exception e) {
            System.err.println("Erro ao definir ícone: " + e.getMessage());
        }

        // Painel principal
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Carrega e exibe a imagem do logo
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

        // Título da tela
        JLabel titulo = new JLabel("LOGIN");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Painel dos campos de email e senha
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setBackground(Color.WHITE);
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setMaximumSize(new Dimension(400, 200));
        fieldsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campo e label do email
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JTextField emailField = new JTextField();
        stylizeField(emailField); // Aplica estilo

        // Campo e label da palavra-passe
        JLabel passwordLabel = new JLabel("Palavra-passe");
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JPasswordField passwordField = new JPasswordField();
        stylizeField(passwordField);

        // Ajusta alinhamento dos labels
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Adiciona campos e espaçamentos
        fieldsPanel.add(emailLabel);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        fieldsPanel.add(emailField);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        fieldsPanel.add(passwordLabel);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        fieldsPanel.add(passwordField);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Botão de login
        JButton loginBtn = new JButton("Entrar");
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginBtn.setBackground(new Color(230, 180, 60));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(400, 40));
        loginBtn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Ação do botão de login
        loginBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String senha = new String(passwordField.getPassword()).trim();

            // Faz login com o serviço
            Funcionario funcionario = funcionarioService.login(email, senha);

            if (funcionario != null) {
                JOptionPane.showMessageDialog(this, "Bem-vindo, " + funcionario.getNome());

                // Guarda os dados do utilizador autenticado
                UtilizadorAtual.email = funcionario.getEmail();
                UtilizadorAtual.tipo = "admin".equalsIgnoreCase(funcionario.getCargo()) ? "admin" : "normal";

                dispose(); // Fecha a janela de login

                // Carrega o menu principal com os repositórios
                var context = com.example.SpringContext.getContext();
                EncomendaRepository encomendaRepository = context.getBean(EncomendaRepository.class);
                ClienteRepository clienteRepository = context.getBean(ClienteRepository.class);
                RececionistaRepository rececionistaRepository = context.getBean(RececionistaRepository.class);

                new MainMenuJava(funcionario, encomendaRepository, clienteRepository, rececionistaRepository);
            }
        });

        // Monta o painel
        panel.add(logo);
        panel.add(titulo);
        panel.add(fieldsPanel);
        panel.add(loginBtn);

        // Adiciona o painel à janela
        add(panel);
        setVisible(true);
    }

    // Método que aplica estilo aos campos de texto
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

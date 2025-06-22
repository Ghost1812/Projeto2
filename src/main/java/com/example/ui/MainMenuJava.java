// Pacote onde está localizada a classe
package com.example.ui;

// Importações necessárias
import com.example.SpringContext;
import com.example.proj2.models.Funcionario;
import com.example.proj2.repositories.ClienteRepository;
import com.example.proj2.repositories.EncomendaRepository;
import com.example.proj2.repositories.RececionistaRepository;
import com.example.proj2.services.FuncionarioService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.Normalizer;

// Classe principal do menu após o login
public class MainMenuJava extends JFrame {

    // Atributos para controle de repositórios e o funcionário autenticado
    private final Funcionario funcionario;
    private final EncomendaRepository encomendaRepository;
    private final ClienteRepository clienteRepository;
    private final RececionistaRepository rececionistaRepository;

    // Cores usadas no design
    private final Color PACKBEE_COLOR = new Color(230, 180, 60);       // cor padrão dos botões
    private final Color HOVER_COLOR = new Color(240, 200, 80);         // cor ao passar o rato
    private final Color BACKGROUND_COLOR = Color.WHITE;                // fundo
    private final Color TEXT_COLOR = new Color(51, 51, 51);            // texto

    // Construtor do menu principal
    public MainMenuJava(
            Funcionario funcionario,
            EncomendaRepository encomendaRepository,
            ClienteRepository clienteRepository,
            RececionistaRepository rececionistaRepository
    ) {
        this.funcionario = funcionario;
        this.encomendaRepository = encomendaRepository;
        this.clienteRepository = clienteRepository;
        this.rececionistaRepository = rececionistaRepository;

        setTitle("PackBee - Menu Principal");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());

        // Tenta carregar o ícone da aplicação
        try {
            var iconURL = getClass().getClassLoader().getResource("images/img.png");
            if (iconURL != null) {
                ImageIcon icon = new ImageIcon(iconURL);
                setIconImage(icon.getImage());
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar ícone: " + e.getMessage());
        }

        // Adiciona os 3 painéis principais: header, centro e rodapé
        add(createHeaderPanel(), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.add(createMenuPanel());
        add(centerPanel, BorderLayout.CENTER);

        add(createFooterPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    // Painel superior (logo + nome do sistema)
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 0, 10, 0));

        ImageIcon logoIcon = null;
        try {
            var logoURL = getClass().getClassLoader().getResource("images/img.png");
            if (logoURL != null) {
                logoIcon = new ImageIcon(logoURL.getPath());
                Image img = logoIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                logoIcon = new ImageIcon(img);
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar logo: " + e.getMessage());
        }

        JLabel titleLabel = new JLabel("PACKBEE", logoIcon, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(new Color(70, 50, 20));
        titleLabel.setIconTextGap(15);
        panel.add(titleLabel);

        return panel;
    }

    // Painel central com os botões do menu, adaptado ao cargo
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Menu Principal");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(TEXT_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        panel.add(title);

        // Normaliza o cargo para facilitar a comparação
        String cargo = normalizar(funcionario.getCargo());
        System.out.println("Cargo normalizado: '" + cargo + "'");

        // Menu para cada tipo de cargo
        if (cargo.equals("admin")) {
            panel.add(createStyledButton("Criar Novo Utilizador", e -> abrirCriarNovoUtilizador()));
            panel.add(Box.createRigidArea(new Dimension(0, 15)));
            panel.add(createStyledButton("Listar Encomendas", e -> abrirListarEncomendas()));
            panel.add(Box.createRigidArea(new Dimension(0, 15)));
            panel.add(createStyledButton("Listar Clientes", e -> abrirListarClientes()));
            panel.add(Box.createRigidArea(new Dimension(0, 15)));
            panel.add(createStyledButton("Listar Funcionários", e -> abrirListarFuncionarios()));
        } else if (cargo.equals("rececionista")) {
            panel.add(createStyledButton("Criar Encomenda", e -> abrirCriarEncomenda()));
            panel.add(Box.createRigidArea(new Dimension(0, 15)));
            panel.add(createStyledButton("Listar Encomendas", e -> abrirListarEncomendas()));
            panel.add(Box.createRigidArea(new Dimension(0, 15)));
            panel.add(createStyledButton("Listar Clientes", e -> abrirListarClientes()));
        } else if (cargo.equals("operador")) {
            panel.add(createStyledButton("Triagem de Encomendas", e -> abrirTriagemEncomendas()));
            panel.add(Box.createRigidArea(new Dimension(0, 15)));
        } else if (cargo.equals("estafeta")) {
            panel.add(createStyledButton("Alterar Estado de Entrega", e -> abrirEntregaEncomendas()));
            panel.add(Box.createRigidArea(new Dimension(0, 15)));
        } else if (cargo.equals("agentefeedback")) {
            panel.add(createStyledButton("Ver Respostas de Feedback", e -> abrirFeedback()));
            panel.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Botão logout
        JButton logoutBtn = createStyledButton("Logout", e -> fazerLogout());
        logoutBtn.setBackground(new Color(245, 245, 245));
        logoutBtn.setForeground(new Color(100, 100, 100));
        panel.add(logoutBtn);

        return panel;
    }

    // Normaliza o cargo removendo acentos e espaços
    private String normalizar(String input) {
        if (input == null) return "";
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "").replaceAll("\\s+", "").toLowerCase();
    }

    // Cria um botão estilizado
    private JButton createStyledButton(String text, java.awt.event.ActionListener action) {
        JButton btn = new JButton(text);
        btn.addActionListener(action);
        btn.setFont(new Font("Arial", Font.PLAIN, 16));
        btn.setBackground(PACKBEE_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setMaximumSize(new Dimension(300, 50));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efeito hover
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

    // Painel inferior com informação do utilizador
    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(new Color(250, 250, 250));
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel userLabel = new JLabel("Usuário: " + funcionario.getNome() + " | Cargo: " + funcionario.getCargo());
        userLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        userLabel.setForeground(new Color(100, 100, 100));
        panel.add(userLabel);

        return panel;
    }

    // Abertura das janelas conforme ação do menu
    private void abrirCriarEncomenda() {
        new CriarEncomendaFrame(clienteRepository, funcionario);
    }

    private void abrirCriarNovoUtilizador() {
        new CriarUtilizadorFrame();
    }

    private void abrirListarEncomendas() {
        new ListarEncomendasFrame();
    }

    private void abrirListarClientes() {
        new ListarClientesFrame(clienteRepository);
    }

    private void abrirListarFuncionarios() {
        new ListarFuncionariosFrame();
    }

    private void fazerLogout() {
        dispose(); // Fecha o menu
        var context = SpringContext.getContext();
        FuncionarioService funcionarioService = context.getBean(FuncionarioService.class);
        new LoginFrame(funcionarioService); // Volta à tela de login
    }

    private void abrirTriagemEncomendas() {
        new TriagemEncomendasFrame();
    }

    private void abrirEntregaEncomendas() {
        new EntregaEncomendasFrame();
    }

    private void abrirFeedback() {
        new VisualizarFeedbackFrame(); // Classe ainda deve ser implementada
    }
}

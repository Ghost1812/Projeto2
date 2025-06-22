package com.example.ui;

import com.example.SpringContext;
import com.example.proj2.models.Feedback;
import com.example.proj2.repositories.FeedbackRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VisualizarFeedbackFrame extends JanelaBase {

    // Repositório para acessar os dados de feedback
    private final FeedbackRepository feedbackRepository;

    public VisualizarFeedbackFrame() {
        // Obtém o bean do repositório a partir do contexto Spring
        this.feedbackRepository = SpringContext.getContext().getBean(FeedbackRepository.class);

        // Configurações básicas da janela
        setTitle("Respostas de Feedback dos Clientes");
        setSize(1000, 600);
        setLocationRelativeTo(null); // centraliza a janela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);

        initComponents(); // inicializa os componentes da UI

        setVisible(true);
    }

    private void initComponents() {
        // Painel principal com margem e cor de fundo
        JPanel contentPane = new JPanel(new BorderLayout(15, 15));
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setBackground(Color.WHITE);
        setContentPane(contentPane);

        // Cabeçalho com logotipo e nome da aplicação
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(Color.WHITE);

        JLabel logoLabel = new JLabel();
        try {
            var logoURL = getClass().getClassLoader().getResource("images/img.png");
            if (logoURL != null) {
                ImageIcon icon = new ImageIcon(logoURL);
                Image image = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(image));
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar logo: " + e.getMessage());
        }

        JLabel titleLabel = new JLabel("PACKBEE");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(new Color(70, 50, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        headerPanel.add(logoLabel);
        headerPanel.add(titleLabel);
        contentPane.add(headerPanel, BorderLayout.NORTH);

        // Subtítulo da tabela
        JLabel subtitle = new JLabel("Respostas de Feedback dos Clientes", JLabel.CENTER);
        subtitle.setFont(new Font("Arial", Font.BOLD, 18));
        subtitle.setForeground(new Color(51, 51, 51));
        contentPane.add(subtitle, BorderLayout.BEFORE_FIRST_LINE);

        // Tabela que irá listar os feedbacks
        JTable table = new JTable();
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[]{"ID", "ID do Cliente", "Reclamação", "Nota de Serviço", "Opiniões", "Questionário"}, 0
        );
        table.setModel(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Rodapé com botão de fechar
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(Color.WHITE);

        JButton fecharBtn = new JButton("Fechar");
        fecharBtn.setPreferredSize(new Dimension(100, 30));
        fecharBtn.setBackground(new Color(230, 180, 60));
        fecharBtn.setForeground(Color.BLACK);
        fecharBtn.setFocusPainted(false);
        fecharBtn.addActionListener(e -> dispose()); // Fecha a janela ao clicar
        footerPanel.add(fecharBtn);

        contentPane.add(footerPanel, BorderLayout.SOUTH);

        preencherTabela(tableModel); // Carrega os dados para a tabela
    }

    // Método responsável por buscar os feedbacks no banco de dados e preencher a tabela
    private void preencherTabela(DefaultTableModel model) {
        List<Feedback> feedbacks = feedbackRepository.findAll();

        for (Feedback f : feedbacks) {
            Integer clienteId = (f.getIdCliente() != null) ? f.getIdCliente().getId() : null;
            model.addRow(new Object[]{
                    f.getId(),
                    clienteId != null ? clienteId.toString() : "Desconhecido",
                    f.getReclamacao(),
                    f.getNotadeservico(),
                    f.getOpinioes(),
                    f.getQuestionario()
            });
        }
    }
}

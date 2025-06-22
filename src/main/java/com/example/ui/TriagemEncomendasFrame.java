package com.example.ui;

import com.example.proj2.models.Encomenda;
import com.example.proj2.repositories.EncomendaRepository;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// Classe da interface para triagem de encomendas (usada por operadores)
public class TriagemEncomendasFrame extends JanelaBase {

    private final EncomendaRepository encomendaRepository;
    private JTable tabela;
    private final Color PACKBEE_COLOR = new Color(230, 180, 60);
    private final Color BACKGROUND_COLOR = Color.WHITE;

    // Construtor da interface
    public TriagemEncomendasFrame() {
        this.encomendaRepository = com.example.SpringContext.getContext().getBean(EncomendaRepository.class);

        setTitle("Triagem de Encomendas");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());

        // Header (logo + título)
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 20, 0, 0));

        JLabel logo = new JLabel();
        try {
            var iconURL = getClass().getClassLoader().getResource("images/img.png");
            if (iconURL != null) {
                logo.setIcon(new ImageIcon(new ImageIcon(iconURL).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
            }
        } catch (Exception ignored) {}

        JLabel titulo = new JLabel("PACKBEE");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(70, 50, 20));
        titulo.setBorder(new EmptyBorder(0, 10, 0, 0));
        headerPanel.add(logo);
        headerPanel.add(titulo);
        add(headerPanel, BorderLayout.NORTH);

        // Centro com a tabela
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel subTitulo = new JLabel("Encomendas para Triagem");
        subTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        subTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));
        centerPanel.add(subTitulo, BorderLayout.NORTH);

        String[] colunas = {
                "ID", "Peso (kg)", "ID Cliente", "ID Rececionista",
                "ID Operador", "Estado Integridade", "Estado Entrega"
        };
        tabela = new JTable(new DefaultTableModel(colunas, 0));
        tabela.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(tabela);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Rodapé com botões
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(BACKGROUND_COLOR);
        footer.setBorder(new EmptyBorder(10, 20, 20, 20));

        JButton editarBtn = new JButton("Editar Integridade");
        JButton fecharBtn = new JButton("Fechar");

        estilizarBotao(editarBtn);
        estilizarBotao(fecharBtn);

        editarBtn.addActionListener(e -> editarIntegridadeSelecionada());
        fecharBtn.addActionListener(e -> dispose());

        footer.add(editarBtn);
        footer.add(fecharBtn);
        add(footer, BorderLayout.SOUTH);

        // Carrega os dados na tabela ao abrir
        carregarEncomendas();
        setVisible(true);
    }

    // Estiliza botões para manter o design consistente
    private void estilizarBotao(JButton botao) {
        botao.setFont(new Font("Arial", Font.PLAIN, 14));
        botao.setBackground(PACKBEE_COLOR);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setPreferredSize(new Dimension(180, 35));
    }

    // Carrega todas as encomendas na tabela
    private void carregarEncomendas() {
        List<Encomenda> encomendas = encomendaRepository.findAll();
        DefaultTableModel model = (DefaultTableModel) tabela.getModel();
        model.setRowCount(0); // limpa a tabela

        for (Encomenda e : encomendas) {
            model.addRow(new Object[]{
                    e.getId(),
                    e.getPeso(),
                    e.getIdCliente() != null ? e.getIdCliente().getId() : "N/A",
                    e.getIdRececionista() != null ? e.getIdRececionista().getId() : "N/A",
                    e.getIdOperador() != null ? e.getIdOperador().getId() : "N/A",
                    e.getEstadoIntegridade() != null ? e.getEstadoIntegridade() : "N/A",
                    e.getEstadoEntrega() != null ? e.getEstadoEntrega() : "N/A"
            });
        }
    }

    // Edita o estado de integridade da encomenda selecionada
    private void editarIntegridadeSelecionada() {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma encomenda para editar.");
            return;
        }

        Integer encomendaId = Integer.valueOf(tabela.getValueAt(selectedRow, 0).toString());
        Encomenda encomenda = encomendaRepository.findById(encomendaId).orElse(null);

        if (encomenda == null) {
            JOptionPane.showMessageDialog(this, "Encomenda não encontrada.");
            return;
        }

        String[] opcoes = {"Intacta", "Deteriorada", "Rasgada", "Molhada", "Outro"};
        String novoEstado = (String) JOptionPane.showInputDialog(
                this,
                "Selecione o novo estado de integridade:",
                "Editar Integridade",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcoes,
                encomenda.getEstadoIntegridade()
        );

        if (novoEstado != null && !novoEstado.isBlank()) {
            encomenda.setEstadoIntegridade(novoEstado);
            encomendaRepository.save(encomenda);
            carregarEncomendas();
            JOptionPane.showMessageDialog(this, "Estado atualizado com sucesso.");
        }
    }
}

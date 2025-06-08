package com.example.ui;

import com.example.proj2.models.Encomenda;
import com.example.proj2.repositories.EncomendaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EntregaEncomendasFrame extends JanelaBase {

    private final EncomendaRepository encomendaRepository;
    private JTable tabela;
    private final Color PACKBEE_COLOR = new Color(230, 180, 60);
    private final Color BACKGROUND_COLOR = Color.WHITE;

    public EntregaEncomendasFrame() {
        this.encomendaRepository = com.example.SpringContext.getContext().getBean(EncomendaRepository.class);

        setTitle("Entrega de Encomendas");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());

        add(criarHeader(), BorderLayout.NORTH);
        add(criarTabelaPanel(), BorderLayout.CENTER);
        add(criarFooter(), BorderLayout.SOUTH);

        carregarEncomendas();
        setVisible(true);
    }

    private JPanel criarHeader() {
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

        return headerPanel;
    }

    private JPanel criarTabelaPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel subTitulo = new JLabel("Encomendas para Entrega");
        subTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        subTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));
        panel.add(subTitulo, BorderLayout.NORTH);

        String[] colunas = {"ID", "Peso (kg)", "ID Cliente", "ID Rececionista", "Estado Integridade", "Estado Entrega"};
        tabela = new JTable(new DefaultTableModel(colunas, 0));
        tabela.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(tabela);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel criarFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(BACKGROUND_COLOR);
        footer.setBorder(new EmptyBorder(10, 20, 20, 20));

        JButton editarBtn = new JButton("Editar Estado de Entrega");
        JButton fecharBtn = new JButton("Fechar");

        estilizarBotao(editarBtn);
        estilizarBotao(fecharBtn);

        editarBtn.addActionListener(e -> editarEstadoEntregaSelecionado());
        fecharBtn.addActionListener(e -> dispose());

        footer.add(editarBtn);
        footer.add(fecharBtn);

        return footer;
    }

    private void estilizarBotao(JButton botao) {
        botao.setFont(new Font("Arial", Font.PLAIN, 14));
        botao.setBackground(PACKBEE_COLOR);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setPreferredSize(new Dimension(220, 35));
    }

    private void carregarEncomendas() {
        List<Encomenda> encomendas = encomendaRepository.findAll();
        DefaultTableModel model = (DefaultTableModel) tabela.getModel();
        model.setRowCount(0);

        for (Encomenda e : encomendas) {
            model.addRow(new Object[]{
                    e.getId(),
                    e.getPeso(),
                    e.getIdCliente() != null ? e.getIdCliente().getId() : "N/A",
                    e.getIdRececionista() != null ? e.getIdRececionista().getId() : "N/A",
                    e.getEstadoIntegridade() != null ? e.getEstadoIntegridade() : "N/A",
                    e.getEstadoEntrega() != null ? e.getEstadoEntrega() : "N/A"
            });
        }
    }

    private void editarEstadoEntregaSelecionado() {
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

        String[] opcoes = {"Entregue", "Em Trânsito", "Não Entregue", "Cancelada", "Outro"};
        String novoEstado = (String) JOptionPane.showInputDialog(
                this,
                "Selecione o novo estado de entrega:",
                "Editar Estado de Entrega",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcoes,
                encomenda.getEstadoEntrega()
        );

        if (novoEstado != null && !novoEstado.isBlank()) {
            encomenda.setEstadoEntrega(novoEstado);
            encomendaRepository.save(encomenda);
            carregarEncomendas();
            JOptionPane.showMessageDialog(this, "Estado de entrega atualizado com sucesso.");
        }
    }
}

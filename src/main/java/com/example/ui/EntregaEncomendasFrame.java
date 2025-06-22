// Define o pacote onde esta classe está localizada
package com.example.ui;

// Importa os modelos e repositórios usados na interface
import com.example.proj2.models.Encomenda;
import com.example.proj2.repositories.EncomendaRepository;

// Importa o Spring para obter os beans (injeção manual)
import org.springframework.beans.factory.annotation.Autowired;

// Importações para a interface Swing e manipulação visual
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// Janela que exibe todas as encomendas e permite editar o estado de entrega
public class EntregaEncomendasFrame extends JanelaBase {

    // Repositório para acessar e atualizar encomendas no banco de dados
    private final EncomendaRepository encomendaRepository;

    // Tabela para exibir os dados
    private JTable tabela;

    // Cores padrão da interface
    private final Color PACKBEE_COLOR = new Color(230, 180, 60);
    private final Color BACKGROUND_COLOR = Color.WHITE;

    // Construtor principal da janela
    public EntregaEncomendasFrame() {
        // Obtém o repositório diretamente do contexto Spring
        this.encomendaRepository = com.example.SpringContext.getContext().getBean(EncomendaRepository.class);

        // Configurações da janela
        setTitle("Entrega de Encomendas");
        setSize(1000, 600);
        setLocationRelativeTo(null); // Centraliza a janela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());

        // Adiciona os 3 principais painéis: cabeçalho, tabela, rodapé
        add(criarHeader(), BorderLayout.NORTH);
        add(criarTabelaPanel(), BorderLayout.CENTER);
        add(criarFooter(), BorderLayout.SOUTH);

        // Carrega os dados da base de dados na tabela
        carregarEncomendas();

        // Exibe a janela
        setVisible(true);
    }

    // Cria o painel superior com logo e título
    private JPanel criarHeader() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 20, 0, 0)); // Margens

        // Tenta carregar o ícone do logotipo
        JLabel logo = new JLabel();
        try {
            var iconURL = getClass().getClassLoader().getResource("images/img.png");
            if (iconURL != null) {
                ImageIcon icon = new ImageIcon(iconURL);
                Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                logo.setIcon(new ImageIcon(img));
            }
        } catch (Exception ignored) {}

        // Título "PACKBEE"
        JLabel titulo = new JLabel("PACKBEE");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(70, 50, 20));
        titulo.setBorder(new EmptyBorder(0, 10, 0, 0)); // Espaço entre ícone e texto

        // Adiciona ao painel
        headerPanel.add(logo);
        headerPanel.add(titulo);

        return headerPanel;
    }

    // Cria o painel central contendo a tabela com encomendas
    private JPanel criarTabelaPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Subtítulo acima da tabela
        JLabel subTitulo = new JLabel("Encomendas para Entrega");
        subTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        subTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));
        panel.add(subTitulo, BorderLayout.NORTH);

        // Define as colunas da tabela
        String[] colunas = {
                "ID", "Peso (kg)", "ID Cliente", "ID Rececionista",
                "Estado Integridade", "Estado Entrega"
        };

        // Cria a tabela com modelo vazio
        tabela = new JTable(new DefaultTableModel(colunas, 0));
        tabela.setRowHeight(28); // Altura das linhas

        // Envolve a tabela com scroll
        JScrollPane scrollPane = new JScrollPane(tabela);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Cria o rodapé com os botões de ação
    private JPanel criarFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(BACKGROUND_COLOR);
        footer.setBorder(new EmptyBorder(10, 20, 20, 20));

        // Botões
        JButton editarBtn = new JButton("Editar Estado de Entrega");
        JButton fecharBtn = new JButton("Fechar");

        // Aplica estilo padrão nos botões
        estilizarBotao(editarBtn);
        estilizarBotao(fecharBtn);

        // Define ações dos botões
        editarBtn.addActionListener(e -> editarEstadoEntregaSelecionado());
        fecharBtn.addActionListener(e -> dispose());

        footer.add(editarBtn);
        footer.add(fecharBtn);

        return footer;
    }

    // Aplica estilo visual aos botões
    private void estilizarBotao(JButton botao) {
        botao.setFont(new Font("Arial", Font.PLAIN, 14));
        botao.setBackground(PACKBEE_COLOR);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setPreferredSize(new Dimension(220, 35));
    }

    // Carrega todas as encomendas da base de dados e atualiza a tabela
    private void carregarEncomendas() {
        List<Encomenda> encomendas = encomendaRepository.findAll();
        DefaultTableModel model = (DefaultTableModel) tabela.getModel();
        model.setRowCount(0); // Limpa a tabela

        // Adiciona linha por linha
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

    // Edita o estado de entrega da encomenda selecionada na tabela
    private void editarEstadoEntregaSelecionado() {
        int selectedRow = tabela.getSelectedRow();

        // Se nenhuma linha estiver selecionada
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma encomenda para editar.");
            return;
        }

        // Obtém o ID da encomenda
        Integer encomendaId = Integer.valueOf(tabela.getValueAt(selectedRow, 0).toString());
        Encomenda encomenda = encomendaRepository.findById(encomendaId).orElse(null);

        if (encomenda == null) {
            JOptionPane.showMessageDialog(this, "Encomenda não encontrada.");
            return;
        }

        // Opções possíveis de estados de entrega
        String[] opcoes = {"Entregue", "Em Trânsito", "Não Entregue", "Cancelada", "Outro"};

        // Mostra diálogo para o utilizador escolher novo estado
        String novoEstado = (String) JOptionPane.showInputDialog(
                this,
                "Selecione o novo estado de entrega:",
                "Editar Estado de Entrega",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcoes,
                encomenda.getEstadoEntrega() // valor inicial
        );

        // Se foi selecionado algo válido
        if (novoEstado != null && !novoEstado.isBlank()) {
            encomenda.setEstadoEntrega(novoEstado);
            encomendaRepository.save(encomenda); // Salva no banco
            carregarEncomendas(); // Atualiza a tabela
            JOptionPane.showMessageDialog(this, "Estado de entrega atualizado com sucesso.");
        }
    }
}

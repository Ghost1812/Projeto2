// Define o pacote onde está localizada a classe
package com.example.ui;

// Importa os modelos e repositórios necessários
import com.example.proj2.models.Cliente;
import com.example.proj2.repositories.ClienteRepository;

// Importações para construção da interface gráfica (Swing)
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

// Classe responsável por exibir a lista de clientes em forma de tabela
public class ListarClientesFrame extends JanelaBase {

    private final ClienteRepository clienteRepository; // Repositório de clientes
    private JTable tabela; // Tabela Swing que exibirá os dados

    // Cores padrão usadas na interface
    private final Color PACKBEE_COLOR = new Color(230, 180, 60);
    private final Color BACKGROUND_COLOR = Color.WHITE;
    private final Color TEXT_COLOR = new Color(51, 51, 51);
    private final Color HEADER_BG_COLOR = new Color(240, 240, 240);

    // Construtor principal
    public ListarClientesFrame(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;

        // Configuração da janela
        setTitle("Lista de Clientes");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza na tela
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(10, 10)); // Layout principal

        // Painel superior com logotipo e título
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Painel principal de conteúdo (contém título, tabela e botões)
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(0, 20, 10, 20)); // Margens internas

        // Título acima da tabela
        JLabel titleLabel = new JLabel("Clientes Cadastrados");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(new EmptyBorder(5, 0, 15, 0));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        // ScrollPane que envolve a tabela
        JScrollPane scrollPane = new JScrollPane();
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Criação da tabela
        tabela = new JTable();
        scrollPane.setViewportView(tabela);

        // Painel de botões (parte inferior)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        // Botões de ação
        JButton adicionarButton = createStyledButton("Adicionar Cliente");
        JButton editarButton = createStyledButton("Editar Selecionado");
        JButton fecharButton = createStyledButton("Fechar");

        // Adiciona os botões ao painel
        buttonPanel.add(adicionarButton);
        buttonPanel.add(editarButton);
        buttonPanel.add(fecharButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Adiciona todo o painel de conteúdo ao centro da janela
        add(contentPanel, BorderLayout.CENTER);

        // Define ações dos botões
        adicionarButton.addActionListener(e ->
                new CriarClienteFrame(clienteRepository, null, this::carregarTabela));

        editarButton.addActionListener(e -> editarClienteSelecionado());

        fecharButton.addActionListener(e -> dispose());

        // Carrega os dados da tabela
        carregarTabela();
        setVisible(true);
    }

    // Cria o painel de cabeçalho com logotipo e nome da empresa
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(15, 20, 15, 0));

        ImageIcon logoIcon = null;
        try {
            var logoURL = getClass().getClassLoader().getResource("images/img.png");
            if (logoURL != null) {
                logoIcon = new ImageIcon(new ImageIcon(logoURL).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
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

    // Estiliza os botões da interface
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

    // Carrega os dados dos clientes na tabela
    public void carregarTabela() {
        try {
            List<Cliente> clientes = clienteRepository.findAll();
            String[] colunas = {
                    "ID", "Nome", "Email", "Contacto",
                    "Rua", "Número Porta", "Código Postal"
            };

            // Criação do modelo da tabela com células não editáveis
            DefaultTableModel model = new DefaultTableModel(colunas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            // Preenche o modelo com os dados dos clientes
            for (Cliente c : clientes) {
                model.addRow(new Object[]{
                        c.getId() != null ? c.getId().toString() : "N/A",
                        c.getNome() != null ? c.getNome() : "N/A",
                        c.getEmail() != null ? c.getEmail() : "N/A",
                        c.getContacto() != null ? c.getContacto().toString() : "N/A",
                        c.getRua() != null ? c.getRua() : "N/A",
                        c.getNumeroPorta() != null ? c.getNumeroPorta().toString() : "N/A",
                        c.getCodpostal() != null ? c.getCodpostal() : "N/A"
                });
            }

            // Define o modelo na tabela
            tabela.setModel(model);

            // Estilização do cabeçalho
            JTableHeader header = tabela.getTableHeader();
            header.setBackground(HEADER_BG_COLOR);
            header.setFont(new Font("Arial", Font.BOLD, 14));
            header.setForeground(TEXT_COLOR);
            header.setPreferredSize(new Dimension(header.getWidth(), 35));

            // Estilo geral da tabela
            tabela.setRowHeight(30);
            tabela.setFont(new Font("Arial", Font.PLAIN, 14));
            tabela.setShowGrid(true);
            tabela.setGridColor(new Color(230, 230, 230));
            tabela.setRowSelectionAllowed(true);
            tabela.setSelectionBackground(new Color(245, 245, 220)); // Cor da linha selecionada

            // Centraliza algumas colunas
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
            tabela.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // Número Porta

            // Define larguras mínimas para colunas
            tabela.getColumnModel().getColumn(0).setPreferredWidth(50);
            tabela.getColumnModel().getColumn(1).setPreferredWidth(150);
            tabela.getColumnModel().getColumn(2).setPreferredWidth(150);
            tabela.getColumnModel().getColumn(3).setPreferredWidth(100);
            tabela.getColumnModel().getColumn(4).setPreferredWidth(180);
            tabela.getColumnModel().getColumn(5).setPreferredWidth(100);
            tabela.getColumnModel().getColumn(6).setPreferredWidth(100);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar clientes: " + e.getMessage());
        }
    }

    // Ação para editar o cliente selecionado na tabela
    private void editarClienteSelecionado() {
        int selectedRow = tabela.getSelectedRow();

        // Se nenhuma linha estiver selecionada
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um cliente para editar",
                    "Nenhum cliente selecionado",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        // Pega o ID do cliente selecionado
        String clienteIdStr = tabela.getValueAt(selectedRow, 0).toString();
        try {
            Long clienteId = Long.parseLong(clienteIdStr);

            // Busca o cliente no repositório
            Cliente cliente = clienteRepository.findById(Math.toIntExact(clienteId)).orElse(null);

            if (cliente == null) {
                JOptionPane.showMessageDialog(this, "Cliente não encontrado.");
                return;
            }

            // Abre a tela de edição já preenchida com os dados
            new CriarClienteFrame(clienteRepository, cliente, this::carregarTabela);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID inválido: " + clienteIdStr);
        }
    }
}

// Define o pacote onde esta classe se encontra
package com.example.ui;

// Importações para construção da interface gráfica (Swing) e conexão com banco de dados (JDBC)
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

// Classe da janela responsável por criar um novo utilizador (funcionário)
public class CriarUtilizadorFrame extends JanelaBase {

    // Definição de cores padrão utilizadas na interface
    private final Color PACKBEE_COLOR = new Color(230, 180, 60);       // Cor do botão
    private final Color BACKGROUND_COLOR = Color.WHITE;                // Cor de fundo
    private final Color TEXT_COLOR = new Color(51, 51, 51);            // Cor do texto

    // Construtor principal da janela
    public CriarUtilizadorFrame() {
        // Configurações da janela
        setTitle("Criar Novo Utilizador");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha só esta janela
        setLocationRelativeTo(null); // Centraliza na tela
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());

        // Painel do formulário
        JPanel formPanel = new JPanel();
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS)); // Layout vertical
        formPanel.setBorder(new EmptyBorder(20, 30, 20, 30)); // Margem interna

        // Título da janela
        JLabel titleLabel = new JLabel("Novo Utilizador");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza
        formPanel.add(titleLabel);

        formPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Espaço

        // Campos do formulário
        JTextField nomeField = createStyledTextField();        // Nome
        JTextField emailField = createStyledTextField();       // Email

        // Campo de senha com estilo personalizado
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));

        JTextField cargoField = createStyledTextField();       // Cargo

        // Adiciona os campos com rótulo
        formPanel.add(createLabeledField("Nome completo:", nomeField));
        formPanel.add(createLabeledField("Email:", emailField));
        formPanel.add(createLabeledField("Palavra-passe:", passwordField));
        formPanel.add(createLabeledField("Cargo:", cargoField));

        formPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Espaço

        // Botão de criação de conta
        JButton criarContaButton = new JButton("Criar conta");
        criarContaButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        criarContaButton.setBackground(PACKBEE_COLOR);
        criarContaButton.setForeground(Color.WHITE);
        criarContaButton.setFont(new Font("Arial", Font.PLAIN, 16));
        criarContaButton.setFocusPainted(false);
        criarContaButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        criarContaButton.setPreferredSize(new Dimension(180, 40));

        // Ação ao clicar no botão
        criarContaButton.addActionListener(e -> {
            // Captura os dados dos campos
            String nome = nomeField.getText().trim();
            String email = emailField.getText().trim();
            String senha = new String(passwordField.getPassword()).trim();
            String cargo = cargoField.getText().trim();

            // Verifica se todos os campos estão preenchidos
            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || cargo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor preencha todos os campos!");
            } else {
                // Se estiver tudo preenchido, chama o método para gravar no banco
                criarContaNoBanco(nome, email, senha, cargo);
            }
        });

        // Adiciona o botão ao formulário
        formPanel.add(criarContaButton);

        // Adiciona o painel ao centro da janela
        add(formPanel, BorderLayout.CENTER);

        // Exibe a janela
        setVisible(true);
    }

    // Método para criar um campo rotulado (label + input)
    private JPanel createLabeledField(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Vertical
        panel.setBackground(BACKGROUND_COLOR);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));  // Espaço
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 15))); // Espaço entre campos

        return panel;
    }

    // Método utilitário para criar um campo de texto estilizado
    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30)); // Largura máxima
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        return field;
    }

    // Método responsável por gravar o utilizador no banco de dados PostgreSQL
    private void criarContaNoBanco(String nome, String email, String senha, String cargo) {
        // Dados de conexão
        String url = "jdbc:postgresql://localhost:5433/Projeto2";
        String usuario = "postgres";
        String senhaDb = "trepeteiro00"; // <-- confirma se está correto antes de apresentar

        // SQL para inserção de novo funcionário
        String sql = "INSERT INTO funcionario (nome, email, password, cargo) VALUES (?, ?, ?, ?)";

        try (
                Connection conn = DriverManager.getConnection(url, usuario, senhaDb);
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            // Preenche os parâmetros da query
            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setString(3, senha);
            stmt.setString(4, cargo);

            // Executa a query e verifica se teve sucesso
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(this, "Utilizador criado com sucesso!");
                dispose(); // Fecha a janela
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao criar utilizador.");
            }

        } catch (Exception ex) {
            // Em caso de erro, exibe mensagem e imprime no console
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

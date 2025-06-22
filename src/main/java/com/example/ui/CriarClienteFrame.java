// Define o pacote onde esta classe está localizada
package com.example.ui;

// Importa as classes necessárias: modelo Cliente e repositório ClienteRepository
import com.example.proj2.models.Cliente;
import com.example.proj2.repositories.ClienteRepository;

// Importações para construção da interface gráfica com Swing e AWT
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

// Classe responsável por criar a janela para cadastrar ou editar um cliente
public class CriarClienteFrame extends JanelaBase {

    // Referência à janela pai (opcional), usada para atualizar a lista de clientes
    private final CriarEncomendaFrame parentFrame;

    // Repositório para salvar ou atualizar clientes
    private final ClienteRepository clienteRepository;

    // Cliente existente (usado no modo de edição)
    private final Cliente clienteExistente;

    // Callback opcional para notificar outras partes do sistema após atualização
    private final Runnable callbackAtualizacao;

    // Cores padronizadas da interface
    private final Color PACKBEE_COLOR = new Color(230, 180, 60); // Cor principal do botão
    private final Color BACKGROUND_COLOR = Color.WHITE; // Cor de fundo da janela
    private final Color TEXT_COLOR = new Color(51, 51, 51); // Cor padrão do texto

    // Construtor para criar um cliente novo (sem cliente existente)
    public CriarClienteFrame(CriarEncomendaFrame parentFrame, ClienteRepository clienteRepository) {
        this.parentFrame = parentFrame;
        this.clienteRepository = clienteRepository;
        this.clienteExistente = null;
        this.callbackAtualizacao = null;

        // Inicializa os componentes da interface
        inicializarFormulario();
    }

    // Construtor para editar um cliente existente
    public CriarClienteFrame(ClienteRepository clienteRepository, Cliente clienteExistente, Runnable callbackAtualizacao) {
        this.parentFrame = null;
        this.clienteRepository = clienteRepository;
        this.clienteExistente = clienteExistente;
        this.callbackAtualizacao = callbackAtualizacao;

        // Inicializa os componentes da interface
        inicializarFormulario();
    }

    // Método responsável por montar toda a interface gráfica da janela
    private void inicializarFormulario() {
        // Define o título da janela com base no modo (criar ou editar)
        setTitle(clienteExistente != null ? "Editar Cliente" : "Criar Novo Cliente");

        // Define tamanho da janela
        setSize(450, 550);

        // Define que ao fechar a janela, ela apenas será descartada
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Centraliza a janela no centro da tela
        setLocationRelativeTo(null);

        // Define cor de fundo e layout principal
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());

        // Cria painel principal do formulário com layout vertical
        JPanel formPanel = new JPanel();
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(20, 30, 20, 30)); // Margens internas

        // Título da janela (dentro do formulário)
        JLabel titleLabel = new JLabel(clienteExistente != null ? "Editar Cliente" : "Novo Cliente");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza o texto
        formPanel.add(titleLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Espaço abaixo do título

        // Campos de entrada do formulário
        JTextField nomeField = createStyledTextField();
        JTextField codpostalField = createStyledTextField();
        JTextField numeroPortaField = createStyledTextField();
        JTextField ruaField = createStyledTextField();
        JTextField contactoField = createStyledTextField();
        JTextField emailField = createStyledTextField();

        // Se for modo edição, pré-preenche os campos com dados existentes
        if (clienteExistente != null) {
            nomeField.setText(clienteExistente.getNome());
            codpostalField.setText(clienteExistente.getCodpostal());
            numeroPortaField.setText(String.valueOf(clienteExistente.getNumeroPorta()));
            ruaField.setText(clienteExistente.getRua());
            contactoField.setText(clienteExistente.getContacto());
            emailField.setText(clienteExistente.getEmail());
        }

        // Adiciona campos rotulados ao painel
        formPanel.add(createLabeledField("Nome:", nomeField));
        formPanel.add(createLabeledField("Código Postal:", codpostalField));
        formPanel.add(createLabeledField("Número Porta:", numeroPortaField));
        formPanel.add(createLabeledField("Rua:", ruaField));
        formPanel.add(createLabeledField("Contacto:", contactoField));
        formPanel.add(createLabeledField("Email:", emailField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Espaço antes do botão

        // Criação e configuração do botão de salvar/criar cliente
        JButton salvarButton = new JButton(clienteExistente != null ? "Salvar Alterações" : "Criar Cliente");
        salvarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        salvarButton.setBackground(PACKBEE_COLOR);
        salvarButton.setForeground(Color.WHITE);
        salvarButton.setFont(new Font("Arial", Font.PLAIN, 16));
        salvarButton.setFocusPainted(false);
        salvarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        salvarButton.setPreferredSize(new Dimension(180, 40));

        // Ação executada ao clicar no botão
        salvarButton.addActionListener(e -> {
            // Captura os dados digitados pelo usuário
            String nome = nomeField.getText().trim();
            String codpostal = codpostalField.getText().trim();
            String numeroPorta = numeroPortaField.getText().trim();
            String rua = ruaField.getText().trim();
            String contacto = contactoField.getText().trim();
            String email = emailField.getText().trim();

            // Verifica se algum campo está vazio
            if (nome.isEmpty() || codpostal.isEmpty() || numeroPorta.isEmpty() || rua.isEmpty() || contacto.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
                return;
            }

            try {
                // Usa o cliente existente (modo edição) ou cria um novo
                Cliente cliente = clienteExistente != null ? clienteExistente : new Cliente();

                // Preenche o objeto Cliente com os dados do formulário
                cliente.setNome(nome);
                cliente.setCodpostal(codpostal);
                cliente.setNumeroPorta(Integer.parseInt(numeroPorta)); // Converte de texto para int
                cliente.setRua(rua);
                cliente.setContacto(contacto);
                cliente.setEmail(email);

                // Salva no repositório (pode ser banco de dados, arquivo, etc.)
                clienteRepository.save(cliente);

                // Mensagem de sucesso
                JOptionPane.showMessageDialog(this, clienteExistente != null ?
                        "Cliente atualizado com sucesso!" : "Cliente criado com sucesso!");

                // Atualiza a janela pai, se existir
                if (parentFrame != null) parentFrame.atualizarClientesDepoisDeCriar();

                // Executa o callback, se definido
                if (callbackAtualizacao != null) callbackAtualizacao.run();

                // Fecha a janela
                dispose();

            } catch (Exception ex) {
                // Em caso de erro, exibe mensagem e mostra no console
                JOptionPane.showMessageDialog(this, "Erro ao salvar cliente: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        // Adiciona o botão ao formulário
        formPanel.add(salvarButton);

        // Adiciona o painel principal ao centro da janela
        add(formPanel, BorderLayout.CENTER);

        // Exibe a janela
        setVisible(true);
    }

    // Método auxiliar para criar um campo com rótulo acima
    private JPanel createLabeledField(String labelText, JTextField field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5))); // Espaço entre rótulo e campo
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 15))); // Espaço após o campo

        return panel;
    }

    // Método auxiliar para criar campos de texto com estilo padrão
    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30)); // Altura padrão, largura flexível
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        return field;
    }
}

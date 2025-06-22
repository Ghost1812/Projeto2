// Define o pacote onde essa classe está localizada
package com.example.ui;

// Importa o serviço responsável pelas regras de negócio relacionadas a funcionários
import com.example.proj2.services.FuncionarioService;

// Importações necessárias para a construção da interface gráfica com Swing e AWT
import javax.swing.*;
import java.awt.*;

// Classe que representa a janela para cadastrar um novo funcionário
// Estende uma classe base chamada JanelaBase (provavelmente define padrão visual comum)
public class CadastrarFuncionarioFrame extends JanelaBase {

    // Construtor da janela, recebe como parâmetro o serviço que vai salvar o funcionário
    public CadastrarFuncionarioFrame(FuncionarioService funcionarioService) {

        // Define o título da janela
        setTitle("Cadastrar Novo Funcionário");

        // Define a janela para abrir em tela cheia
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Define que ao fechar esta janela, ela apenas será descartada (sem encerrar o programa)
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Define cor de fundo da janela
        getContentPane().setBackground(Color.WHITE);

        // Define layout principal como GridBagLayout (permite centralizar o painel)
        setLayout(new GridBagLayout());

        // Criação do painel principal onde os componentes do formulário serão colocados
        JPanel panel = new JPanel();

        // Define o layout do painel como vertical (um componente abaixo do outro)
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Define cor de fundo do painel como branca
        panel.setBackground(Color.WHITE);

        // Define tamanho máximo do painel
        panel.setMaximumSize(new Dimension(400, 500));

        // Cria um rótulo (texto fixo) para o título da janela
        JLabel titulo = new JLabel("Novo Funcionário");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26)); // Define fonte grande e em negrito
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza horizontalmente
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0)); // Espaçamento externo

        // Criação dos campos de entrada do formulário
        JTextField nomeField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField cargoField = new JTextField();

        // Aplica estilo visual a cada campo
        stylizeField(nomeField, "Nome");
        stylizeField(emailField, "Email");
        stylizeField(passwordField, "Palavra-passe");
        stylizeField(cargoField, "Cargo");

        // Criação do botão de cadastro
        JButton cadastrar = new JButton("Cadastrar");
        cadastrar.setFont(new Font("SansSerif", Font.BOLD, 16)); // Fonte em negrito
        cadastrar.setBackground(new Color(60, 180, 90)); // Cor verde escuro
        cadastrar.setForeground(Color.WHITE); // Texto branco
        cadastrar.setFocusPainted(false); // Remove o contorno de foco
        cadastrar.setMaximumSize(new Dimension(200, 40)); // Define tamanho máximo
        cadastrar.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza
        cadastrar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        // Adiciona uma ação ao clicar no botão "Cadastrar"
        cadastrar.addActionListener(e -> {
            // Captura os dados dos campos preenchidos pelo usuário
            String nome = nomeField.getText().trim();
            String email = emailField.getText().trim();
            String senha = new String(passwordField.getPassword()).trim(); // Conversão da senha
            String cargo = cargoField.getText().trim();

            // Chama o método do serviço para criar o funcionário com os dados informados
            funcionarioService.criarFuncionario(email, senha, cargo, nome);

            // Exibe uma mensagem de sucesso para o usuário
            JOptionPane.showMessageDialog(this, "Funcionário cadastrado com sucesso!");

            // Fecha a janela após o cadastro
            dispose();
        });

        // Adiciona os componentes ao painel principal
        panel.add(titulo);
        panel.add(nomeField);
        panel.add(emailField);
        panel.add(passwordField);
        panel.add(cargoField);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // Espaço entre os campos e o botão
        panel.add(cadastrar);

        // Adiciona o painel à janela
        add(panel);

        // Torna a janela visível
        setVisible(true);
    }

    // Método auxiliar para aplicar estilo padrão aos campos de entrada
    private void stylizeField(JTextField field, String placeholder) {
        field.setMaximumSize(new Dimension(400, 40)); // Tamanho máximo
        field.setFont(new Font("SansSerif", Font.PLAIN, 14)); // Fonte padrão
        field.setBorder(BorderFactory.createTitledBorder(placeholder)); // "Placeholder" simulado como borda com título
        field.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza o campo
    }
}

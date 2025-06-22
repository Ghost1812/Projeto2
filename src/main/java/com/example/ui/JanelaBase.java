// Define o pacote da classe
package com.example.ui;

// Importa as bibliotecas Swing para criação da interface e AWT para manipulação de imagens
import javax.swing.*;
import java.awt.*;

// Classe abstrata que serve como base para todas as janelas do projeto
// Ao estender JFrame, ela já é uma janela Swing
public abstract class JanelaBase extends JFrame {

    // Construtor da classe base
    public JanelaBase() {
        try {
            // Tenta carregar o ícone padrão da aplicação a partir da pasta "resources/images"
            var iconURL = getClass().getClassLoader().getResource("images/img.png");

            // Se encontrar o ícone, define como ícone da janela (canto superior esquerdo)
            if (iconURL != null) {
                ImageIcon icon = new ImageIcon(iconURL);
                setIconImage(icon.getImage());
            }

        } catch (Exception e) {
            // Se ocorrer algum erro ao carregar o ícone, exibe no terminal
            System.err.println("Erro ao carregar ícone da janela base.");
        }
    }
}

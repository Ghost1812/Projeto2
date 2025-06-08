package com.example.ui;

import javax.swing.*;
import java.awt.*;

public abstract class JanelaBase extends JFrame {
    public JanelaBase() {
        try {
            var iconURL = getClass().getClassLoader().getResource("images/img.png");
            if (iconURL != null) {
                ImageIcon icon = new ImageIcon(iconURL);
                setIconImage(icon.getImage());
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar ícone da janela base.");
        }
    }
}

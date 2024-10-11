package org.example.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class BatchProcessingUI {

    // TODO: Make this a pop-up panel (JPanel) instead, so that it displays all of the different results and can be clicked through.
        /** This class should appear when a file is uploaded by the user.
        *
        */

    public BatchProcessingUI() {
        FlatLightLaf.setup();
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        JFrame frame = new JFrame();
        frame.getContentPane().setBackground(new Color(195, 224, 229));
        frame.setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 400,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public JPanel createLipidInfoPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(195, 224, 229));
        return panel;
    }

    public JPanel createLipidTablePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(195, 224, 229));
        return panel;
    }}
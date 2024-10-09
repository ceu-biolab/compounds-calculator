package org.example.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;
import org.example.exceptions.FattyAcidCreation_Exception;
import org.example.exceptions.InvalidFormula_Exception;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;

public class SidePanelUI {
    private static JFrame frame;
    private static JPanel sidePanel = null;
    private static MainPageUI interfaceUI;
    private static AdductTransformerUI adductTransformerUI;

    public SidePanelUI() {
        frame = new JFrame();
        frame.setLayout(new MigLayout("", "[grow,fill]", "[grow, fill][grow, fill]"));
        try {
            interfaceUI = new MainPageUI();
        } catch (SQLException | InvalidFormula_Exception | FattyAcidCreation_Exception e) {
            throw new RuntimeException(e);
        }
        adductTransformerUI = new AdductTransformerUI();
        homeFrame();
        frame.add(sidePanel, "wrap, center, grow");
        frame.add(interfaceUI, "center, grow");
        frame.getContentPane().setBackground(new Color(195, 224, 229));
        frame.setLocationRelativeTo(null);
        frame.setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        frame.setVisible(true);
    }

    public void homeFrame() {
        FlatLightLaf.setup();
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        sidePanel = new JPanel();
        sidePanel.setLayout(new MigLayout("", "[fill]", "[fill]"));
        sidePanel.setBackground(Color.WHITE);
        sidePanel.setMaximumSize(new Dimension(1615, 65));
        sidePanel.setPreferredSize(new Dimension(1615, 65));
        sidePanel.setMinimumSize(new Dimension(1615, 65));
        sidePanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");

        JButton homeButton = new JButton("  Home");
        configureComponents(homeButton, "src/main/resources/Home_Icon.png");
        homeButton.addActionListener(e -> {
            frame.remove(adductTransformerUI);
            frame.add(interfaceUI);
            frame.setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
                    (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
            frame.revalidate();
            frame.repaint();
        });

        JButton adductTransformerButton = new JButton("  Adduct Transformer");
        configureComponents(adductTransformerButton, "src/main/resources/Transformer_icon.png");
        adductTransformerButton.addActionListener(e -> {
            frame.remove(interfaceUI);
            frame.add(adductTransformerUI);
            frame.setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
                    (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
            frame.revalidate();
            frame.repaint();
        });

        JButton batchProcessingButton = new JButton("  Batch Processing");
        configureComponents(batchProcessingButton, "src/main/resources/Transformer_icon.png");
        batchProcessingButton.addActionListener(e -> {
            frame.remove(interfaceUI);
            frame.add(adductTransformerUI);
            frame.setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
                    (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
            frame.revalidate();
            frame.repaint();
        });

        JButton helpButton = new JButton("  Help");
        configureComponents(helpButton, "src/main/resources/Help_Icon.png");
        helpButton.setBackground(Color.WHITE);
        helpButton.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        helpButton.setIcon(new ImageIcon("src/main/resources/Help_Icon.png"));
        helpButton.setBorder(new LineBorder(Color.white));
        helpButton.setHorizontalAlignment(SwingConstants.LEFT);
        helpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    URL url = new URL("https://github.com/pilarbourg/compounds-calculator");
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        try {
                            desktop.browse(url.toURI());
                        } catch (IOException | URISyntaxException exception) {
                            JOptionPane.showMessageDialog(null, "An error occurred while trying to direct you to" +
                                    " the webpage. Please try again.");
                        }
                    }
                } catch (MalformedURLException exception) {
                    throw new RuntimeException(exception);
                }

            }
        });

        JButton exitButton = new JButton("  Exit");
        configureComponents(exitButton, "src/main/resources/Exit_Icon.png");
        exitButton.addActionListener(e -> {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        });

        sidePanel.add(homeButton, "gapright 50");
        sidePanel.add(adductTransformerButton, "gapright 50");
        sidePanel.add(batchProcessingButton, "gapright 650");
        sidePanel.add(helpButton, "gapleft 50");
        sidePanel.add(exitButton, "gapleft 50");
    }

    public static void configureComponents(JButton component, String fileName) {
        component.setFont(new Font("Arial", Font.BOLD, 18));
        component.setBackground(Color.WHITE);
        component.setForeground(new Color(65, 114, 159));
        component.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        component.setIcon(new ImageIcon(fileName));
        component.setBorder(new LineBorder(Color.white));
        component.setHorizontalAlignment(SwingConstants.LEFT);
    }
}
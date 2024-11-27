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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;

public class SidePanelUI {
    public static JFrame frame;
    public static JPanel sidePanel = null;
    public static LipidCalculatorUI lipidCalculatorUI;
    public static AdductTransformerUI adductTransformerUI;
    public static PatternRecognitionUI patternRecognitionUI;
    public JButton lipidCalculatorButton = new JButton("  Lipid Calculator");
    public JButton adductTransformerButton = new JButton("  Adduct Transformer");
    public JButton patternRecognitionButton = new JButton("  Pattern Recognition Finder");

    public SidePanelUI() {
        FlatLightLaf.setup();
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        frame = new JFrame();
        frame.setLayout(new MigLayout("", "[grow,fill]", "[grow, fill][grow, fill]"));
        try {
            lipidCalculatorUI = new LipidCalculatorUI();
            patternRecognitionUI = new PatternRecognitionUI();
        } catch (SQLException | InvalidFormula_Exception | FattyAcidCreation_Exception e) {
            throw new RuntimeException(e);
        }
        adductTransformerUI = new AdductTransformerUI();
        homePage();
        frame.add(sidePanel, "wrap, align center, grow");
        frame.add(lipidCalculatorUI, "align center, grow");
        frame.getContentPane().setBackground(new Color(195, 224, 229));
        frame.setLocationRelativeTo(null);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
        frame.setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    public static void updateUI(Component component) {
        frame.remove(lipidCalculatorUI);
        frame.remove(adductTransformerUI);
        frame.remove(patternRecognitionUI);
        frame.add(component, "align center, grow");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.revalidate();
        frame.repaint();
    }

    public void homePage() {
        FlatLightLaf.setup();
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();

        sidePanel = new JPanel();
        sidePanel.setLayout(new MigLayout("", "", "[fill]"));
        sidePanel.setBackground(Color.WHITE);
        sidePanel.setPreferredSize(new Dimension((int) width - 70, 65));
        sidePanel.setMinimumSize(new Dimension((int) width - 70, 65));
        sidePanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");

        configureComponents(lipidCalculatorButton, "src/main/resources/LipidCalculator_Icon.png");
        lipidCalculatorButton.setBackground(new Color(231, 242, 245));
        lipidCalculatorButton.addActionListener(_ -> {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            updateUI(lipidCalculatorUI);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        });

        configureComponents(adductTransformerButton, "src/main/resources/Transformer_icon.png");
        adductTransformerButton.addActionListener(_ -> {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            updateUI(adductTransformerUI);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        });

        configureComponents(patternRecognitionButton, "src/main/resources/PatternRecognition_Icon.png");
        patternRecognitionButton.addActionListener(_ -> {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            updateUI(patternRecognitionUI);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        });

        JButton helpButton = new JButton("  Github");
        configureComponents(helpButton, "src/main/resources/Help_Icon.png");
        helpButton.setBorder(new LineBorder(Color.WHITE, 0));
        helpButton.setIcon(new ImageIcon("src/main/resources/Help_Icon.png"));
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

        sidePanel.add(lipidCalculatorButton, "gapright 50");
        sidePanel.add(adductTransformerButton, "gapright 50");
        sidePanel.add(patternRecognitionButton, "gapright 450");
        sidePanel.add(helpButton, "gapleft 50");
        sidePanel.add(exitButton, "gapleft 50");
    }

    public static void configureComponents(JButton component, String fileName) {
        component.setFont(new Font("Arial", Font.BOLD, 18));
        component.setBackground(Color.WHITE);
        component.setForeground(new Color(65, 114, 159));
        component.setIcon(new ImageIcon(fileName));
        component.setBorder(new LineBorder(Color.WHITE, 0));
        component.setHorizontalAlignment(SwingConstants.LEFT);
    }
}
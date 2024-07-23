package org.example.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class SidePanelUI {
    private static JFrame frame;
    private static JPanel sideBar_Panel = null;
    private JButton home_Button = null;
    private JButton upload_Button = null;
    private JButton exit_Button = null;
    private JButton help_Button = null;
    private JButton adductTransformerButton = null;
    private static MainPageUI interfaceUI;
    private static AdductTransformerUI adductTransformerUI;

    public SidePanelUI() {
        frame = new JFrame();
        frame.setLayout(new MigLayout("", "[grow,fill]", "[][grow, fill]"));
        interfaceUI = new MainPageUI();
        adductTransformerUI = new AdductTransformerUI();
        homeFrame();
        frame.add(sideBar_Panel);
        frame.add(interfaceUI);
        frame.getContentPane().setBackground(new Color(227, 235, 242));
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

        sideBar_Panel = new JPanel();
        sideBar_Panel.setLayout(new MigLayout("", "[fill]", "[fill]"));
        sideBar_Panel.setBackground(Color.WHITE);
        sideBar_Panel.setMaximumSize(new Dimension(230, 865));
        sideBar_Panel.setPreferredSize(new Dimension(230, 865));
        sideBar_Panel.setMinimumSize(new Dimension(230, 865));
        sideBar_Panel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");

        home_Button = new JButton("Home");
        home_Button.setFont(new Font("Arial", Font.BOLD, 18));
        home_Button.setForeground(new Color(52, 94, 125));
        home_Button.setBackground(new Color(227, 235, 242));
        home_Button.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        home_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(adductTransformerUI);
                frame.add(interfaceUI);
                frame.pack();
            }
        });

        upload_Button = new JButton("Upload File");
        upload_Button.setFont(new Font("Arial", Font.BOLD, 18));
        upload_Button.setForeground(new Color(52, 94, 125));
        upload_Button.setBackground(new Color(227, 235, 242));
        upload_Button.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        upload_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser j = new JFileChooser();
                j.showSaveDialog(null);
            }
        });

        help_Button = new JButton("Help");
        help_Button.setFont(new Font("Arial", Font.BOLD, 18));
        help_Button.setForeground(new Color(52, 94, 125));
        help_Button.setBackground(new Color(227, 235, 242));
        help_Button.putClientProperty(FlatClientProperties.STYLE, "arc: 40");

        adductTransformerButton = new JButton("Adduct Transformer");
        adductTransformerButton.setFont(new Font("Arial", Font.BOLD, 18));
        adductTransformerButton.setForeground(new Color(52, 94, 125));
        adductTransformerButton.setBackground(new Color(227, 235, 242));
        adductTransformerButton.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        adductTransformerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(interfaceUI);
                frame.add(adductTransformerUI);
                frame.pack();
            }
        });

        exit_Button = new JButton("Exit");
        exit_Button.setFont(new Font("Arial", Font.BOLD, 18));
        exit_Button.setForeground(new Color(52, 94, 125));
        exit_Button.setBackground(new Color(227, 235, 242));
        exit_Button.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        exit_Button.addActionListener(e -> {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        });

        sideBar_Panel.add(home_Button, "gaptop 5, wrap, center");
        sideBar_Panel.add(upload_Button, "gaptop 25, wrap, center");
        sideBar_Panel.add(adductTransformerButton, "gaptop 25, wrap, center");
        sideBar_Panel.add(help_Button, "gaptop 550, wrap, center");
        sideBar_Panel.add(exit_Button, "gaptop 25, center");

    }
}

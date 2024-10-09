package org.example.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class BatchProcessingUI extends JPanel {
    public BatchProcessingUI() {
        FlatLightLaf.setup();
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        setLayout(new MigLayout("", "[grow, fill]25[grow, fill]",
                "[grow, fill]25[grow, fill]"));
        setBackground(new Color(195, 224, 229));
        JPanel panel1 = new JPanel();
        panel1.setMinimumSize(new Dimension(1450, 800));
        panel1.setMaximumSize(new Dimension(1450, 800));
        panel1.setPreferredSize(new Dimension(1450, 800));
        panel1.setBackground(Color.WHITE);
        panel1.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        add(panel1);
        setVisible(true);
    }
}

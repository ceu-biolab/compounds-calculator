package org.example.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class AdductTransformerUI extends JPanel {
    public AdductTransformerUI() {
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
        //** Dada una m/z cual sería la masa de todos sus aductos
        //** Select all, etc.
        //** Create own adducts by inserting a formula (should have regex of [M+X]Q+/-)
            //** 1. Find M from m/z // same with 2
            //** 2. Find all m/z from M
            //** 3. Add-ons (from formulas)  --formulas de calculo de M de m/z
        //** Separate pos/neg
    /** Básicamente, hay que refactorizar e incluir en FormulaValidation los casos de uso para obtener el M desde el MZ  y los MZs
    * desde el M teniendo en cuenta que M puede tomar cualquier número y no exclusivamente una fórmula! Si es muy largo o al inicio
    * te lía mañana lo vemos, pero parece una buena idea comenzar con los tests para saber los casos y los resultados y luego pasar
    * a su implementación que será refactorizar este código.
    * */
}

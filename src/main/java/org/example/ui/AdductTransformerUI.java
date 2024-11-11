package org.example.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;

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
        setLayout(new MigLayout("", "[grow, fill]25[grow, fill]25[grow, fill]",
                "[grow, fill]25[grow, fill]"));
        setBackground(new Color(195, 224, 229));
        JPanel graphPanel = new JPanel(new MigLayout());
        graphPanel.setMinimumSize(new Dimension(1000, 420));
        graphPanel.setBackground(Color.WHITE);
        graphPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        JPanel chartPanel = new XChartPanel<XYChart>(configureChart());
        graphPanel.add(chartPanel, "gaptop 15");

        JPanel mToMZPanel = new JPanel();
        mToMZPanel.setMinimumSize(new Dimension(500, 320));
        mToMZPanel.setBackground(Color.WHITE);
        mToMZPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");

        JPanel mzToMPanel = new JPanel();
        mzToMPanel.setMinimumSize(new Dimension(500, 320));
        mzToMPanel.setBackground(Color.WHITE);
        mzToMPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");

        JPanel createAdductsPanel = new JPanel();
        createAdductsPanel.setMinimumSize(new Dimension(500, 680));
        createAdductsPanel.setBackground(Color.WHITE);
        createAdductsPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");

        add(graphPanel, "span 2 1");
        add(createAdductsPanel, "span 1 2, wrap");
        add(mToMZPanel);
        add(mzToMPanel);
        setVisible(true);
    }

    public static XYChart configureChart(){
        XYChart chart = new XYChartBuilder().width(1000).height(380).title("Likely Adduct Peaks").xAxisTitle("X").yAxisTitle("Y").build();
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Area);
        chart.getStyler().setChartBackgroundColor(Color.WHITE);
        Color[] colors = {new Color(195, 224, 229), new Color(123, 202, 215), new Color(60, 164, 182)};
        chart.getStyler().setSeriesColors(colors);
        chart.getStyler().setChartFontColor(new Color(65, 114, 159));
        chart.getStyler().setPlotBorderColor(Color.WHITE);
        chart.getStyler().setAxisTickMarksColor(new Color(65, 114, 159));
        chart.getStyler().setAxisTickLabelsColor(new Color(65, 114, 159));
        chart.getStyler().setChartTitleFont(new Font("Arial", Font.BOLD, 16));
        chart.addSeries("[M+H]+", new double[]{0, 3, 5, 7, 9}, new double[]{-3, 5, 9, 6, 5});
        chart.addSeries("[M+Na]+", new double[]{0, 2, 4, 6, 9}, new double[]{-1, 6, 4, 0, 4});
        chart.addSeries("[M+H-H2O]+", new double[]{0, 1, 3, 8, 9}, new double[]{-2, -1, 1, 0, 1});
        return chart;
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
     * DIMEROS (2M+H), CARGAS [M+2H]2+ CUANDO TENEMOS UNA DOBLE CARGA PRIMERO SE SUMAN LOS DOS HIDRÓGENOS Y LUEGO SE DIVIDE ENTRE 2
     * */

}

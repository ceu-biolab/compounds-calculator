package org.example.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;
import org.example.adduct.Transformer;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static org.example.ui.MainPageUI.configureComponents;
import static org.example.ui.MainPageUI.configureTextComponents;

public class AdductTransformerUI extends JPanel {
    public JPanel massToMZPanel = new JPanel(new MigLayout("", "20[grow, fill]20[]20[grow, fill]20", "20[]20[grow, fill]20"));
    public JPanel mzToMassPanel = new JPanel(new MigLayout("", "20[grow, fill]20[]20[grow, fill]20", "20[]20[grow, fill]20"));
    JPanel createAdductsPanel = new JPanel(new MigLayout("", "15[grow]15", ""));

    public JTextArea monoMassTextPane1A = new JTextArea();
    public JTextArea mzTextPane1B = new JTextArea();
    public JButton massToMzButton = new JButton();

    public JTextArea monoMassTextPane2B = new JTextArea();
    public JTextArea mzTextPane2A = new JTextArea();
    public JButton mzToMassButton = new JButton();

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

        DecimalFormat numberFormat = new DecimalFormat("#.0000");

        JPanel graphPanel = new JPanel(new MigLayout("", "[grow, fill]", "[grow, fill]"));
        graphPanel.setMinimumSize(new Dimension(1000, 420));
        graphPanel.setBackground(Color.WHITE);
        graphPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        JPanel chartPanel = new XChartPanel<XYChart>(configureChart());
        JLabel adductPeaksLabel = new JLabel("Likely Adduct Peaks According to Input");
        configureTextComponents(adductPeaksLabel);

        graphPanel.add(adductPeaksLabel, "gapleft 15, gaptop 15, wrap");
        graphPanel.add(chartPanel, "gaptop 15, gapbottom 300, grow");

        JLabel massToMzLabel = new JLabel("   Find mz from M values");
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"   [M+H]+  ", "   [M+NH4]+  ", "   [M+C2H6N2+H]+  "});
        massToMzLabel.setIcon(new ImageIcon("src/main/resources/FindValues_Icon.png"));
        massToMzLabel.setHorizontalAlignment(SwingConstants.CENTER);
        configureTextComponents(massToMzLabel);
        massToMZPanel.setMinimumSize(new Dimension(500, 320));
        massToMZPanel.setBackground(Color.WHITE);
        massToMZPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        massToMzButton.addActionListener(e -> {
            List<Double> mzValues = new ArrayList<>();
            if (monoMassTextPane1A != null) {
                String[] massValues = monoMassTextPane1A.getText().split("[\\s,;\\t]");
                String adduct = comboBox.getSelectedItem().toString().replaceAll(" ", "");
                for (String mass : massValues) {
                    mzValues.add(Double.valueOf(numberFormat.format(Transformer.getMassOfAdductFromMonoMass(Double.parseDouble(mass), adduct))));
                }
            }
            mzTextPane1B.setText(mzValues.toString());
        });

        JLabel mzToMassLabel = new JLabel("   Find M from mz values");
        JComboBox<String> comboBox2 = new JComboBox<>(new String[]{"   [M+H]+  ", "   [M+NH4]+  ", "   [M+C2H6N2+H]+  "});
        mzToMassLabel.setIcon(new ImageIcon("src/main/resources/FindValues_Icon copy.png"));
        mzToMassLabel.setHorizontalAlignment(SwingConstants.CENTER);
        configureTextComponents(mzToMassLabel);
        mzToMassPanel.setMinimumSize(new Dimension(500, 320));
        mzToMassPanel.setBackground(Color.WHITE);
        mzToMassPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        mzToMassButton.addActionListener(e -> {
            List<Double> massValues = new ArrayList<>();
            if (mzTextPane2A != null) {
                String[] mzValues = mzTextPane2A.getText().split("[\\s,;\\t]");
                String adduct = comboBox2.getSelectedItem().toString().replaceAll(" ", "");
                for (String mz : mzValues) {
                    massValues.add(Double.valueOf(numberFormat.format(Transformer.getMonoisotopicMassFromMZ(Double.parseDouble(mz), adduct))));
                }
            }
            monoMassTextPane2B.setText(massValues.toString());
        });

        createAdductsPanel.setMinimumSize(new Dimension(500, 680));
        createAdductsPanel.setBackground(Color.WHITE);
        createAdductsPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        JLabel createAdductsLabel = new JLabel("   Create Adduct");
        createAdductsLabel.setIcon(new ImageIcon("src/main/resources/CreateAdduct_Icon.png"));
        configureTextComponents(createAdductsLabel);
        JLabel createAdductsSubLabel = new JLabel("Define an adduct for use in the application.");
        JLabel createAdductsSubLabel2 = new JLabel("Valid adducts are automatically added for use in the list of adducts");
        configureTextComponents(createAdductsSubLabel);
        configureTextComponents(createAdductsSubLabel2);
        createAdductsSubLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        createAdductsSubLabel2.setFont(new Font("Arial", Font.ITALIC, 12));

        JLabel adductFormulaLabel = new JLabel("Introduce in [M+X]q+/- Format: ");
        configureTextComponents(adductFormulaLabel);
        JPanel adductFormulaPanel = new JPanel(new MigLayout("", "[grow, fill]", "[grow, fill]"));
        JTextField adductFormulaTextField = new JTextField();
        configureComponents(adductFormulaTextField);
        configureComponents(adductFormulaPanel);
        adductFormulaPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        adductFormulaTextField.putClientProperty(FlatClientProperties.STYLE, "arc: 20");
        adductFormulaTextField.setBorder(BorderFactory.createLineBorder(new Color(231, 242, 245)));
        adductFormulaPanel.add(adductFormulaTextField, "grow");

        JButton confirmAdduct = new JButton(" Done ");
        configureTextComponents(confirmAdduct);
        confirmAdduct.setBorder(new LineBorder(Color.WHITE, 1));

        //** ---------------------------------
        JPanel testPanel = new JPanel(new MigLayout());
        testPanel.setBackground(new Color(231, 242, 245));
        testPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        configureComponents(comboBox);
        comboBox.setBackground(Color.WHITE);
        comboBox.putClientProperty(FlatClientProperties.STYLE, "arc: 40");

        testPanel.add(comboBox, "gapbottom 10, wrap");
        testPanel.add(configurePanel(monoMassTextPane1A), "align center");
        testPanel.add(configureButton(massToMzButton), "align center");
        testPanel.add(configurePanel(mzTextPane1B), "align center");

        JPanel testPanel2 = new JPanel(new MigLayout());
        testPanel2.setBackground(new Color(231, 242, 245));
        testPanel2.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        configureComponents(comboBox2);
        comboBox2.setBackground(Color.WHITE);
        comboBox2.putClientProperty(FlatClientProperties.STYLE, "arc: 40");

        testPanel2.add(comboBox2, "gapbottom 10, wrap");
        testPanel2.add(configurePanel(mzTextPane2A), "align center");
        testPanel2.add(configureButton(mzToMassButton), "align center");
        testPanel2.add(configurePanel(monoMassTextPane2B), "align center");
        //** ---------------------------------

        createAdductsPanel.add(createAdductsLabel, "wrap");
        createAdductsPanel.add(createAdductsSubLabel, "gaptop 5, align center, wrap");
        createAdductsPanel.add(createAdductsSubLabel2, "align center, wrap");
        createAdductsPanel.add(adductFormulaLabel, "gaptop 10, wrap");
        createAdductsPanel.add(adductFormulaPanel, "grow, gaptop 10, wrap");
        createAdductsPanel.add(confirmAdduct, "align center, gaptop 10, gapbottom 25, wrap");

        createAdductsPanel.add(massToMzLabel, "gapbottom 5, wrap");
        createAdductsPanel.add(testPanel, "grow, gapbottom 25, wrap");

        createAdductsPanel.add(mzToMassLabel, "gapbottom 5, wrap");
        createAdductsPanel.add(testPanel2, "grow, wrap");

        add(createAdductsPanel, "span 1 2");
        add(graphPanel, "span 2 2");
        setVisible(true);
    }

    public JPanel configurePanel(JTextArea textArea) {
        JPanel basePanel = new JPanel(new MigLayout("", "[grow, fill]", "[grow, fill]"));
        basePanel.setMinimumSize(new Dimension(createAdductsPanel.getMinimumSize().width / 3, createAdductsPanel.getMinimumSize().height / 6));
        textArea.setMinimumSize(new Dimension(createAdductsPanel.getMinimumSize().width / 3, createAdductsPanel.getMinimumSize().height / 6));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        configureComponents(textArea);
        configureComponents(basePanel);
        basePanel.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        basePanel.setBackground(Color.WHITE);
        textArea.setBackground(Color.WHITE);
        basePanel.add(textArea);
        return basePanel;
    }

    public JButton configureButton(JButton button) {
        configureComponents(button);
        button.setIcon(new ImageIcon("src/main/resources/Transformer_Icon.png"));
        button.setBorder(new LineBorder(new Color(231, 242, 245)));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        return button;
    }

    public static XYChart configureChart() {
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

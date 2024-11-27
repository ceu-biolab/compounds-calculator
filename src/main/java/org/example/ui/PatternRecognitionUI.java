package org.example.ui;

import ceu.biolab.IncorrectAdduct;
import ceu.biolab.IncorrectFormula;
import ceu.biolab.NotFoundElement;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;
import org.example.adduct.AdductsLists;
import org.example.databases.Database;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static org.example.ui.LipidCalculatorUI.*;

public class PatternRecognitionUI extends JPanel {
    public JPanel adductsPanel = new JPanel();
    public JPanel graphPanel = new JPanel(new MigLayout("", "[grow, fill]", "[grow, fill]"));
    public static List<JCheckBox> adductCheckBoxList = new ArrayList<>();
    public static List<String> chosenAdductCheckBoxes = new ArrayList<>();
    public static XYChart chart = new XYChartBuilder().width(350).height(400).xAxisTitle("Retention Time").yAxisTitle("Intensity").build();
    public static JTextField lipidTextField = new JTextField();

    public PatternRecognitionUI() {
        FlatLightLaf.setup();
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        setLayout(new MigLayout("", "25[]25[]25",
                ""));
        setBackground(new Color(195, 224, 229));

        graphPanel.setMinimumSize(new Dimension(1225, 420));
        graphPanel.setBackground(Color.WHITE);
        graphPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");

        XChartPanel<XYChart> chartPanel = new XChartPanel<>(configureChart());
        chartPanel.setMinimumSize(new Dimension(1100, 550));

        JLabel adductPeaksLabel = new JLabel("Likely Adduct Peaks According to Lipid");
        configureTextComponents(adductPeaksLabel);

        JPanel lipidSubPanel = new JPanel(new MigLayout("", "[grow, fill]", "[grow, fill]"));
        configureComponents(lipidTextField);
        configureComponents(lipidSubPanel);
        lipidSubPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        lipidTextField.putClientProperty(FlatClientProperties.STYLE, "arc: 20");
        lipidTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "  Lipid, ie. TG(16:0/16:0/16:0)");
        lipidTextField.setBorder(BorderFactory.createLineBorder(new Color(231, 242, 245)));
        lipidSubPanel.add(lipidTextField, "grow");

        JLabel selectAdductLabel = new JLabel("First introduce the lipid, then select an adduct from the list on the right to begin.");
        configureTextComponents(selectAdductLabel);
        selectAdductLabel.setFont(new Font("Arial", Font.ITALIC, 14));

        graphPanel.add(adductPeaksLabel, "gapleft 15, gaptop 15, wrap");
        graphPanel.add(selectAdductLabel, "gapleft 30, gaptop 10, wrap");
        graphPanel.add(lipidSubPanel, "gapleft 30, gaptop 10, gapright 500, wrap");
        graphPanel.add(chartPanel, "gaptop 50, gapbottom 100, span 2 1, align center, grow");

        createFullAdductsListPanel();

        add(graphPanel);
        add(adductsPanel);
        setVisible(true);
    }

    public static XYChart configureChart() {
        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        chart.getStyler().setMarkerSize(0);
        chart.getStyler().setChartBackgroundColor(Color.WHITE);
        chart.getStyler().setChartFontColor(new Color(65, 114, 159));
        chart.getStyler().setPlotBorderColor(Color.WHITE);
        chart.getStyler().setAxisTickMarksColor(new Color(65, 114, 159));
        chart.getStyler().setAxisTickLabelsColor(new Color(65, 114, 159));
        chart.getStyler().setChartTitleFont(new Font("Arial", Font.BOLD, 16));
        chart.getStyler().setYAxisMin(0.0d);
        chart.getStyler().setYAxisMax(100.0d);
        chart.getStyler().setXAxisMin(0.0d);
        return chart;
    }

    public void updateChart(String compoundName) throws IncorrectAdduct, NotFoundElement, IncorrectFormula {
        Map<String, XYSeries> map = chart.getSeriesMap();

        List<String> currentSeries = new ArrayList<>(map.keySet());
        for (String existingAdduct : currentSeries) {
            if (!chosenAdductCheckBoxes.contains(existingAdduct)) {
                chart.removeSeries(existingAdduct);
            }
        }

        double retentionTime = 0;

        for (String adduct : chosenAdductCheckBoxes) {
            if (!map.containsKey(adduct)) {
                Random random = new Random();
                try {
                    retentionTime = Database.getRetentionTimeOfLipid(compoundName);
                    double[] xData = generateXData(retentionTime);
                    double amplitude = random.nextInt(40, 90);
                    double[] yData = generateGaussianCurve(xData, retentionTime, 1.0 / 3.0, amplitude);
                    chart.addSeries(adduct, xData, yData);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        for (Component component : graphPanel.getComponents()) {
            if (component instanceof XChartPanel) {
                graphPanel.remove(component);
            }
        }

        XChartPanel<XYChart> chartPanel = new XChartPanel<>(configureChart());
        double minimum = retentionTime - 1;
        if (minimum >= 0) {
            chart.getStyler().setXAxisMin(minimum);
        } else {
            chart.getStyler().setXAxisMin(0.0d);
        }
        chart.getStyler().setXAxisMax(retentionTime + 1);
        chartPanel.setMinimumSize(new Dimension(1100, 550));
        graphPanel.add(chartPanel, "gaptop 30, gapbottom 300, span 2 1, grow");
        graphPanel.revalidate();
        graphPanel.repaint();
    }

    private static double[] generateXData(double rt) {
        List<Double> xData = new ArrayList<>();
        for (double x = rt - 1; x <= rt + 1; x += 0.01) {
            if (x >= 0) {
                xData.add(x);
            }
        }
        return xData.stream().mapToDouble(Double::doubleValue).toArray();
    }

    private static double[] generateGaussianCurve(double[] xData, double rt, double sigma, double amplitude) {
        double[] yData = new double[xData.length];
        for (int i = 0; i < xData.length; i++) {
            yData[i] = amplitude * Math.exp(-Math.pow(xData[i] - rt, 2) / (2 * Math.pow(sigma, 2)));
        }
        return yData;
    }

    public void createFullAdductsListPanel() {
        adductsPanel.setLayout(new MigLayout("", "[grow, fill]", ""));
        adductsPanel.setBackground(Color.WHITE);
        adductsPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");

        JLabel adductsLabel = new JLabel("Adducts");
        configureTextComponents(adductsLabel);
        adductsPanel.add(adductsLabel, "wrap");

        JLabel adductsDescLabel = new JLabel("Choose an adduct to view the pattern recognition.");
        configureTextComponents(adductsDescLabel);
        adductsDescLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        adductsPanel.add(adductsDescLabel, "gapbottom 5, wrap");

        JCheckBox selectAllCheckBox = new JCheckBox("Select All");
        configureTextComponents(selectAllCheckBox);
        adductsPanel.add(selectAllCheckBox, "gapleft 15, wrap");

        List<JPanel> checkBoxPanels = new ArrayList<>();
        JPanel subPanel = new JPanel(new MigLayout());
        subPanel.setBackground(Color.WHITE);
        String[] adducts = AdductsLists.MAPADDUCTS.keySet().toArray(new String[0]);

        for (String adduct : adducts) {
            JCheckBox checkBox = new JCheckBox(adduct);
            JPanel checkBoxPanel = new JPanel(new BorderLayout());
            checkBoxPanels.add(checkBoxPanel);
            checkBoxPanel.setBorder(new EmptyBorder(0, 10, 0, 0));

            checkBoxPanel.setMinimumSize(new Dimension(290, 30));
            checkBox.setMinimumSize(new Dimension(290, 30));

            checkBoxPanel.setBackground(new Color(231, 242, 245));
            checkBoxPanel.add(checkBox);
            checkBoxPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 20");
            checkBox.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    checkBoxPanel.setBackground(new Color(195, 224, 229));
                    checkBox.setBackground(new Color(195, 224, 229));
                    chosenAdductCheckBoxes.add(checkBox.getText());
                    findLipidMassForChart();
                } else {
                    checkBoxPanel.setBackground(new Color(231, 242, 245));
                    checkBox.setBackground(new Color(231, 242, 245));
                    chosenAdductCheckBoxes.remove(checkBox.getText());
                    findLipidMassForChart();
                }
            });
            adductCheckBoxList.add(checkBox);
            configureTextComponents(checkBox);
            checkBox.setFont(new Font("Arial", Font.BOLD, 16));
            /*if (adduct.equals("[M+H]+") || adduct.equals("[M+Na]+") || adduct.equals("[M+NH4]+")) {
                checkBox.setSelected(true);
                checkBoxPanel.setBackground(new Color(195, 224, 229));
                checkBox.setBackground(new Color(195, 224, 229));
                chosenAdductCheckBoxes.add(checkBox.getText());
            }*/
            subPanel.add(checkBoxPanel, "wrap, gapbottom 3");
        }

        selectAllCheckBox.addItemListener(e -> updatePanelWhenSelected(checkBoxPanels, e));

        JScrollPane adductsScrollPane = new JScrollPane(subPanel);
        adductsScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        adductsScrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        adductsPanel.add(adductsScrollPane);
    }

    static void updatePanelWhenSelected(List<JPanel> checkBoxPanels, ItemEvent e) {
        boolean isSelected = (e.getStateChange() == ItemEvent.SELECTED);
        Color bg = isSelected ? new Color(195, 224, 229) : new Color(231, 242, 245);

        for (JPanel panel : checkBoxPanels) {
            panel.setBackground(bg);
            panel.putClientProperty(FlatClientProperties.STYLE, "arc: 20");
            panel.revalidate();
            panel.repaint();

            for (Component component : panel.getComponents()) {
                if (component instanceof JCheckBox checkBox) {
                    checkBox.setSelected(isSelected);
                    checkBox.setBackground(bg);
                }
            }
        }
    }

    private void findLipidMassForChart() {
        try {
            if (!(lipidTextField == null)) {
                String compoundName = lipidTextField.getText();
                updateChart(compoundName);
            }
        } catch (IncorrectAdduct | NotFoundElement | IncorrectFormula ex) {
            throw new RuntimeException(ex);
        }
    }

}


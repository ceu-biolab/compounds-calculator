package org.example.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;
import org.example.adduct.AdductsLists;
import org.example.adduct.Transformer;
import org.example.utils.CSVUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static org.example.ui.LipidCalculatorUI.*;

public class AdductTransformerUI extends JPanel {
    public JPanel massToMZPanel = new JPanel(new MigLayout("", "20[grow, fill]20[]20[grow, fill]20", "20[]20[grow, fill]20"));
    public JPanel mzToMassPanel = new JPanel(new MigLayout("", "20[grow, fill]20[]20[grow, fill]20", "20[]20[grow, fill]20"));
    public JPanel createAdductsPanel = new JPanel(new MigLayout("", "15[grow]15", ""));
    public JPanel adductsPanel = new JPanel();
    public static List<JCheckBox> adductCheckBoxList = new ArrayList<>();
    public static List<String> chosenAdductCheckBoxes = new ArrayList<>();
    public JTextField monoMassTextField = new JTextField();
    public JTextField mzTextField = new JTextField();
    public JPanel mzResultsPanel = new JPanel(new MigLayout());
    public JPanel massResultsPanel = new JPanel(new MigLayout());
    public DecimalFormat numberFormat = new DecimalFormat("#.0000");
    public String[][] mzResultsData = null;
    public String[][] massResultsData = null;
    public JButton exportButton1 = configureExportButton();
    public JButton exportButton2 = configureExportButton();

    public AdductTransformerUI() {
        FlatLightLaf.setup();
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        setLayout(new MigLayout("", "25[grow, fill]25[grow, fill]25",
                "25[grow, fill]25[grow, fill]25"));
        setBackground(new Color(195, 224, 229));

        massToMZPanel.setMinimumSize(new Dimension(500, 320));
        massToMZPanel.setBackground(Color.WHITE);
        massToMZPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        monoMassTextField.setBorder(new LineBorder(Color.WHITE));

        mzToMassPanel.setMinimumSize(new Dimension(500, 320));
        mzToMassPanel.setBackground(Color.WHITE);
        mzToMassPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        mzTextField.setBorder(new LineBorder(Color.WHITE));

        createAdductsPanel.setMinimumSize(new Dimension(375, 240));
        createAdductsPanel.setBackground(Color.WHITE);
        createAdductsPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        JLabel createAdductsLabel = new JLabel("   Create Adduct");
        createAdductsLabel.setIcon(new ImageIcon("src/main/resources/CreateAdduct_Icon.png"));
        configureTextComponents(createAdductsLabel);
        JLabel createAdductsSubLabel = new JLabel("Define an adduct for use in the application.");
        configureTextComponents(createAdductsSubLabel);
        createAdductsSubLabel.setFont(new Font("Arial", Font.ITALIC, 14));

        JLabel adductFormulaLabel = new JLabel("Introduce in [M+X]q+/- format: ");
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
        List<String> adductsList = new ArrayList<>(List.of("[M+H]+", "[M+Na]+", "[M+K]+", "[M+NH4]+", "[M+H-H2O]+", "[M+C2H6N2+H]+"));
        confirmAdduct.addActionListener(_ -> {
            adductsList.add(adductFormulaTextField.getText());
            String[] finalArray = adductsList.toArray(new String[0]);
            updateAdductPanel(finalArray);
            JOptionPane.showMessageDialog(null, "Adduct has sucessfully been added to the list of adducts.");
        });

        createAdductsPanel.add(createAdductsLabel, "wrap");
        createAdductsPanel.add(createAdductsSubLabel, "gaptop 5, align center, wrap");
        createAdductsPanel.add(adductFormulaLabel, "gaptop 10, wrap");
        createAdductsPanel.add(adductFormulaPanel, "grow, gaptop 10, wrap");
        createAdductsPanel.add(confirmAdduct, "align center, gaptop 10, gapbottom 25, wrap");

        createFullAdductsListPanel();
        add(createAdductsPanel, "grow");
        add(configureCalculationsPanel(), "grow, span 1 2, wrap");
        add(adductsPanel, "grow");
        setVisible(true);
    }

    public JPanel configureCalculationsPanel() {
        JPanel calculationsPanel = new JPanel(new MigLayout("", "50[grow, fill]100[grow, fill]50", "50[]25[]25[]25[]25"));
        calculationsPanel.setBackground(Color.WHITE);
        calculationsPanel.setMinimumSize(new Dimension(1200, 800));
        calculationsPanel.putClientProperty(FlatClientProperties.STYLE, "arc:40");

        JLabel massToMzLabel = new JLabel("   Find mz values from monoisotopic masses");
        massToMzLabel.setIcon(new ImageIcon("src/main/resources/FindValues_Icon.png"));
        configureTextComponents(massToMzLabel);

        JLabel mzToMassLabel = new JLabel("   Find monoisotopic masses from mz values");
        mzToMassLabel.setIcon(new ImageIcon("src/main/resources/FindValues_Icon copy.png"));
        configureTextComponents(mzToMassLabel);

        calculationsPanel.add(massToMzLabel, "gapbottom 5");
        calculationsPanel.add(mzToMassLabel, "gapbottom 5, wrap");
        calculationsPanel.add(configureMassInputPanel(), "gapbottom 15");
        calculationsPanel.add(configureMzInputPanel(), "gapbottom 15, wrap");
        calculationsPanel.add(configureMzResultsPanel(), "gapbottom 25");
        calculationsPanel.add(configureMassResultsPanel(), "gapbottom 25");
        return calculationsPanel;
    }

    public JPanel configureMzResultsPanel() {
        mzResultsPanel.setBackground(Color.WHITE);
        JLabel resultsLabel1 = new JLabel("m/z results");
        configureTextComponents(resultsLabel1);
        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(new ImageIcon("src/main/resources/SearchM_Icon.png"));
        JLabel waitingForInputLabel = new JLabel("Introduce a mass to get started.");
        configureTextComponents(waitingForInputLabel);
        waitingForInputLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel waitingForInputPanel1 = new JPanel(new MigLayout("", "100[]100", "150[]150"));
        waitingForInputPanel1.setBackground(new Color(231, 242, 245));
        waitingForInputPanel1.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        waitingForInputPanel1.add(waitingForInputLabel, "align center");

        mzResultsPanel.add(resultsLabel1, "gapbottom 15, wrap");
        mzResultsPanel.add(waitingForInputPanel1, "align center, gaptop 25, gapbottom 15");
        mzResultsPanel.setMinimumSize(new Dimension(300, 200));
        mzResultsPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        return mzResultsPanel;
    }

    public JPanel configureMassResultsPanel() {
        massResultsPanel.setBackground(Color.WHITE);
        JLabel resultsLabel2 = new JLabel("Monoisotopic mass results");
        configureTextComponents(resultsLabel2);
        JLabel iconLabel2 = new JLabel();
        iconLabel2.setIcon(new ImageIcon("src/main/resources/SearchMZ_Icon.png"));
        JLabel waitingForInputLabel2 = new JLabel("Introduce an m/z value to get started.");
        configureTextComponents(waitingForInputLabel2);
        waitingForInputLabel2.setFont(new Font("Arial", Font.BOLD, 16));
        JPanel waitingForInputPanel2 = new JPanel(new MigLayout("", "80[]80", "150[]150"));
        waitingForInputPanel2.setBackground(new Color(231, 242, 245));
        waitingForInputPanel2.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        waitingForInputPanel2.add(waitingForInputLabel2);
        massResultsPanel.add(resultsLabel2, "gapbottom 15, wrap");
        massResultsPanel.add(waitingForInputPanel2, "align center, gaptop 25, gapbottom 15");
        massResultsPanel.setMinimumSize(new Dimension(300, 200));
        massResultsPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        return massResultsPanel;
    }

    public JPanel configureMassInputPanel() {
        JLabel monoMassLabel1 = new JLabel("Monoisotopic mass");
        configureTextComponents(monoMassLabel1);
        monoMassLabel1.setFont(new Font("Arial", Font.BOLD, 16));
        JPanel mzResultsPanel = new JPanel(new MigLayout("", "25[grow, fill][grow, fill]25", "[grow, fill]"));
        mzResultsPanel.setBackground(new Color(231, 242, 245));
        mzResultsPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        JButton goButton1 = new JButton();
        configureComponents(goButton1);
        goButton1.setBorder(new LineBorder(Color.WHITE, 0));
        goButton1.setIcon(new ImageIcon("src/main/resources/Go_Icon.png"));
        goButton1.addActionListener(_ -> {
            List<String[]> finalLipidDataList = new ArrayList<>();

            if (mzTextField != null && !(mzTextField.getText().isEmpty())) {
                String monoMassValue = monoMassTextField.getText();
                if (chosenAdductCheckBoxes.isEmpty()) {
                    JOptionPane.showMessageDialog(this.massResultsPanel, "Please select at least one adduct");
                    return;
                }

                chosenAdductCheckBoxes.forEach(adduct -> {
                    double mass = Transformer.getMassOfAdductFromMonoMass(Double.parseDouble(monoMassValue), adduct);
                    String[] row = new String[2];
                    row[0] = numberFormat.format(mass);
                    row[1] = adduct;
                    finalLipidDataList.add(row);
                });

                mzResultsData = new String[finalLipidDataList.size()][2];
                for (int i = 0; i < finalLipidDataList.size(); i++) {
                    mzResultsData[i] = finalLipidDataList.get(i);
                }

                JLabel resultsLabel2 = new JLabel("Mass results");
                configureTextComponents(resultsLabel2);
                JTable mzTable = getjTable("m/z", mzResultsData);
                configureExportAction("m/z", exportButton1, mzResultsData);
                configureScrollPane(resultsLabel2, exportButton1, mzTable, this.mzResultsPanel);
            } else {
                JOptionPane.showMessageDialog(this.mzResultsPanel, "Please enter a valid m/z value");
            }
        });

        mzResultsPanel.add(monoMassLabel1, "gapbottom 10, wrap");
        mzResultsPanel.add(configurePanelForTextField(monoMassTextField), "grow, gapright 10");
        mzResultsPanel.add(goButton1, "gapright 5");
        mzResultsPanel.setMinimumSize(new Dimension(50, 100));
        return mzResultsPanel;
    }

    public JPanel configureMzInputPanel() {
        JPanel massResultsPanel = new JPanel(new MigLayout("", "25[grow, fill][grow, fill]25", "[grow, fill]"));
        massResultsPanel.setBackground(new Color(231, 242, 245));
        massResultsPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");

        JLabel mzLabel2 = new JLabel("m/z");
        configureTextComponents(mzLabel2);
        mzLabel2.setFont(new Font("Arial", Font.BOLD, 16));

        JButton goButton2 = new JButton();
        configureComponents(goButton2);
        goButton2.setBorder(new EmptyBorder(0, 0, 0, 0));
        goButton2.setIcon(new ImageIcon("src/main/resources/Go_Icon.png"));
        goButton2.addActionListener(_ -> {
            List<String[]> finalLipidDataList = new ArrayList<>();

            if (mzTextField != null && !(mzTextField.getText().isEmpty())) {
                String mzValue = mzTextField.getText();
                if (chosenAdductCheckBoxes.isEmpty()) {
                    JOptionPane.showMessageDialog(this.massResultsPanel, "Please select at least one adduct");
                    return;
                }

                chosenAdductCheckBoxes.forEach(adduct -> {
                    double mass = Transformer.getMonoisotopicMassFromMZ(Double.parseDouble(mzValue), adduct);
                    String[] row = new String[2];
                    row[0] = numberFormat.format(mass);
                    row[1] = adduct;
                    finalLipidDataList.add(row);
                });

                massResultsData = new String[finalLipidDataList.size()][2];
                for (int i = 0; i < finalLipidDataList.size(); i++) {
                    massResultsData[i] = finalLipidDataList.get(i);
                }

                JLabel resultsLabel2 = new JLabel("Mass results");
                configureTextComponents(resultsLabel2);
                JTable massTable = getjTable("Monoisotopic Mass", massResultsData);
                configureExportAction("Monoisotopic Mass", exportButton2, massResultsData);
                configureScrollPane(resultsLabel2, exportButton2, massTable, this.massResultsPanel);
            } else {
                JOptionPane.showMessageDialog(this.massResultsPanel, "Please enter a valid m/z value");
            }
        });

        massResultsPanel.add(mzLabel2, "gapbottom 10, wrap");
        massResultsPanel.add(configurePanelForTextField(mzTextField), "grow, gapright 10");
        massResultsPanel.add(goButton2);
        return massResultsPanel;
    }

    public static void configureExportAction(String varType, JButton exportButton, String[][] massResultsData) {
        exportButton.addActionListener(_ -> {
            int choice = JOptionPane.showConfirmDialog(null,
                    "Do you wish to export this information to CSV format?",
                    "Export to CSV", JOptionPane.YES_NO_CANCEL_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                try {
                    CSVUtils csvUtils = new CSVUtils();
                    csvUtils.createAndWriteCSVofTransformerData(varType, massResultsData);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Current data set is empty. Please introduce data before attempting to create a new file.");
                }
            } else if (choice == JOptionPane.NO_OPTION || choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION) {
                JOptionPane.showMessageDialog(null, "Operation cancelled.");
            } else {
                JOptionPane.showMessageDialog(null, "File already exists.");
            }
        });
    }

    public JButton configureExportButton() {
        JButton exportButton = new JButton();
        configureComponents(exportButton);
        exportButton.setBackground(Color.WHITE);
        exportButton.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        exportButton.setIcon(new ImageIcon("src/main/resources/Download_Icon.png"));
        exportButton.setBorder(new LineBorder(Color.white));
        exportButton.setHorizontalAlignment(SwingConstants.LEFT);
        return exportButton;
    }

    private void configureScrollPane(JLabel resultsLabel, JButton exportButton, JTable massTable, JPanel resultsPanel) {
        configureTable(massTable);
        JScrollPane lipidScrollPane = new JScrollPane(massTable);
        lipidScrollPane.setPreferredSize(new Dimension(500, 400));
        lipidScrollPane.setBorder(new LineBorder(Color.WHITE, 1));
        resultsPanel.removeAll();
        resultsPanel.add(resultsLabel, "gapbottom 15");
        resultsPanel.add(exportButton, "gapbottom 15, push, al right, wrap");
        resultsPanel.add(lipidScrollPane);
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    static void configureTable(JTable massTable) {
        massTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        massTable.getTableHeader().setBackground(Color.WHITE);
        massTable.getTableHeader().setForeground(new Color(65, 114, 159));
        massTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        massTable.setForeground(new Color(65, 114, 159));
        massTable.setFont(new Font("Arial", Font.BOLD, 15));
        massTable.getTableHeader().setReorderingAllowed(false);
        massTable.setRowHeight(massTable.getRowHeight() + 15);
    }

    private static JTable getjTable(String var, String[][] lipidData) {
        String[] columnNames = {var, "Adduct"};

        TableModel tableModel = new DefaultTableModel(lipidData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        return new JTable(tableModel) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                Color alternateColor = new Color(231, 242, 245);
                Color whiteColor = Color.WHITE;
                if (!comp.getBackground().equals(getSelectionBackground())) {
                    Color color = (row % 2 == 0 ? alternateColor : whiteColor);
                    comp.setBackground(color);
                }
                return comp;
            }
        };
    }

    public JPanel configurePanelForTextField(JTextField textField) {
        JPanel basePanel = new JPanel(new MigLayout("", "[grow, fill]", "[grow, fill]"));
        configureComponents(textField);
        configureComponents(basePanel);
        basePanel.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        basePanel.setBackground(Color.WHITE);
        textField.setBackground(Color.WHITE);
        basePanel.setMinimumSize(new Dimension(375, 50));
        basePanel.add(textField);
        return basePanel;
    }

    public void createFullAdductsListPanel() {
        adductsPanel.setLayout(new MigLayout("", "[grow, fill]", ""));
        adductsPanel.setBackground(Color.WHITE);
        adductsPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        adductsPanel.setMinimumSize(new Dimension(375, 535));

        JLabel adductsLabel = new JLabel("Adducts");
        configureTextComponents(adductsLabel);
        adductsPanel.add(adductsLabel, "wrap");

        JLabel adductsDescLabel = new JLabel("Choose an adduct to apply to the mass or m/z.");
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

            checkBoxPanel.setMinimumSize(new Dimension(375, 30));
            checkBox.setMinimumSize(new Dimension(375, 30));

            checkBoxPanel.setBackground(new Color(231, 242, 245));
            checkBoxPanel.add(checkBox);
            checkBoxPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 20");
            checkBox.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    checkBoxPanel.setBackground(new Color(195, 224, 229));
                    checkBox.setBackground(new Color(195, 224, 229));
                    chosenAdductCheckBoxes.add(checkBox.getText());
                } else {
                    checkBoxPanel.setBackground(new Color(231, 242, 245));
                    checkBox.setBackground(new Color(231, 242, 245));
                    chosenAdductCheckBoxes.remove(checkBox.getText());
                }
            });
            adductCheckBoxList.add(checkBox);
            configureTextComponents(checkBox);
            checkBox.setFont(new Font("Arial", Font.BOLD, 16));
            subPanel.add(checkBoxPanel, "wrap, gapbottom 3");
        }

        selectAllCheckBox.addItemListener(e -> {
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
        });

        JScrollPane adductsScrollPane = new JScrollPane(subPanel);
        adductsScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        adductsScrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        adductsPanel.add(adductsScrollPane);
    }
}


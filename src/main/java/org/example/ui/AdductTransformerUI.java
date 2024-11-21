package org.example.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;
import org.example.adduct.AdductsLists;
import org.example.adduct.Transformer;
import org.example.domain.MSLipid;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
    public JButton massToMzButton = new JButton();
    public JTextField mzTextField = new JTextField();
    public JButton mzToMassButton = new JButton();
    public JScrollPane mzScrollPane = createTableOfMZs();
    public JScrollPane massScrollPane = createTableOfMonoMasses();

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

        DecimalFormat numberFormat = new DecimalFormat("#.0000");

        JLabel massToMzLabel = new JLabel("   Find mz values from monoisotopic masses");
        massToMzLabel.setIcon(new ImageIcon("src/main/resources/FindValues_Icon.png"));
        configureTextComponents(massToMzLabel);
        massToMZPanel.setMinimumSize(new Dimension(500, 320));
        massToMZPanel.setBackground(Color.WHITE);
        massToMZPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        monoMassTextField.setBorder(new LineBorder(Color.WHITE));
        massToMzButton.addActionListener(e -> {
            List<Double> mzValues = new ArrayList<>();
            if (monoMassTextField != null) {
                String[] massValues = monoMassTextField.getText().replaceAll("[\\[\\]]", "").split("[\\s,;\\t]+");
                // TODO: GET FROM ADDUCTS LIST
                for (String mass : massValues) {
                    mzValues.add(Double.valueOf(numberFormat.format(Transformer.getMassOfAdductFromMonoMass(Double.parseDouble(mass), "[M+NH4]+"))));
                }
            }
            StringBuilder stringBuilder = new StringBuilder();

            for (Object value : mzValues) {
                stringBuilder.append(value.toString()).append("\t");
            }
        });

        JLabel mzToMassLabel = new JLabel("   Find monoisotopic masses from mz values");
        JLabel monoMassLabel1 = new JLabel("Monoisotopic mass");
        configureTextComponents(monoMassLabel1);
        JLabel mzLabel2 = new JLabel("m/z");
        configureTextComponents(mzLabel2);
        monoMassLabel1.setFont(new Font("Arial", Font.BOLD, 16));
        mzLabel2.setFont(new Font("Arial", Font.BOLD, 16));

        mzToMassLabel.setIcon(new ImageIcon("src/main/resources/FindValues_Icon copy.png"));
        configureTextComponents(mzToMassLabel);
        mzToMassPanel.setMinimumSize(new Dimension(500, 320));
        mzToMassPanel.setBackground(Color.WHITE);
        mzToMassPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        mzTextField.setBorder(new LineBorder(Color.WHITE));
        mzToMassButton.addActionListener(e -> {
            List<Double> massValues = new ArrayList<>();
            if (mzTextField != null) {
                /* TODO String mzValue = mzTextField.getText();
                for (String adduct : chosenAdductCheckBoxes) {
                    massValues.add(Double.valueOf(numberFormat.format(Transformer.getMonoisotopicMassFromMZ(Double.parseDouble(mzValue), adduct))));

                }
                String[][] localLipidData = new String[massValues.size()][8];

                for (double d : massValues) {
                    localLipidData[i][0] = d;
                    localLipidData[i][1] = d;
                }
                JScrollPane tableScrollPane = configureTable(massValues, 2);*/
            }

            StringBuilder stringBuilder = new StringBuilder();

            for (Object value : massValues) {
                stringBuilder.append(value.toString()).append("\t");
            }
        });

        createAdductsPanel.setMinimumSize(new Dimension(375, 240));
        createAdductsPanel.setMaximumSize(new Dimension(375, 240));
        createAdductsPanel.setBackground(Color.WHITE);
        createAdductsPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        JLabel createAdductsLabel = new JLabel("   Create Adduct");
        createAdductsLabel.setIcon(new ImageIcon("src/main/resources/CreateAdduct_Icon.png"));
        configureTextComponents(createAdductsLabel);
        JLabel createAdductsSubLabel = new JLabel("Define an adduct for use in the application.");
        configureTextComponents(createAdductsSubLabel);
        createAdductsSubLabel.setFont(new Font("Arial", Font.ITALIC, 14));

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
        List<String> adductsList = new ArrayList<>(List.of("[M+H]+", "[M+Na]+", "[M+K]+", "[M+NH4]+", "[M+H-H2O]+", "[M+C2H6N2+H]+"));
        confirmAdduct.addActionListener(e -> {
            adductsList.add(adductFormulaTextField.getText());
            String[] finalArray = adductsList.toArray(new String[0]);
            updateAdductPanel(finalArray);
            JOptionPane.showMessageDialog(null, "Adduct has sucessfully been added to the list of adducts.");
        });

        JPanel testPanel = new JPanel(new MigLayout("", "25[grow, fill][grow, fill]25", "[grow, fill]"));
        testPanel.setBackground(new Color(231, 242, 245));
        testPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        JButton goButton1 = new JButton();
        configureComponents(goButton1);
        goButton1.setBorder(new LineBorder(Color.WHITE, 0));
        goButton1.setIcon(new ImageIcon("src/main/resources/Go_Icon.png"));
        testPanel.add(monoMassLabel1, "gapbottom 10, wrap");
        testPanel.add(configurePanelForTextField(monoMassTextField), "grow, gapright 10");
        testPanel.add(goButton1, "gapright 5");
        testPanel.setMinimumSize(new Dimension(50, 100));

        JPanel testPanel2 = new JPanel(new MigLayout("", "25[grow, fill][grow, fill]25", "[grow, fill]"));
        testPanel2.setBackground(new Color(231, 242, 245));
        testPanel2.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        JButton goButton2 = new JButton();
        configureComponents(goButton2);
        goButton2.setBorder(new EmptyBorder(0, 0, 0, 0));
        goButton2.setIcon(new ImageIcon("src/main/resources/Go_Icon.png"));
        testPanel2.add(mzLabel2, "gapbottom 10, wrap");
        testPanel2.add(configurePanelForTextField(mzTextField), "grow, gapright 10");
        testPanel2.add(goButton2);

        createAdductsPanel.add(createAdductsLabel, "wrap");
        createAdductsPanel.add(createAdductsSubLabel, "gaptop 5, align center, wrap");
        createAdductsPanel.add(adductFormulaLabel, "gaptop 10, wrap");
        createAdductsPanel.add(adductFormulaPanel, "grow, gaptop 10, wrap");
        createAdductsPanel.add(confirmAdduct, "align center, gaptop 10, gapbottom 25, wrap");

        JPanel panel2 = new JPanel(new MigLayout("", "50[grow, fill]100[grow, fill]50", "50[]25[]25[]25[]25"));
        panel2.setBackground(Color.WHITE);
        panel2.setMinimumSize(new Dimension(1200, 800));
        panel2.putClientProperty(FlatClientProperties.STYLE, "arc:40");

        JPanel resultsPanel1 = new JPanel(new MigLayout());
        resultsPanel1.setBackground(Color.WHITE);
        JLabel resultsLabel1 = new JLabel("m/z results");
        configureTextComponents(resultsLabel1);
        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(new ImageIcon("src/main/resources/SearchM_Icon.png"));
        JLabel waitingForInputLabel = new JLabel("Introduce a mass to get started.");
        configureTextComponents(waitingForInputLabel);
        waitingForInputLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel circlePanel = new JPanel(new MigLayout("", "100[]100", "150[]150"));
        circlePanel.setBackground(new Color(231, 242, 245));
        circlePanel.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        circlePanel.add(waitingForInputLabel, "align center");

        resultsPanel1.add(resultsLabel1, "gapbottom 15, wrap");
        resultsPanel1.add(mzScrollPane, "gapbottom 10, wrap");
        resultsPanel1.add(circlePanel, "align center, gaptop 25, gapbottom 15");
        resultsPanel1.setMinimumSize(new Dimension(300, 200));
        resultsPanel1.putClientProperty(FlatClientProperties.STYLE, "arc:20");

        JPanel resultsPanel2 = new JPanel(new MigLayout());
        resultsPanel2.setBackground(Color.WHITE);
        JLabel resultsLabel2 = new JLabel("Monoisotopic mass results");
        configureTextComponents(resultsLabel2);
        JLabel iconLabel2 = new JLabel();
        iconLabel2.setIcon(new ImageIcon("src/main/resources/SearchMZ_Icon.png"));
        JLabel waitingForInputLabel2 = new JLabel("Introduce an m/z value to get started.");
        configureTextComponents(waitingForInputLabel2);
        waitingForInputLabel2.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel circlePanel2 = new JPanel(new MigLayout("", "80[]80", "150[]150"));
        circlePanel2.setBackground(new Color(231, 242, 245));
        circlePanel2.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        circlePanel2.add(waitingForInputLabel2);

        resultsPanel2.add(resultsLabel2, "gapbottom 15, wrap");
        resultsPanel2.add(massScrollPane, "gapbottom 10, wrap");
        resultsPanel2.add(circlePanel2, "align center, gaptop 25, gapbottom 15");
        resultsPanel2.setMinimumSize(new Dimension(300, 200));
        resultsPanel2.putClientProperty(FlatClientProperties.STYLE, "arc:20");

        panel2.add(massToMzLabel, "gapbottom 5");
        panel2.add(mzToMassLabel, "gapbottom 5, wrap");
        panel2.add(testPanel, "gapbottom 15");
        panel2.add(testPanel2, "gapbottom 15, wrap");
        panel2.add(resultsPanel1, "gapbottom 25");
        panel2.add(resultsPanel2, "gapbottom 25");

        createFullAdductsListPanel();
        add(createAdductsPanel, "grow");
        add(panel2, "grow, span 1 2, wrap");
        add(adductsPanel, "grow");
        setVisible(true);
    }

    public JScrollPane createTableOfMZs() {
        String[] tableTitles = new String[]{"m/z", "Adduct"};
        return getjScrollPane(tableTitles);
    }

    private JScrollPane getjScrollPane(String[] tableTitles) {
        DefaultTableModel model = new DefaultTableModel(tableTitles, 0);
        JTable table = new JTable(model);
        JScrollPane jScrollPane = new JScrollPane(table);
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setForeground(new Color(65, 114, 159));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        table.getTableHeader().setReorderingAllowed(false);
        jScrollPane.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        jScrollPane.setBorder(new LineBorder(Color.WHITE, 1));
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        return jScrollPane;
    }

    public JScrollPane createTableOfMonoMasses() {
        String[] tableTitles = new String[]{"Monoisotopic Mass", "Adduct"};
        return getjScrollPane(tableTitles);
    }

    public JTable configureTable(String[][] data, int numRows) {
        TableModel tableModel = new DefaultTableModel(data, numRows) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel) {
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

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setForeground(new Color(65, 114, 159));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        table.setForeground(new Color(65, 114, 159));
        table.setFont(new Font("Arial", Font.BOLD, 15));
        table.getTableHeader().setReorderingAllowed(false);
        table.setModel(tableModel);
        table.setRowHeight(table.getRowHeight() + 15);
        table.setBorder(new LineBorder(Color.WHITE, 1));
        return table;
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
        adductsPanel.setMaximumSize(new Dimension(375, 535));

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
            checkBoxPanel.setMaximumSize(new Dimension(375, 30));
            checkBox.setMinimumSize(new Dimension(375, 30));
            checkBox.setMaximumSize(new Dimension(375, 30));

            checkBoxPanel.setBackground(new Color(231, 242, 245));
            checkBoxPanel.add(checkBox);
            checkBoxPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 20");
            checkBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        checkBoxPanel.setBackground(new Color(195, 224, 229));
                        checkBox.setBackground(new Color(195, 224, 229));
                        chosenAdductCheckBoxes.add(checkBox.getText());
                    } else {
                        checkBoxPanel.setBackground(new Color(231, 242, 245));
                        checkBox.setBackground(new Color(231, 242, 245));
                        chosenAdductCheckBoxes.remove(checkBox.getText());
                    }
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
                panel.setMaximumSize(new Dimension(500, 30));
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


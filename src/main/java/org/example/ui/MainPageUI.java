package org.example.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;
import org.example.databases.Database;
import org.example.domain.*;
import org.example.exceptions.FattyAcidCreation_Exception;
import org.example.exceptions.InvalidFormula_Exception;
import org.example.utils.CSVUtils;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class MainPageUI extends JPanel {
    private final JButton searchButton;
    private final JButton exportButton;
    private final JButton uploadButton;
    private final JButton clearButton;

    private final JPanel tablePanel;
    private final JPanel searchButtonsPanel;
    private final JPanel radioButtonsPanel;
    private final JPanel inputSubpanel;
    public JPanel adductsPanel;

    public JTextField NLoss1_Input;
    public JTextField NLoss2_Input;
    public JTextField NLoss3_Input;
    public JTextField NLoss4_Input;
    public JTextField PI_Input;

    private final JLabel precursorIonLabel;
    private final JLabel neutralLossesLabel;
    private final JLabel adductsLabel;

    private JTable table = null;
    private DefaultTableModel tableModel = null;
    private String[] tableTitles = null;
    private String[][] lipidData = null;
    private final Database database;
    private final ButtonGroup buttonGroup;
    private final JComboBox<String> ionComboBox;

    public MainPageUI() throws SQLException, InvalidFormula_Exception, FattyAcidCreation_Exception {
        FlatLightLaf.setup();
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        searchButton = new JButton("  Begin Search");
        exportButton = new JButton("  Export to CSV");
        uploadButton = new JButton("  Upload File");
        clearButton = new JButton("  Clear Input");
        tablePanel = new JPanel();
        searchButtonsPanel = new JPanel();
        radioButtonsPanel = new JPanel();
        inputSubpanel = new JPanel();
        NLoss1_Input = new JTextField();
        NLoss2_Input = new JTextField();
        NLoss3_Input = new JTextField();
        NLoss4_Input = new JTextField();
        PI_Input = new JTextField();
        precursorIonLabel = new JLabel("    Precursor Ion");
        neutralLossesLabel = new JLabel("    Neutral Losses");
        adductsLabel = new JLabel("    Adducts");
        adductsPanel = new JPanel();
        ionComboBox = new JComboBox<>(new String[]{"   View Positive Adducts  ", "   View Negative Adducts  "});
        database = new Database();
        buttonGroup = new ButtonGroup();
        setLayout(new MigLayout("", "[grow, fill]25[grow, fill]25[grow, fill]", "[grow, fill]25[grow, fill]"));
        setBackground(new Color(195, 224, 229));
        createTable();
        createRadioButtons();
        createInputPanel();
        createButtons();
        createAdductsPanel();

        add(tablePanel, "span 2");
        add(radioButtonsPanel, "wrap");
        add(inputSubpanel);
        add(adductsPanel);
        add(searchButtonsPanel);
        setVisible(true);
    }

    public void createTable() {
        tablePanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setPreferredSize(new Dimension(1250, 500));
        tablePanel.setSize(1250, 500);
        tablePanel.setLayout(new MigLayout("", "[grow, fill]", "[grow, fill]"));
        tableTitles = new String[]{"Cas ID", "Compound Name", "Species Shorthand", "Compound Formula", "Compound Mass", "Adduct", "M/Z"};

        DefaultTableModel model = new DefaultTableModel(tableTitles, 0);
        table = new JTable(model);
        JScrollPane jScrollPane = new JScrollPane(table);
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setForeground(new Color(65, 114, 159));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(175);
        table.getTableHeader().setReorderingAllowed(false);
        jScrollPane.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        jScrollPane.setBorder(new LineBorder(Color.WHITE, 1));
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        tablePanel.add(jScrollPane, "center, grow");
    }

    public void createButtons() {
        searchButtonsPanel.setBackground(Color.WHITE);
        searchButtonsPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        searchButtonsPanel.setLayout(new MigLayout("", "25[grow, fill]25", "25[grow, fill]25[grow, fill]25"));

        configureComponents(searchButton);
        searchButton.setBackground(Color.WHITE);
        searchButton.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        searchButton.setIcon(new ImageIcon("src/main/resources/Search_Icon.png"));
        searchButton.setBorder(new LineBorder(Color.white));
        searchButton.setHorizontalAlignment(SwingConstants.LEFT);
        searchButton.addActionListener(e -> {
            tablePanel.removeAll();
            JScrollPane lipidScrollPane = createLipidScrollPane();
            lipidScrollPane.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
            lipidScrollPane.setPreferredSize(new Dimension(1250, 500));
            lipidScrollPane.setBorder(new LineBorder(Color.WHITE, 1));
            tablePanel.add(lipidScrollPane, "center, grow");
            tablePanel.revalidate();
            tablePanel.repaint();
        });

        configureComponents(exportButton);
        exportButton.setBackground(Color.WHITE);
        exportButton.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        exportButton.setIcon(new ImageIcon("src/main/resources/Download_Icon.png"));
        exportButton.setBorder(new LineBorder(Color.white));
        exportButton.setHorizontalAlignment(SwingConstants.LEFT);
        exportButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(null, "Do you wish to export this information to CSV format?", "Export to CSV", JOptionPane.YES_NO_CANCEL_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                try {
                    CSVUtils csvUtils = new CSVUtils();
                    csvUtils.createAndWriteCSV(lipidData);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Current data set is empty. Please introduce data before attempting to create a new file.");
                }
            } else if (choice == JOptionPane.NO_OPTION || choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION) {
                JOptionPane.showMessageDialog(null, "Operation cancelled.");
            } else {
                JOptionPane.showMessageDialog(null, "File already exists.");
            }
        });

        configureComponents(uploadButton);
        uploadButton.setBackground(Color.WHITE);
        uploadButton.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        uploadButton.setIcon(new ImageIcon("src/main/resources/Upload_Icon.png"));
        uploadButton.setBorder(new LineBorder(Color.white));
        uploadButton.setHorizontalAlignment(SwingConstants.LEFT);
        uploadButton.addActionListener(e -> {
            CSVUtils csvUtils = new CSVUtils();
            FileDialog fileDialog = new FileDialog(new Frame(), "Choose a file in CSV format.", FileDialog.LOAD);
            fileDialog.setVisible(true);
            String fileName = fileDialog.getFile();
            String fileDirectory = fileDialog.getDirectory();
            if (fileName != null && fileDirectory != null) {
                try {
                    csvUtils.readCSVForBatchProcessing(new File(fileDirectory + fileName));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        configureComponents(clearButton);
        clearButton.setBackground(Color.WHITE);
        clearButton.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        clearButton.setIcon(new ImageIcon("src/main/resources/Clear_Icon.png"));
        clearButton.setBorder(new LineBorder(Color.white));
        clearButton.setHorizontalAlignment(SwingConstants.LEFT);
        clearButton.addActionListener(e -> {
            NLoss1_Input.setText(null);
            NLoss2_Input.setText(null);
            NLoss3_Input.setText(null);
            NLoss4_Input.setText(null);
            PI_Input.setText(null);
        });

        searchButtonsPanel.add(searchButton, "wrap");
        searchButtonsPanel.add(clearButton, "wrap");
        searchButtonsPanel.add(exportButton, "wrap");
        searchButtonsPanel.add(uploadButton, "wrap");
    }

    public JScrollPane createLipidScrollPane() {
        Set<Double> neutralLossAssociatedIonsInput = new LinkedHashSet<>();
        JTextField[] neutralLossInputFields = {NLoss1_Input, NLoss2_Input, NLoss3_Input, NLoss4_Input};
        for (JTextField field : neutralLossInputFields) {
            addNeutralLossInput(neutralLossAssociatedIonsInput, field);
        }

        try {
            if (checkIfTextFieldIsNotEmpty(PI_Input.getText())) {
                Set<MSLipid> lipidSet = database.getAllLipidsFromDatabase(
                        getSelectedRadioButton(),
                        Double.parseDouble(PI_Input.getText()),
                        neutralLossAssociatedIonsInput
                );
                lipidData = new String[lipidSet.size()][7];
                createLipidDataForTable(lipidSet, lipidData);
            }
        } catch (SQLException | FattyAcidCreation_Exception | InvalidFormula_Exception exception) {
            JOptionPane.showMessageDialog(null, "An error occurred while searching the database. " +
                    "Please review data inputs and try again.");
        }

        tableModel = new DefaultTableModel(lipidData, tableTitles) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        configureTable(lipidData);
        return new JScrollPane(table);
    }

    private void addNeutralLossInput(Set<Double> inputSet, JTextField textField) {
        if (checkIfTextFieldIsNotEmpty(textField.getText())) {
            try {
                inputSet.add(Double.parseDouble(textField.getText()));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number. Remember values " +
                        "should maintain the international standard for digits. For example, " +
                        "824.0293.");
            }
        }
    }

    public static void createLipidDataForTable(Set<MSLipid> lipidSet, String[][] lipidData) {
        int i = 0;
        for (MSLipid lipid : lipidSet) {
            lipidData[i][0] = lipid.getCasID();
            lipidData[i][1] = lipid.getCompoundName();
            lipidData[i][2] = lipid.calculateSpeciesShorthand(lipid);
            lipidData[i][3] = lipid.getFormula();
            lipidData[i][4] = String.valueOf(lipid.getMass());
            lipidData[i][5] = "[M+NH4]+";
            lipidData[i][6] = String.valueOf(lipid.calculateMZWithAdduct("[M+NH3]+", 1));
            i++;
        }
    }

    public void configureTable(String[][] data) {
        tableModel = new DefaultTableModel(data, tableTitles) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel) {
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

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (table.getSelectedColumn() == 0) {
                    try {
                        URL url = new URL("https://webbook.nist.gov/cgi/cbook.cgi?ID=" + table.getValueAt(table.getSelectedRow(),
                                table.getSelectedColumn()) + "&Units=SI");
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
            }
        });

        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(175);
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
    }

    public void createInputPanel() {
        inputSubpanel.setPreferredSize(new Dimension(1000, 300));
        inputSubpanel.setSize(new Dimension(1000, 300));
        inputSubpanel.setMinimumSize(new Dimension(1000, 300));
        inputSubpanel.setMaximumSize(new Dimension(1000, 300));

        inputSubpanel.setLayout(new MigLayout("", "[grow, fill]15[grow, fill]15", "15[grow, fill]10[grow, fill]15[grow, fill]10[grow,fill]15"));
        inputSubpanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        inputSubpanel.setBackground(Color.WHITE);

        NLoss1_Input.setColumns(30);
        configureComponents(NLoss1_Input);
        NLoss1_Input.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        NLoss1_Input.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "  Neutral Loss Associated Ion 1");
        NLoss2_Input.setColumns(30);
        configureComponents(NLoss2_Input);
        NLoss2_Input.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        NLoss2_Input.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "  Neutral Loss Associated Ion 2 (optional)");
        NLoss3_Input.setColumns(30);
        configureComponents(NLoss3_Input);
        NLoss3_Input.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        NLoss3_Input.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "  Neutral Loss Associated Ion 3 (optional)");
        NLoss4_Input.setColumns(30);
        configureComponents(NLoss4_Input);
        NLoss4_Input.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        NLoss4_Input.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "  Neutral Loss Associated Ion 4 (optional)");
        PI_Input.setColumns(15);
        configureComponents(PI_Input);
        PI_Input.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        PI_Input.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "  Precursor Ion, m/z");
        configureComponents(ionComboBox);
        ionComboBox.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        ionComboBox.setToolTipText("Choose the list of adducts based on charge.");
        ionComboBox.addActionListener(e -> updateListOfAdductsAccordingToCharge(Objects.requireNonNull(ionComboBox.getSelectedItem()).toString()));
        configureTextComponents(precursorIonLabel);
        configureTextComponents(neutralLossesLabel);

        inputSubpanel.add(precursorIonLabel, "wrap");
        inputSubpanel.add(PI_Input);
        inputSubpanel.add(ionComboBox, "wrap, gapleft 75");
        inputSubpanel.add(neutralLossesLabel, "wrap, gaptop 15");
        inputSubpanel.add(NLoss1_Input, "wrap, span 2");
        inputSubpanel.add(NLoss2_Input, "wrap, span 2");
        inputSubpanel.add(NLoss3_Input, "wrap, span 2");
        inputSubpanel.add(NLoss4_Input, "span 2");
    }

    public void createRadioButtons() {
        radioButtonsPanel.setBackground(Color.WHITE);
        radioButtonsPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        radioButtonsPanel.setLayout(new MigLayout());

        JLabel radioButtonsLabel = new JLabel("Lipid Head Groups");
        configureTextComponents(radioButtonsLabel);

        JRadioButton buttonCE = new JRadioButton(" CE");
        JRadioButton buttonCER = new JRadioButton(" CER");
        JRadioButton buttonDG = new JRadioButton(" DG");
        JRadioButton buttonMG = new JRadioButton(" MG");
        JRadioButton buttonPA = new JRadioButton(" PA");
        JRadioButton buttonPC = new JRadioButton(" PC");
        JRadioButton buttonPE = new JRadioButton(" PE");
        JRadioButton buttonPI = new JRadioButton(" PI");
        JRadioButton buttonPG = new JRadioButton(" PG");
        JRadioButton buttonPS = new JRadioButton(" PS");
        JRadioButton buttonSM = new JRadioButton(" SM");
        JRadioButton buttonTG = new JRadioButton(" TG", true);
        JRadioButton buttonCL = new JRadioButton(" CL");

        configureTextComponents(buttonCE);
        configureTextComponents(buttonCER);
        configureTextComponents(buttonDG);
        configureTextComponents(buttonMG);
        configureTextComponents(buttonPA);
        configureTextComponents(buttonPC);
        configureTextComponents(buttonPE);
        configureTextComponents(buttonPI);
        configureTextComponents(buttonPG);
        configureTextComponents(buttonPS);
        configureTextComponents(buttonSM);
        configureTextComponents(buttonTG);
        configureTextComponents(buttonCL);

        buttonGroup.add(buttonCE);
        buttonGroup.add(buttonCER);
        buttonGroup.add(buttonDG);
        buttonGroup.add(buttonMG);
        buttonGroup.add(buttonPA);
        buttonGroup.add(buttonPC);
        buttonGroup.add(buttonPE);
        buttonGroup.add(buttonPI);
        buttonGroup.add(buttonPG);
        buttonGroup.add(buttonPS);
        buttonGroup.add(buttonSM);
        buttonGroup.add(buttonTG);
        buttonGroup.add(buttonCL);

        radioButtonsPanel.add(radioButtonsLabel, "wrap");
        radioButtonsPanel.add(buttonCE, "wrap, gapleft 10");
        radioButtonsPanel.add(buttonCER, "wrap, gapleft 10");
        radioButtonsPanel.add(buttonDG, "wrap, gapleft 10");
        radioButtonsPanel.add(buttonMG, "wrap, gapleft 10");
        radioButtonsPanel.add(buttonPA, "wrap, gapleft 10");
        radioButtonsPanel.add(buttonPC, "wrap, gapleft 10");
        radioButtonsPanel.add(buttonPE, "wrap, gapleft 10");
        radioButtonsPanel.add(buttonPI, "wrap, gapleft 10");
        radioButtonsPanel.add(buttonPG, "wrap, gapleft 10");
        radioButtonsPanel.add(buttonPS, "wrap, gapleft 10");
        radioButtonsPanel.add(buttonSM, "wrap, gapleft 10");
        radioButtonsPanel.add(buttonTG, "wrap, gapleft 10");
        radioButtonsPanel.add(buttonCL, "wrap, gapleft 10");
    }

    public void createAdductsPanel() {
        configureTextComponents(adductsLabel);
        adductsPanel.setLayout(new MigLayout("", "[]", ""));
        adductsPanel.setBackground(Color.WHITE);
        adductsPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        adductsPanel.setPreferredSize(new Dimension(350, 300));
        adductsPanel.setSize(new Dimension(350, 300));
        adductsPanel.setMinimumSize(new Dimension(350, 300));
        adductsPanel.setMaximumSize(new Dimension(350, 300));

        adductsPanel.add(adductsLabel, "wrap, gapbottom 4");
        updateAdductPanel(Adduct.getPositiveAdducts());
        adductsPanel.setVisible(true);
    }

    public static void configureComponents(Component component) {
        component.setFont(new Font("Arial", Font.BOLD, 16));
        component.setBackground(new Color(231, 242, 245));
        component.setForeground(new Color(65, 114, 159));
    }

    public static void configureTextComponents(Component component) {
        component.setFont(new Font("Arial", Font.BOLD, 17));
        component.setForeground(new Color(65, 114, 159));
    }

    public void updateListOfAdductsAccordingToCharge(String charge) {
        if (charge.equals("   View Positive Adducts  ")) {
            String[] string = Adduct.getPositiveAdducts();
            updateAdductPanel(string);
        } else if (charge.equals("   View Negative Adducts  ")) {
            String[] string = Adduct.getNegativeAdducts();
            updateAdductPanel(string);
        }
        adductsPanel.revalidate();
        adductsPanel.repaint();
    }

    public void updateAdductPanel(String[] adducts) {
        for (Component component : adductsPanel.getComponents()) {
            if (component instanceof JCheckBox) {
                adductsPanel.remove(component);
            }
        }
        int i = 0;
        for (String adduct : adducts) {
            JCheckBox checkBox = new JCheckBox(adduct);
            configureTextComponents(checkBox);
            if (i % 2 == 0) {
                adductsPanel.add(checkBox, "gapleft 10, gaptop 5, gapbottom 5");
            } else {
                adductsPanel.add(checkBox, "wrap, gapleft 10, gaptop 5, gapbottom 5");
            }
            i++;
        }
        adductsPanel.revalidate();
        adductsPanel.repaint();
    }

    public boolean checkIfTextFieldIsNotEmpty(String textFieldInput) {
        return !textFieldInput.isEmpty();
    }

    public LipidType getSelectedRadioButton() {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements(); ) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return LipidType.valueOf(button.getText().replaceAll(" ", ""));
            }
        }
        return null;
    }
}


package org.example.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;
import org.example.databases.Database;
import org.example.databases.QueryParameters;
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
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class MainPageUI extends JPanel {
    private final JButton searchButton;
    private final JButton exportButton;
    private final JButton uploadButton;
    private final JButton clearButton;
    private final JButton getTemplateButton;

    private final JPanel tablePanel;
    private final JPanel searchButtonsPanel;
    private final JPanel lipidHeadGroupsPanel;
    private final JPanel inputSubpanel;
    public JPanel adductsPanel;

    public JTextField NLoss1_Input;
    public JTextField NLoss2_Input;
    public JTextField NLoss3_Input;
    public JTextField NLoss4_Input;
    public JTextField PI_Input;
    public JTextField tolerancePI_Input;
    public JTextField toleranceNL_Input;

    private final JLabel precursorIonLabel;
    private final JLabel neutralLossesLabel;
    private final JLabel adductsLabel;
    private final JLabel tolerancePILabel;
    private final JLabel toleranceNLLabel;

    private JTable table = null;
    private DefaultTableModel tableModel = null;
    private String[] tableTitles = null;
    private String[][] lipidData = null;
    private static ButtonGroup buttonGroup;
    private final JComboBox<String> ionComboBox;
    private static List<JCheckBox> checkBoxList;

    public MainPageUI() throws SQLException, InvalidFormula_Exception, FattyAcidCreation_Exception {
        FlatLightLaf.setup();
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        searchButton = new JButton("  Begin Search");
        exportButton = new JButton("  Export to CSV");
        uploadButton = new JButton("  Batch Processing");
        clearButton = new JButton("  Clear Input");
        getTemplateButton = new JButton("  File Template");
        tablePanel = new JPanel();
        searchButtonsPanel = new JPanel();
        lipidHeadGroupsPanel = new JPanel();
        inputSubpanel = new JPanel();
        NLoss1_Input = new JTextField();
        NLoss2_Input = new JTextField();
        NLoss3_Input = new JTextField();
        NLoss4_Input = new JTextField();
        PI_Input = new JTextField();
        tolerancePI_Input = new JTextField();
        toleranceNL_Input = new JTextField();

        precursorIonLabel = new JLabel("    Precursor Ion");
        neutralLossesLabel = new JLabel("    Neutral Losses");
        tolerancePILabel = new JLabel("    Precursor Ion Tolerance (ppm)");
        toleranceNLLabel = new JLabel("    Neutral Loss Tolerance (ppm)");
        adductsLabel = new JLabel("    Adducts");
        adductsPanel = new JPanel();
        ionComboBox = new JComboBox<>(new String[]{"   View Positive Adducts  ", "   View Negative Adducts  "});
        new Database();
        buttonGroup = new ButtonGroup();
        checkBoxList = new ArrayList<>();
        setLayout(new MigLayout("", "[grow, fill]25[grow, fill]25[grow, fill]", "[grow, fill]25[grow, fill]"));
        setBackground(new Color(195, 224, 229));
        createTable();
        createLipidHeadGroupsPanel();
        createInputPanel();
        createButtons();
        createAdductsPanel();

        add(tablePanel, "span 2");
        add(lipidHeadGroupsPanel, "wrap");
        add(inputSubpanel);
        add(adductsPanel);
        add(searchButtonsPanel);
        setVisible(true);
    }

    public void createTable() {
        tablePanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setPreferredSize(new Dimension(1250, 500));
        tablePanel.setLayout(new MigLayout("", "[grow, fill]", "[grow, fill]"));
        tableTitles = new String[]{"Compound Name", "Species Shorthand", "Compound Formula", "Compound Mass", "Adduct", "m/z", "CMM ID"};

        DefaultTableModel model = new DefaultTableModel(tableTitles, 0);
        table = new JTable(model);
        JScrollPane jScrollPane = new JScrollPane(table);
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setForeground(new Color(65, 114, 159));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        table.getColumnModel().getColumn(0).setPreferredWidth(175);
        table.getColumnModel().getColumn(1).setPreferredWidth(45);
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
            List<String> checkBoxesForSearch = getAdductsChosen();
            if (checkBoxesForSearch.isEmpty()) {
                JOptionPane.showMessageDialog(tablePanel, "Please select at least one adduct");
            } else {
                tablePanel.add(lipidScrollPane, "center, grow");
                tablePanel.revalidate();
                tablePanel.repaint();
            }
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
                    // todo: idk if this works
                    // FileDialog folderDirectory = new FileDialog(new Frame(), "Choose a folder to store files.", FileDialog.LOAD);
                    // , String.valueOf(folderDirectory.getDirectory())
                    List<String> adducts = getAdductsChosen();
                    for (String adduct : adducts) {
                        csvUtils.readCSVAndWriteResultsToFile(new File(fileDirectory + fileName), adduct);
                    }
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

        configureComponents(getTemplateButton);
        getTemplateButton.setBackground(Color.WHITE);
        getTemplateButton.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        getTemplateButton.setIcon(new ImageIcon("src/main/resources/Template_Icon.png"));
        getTemplateButton.setBorder(new LineBorder(Color.white));
        getTemplateButton.setHorizontalAlignment(SwingConstants.LEFT);
        getTemplateButton.addActionListener(e -> {
            // todo
            JOptionPane.showMessageDialog(null, "Not sure what the template should look like yet...");
        });

        searchButtonsPanel.add(searchButton, "wrap");
        searchButtonsPanel.add(clearButton, "wrap");
        searchButtonsPanel.add(exportButton, "wrap");
        searchButtonsPanel.add(uploadButton, "wrap");
        searchButtonsPanel.add(getTemplateButton, "wrap");
    }

    public JScrollPane createLipidScrollPane() {
        Set<Double> neutralLossAssociatedIonsInput = new LinkedHashSet<>();
        JTextField[] neutralLossInputFields = {NLoss1_Input, NLoss2_Input, NLoss3_Input, NLoss4_Input};
        for (JTextField field : neutralLossInputFields) {
            addNeutralLossInput(neutralLossAssociatedIonsInput, field);
        }

        try {
            if (checkIfTextFieldIsNotEmpty(PI_Input.getText())) {
                createLipidDataForTable(neutralLossAssociatedIonsInput);
            }
        } catch (SQLException | FattyAcidCreation_Exception | InvalidFormula_Exception |
                 NullPointerException exception) {
            JOptionPane.showMessageDialog(null, "No results found.");
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
                JOptionPane.showMessageDialog(null, "Please enter a valid number. All values " +
                        "should maintain the international standard for digits. For example, " +
                        "824.0293.");
            }
        }
    }

    public void createLipidDataForTable(Set<Double> neutralLossAssociatedIonsInput) throws SQLException, InvalidFormula_Exception, FattyAcidCreation_Exception {
        int i;
        DecimalFormat numberFormat = new DecimalFormat("#.0000");
        Set<MSLipid> lipidSet;
        List<String> adducts = getAdductsChosen();

        List<String[]> finalLipidDataList = new ArrayList<>();

        for (String adduct : adducts) {
            QueryParameters queryParameters = new QueryParameters();
            lipidSet = queryParameters.findLipidsInDatabase(getSelectedRadioButton(), Double.parseDouble(PI_Input.getText()),
                    neutralLossAssociatedIonsInput, adduct);

            String[][] localLipidData = new String[lipidSet.size()][7];
            i = 0;

            for (MSLipid lipid : lipidSet) {
                localLipidData[i][0] = lipid.getCompoundName();
                localLipidData[i][1] = lipid.calculateSpeciesShorthand(lipid);
                localLipidData[i][2] = lipid.getFormula();
                localLipidData[i][3] = numberFormat.format(lipid.getMass());
                localLipidData[i][4] = adduct;
                localLipidData[i][5] = lipid.calculateMZWithAdduct(adduct, 1);
                localLipidData[i][6] = lipid.getCompoundID();
                finalLipidDataList.add(localLipidData[i]);
                i++;
            }
        }
        lipidData = new String[finalLipidDataList.size()][7];
        for (int j = 0; j < finalLipidDataList.size(); j++) {
            lipidData[j] = finalLipidDataList.get(j);
        }
        if (lipidData.length == 0) {
            JOptionPane.showMessageDialog(null, "An error occurred while searching the database. " +
                    "Please ensure that the chosen adduct and lipid group are correct.");
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
                if (table.getSelectedColumn() == 7) {
                    try {
                        URL url = new URL("https://ceumass.eps.uspceu.es/mediator/api/v3/compounds/" + table.getValueAt(table.getSelectedRow(),
                                table.getSelectedColumn()));
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
        table.getColumnModel().getColumn(0).setPreferredWidth(175);
        table.getColumnModel().getColumn(1).setPreferredWidth(45);
        table.setForeground(new Color(65, 114, 159));
        table.setFont(new Font("Arial", Font.BOLD, 15));
        table.getTableHeader().setReorderingAllowed(false);
        table.setModel(tableModel);
        table.setRowHeight(table.getRowHeight() + 15);
        table.setBorder(new LineBorder(Color.WHITE, 1));
    }

    public void createInputPanel() {
        inputSubpanel.setPreferredSize(new Dimension(1050, 340));
        inputSubpanel.setMaximumSize(new Dimension(1050, 340));

        inputSubpanel.setLayout(new MigLayout("", "[grow, fill]50[grow, fill]15", "15[grow, fill]10[grow, fill]15[grow, fill]10[grow,fill]15"));
        inputSubpanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        inputSubpanel.setBackground(Color.WHITE);

        NLoss1_Input.setColumns(15);
        configureComponents(NLoss1_Input);
        NLoss1_Input.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        NLoss1_Input.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "  Neutral Loss Associated Ion 1");
        NLoss2_Input.setColumns(15);
        configureComponents(NLoss2_Input);
        NLoss2_Input.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        NLoss2_Input.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "  Neutral Loss Associated Ion 2 (optional)");
        NLoss3_Input.setColumns(15);
        configureComponents(NLoss3_Input);
        NLoss3_Input.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        NLoss3_Input.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "  Neutral Loss Associated Ion 3 (optional)");
        NLoss4_Input.setColumns(15);
        configureComponents(NLoss4_Input);
        NLoss4_Input.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        NLoss4_Input.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "  Neutral Loss Associated Ion 4 (optional)");
        PI_Input.setColumns(15);
        configureComponents(PI_Input);
        PI_Input.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        PI_Input.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "  Precursor Ion, m/z");
        tolerancePI_Input.setColumns(15);
        configureComponents(tolerancePI_Input);
        tolerancePI_Input.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        tolerancePI_Input.setText("20.0");
        toleranceNL_Input.setColumns(15);
        configureComponents(toleranceNL_Input);
        toleranceNL_Input.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        toleranceNL_Input.setText("30.0");

        configureTextComponents(precursorIonLabel);
        configureTextComponents(neutralLossesLabel);
        configureTextComponents(tolerancePILabel);
        configureTextComponents(toleranceNLLabel);

        inputSubpanel.add(precursorIonLabel);
        inputSubpanel.add(tolerancePILabel, "wrap");
        inputSubpanel.add(PI_Input);
        inputSubpanel.add(tolerancePI_Input, "wrap");
        inputSubpanel.add(neutralLossesLabel);
        inputSubpanel.add(toleranceNLLabel, "wrap, gaptop 15");
        inputSubpanel.add(NLoss1_Input);
        inputSubpanel.add(toleranceNL_Input, "wrap");
        inputSubpanel.add(NLoss2_Input, "wrap");
        inputSubpanel.add(NLoss3_Input, "wrap");
        inputSubpanel.add(NLoss4_Input);
    }

    public void createLipidHeadGroupsPanel() {
        lipidHeadGroupsPanel.setBackground(Color.WHITE);
        lipidHeadGroupsPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        lipidHeadGroupsPanel.setLayout(new MigLayout());

        JLabel lipidHeadGroupsLabel = new JLabel("Lipid Head Groups");
        configureTextComponents(lipidHeadGroupsLabel);

        JCheckBox buttonCE = new JCheckBox(" CE");
        JCheckBox buttonCER = new JCheckBox(" CER");
        JCheckBox buttonDG = new JCheckBox(" DG");
        JCheckBox buttonMG = new JCheckBox(" MG");
        JCheckBox buttonPA = new JCheckBox(" PA");
        JCheckBox buttonPC = new JCheckBox(" PC");
        JCheckBox buttonPE = new JCheckBox(" PE");
        JCheckBox buttonPI = new JCheckBox(" PI");
        JCheckBox buttonPG = new JCheckBox(" PG");
        JCheckBox buttonPS = new JCheckBox(" PS");
        JCheckBox buttonSM = new JCheckBox(" SM");
        JCheckBox buttonTG = new JCheckBox(" TG");
        JCheckBox buttonCL = new JCheckBox(" CL");
        JCheckBox buttonAll = new JCheckBox(" Select All", true);

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
        configureTextComponents(buttonAll);

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
        buttonGroup.add(buttonAll);

        lipidHeadGroupsPanel.add(lipidHeadGroupsLabel, "wrap");
        lipidHeadGroupsPanel.add(buttonCE, "wrap, gapleft 15, gaptop 2");
        lipidHeadGroupsPanel.add(buttonDG, "wrap, gapleft 15, gaptop 2");
        lipidHeadGroupsPanel.add(buttonMG, "wrap, gapleft 15, gaptop 2");
        lipidHeadGroupsPanel.add(buttonPA, "wrap, gapleft 15, gaptop 2");
        lipidHeadGroupsPanel.add(buttonPC, "wrap, gapleft 15, gaptop 2");
        lipidHeadGroupsPanel.add(buttonPE, "wrap, gapleft 15, gaptop 2");
        lipidHeadGroupsPanel.add(buttonPI, "wrap, gapleft 15, gaptop 2");
        lipidHeadGroupsPanel.add(buttonPG, "wrap, gapleft 15, gaptop 2");
        lipidHeadGroupsPanel.add(buttonPS, "wrap, gapleft 15, gaptop 2");
        lipidHeadGroupsPanel.add(buttonTG, "wrap, gapleft 15, gaptop 2");
        lipidHeadGroupsPanel.add(buttonCL, "wrap, gapleft 15, gaptop 2");
        lipidHeadGroupsPanel.add(buttonAll, "wrap, gapleft 15, gaptop 2");
    }

    public void createAdductsPanel() {
        configureTextComponents(adductsLabel);
        adductsPanel.setLayout(new MigLayout("", "[]", ""));
        adductsPanel.setBackground(Color.WHITE);
        adductsPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        adductsPanel.setPreferredSize(new Dimension(300, 340));
        adductsPanel.setMaximumSize(new Dimension(300, 340));
        adductsPanel.add(adductsLabel, "wrap, gapbottom 4");
        configureComponents(ionComboBox);
        ionComboBox.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        ionComboBox.setToolTipText("Choose the list of adducts based on charge.");
        ionComboBox.addActionListener(e -> updateListOfAdductsAccordingToCharge(Objects.requireNonNull(ionComboBox.getSelectedItem()).toString()));
        adductsPanel.add(ionComboBox, "wrap, span 2, gapbottom 10");
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
        for (JCheckBox checkBox : checkBoxList) {
            checkBox.setSelected(false);
        }
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
        for (String adduct : adducts) {
            JCheckBox checkBox = new JCheckBox(adduct);
            checkBoxList.add(checkBox);
            configureTextComponents(checkBox);
            checkBox.setFont(new Font("Arial", Font.BOLD, 16));
            adductsPanel.add(checkBox, "gapleft 10, gaptop 5, wrap");
        }
        JCheckBox checkBox = new JCheckBox("Select All");
        configureTextComponents(checkBox);
        adductsPanel.add(checkBox, "gapleft 10, gaptop 5, gapbottom 5");
        adductsPanel.revalidate();
        adductsPanel.repaint();
    }

    public boolean checkIfTextFieldIsNotEmpty(String textFieldInput) {
        return !textFieldInput.isEmpty();
    }

    public static LipidType getSelectedRadioButton() {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements(); ) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return LipidType.valueOf(button.getText().replaceAll(" ", ""));
            }
        }
        return null;
    }

    public static List<String> getAdductsChosen() {
        List<String> adducts = new ArrayList<>();
        for (JCheckBox checkBox : checkBoxList) {
            if (checkBox.isSelected()) {
                adducts.add(checkBox.getText());
            }
        }
        // TODO: ADD IF(SELECTALL) { }
        return adducts;
    }
}


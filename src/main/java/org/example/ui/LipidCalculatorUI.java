package org.example.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;
import org.example.adduct.Transformer;
import org.example.databases.Database;
import org.example.databases.QueryParameters;
import org.example.domain.*;
import org.example.exceptions.FattyAcidCreation_Exception;
import org.example.exceptions.InvalidFormula_Exception;
import org.example.utils.CSVUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

public class LipidCalculatorUI extends JPanel {
    private final JButton searchButton;
    private final JButton exportButton;
    private final JButton uploadButton;
    private final JButton clearButton;
    private final JButton getTemplateButton;

    private final JPanel tablePanel;
    private final JPanel searchButtonsPanel;
    private final JPanel lipidHeadGroupsPanel;
    private final JPanel inputSubpanel;
    public static JPanel adductsPanel;

    public JTextArea neutralLossIonsTextPane;
    public JTextField precursorIonTextField;
    public JTextField toleranceOfPITextField;
    public JTextField toleranceOfNLTextField;

    private final JLabel precursorIonLabel;
    private final JLabel neutralLossesLabel;
    private final JLabel tolerancePILabel;
    private final JLabel toleranceNLLabel;
    private final JLabel tableLabel;

    private JTable table = null;
    private DefaultTableModel tableModel = null;
    private String[] tableTitles = null;
    private String[][] lipidData = null;
    private static List<JCheckBox> lipidHeadGroupsCheckBoxList;
    private final JComboBox<String> ionComboBox;
    private static List<JCheckBox> adductCheckBoxList;

    public LipidCalculatorUI() throws SQLException, InvalidFormula_Exception, FattyAcidCreation_Exception {
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
        tablePanel = new JPanel(new MigLayout("", "15[grow]15", "15[grow]15[grow]15[grow]15"));
        searchButtonsPanel = new JPanel();
        lipidHeadGroupsPanel = new JPanel();
        inputSubpanel = new JPanel();
        neutralLossIonsTextPane = new JTextArea(9, 10);
        precursorIonTextField = new JTextField();
        toleranceOfPITextField = new JTextField();
        toleranceOfNLTextField = new JTextField();

        precursorIonLabel = new JLabel("    Precursor Ion");
        neutralLossesLabel = new JLabel("    Neutral Loss Associated Ions");
        tolerancePILabel = new JLabel("    Precursor Ion Tolerance (ppm)");
        toleranceNLLabel = new JLabel("    Neutral Loss Tolerance (ppm)");
        tableLabel = new JLabel("   Lipid Results");
        adductsPanel = new JPanel();
        ionComboBox = new JComboBox<>(new String[]{"   View Positive Adducts  ", "   View Negative Adducts  "});
        new Database();
        lipidHeadGroupsCheckBoxList = new ArrayList<>();
        adductCheckBoxList = new ArrayList<>();
        setLayout(new MigLayout("", "[grow, fill]25[grow, fill]25[grow, fill]25[grow, fill]", "25[grow, fill]25[grow, fill]25"));
        setBackground(new Color(195, 224, 229));
        createTable();
        createLipidHeadGroupsPanel();
        createInputPanel();
        createButtons();
        createAdductsPanel();

        add(tablePanel, "span 4, wrap");
        add(inputSubpanel);
        add(adductsPanel);
        add(lipidHeadGroupsPanel);
        add(searchButtonsPanel);
        setVisible(true);
    }

    public void createTable() {
        configureTextComponents(tableLabel);
        tablePanel.add(tableLabel, "wrap");
        tablePanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setPreferredSize(new Dimension(1250, 500));

        tableTitles = new String[]{"Compound Name", "Species Shorthand", "Compound Formula", "Compound Mass", "Adduct", "m/z", "CMM ID", "Adduct Pattern"};
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

        JPanel emptyTablePanel = new JPanel(new MigLayout("", "15[grow]15", "15[]15[]15[]15"));
        emptyTablePanel.setBackground(Color.WHITE);

        JLabel emptyIconLabel = new JLabel();
        emptyIconLabel.setIcon(new ImageIcon("src/main/resources/EmptyTable_Icon.png"));
        emptyIconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel noResultsLabel = new JLabel("No results yet...");
        configureTextComponents(noResultsLabel);
        noResultsLabel.setFont(new Font("Arial", Font.BOLD, 20));
        noResultsLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel tryInputtingInfoLabel = new JLabel("Start by inputting some data.");
        tryInputtingInfoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        configureTextComponents(tryInputtingInfoLabel);
        tryInputtingInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        emptyTablePanel.add(emptyIconLabel, "align center, wrap");
        emptyTablePanel.add(noResultsLabel, "align center, wrap");
        emptyTablePanel.add(tryInputtingInfoLabel, "align center, wrap");

        tablePanel.add(jScrollPane, "cell 0 1, align center, growx, wrap");
        tablePanel.add(emptyTablePanel, "cell 0 2, align center, growx, gapbottom 60");
    }

    public void createButtons() {
        searchButtonsPanel.setBackground(Color.WHITE);
        searchButtonsPanel.setPreferredSize(new Dimension(400, 340));
        searchButtonsPanel.setMaximumSize(new Dimension(400, 340));
        searchButtonsPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        searchButtonsPanel.setLayout(new MigLayout("", "25[grow, fill]25", "25[grow, fill]25[grow, fill]25"));

        configureComponents(searchButton);
        searchButton.setBackground(Color.WHITE);
        searchButton.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        searchButton.setIcon(new ImageIcon("src/main/resources/Search_Icon.png"));
        searchButton.setBorder(new LineBorder(Color.white));
        searchButton.setHorizontalAlignment(SwingConstants.LEFT);
        searchButton.addActionListener(_ -> {
            tablePanel.removeAll();
            tablePanel.add(tableLabel, "wrap");
            configureTextComponents(tableLabel);
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

        configureComponents(clearButton);
        clearButton.setBackground(Color.WHITE);
        clearButton.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        clearButton.setIcon(new ImageIcon("src/main/resources/Clear_Icon.png"));
        clearButton.setBorder(new LineBorder(Color.white));
        clearButton.setHorizontalAlignment(SwingConstants.LEFT);
        clearButton.addActionListener(_ -> {
            neutralLossIonsTextPane.setText(null);
            precursorIonTextField.setText(null);
        });

        configureComponents(exportButton);
        exportButton.setBackground(Color.WHITE);
        exportButton.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        exportButton.setIcon(new ImageIcon("src/main/resources/Download_Icon.png"));
        exportButton.setBorder(new LineBorder(Color.white));
        exportButton.setHorizontalAlignment(SwingConstants.LEFT);
        exportButton.addActionListener(_ -> {
            int choice = JOptionPane.showConfirmDialog(null,
                    "Do you wish to export this information to CSV format?",
                    "Export to CSV", JOptionPane.YES_NO_CANCEL_OPTION);
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
        uploadButton.addActionListener(_ -> {
            CSVUtils csvUtils = new CSVUtils();
            FileDialog fileDialog = new FileDialog(new Frame(), "Choose a file in CSV format.", FileDialog.LOAD);
            fileDialog.setVisible(true);
            String fileName = fileDialog.getFile();
            String fileDirectory = fileDialog.getDirectory();
            if (fileName != null && fileDirectory != null) {
                try {
                    csvUtils.readCSVAndWriteResultsToFile(new File(fileDirectory + fileName), getAdductsChosen(),
                            getLipidHeadGroupsChosen());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        configureComponents(getTemplateButton);
        getTemplateButton.setBackground(Color.WHITE);
        getTemplateButton.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        getTemplateButton.setIcon(new ImageIcon("src/main/resources/Template_Icon.png"));
        getTemplateButton.setBorder(new LineBorder(Color.white));
        getTemplateButton.setHorizontalAlignment(SwingConstants.LEFT);
        getTemplateButton.addActionListener(_ -> {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            try (InputStream inputStream = classLoader.getResourceAsStream("Batch Processing Template.csv")) {

                if (inputStream == null) {
                    System.out.println("File not found in resources!");
                    return;
                }

                File tempFile = Files.createTempFile("Template", ".csv").toFile();
                tempFile.deleteOnExit();

                try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }

                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.open(tempFile);
                } else {
                    System.out.println("Desktop is not supported.");
                }

            } catch (Exception exception) {
                JOptionPane.showMessageDialog(null, "Desktop is not supported.");
            }
        });

        searchButtonsPanel.add(searchButton, "gaptop 10, gapleft 10, wrap");
        searchButtonsPanel.add(clearButton, "gapleft 10, wrap");
        searchButtonsPanel.add(exportButton, "gapleft 10, wrap");
        searchButtonsPanel.add(uploadButton, "gapleft 10, wrap");
        searchButtonsPanel.add(getTemplateButton, "gapleft 10, wrap");
    }

    public JScrollPane createLipidScrollPane() {
        Set<Double> neutralLossAssociatedIonsInput = new LinkedHashSet<>();

        try {
            if (neutralLossIonsTextPane != null) {
                String[] neutralLosses = neutralLossIonsTextPane.getText().replaceAll(" ", "").split("[\\s,;\\t]");
                for (String neutralLoss : neutralLosses) {
                    neutralLossAssociatedIonsInput.add(Double.parseDouble(neutralLoss));
                }
            }
            if (checkIfTextFieldIsNotEmpty(precursorIonTextField.getText())) {
                createLipidDataForTable(neutralLossAssociatedIonsInput);
            }
        } catch (SQLException | FattyAcidCreation_Exception | InvalidFormula_Exception |
                 NullPointerException | NumberFormatException exception) {
            JOptionPane.showMessageDialog(null, "Error in searching database.");
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

    public void createLipidDataForTable(Set<Double> neutralLossAssociatedIonsInput)
            throws SQLException, InvalidFormula_Exception, FattyAcidCreation_Exception {
        DecimalFormat numberFormat = new DecimalFormat("#.0000");
        List<String> adducts = getAdductsChosen();
        List<String[]> finalLipidDataList = new ArrayList<>();
        boolean hasResults = false;

        for (String adduct : adducts) {
            QueryParameters queryParameters = new QueryParameters();
            Set<MSLipid> finalLipids = new HashSet<>();
            for (LipidType lipidType : getLipidHeadGroupsChosen()) {
                Set<MSLipid> lipidSet = queryParameters.returnSetOfLipidsFoundInDatabase(
                        lipidType,
                        Double.parseDouble(precursorIonTextField.getText()),
                        neutralLossAssociatedIonsInput,
                        adduct
                );
                if (!(lipidSet == null)) {
                    finalLipids.addAll(lipidSet);
                }
            }

            if (finalLipids.isEmpty()) {
                System.out.println("No results found for adduct: " + adduct);
                continue;
            }

            hasResults = true;
            int i = 0;
            String[][] localLipidData = new String[finalLipids.size()][8];

            for (MSLipid lipid : finalLipids) {
                localLipidData[i][0] = lipid.getCompoundName();
                localLipidData[i][1] = lipid.calculateSpeciesShorthand(lipid);
                localLipidData[i][2] = lipid.getFormula();
                localLipidData[i][3] = numberFormat.format(lipid.getMass());
                localLipidData[i][4] = adduct;
                localLipidData[i][5] = numberFormat.format(Transformer.getMassOfAdductFromMonoMass(lipid.getMass(), adduct));
                localLipidData[i][6] = lipid.getCompoundID();
                localLipidData[i][7] = "View";
                finalLipidDataList.add(localLipidData[i]);
                i++;
            }
        }

        lipidData = new String[finalLipidDataList.size()][8];
        for (int j = 0; j < finalLipidDataList.size(); j++) {
            lipidData[j] = finalLipidDataList.get(j);
        }

        if (!hasResults) {
            JOptionPane.showMessageDialog(null, "No results found for any chosen adduct or lipid group. " +
                    "Please verify your input and try again.");
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
                if (table.getSelectedColumn() == 6) {
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

                if (table.getSelectedColumn() == 7) {
                    SidePanelUI.updateUI(SidePanelUI.patternRecognitionUI);
                    PatternRecognitionUI.lipidTextField.setText(String.valueOf(table.getModel().getValueAt(table.getSelectedRow(), 0)));
                    for (int i = 0; i < 5; i++) {
                        PatternRecognitionUI.adductCheckBoxList.get(i).setSelected(true);
                    }
                }
            }
        });

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
        inputSubpanel.setPreferredSize(new Dimension(800, 340));
        inputSubpanel.setMaximumSize(new Dimension(800, 340));
        inputSubpanel.setLayout(new MigLayout("", "[]50[]", "[][]"));
        inputSubpanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        inputSubpanel.setBackground(Color.WHITE);

        JPanel NLIonsPanel = new JPanel();
        NLIonsPanel.setMaximumSize(new Dimension(300, 300));
        neutralLossIonsTextPane.setMaximumSize(new Dimension(300, 300));
        neutralLossIonsTextPane.setColumns(20);
        neutralLossIonsTextPane.setLineWrap(true);
        neutralLossIonsTextPane.setWrapStyleWord(true);
        configureComponents(neutralLossIonsTextPane);
        configureComponents(NLIonsPanel);
        NLIonsPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        NLIonsPanel.setBackground(new Color(231, 242, 245));
        NLIonsPanel.add(neutralLossIonsTextPane);

        JPanel precursorIonPanel = new JPanel();
        precursorIonTextField.setColumns(21);
        configureComponents(precursorIonTextField);
        configureComponents(precursorIonPanel);
        precursorIonPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        precursorIonTextField.putClientProperty(FlatClientProperties.STYLE, "arc: 20");
        precursorIonTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "  Precursor Ion m/z");
        precursorIonTextField.setBorder(BorderFactory.createLineBorder(new Color(231, 242, 245)));
        precursorIonPanel.add(precursorIonTextField);

        JPanel toleranceOfPIPanel = new JPanel();
        toleranceOfPITextField.setColumns(10);
        configureComponents(toleranceOfPITextField);
        configureComponents(toleranceOfPIPanel);
        toleranceOfPIPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 20");
        toleranceOfPITextField.putClientProperty(FlatClientProperties.STYLE, "arc: 20");
        toleranceOfPITextField.setText("20.0");
        toleranceOfPITextField.setBorder(BorderFactory.createLineBorder(new Color(231, 242, 245)));
        toleranceOfPIPanel.add(toleranceOfPITextField);

        JPanel toleranceOfNLPanel = new JPanel();
        toleranceOfNLTextField.setColumns(10);
        configureComponents(toleranceOfNLTextField);
        configureComponents(toleranceOfNLPanel);
        toleranceOfNLPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 20");
        toleranceOfNLTextField.putClientProperty(FlatClientProperties.STYLE, "arc: 20");
        toleranceOfNLTextField.setText("30.0");
        toleranceOfNLTextField.setBorder(BorderFactory.createLineBorder(new Color(231, 242, 245)));
        toleranceOfNLPanel.add(toleranceOfNLTextField);

        configureTextComponents(precursorIonLabel);
        configureTextComponents(neutralLossesLabel);
        configureTextComponents(tolerancePILabel);
        configureTextComponents(toleranceNLLabel);

        inputSubpanel.add(precursorIonLabel, "gapleft 15, gaptop 15");
        inputSubpanel.add(tolerancePILabel, "wrap");
        inputSubpanel.add(precursorIonPanel, "gapleft 15");
        inputSubpanel.add(toleranceOfPIPanel, "wrap");
        inputSubpanel.add(neutralLossesLabel, "gapleft 15, gaptop 25");
        inputSubpanel.add(toleranceNLLabel, "wrap");
        inputSubpanel.add(NLIonsPanel, "gapleft 15, span 1 3");
        inputSubpanel.add(toleranceOfNLPanel, "wrap");
    }

    public void createLipidHeadGroupsPanel() {
        lipidHeadGroupsPanel.setBackground(Color.WHITE);
        lipidHeadGroupsPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        lipidHeadGroupsPanel.setLayout(new MigLayout("", "10[grow, fill]10", "10[grow, fill]10"));
        lipidHeadGroupsPanel.setMinimumSize(new Dimension(200, 340));
        lipidHeadGroupsPanel.setMaximumSize(new Dimension(200, 340));

        JLabel lipidHeadGroupsLabel = new JLabel("Lipid Head Groups");
        configureTextComponents(lipidHeadGroupsLabel);
        lipidHeadGroupsPanel.add(lipidHeadGroupsLabel, "gapleft 3, gaptop 2, wrap");
        JCheckBox selectAllCheckBox = new JCheckBox("Select All");
        configureTextComponents(selectAllCheckBox);
        lipidHeadGroupsPanel.add(selectAllCheckBox, "gapleft 15, gaptop 7, wrap");
        List<JPanel> checkBoxPanels = new ArrayList<>();

        String[] lipidHeadGroupsStrings = {"TG", "DG", "MG", "CE", "PA", "PC", "PE", "PI", "PG", "PS", "CL"};
        JPanel lipidSubPanel = new JPanel(new MigLayout("", "10[grow, fill]10", "[grow, fill]"));
        lipidSubPanel.setBackground(Color.WHITE);

        for (String lipidHeadGroup : lipidHeadGroupsStrings) {
            JCheckBox checkBox = new JCheckBox(lipidHeadGroup);
            JPanel checkBoxPanel = new JPanel(new BorderLayout());
            checkBoxPanels.add(checkBoxPanel);
            checkBoxPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
            checkBoxPanel.setMinimumSize(new Dimension(0, 30));
            configureCheckBoxPanel(checkBox, checkBoxPanel);

            selectAllCheckBox.addItemListener(e -> {
                boolean isSelected = (e.getStateChange() == ItemEvent.SELECTED);
                Color bg = isSelected ? new Color(195, 224, 229) : new Color(231, 242, 245);

                for (JPanel panel : checkBoxPanels) {
                    panel.setBackground(bg);
                    panel.putClientProperty(FlatClientProperties.STYLE, "arc: 20");
                    panel.repaint();

                    for (Component component : panel.getComponents()) {
                        if (component instanceof JCheckBox checkBox1) {
                            checkBox1.setSelected(isSelected);
                            checkBox1.setBackground(bg);
                        }
                    }
                }
            });

            lipidHeadGroupsCheckBoxList.add(checkBox);
            configureTextComponents(checkBox);
            checkBox.setFont(new Font("Arial", Font.BOLD, 16));
            lipidSubPanel.add(checkBoxPanel, "wrap, gapbottom 3");
        }
        JScrollPane lipidsScrollPane = new JScrollPane(lipidSubPanel);
        lipidsScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        lipidsScrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        lipidHeadGroupsPanel.add(lipidsScrollPane);
        lipidHeadGroupsPanel.revalidate();
        lipidHeadGroupsPanel.repaint();
    }

    private static void configureCheckBoxPanel(JCheckBox checkBox, JPanel checkBoxPanel) {
        checkBoxPanel.setBackground(new Color(231, 242, 245));
        checkBoxPanel.add(checkBox);
        checkBoxPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 20");
        checkBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                checkBoxPanel.setBackground(new Color(195, 224, 229));
                checkBox.setBackground(new Color(195, 224, 229));
            } else {
                checkBoxPanel.setBackground(new Color(231, 242, 245));
                checkBox.setBackground(new Color(231, 242, 245));
            }
        });
    }

    public void createAdductsPanel() {
        adductsPanel.setLayout(new MigLayout("", "10[grow, fill]10", "10[grow, fill]10"));
        adductsPanel.setBackground(Color.WHITE);
        adductsPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        adductsPanel.setMinimumSize(new Dimension(360, 340));
        adductsPanel.setPreferredSize(new Dimension(360, 340));
        adductsPanel.setMaximumSize(new Dimension(360, 340));
        configureComponents(ionComboBox);
        ionComboBox.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        ionComboBox.setToolTipText("Choose the list of adducts based on charge.");
        ionComboBox.addActionListener(_ -> updateListOfAdductsAccordingToCharge(Objects.requireNonNull(ionComboBox.getSelectedItem()).toString()));
        adductsPanel.add(ionComboBox, "gapbottom 10, wrap");
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
        for (JCheckBox checkBox : adductCheckBoxList) {
            checkBox.setSelected(false);
        }

        if (charge.equals("   View Positive Adducts  ")) {
            String[] string = {"[M+H]+", "[M+Na]+", "[M+K]+", "[M+NH4]+", "[M+H-H2O]+", "[M+C2H6N2+H]+"};
            updateAdductPanel(string);
        } else if (charge.equals("   View Negative Adducts  ")) {
            String[] string = {"[M-H]-", "[M+Cl]-", "[M-H-H2O]-", "[M+HCOOH-H]-", "[M+CH3COOH-H]-"};
            updateAdductPanel(string);

        }

        adductsPanel.revalidate();
        adductsPanel.repaint();
    }

    public static void updateAdductPanel(String[] adducts) {
        for (Component component : adductsPanel.getComponents()) {
            if (component instanceof JPanel || component instanceof JCheckBox || component instanceof JScrollPane) {
                adductsPanel.remove(component);
            }
        }

        JCheckBox selectAllCheckBox = new JCheckBox("Select All");
        configureTextComponents(selectAllCheckBox);
        adductsPanel.add(selectAllCheckBox, "gapleft 15, wrap");

        List<JPanel> checkBoxPanels = new ArrayList<>();

        JPanel subPanel = new JPanel(new MigLayout());
        subPanel.setBackground(Color.WHITE);

        for (String adduct : adducts) {
            JCheckBox checkBox = new JCheckBox(adduct);
            JPanel checkBoxPanel = new JPanel(new BorderLayout());
            checkBoxPanels.add(checkBoxPanel);
            checkBoxPanel.setBorder(new EmptyBorder(0, 10, 0, 0));

            checkBoxPanel.setMinimumSize(new Dimension(290, 30));
            checkBoxPanel.setMaximumSize(new Dimension(290, 30));
            checkBox.setMinimumSize(new Dimension(290, 30));
            checkBox.setMaximumSize(new Dimension(290, 30));

            configureCheckBoxPanel(checkBox, checkBoxPanel);
            adductCheckBoxList.add(checkBox);
            configureTextComponents(checkBox);
            checkBox.setFont(new Font("Arial", Font.BOLD, 16));
            subPanel.add(checkBoxPanel, "wrap, gapbottom 3");
        }

        selectAllCheckBox.addItemListener(e -> PatternRecognitionUI.updatePanelWhenSelected(checkBoxPanels, e));

        JScrollPane adductsScrollPane = new JScrollPane(subPanel);
        adductsScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        adductsScrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        adductsPanel.add(adductsScrollPane);
        adductsPanel.revalidate();
        adductsPanel.repaint();
    }

    public boolean checkIfTextFieldIsNotEmpty(String textFieldInput) {
        return !textFieldInput.isEmpty();
    }

    public static List<String> getAdductsChosen() {
        List<String> adducts = new ArrayList<>();

        boolean isSelectAllSelected = false;
        for (JCheckBox checkBox : adductCheckBoxList) {
            if (checkBox.getText().equals("Select All") && checkBox.isSelected()) {
                isSelectAllSelected = true;
                break;
            }
        }

        if (isSelectAllSelected) {
            for (JCheckBox checkBox : adductCheckBoxList) {
                adducts.add(checkBox.getText());
            }
        } else {
            for (JCheckBox checkBox : adductCheckBoxList) {
                if (checkBox.isSelected()) {
                    adducts.add(checkBox.getText());
                }
            }
        }

        return adducts;
    }

    public static List<LipidType> getLipidHeadGroupsChosen() {
        List<LipidType> lipidHeadGroups = new ArrayList<>();
        for (JCheckBox checkBox : lipidHeadGroupsCheckBoxList) {
            if (checkBox.isSelected()) {
                if (checkBox.getText().equals("Select All")) {
                    for (JCheckBox checkBoxWhenAll : lipidHeadGroupsCheckBoxList) {
                        lipidHeadGroups.add(LipidType.valueOf(checkBoxWhenAll.getText()));
                    }
                }
                lipidHeadGroups.add(LipidType.valueOf(checkBox.getText()));
            }
        }
        return lipidHeadGroups;
    }
}


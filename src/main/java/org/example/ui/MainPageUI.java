package org.example.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;
import org.example.databases.Database;
import org.example.domain.*;
import org.example.exceptions.FattyAcidCreation_Exception;
import org.example.exceptions.InvalidFormula_Exception;
import org.example.utils.CSVUtils;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class MainPageUI extends JPanel {
    private JButton searchButton;
    private JButton exportButton;
    private JButton uploadButton;
    private JButton clearButton;

    private JPanel tablePanel = null;
    private JPanel subpanel2 = null;
    private JPanel radioButtonsPanel = null;
    private JTable table = null;
    private DefaultTableModel tableModel = null;
    private String[] tableTitles = null;
    private JScrollPane jScrollPane = null;
    private JPanel inputSubpanel = null;
    public JTextField NLoss1_Input = null;
    public JTextField NLoss2_Input = null;
    public JTextField NLoss3_Input = null;
    public JTextField NLoss4_Input = null;
    public JTextField PI_Input = null;
    private JLabel PI_Label = null;
    private JLabel NLosses_Label = null;
    private JLabel adducts_Label = null;
    public JPanel adductsPanel = null;
    private JComboBox ionComboBox = null;
    private DefaultListModel<String> listModel;
    private URL url = null;
    private LinkedHashSet<Lipid> lipidsSet;
    private String[][] lipidData = null;
    private Set<Double> neutralLossAssociatedIonsInput = null;
    private DefaultTableModel model;
    private Database database;

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
        subpanel2 = new JPanel();
        radioButtonsPanel = new JPanel();
        inputSubpanel = new JPanel();
        NLoss1_Input = new JTextField();
        NLoss2_Input = new JTextField();
        NLoss3_Input = new JTextField();
        NLoss4_Input = new JTextField();
        PI_Input = new JTextField();
        PI_Label = new JLabel("    Precursor Ion");
        NLosses_Label = new JLabel("    Neutral Losses");
        adducts_Label = new JLabel("    Adducts");
        listModel = new DefaultListModel<>();
        adductsPanel = new JPanel();
        ionComboBox = new JComboBox(new String[]{"   View Positive Adducts  ", "   View Negative Adducts  "});
        database = new Database();

        setLayout(new MigLayout("", "[grow, fill]25[grow, fill]25[grow, fill]",
                "[grow, fill]25[grow, fill]"));
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
        add(subpanel2);
        setVisible(true);
    }

    public void createTable() {
        tablePanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setPreferredSize(new Dimension(1250, 500));
        tablePanel.setSize(1250, 500);
        tablePanel.setLayout(new MigLayout("", "[grow, fill]", "[grow, fill]"));
        tableTitles = new String[]{"Cas ID", "Compound Name", "Species Shorthand", "Compound Formula", "Compound Mass", "Adduct", "M/Z"};

        model = new DefaultTableModel(tableTitles, 0);
        table = new JTable(model);
        jScrollPane = new JScrollPane(table);
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setForeground(new Color(65, 114, 159));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));

        configureTable(new String[0][]);
        jScrollPane.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        jScrollPane.setBorder(new LineBorder(Color.WHITE, 1));
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        tablePanel.add(jScrollPane, "center, grow");
    }

    public void createButtons() {
        subpanel2.setBackground(Color.WHITE);
        subpanel2.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        subpanel2.setLayout(new MigLayout("", "25[grow, fill]25",
                "25[grow, fill]25[grow, fill]25"));

        configureComponents(searchButton);
        searchButton.setBackground(Color.WHITE);
        searchButton.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        searchButton.setIcon(new ImageIcon("src/main/resources/Search_Icon.png"));
        searchButton.setBorder(new LineBorder(Color.white));
        searchButton.setHorizontalAlignment(SwingConstants.LEFT);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tablePanel.removeAll();
                try {
                    JScrollPane lipidScrollPane = createLipidScrollPane();
                    lipidScrollPane.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
                    lipidScrollPane.setPreferredSize(new Dimension(1250, 500));
                    lipidScrollPane.setBorder(new LineBorder(Color.WHITE, 1));
                    tablePanel.add(lipidScrollPane, "center, grow");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
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
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "Do you wish to export this information to PDF format?",
                        "Export to PDF", JOptionPane.YES_NO_CANCEL_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    try {
                        // todo replace with CSVUtils
                        new CSVUtils();
                        JOptionPane.showMessageDialog(null, "File created successfully!");
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (choice == JOptionPane.NO_OPTION | choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION) {
                    JOptionPane.showMessageDialog(null, "Operation cancelled.");
                } else {
                    JOptionPane.showMessageDialog(null, "File already exists.");
                }
            }
        });

        configureComponents(uploadButton);
        uploadButton.setBackground(Color.WHITE);
        uploadButton.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        uploadButton.setIcon(new ImageIcon("src/main/resources/Upload_Icon.png"));
        uploadButton.setBorder(new LineBorder(Color.white));
        uploadButton.setHorizontalAlignment(SwingConstants.LEFT);
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                int r = j.showOpenDialog(null);
                if (r == JFileChooser.APPROVE_OPTION) {
                    //l.setText(j.getSelectedFile().getAbsolutePath());
                }
            }
        });
        configureComponents(clearButton);
        clearButton.setBackground(Color.WHITE);
        clearButton.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        clearButton.setIcon(new ImageIcon("src/main/resources/Clear_Icon.png"));
        clearButton.setBorder(new LineBorder(Color.white));
        clearButton.setHorizontalAlignment(SwingConstants.LEFT);
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NLoss1_Input.setText(null);
                NLoss2_Input.setText(null);
                NLoss3_Input.setText(null);
                NLoss4_Input.setText(null);
                PI_Input.setText(null);
            }
        });

        subpanel2.add(searchButton, "wrap");
        subpanel2.add(clearButton, "wrap");
        subpanel2.add(exportButton, "wrap");
        subpanel2.add(uploadButton, "wrap");
    }

    public JScrollPane createLipidScrollPane() {
        neutralLossAssociatedIonsInput = new LinkedHashSet<>();
        if (checkIfTextFieldIsNotEmpty(NLoss1_Input.getText())) {
            neutralLossAssociatedIonsInput.add(Double.parseDouble(NLoss1_Input.getText()));
        }

        if (checkIfTextFieldIsNotEmpty(NLoss2_Input.getText())) {
            neutralLossAssociatedIonsInput.add(Double.parseDouble(NLoss2_Input.getText()));
        }

        if (checkIfTextFieldIsNotEmpty(NLoss3_Input.getText())) {
            neutralLossAssociatedIonsInput.add(Double.parseDouble(NLoss3_Input.getText()));
        }

        if (checkIfTextFieldIsNotEmpty(NLoss4_Input.getText())) {
            neutralLossAssociatedIonsInput.add(Double.parseDouble(NLoss4_Input.getText()));
        }

        Set<MSLipid> lipidSet;
        try {
            if (checkIfTextFieldIsNotEmpty(PI_Input.getText())) {
                lipidSet = database.getAllLipidsFromDatabase(LipidType.TG, Double.parseDouble(PI_Input.getText()), neutralLossAssociatedIonsInput);

                lipidData = new String[lipidSet.size()][7];
                int i = 0;
                for (MSLipid lipid : lipidSet) {
                    lipidData[i][0] = lipid.getCasID();
                    lipidData[i][1] = lipid.getCompoundName();
                    lipidData[i][2] = calculateSpeciesShorthand(lipid);
                    lipidData[i][3] = lipid.getFormula();
                    lipidData[i][4] = String.valueOf(lipid.getMass());
                    lipidData[i][5] = "[M+NH4]+";
                    lipidData[i][6] = String.valueOf(lipid.calculateMZWithAdduct("[M+NH3]+", 1));
                    i++;
                }
            }
        } catch (SQLException | FattyAcidCreation_Exception | InvalidFormula_Exception ex) {
            ex.printStackTrace();
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
                    color = null;
                }
                return comp;
            }
        };

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setForeground(new Color(65, 114, 159));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        table.setForeground(new Color(65, 114, 159));
        table.setFont(new Font("Arial", Font.PLAIN, 15));
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

        inputSubpanel.setLayout(new MigLayout("", "[grow, fill]15[grow, fill]15",
                "15[grow, fill]10[grow, fill]15[grow, fill]10[grow,fill]15"));
        inputSubpanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        inputSubpanel.setBackground(Color.WHITE);

        NLoss1_Input.setColumns(30);
        configureComponents(NLoss1_Input);
        NLoss1_Input.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        NLoss1_Input.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,
                "  Neutral Loss Associated Ion 1");
        NLoss2_Input.setColumns(30);
        configureComponents(NLoss2_Input);
        NLoss2_Input.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        NLoss2_Input.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,
                "  Neutral Loss Associated Ion 2 (optional)");
        NLoss3_Input.setColumns(30);
        configureComponents(NLoss3_Input);
        NLoss3_Input.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        NLoss3_Input.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,
                "  Neutral Loss Associated Ion 3 (optional)");
        NLoss4_Input.setColumns(30);
        configureComponents(NLoss4_Input);
        NLoss4_Input.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        NLoss4_Input.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,
                "  Neutral Loss Associated Ion 4 (optional)");
        PI_Input.setColumns(15);
        configureComponents(PI_Input);
        PI_Input.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        PI_Input.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,
                "  Precursor Ion, m/z");
        configureComponents(ionComboBox);
        ionComboBox.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        ionComboBox.setToolTipText("Choose the list of adducts based on charge.");
        ionComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateListOfAdductsAccordingToCharge(ionComboBox.getSelectedItem().toString());
            }
        });
        configureLabelComponents(PI_Label);
        configureLabelComponents(NLosses_Label);

        inputSubpanel.add(PI_Label, "wrap");
        inputSubpanel.add(PI_Input);
        inputSubpanel.add(ionComboBox, "wrap, gapleft 75");
        inputSubpanel.add(NLosses_Label, "wrap, gaptop 15");
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
        configureLabelComponents(radioButtonsLabel);

        JRadioButton buttonUNKNOWN = new JRadioButton(" UNKNOWN", true);
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
        JRadioButton buttonTG = new JRadioButton(" TG");
        JRadioButton buttonCL = new JRadioButton(" CL");

        configureTextComponents(buttonUNKNOWN);
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

        ButtonGroup group = new ButtonGroup();
        group.add(buttonUNKNOWN);
        group.add(buttonCE);
        group.add(buttonCER);
        group.add(buttonDG);
        group.add(buttonMG);
        group.add(buttonPA);
        group.add(buttonPC);
        group.add(buttonPE);
        group.add(buttonPI);
        group.add(buttonPG);
        group.add(buttonPS);
        group.add(buttonSM);
        group.add(buttonTG);
        group.add(buttonCL);
        //** consider unknown for switch() lipidtypes
        radioButtonsPanel.add(radioButtonsLabel, "wrap");
        radioButtonsPanel.add(buttonUNKNOWN, "wrap, gapleft 10");
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
        configureLabelComponents(adducts_Label);
        adductsPanel.setLayout(new MigLayout("", "[]", ""));
        adductsPanel.setBackground(Color.WHITE);
        adductsPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        adductsPanel.setPreferredSize(new Dimension(350, 300));
        adductsPanel.setSize(new Dimension(350, 300));
        adductsPanel.setMinimumSize(new Dimension(350, 300));
        adductsPanel.setMaximumSize(new Dimension(350, 300));

        adductsPanel.add(adducts_Label, "wrap, gapbottom 4");
        updateAdductPanel(Adduct.getPositiveAdducts());
        adductsPanel.setVisible(true);
    }

    public static void configureComponents(Component component) {
        component.setFont(new Font("Arial", Font.BOLD, 16));
        component.setBackground(new Color(231, 242, 245));
        component.setForeground(new Color(65, 114, 159));
    }

    public static void configureLabelComponents(Component component) {
        component.setFont(new Font("Arial", Font.BOLD, 17));
        component.setForeground(new Color(65, 114, 159));
    }

    public static void configureTextComponents(Component component) {
        component.setFont(new Font("Arial", Font.PLAIN, 17));
        component.setForeground(new Color(65, 114, 159));
    }

    public void updateUI_LipidType(LipidType lipidType) {
        switch (lipidType) {
            case CE:
                updateUI();
            case DG:
                updateUI();
            case MG:
                updateUI();
            case PA:
                updateUI();
            case PC:
                updateUI();
            case PE:
                updateUI();
            case PI:
                updateUI();
            case PG:
                updateUI();
            case PS:
                updateUI();
            case TG:
                updateUI();
            case CL:
                updateUI();
        }
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
        if (!textFieldInput.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public String calculateSpeciesShorthand(MSLipid lipid) {
        int carbonAtoms = 0;
        int doubleBonds = 0;
        for (FattyAcid fattyAcid : lipid.getFattyAcids()) {
            carbonAtoms += fattyAcid.getCarbonAtoms();
            doubleBonds += fattyAcid.getDoubleBonds();
        }
        return lipid.getLipidSkeletalStructure().getLipidType().toString() + " " + carbonAtoms + ":" + doubleBonds;
    }
}


package org.example.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;
import org.example.databases.Database;
import org.example.domain.Adduct;
import org.example.domain.Lipid;
import org.example.domain.LipidType;
import org.example.domain.MSLipid;
import org.example.exceptions.FattyAcidCreation_Exception;
import org.example.exceptions.InvalidFormula_Exception;
import org.example.utils.PDFUtils;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

public class MainPageUI extends JPanel {
    private JButton searchButton;
    private JButton exportButton;
    private JButton uploadButton;

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
    private JLabel ion_Label = null;
    private JLabel adducts_Label = null;
    public JPanel adductsPanel = null;
    private JComboBox ionComboBox = null;
    private DefaultListModel<String> listModel;
    private URL url = null;
    private LinkedHashSet<Lipid> lipidsSet;
    private String[][] lipidData = null;
    private Set<Double> neutralLossAssociatedIonsInput = null;

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
        ion_Label = new JLabel("    Ion Charge");
        adducts_Label = new JLabel("    Adducts");
        listModel = new DefaultListModel<>();
        adductsPanel = new JPanel();
        adductsPanel.setLayout(new MigLayout());
        updateListPanel(Adduct.getPositiveAdducts());
        adductsPanel.setPreferredSize(new Dimension(350, 250));
        adductsPanel.setMinimumSize(new Dimension(350, 250));
        adductsPanel.setMaximumSize(new Dimension(350, 250));

        neutralLossAssociatedIonsInput = new LinkedHashSet<>();
        ionComboBox = new JComboBox(new String[]{"+  Positively Charged Adducts", "-  Negatively Charged Adducts"});
        setLayout(new MigLayout("", "[grow, fill]25[grow, fill]",
                "[grow, fill]25[grow, fill]"));
        setBackground(new Color(227, 235, 242));
        createTable();
        createRadioButtons();
        createInputPanel();
        createButtons();

        add(tablePanel);
        add(radioButtonsPanel, "wrap");
        add(inputSubpanel);
        add(subpanel2);
        setVisible(true);
    }

    public void createTable() {
        tablePanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setLayout(new MigLayout("", "[grow, fill]", "[grow, fill]"));
        tableTitles = new String[]{"Cas ID", "Compound Name", "Compound Formula", "Compound Mass", "Adduct", "M/Z"};
        configureTable(new String[0][]);

        jScrollPane = new JScrollPane(table);
        jScrollPane.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        jScrollPane.setPreferredSize(new Dimension(1090, 500));
        jScrollPane.setBorder(new LineBorder(Color.WHITE, 1));

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
                    lipidScrollPane.setPreferredSize(new Dimension(1090, 500));
                    lipidScrollPane.setBorder(new LineBorder(Color.WHITE, 1));
                    tablePanel.add(lipidScrollPane, "center, grow");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                tablePanel.updateUI();
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
                        new PDFUtils(JOptionPane.showInputDialog("Introduce File Name:"));
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

        subpanel2.add(searchButton, "wrap");
        subpanel2.add(exportButton, "wrap");
        subpanel2.add(uploadButton);

    }

    public JScrollPane createLipidScrollPane() {
        Database database = new Database();
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

        Set<MSLipid> uncheckedLipidSet;
        Set<MSLipid> checkedLipidSet;
        try {
            if (checkIfTextFieldIsNotEmpty(PI_Input.getText())) {
                uncheckedLipidSet = database.getAllLipidsFromDatabase(LipidType.TG, Double.parseDouble(PI_Input.getText()), neutralLossAssociatedIonsInput);

                checkedLipidSet = database.limitListOfLipidsAccordingToPrecursorIon(uncheckedLipidSet, Double.parseDouble(PI_Input.getText()), "[M+NH3]+");

                lipidData = new String[uncheckedLipidSet.size()][6];
                int i = 0;
                for (MSLipid lipid : uncheckedLipidSet) {
                    lipidData[i][0] = lipid.getCasID();
                    lipidData[i][1] = lipid.getCompoundName();
                    lipidData[i][2] = lipid.getFormula();
                    lipidData[i][3] = String.valueOf(lipid.getMass());
                    lipidData[i][4] = "[M+NH3]+";
                    lipidData[i][5] = String.valueOf(lipid.calculateMZWithAdduct("[M+NH3]+", 1));
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
                Color alternateColor = new Color(227, 235, 242);
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
        table.getTableHeader().setForeground(new Color(52, 94, 125));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        table.setForeground(new Color(52, 94, 125));
        table.setFont(new Font("Arial", Font.PLAIN, 15));
        table.getTableHeader().setReorderingAllowed(false);
        table.setModel(tableModel);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.setRowHeight(table.getRowHeight() + 15);
        table.setPreferredSize(new Dimension(1090, 500));
        table.setBorder(new LineBorder(Color.WHITE, 1));
    }

    public void createInputPanel() {
        inputSubpanel.setLayout(new MigLayout("", "[grow, fill]15[grow, fill]15[grow,fill]",
                "15[grow, fill]15[grow, fill]15[grow, fill]15[grow,fill]15"));
        inputSubpanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        inputSubpanel.setBackground(Color.WHITE);

        NLoss1_Input.setColumns(30);
        configureComponents(NLoss1_Input);
        NLoss1_Input.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        NLoss1_Input.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,
                "Neutral Loss Associated Ion 1");
        NLoss2_Input.setColumns(30);
        configureComponents(NLoss2_Input);
        NLoss2_Input.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        NLoss2_Input.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,
                "Neutral Loss Associated Ion 2 (optional)");
        NLoss3_Input.setColumns(30);
        configureComponents(NLoss3_Input);
        NLoss3_Input.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        NLoss3_Input.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,
                "Neutral Loss Associated Ion 3 (optional)");
        NLoss4_Input.setColumns(30);
        configureComponents(NLoss4_Input);
        NLoss4_Input.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        NLoss4_Input.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,
                "Neutral Loss Associated Ion 4 (optional)");
        PI_Input.setColumns(15);
        configureComponents(PI_Input);
        PI_Input.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        PI_Input.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,
                "Precursor Ion, m/z");

        configureComponents(ionComboBox);
        ionComboBox.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        ionComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateListOfAdductsAccordingToCharge(ionComboBox.getSelectedItem().toString());
            }
        });

        configureComponents(adductsPanel);
        adductsPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");

        configureLabelComponents(PI_Label);
        configureLabelComponents(NLosses_Label);
        configureLabelComponents(ion_Label);
        configureLabelComponents(adducts_Label);

        inputSubpanel.add(PI_Label);
        inputSubpanel.add(ion_Label);
        inputSubpanel.add(adducts_Label, "wrap");
        inputSubpanel.add(PI_Input);
        inputSubpanel.add(ionComboBox);
        inputSubpanel.add(adductsPanel, "wrap, span 1 6");
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
        //     CE, CER, DG, MG, PA, PC, PE, PI, PG, PS, SM, TG, CL;
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
        JRadioButton buttonUNKNOWN = new JRadioButton(" UNK");

        configureLabelComponents(buttonCE);
        configureLabelComponents(buttonCER);
        configureLabelComponents(buttonDG);
        configureLabelComponents(buttonMG);
        configureLabelComponents(buttonPA);
        configureLabelComponents(buttonPC);
        configureLabelComponents(buttonPE);
        configureLabelComponents(buttonPI);
        configureLabelComponents(buttonPG);
        configureLabelComponents(buttonPS);
        configureLabelComponents(buttonSM);
        configureLabelComponents(buttonTG);
        configureLabelComponents(buttonCL);
        configureLabelComponents(buttonUNKNOWN);

        ButtonGroup group = new ButtonGroup();
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
        group.add(buttonUNKNOWN);

        radioButtonsPanel.add(buttonCE, "wrap");
        radioButtonsPanel.add(buttonCER, "wrap");
        radioButtonsPanel.add(buttonDG, "wrap");
        radioButtonsPanel.add(buttonMG, "wrap");
        radioButtonsPanel.add(buttonPA, "wrap");
        radioButtonsPanel.add(buttonPC, "wrap");
        radioButtonsPanel.add(buttonPE, "wrap");
        radioButtonsPanel.add(buttonPI, "wrap");
        radioButtonsPanel.add(buttonPG, "wrap");
        radioButtonsPanel.add(buttonPS, "wrap");
        radioButtonsPanel.add(buttonSM, "wrap");
        radioButtonsPanel.add(buttonTG, "wrap");
        radioButtonsPanel.add(buttonCL, "wrap");
        radioButtonsPanel.add(buttonUNKNOWN, "wrap");
    }

    public static void configureComponents(Component component) {
        component.setFont(new Font("Arial", Font.BOLD, 15));
        component.setBackground(new Color(227, 235, 242));
        component.setForeground(new Color(52, 94, 125));
    }

    public static void configureLabelComponents(Component component) {
        component.setFont(new Font("Arial", Font.BOLD, 17));
        component.setForeground(new Color(52, 94, 125));
    }

    public void updateUI_LipidType(LipidType lipidType) {
        switch (lipidType) {
            case CE: // todo search database for CE(16:0/16:0)
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
        if (charge.equals("+  Positively Charged Adducts")) {
            String[] string = Adduct.getPositiveAdducts();
            updateListPanel(string);
        } else if (charge.equals("-  Negatively Charged Adducts")) {
            String[] string = Adduct.getNegativeAdducts();
            updateListPanel(string);
        }
        adductsPanel.revalidate();
        adductsPanel.repaint();
    }

    private void updateListPanel(String[] adducts) {
        adductsPanel.removeAll();
        for (String adduct : adducts) {
            JCheckBox checkBox = new JCheckBox(adduct);
            configureLabelComponents(checkBox);
            adductsPanel.add(checkBox, "wrap");
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

}


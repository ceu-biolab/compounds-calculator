package org.example.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;
import org.example.domain.Adduct;
import org.example.domain.Lipid;
import org.example.domain.LipidType;
import org.example.utils.PDFUtils;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashSet;

public class MainPageUI extends JPanel {
    private JButton searchButton;
    private JButton exportButton = null;
    private JPanel subpanel1 = null;
    private JPanel subpanel2 = null;
    private JPanel radioButtonsPanel = null;
    private JTable table = null;
    private DefaultTableModel tableModel = null;
    private String[][] lipids = null;
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
    private JList adductsList = null;
    public JScrollPane adductsScrollPane = null;
    private JComboBox ionComboBox = null;
    private DefaultListModel<String> listModel;
    private URL url = null;
    private LinkedHashSet<Lipid> lipidsSet;

    public MainPageUI() {
        FlatLightLaf.setup();
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        searchButton = new JButton("Search Database");
        exportButton = new JButton("Export to PDF");
        subpanel1 = new JPanel();
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
        adductsList = new JList<>(listModel);
        updateListModel(Adduct.getPositiveAdducts());
        adductsScrollPane = new JScrollPane(adductsList);
        adductsList.setCellRenderer(new CheckboxListCellRenderer());
        ionComboBox = new JComboBox(new String[]{"+  Positively Charged Adducts", "-  Negatively Charged Adducts"});

        setLayout(new MigLayout("", "[grow, fill]25[grow, fill]",
                "[grow, fill]25[grow, fill]"));
        setBackground(new Color(227, 235, 242));
        createTable();
        add(subpanel1);
        createRadioButtons();
        add(radioButtonsPanel, "wrap");
        createInputPanel();
        add(inputSubpanel);
        createButtons();
        add(subpanel2);
        setVisible(true);
    }

    public void createTable() {
        subpanel1.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        subpanel1.setBackground(Color.WHITE);
        subpanel1.setLayout(new MigLayout("", "[grow, fill]", "[grow, fill]"));
        tableTitles = new String[]{"Compound ID", "Compound Name", "Formula", "Adduct", "Precursor Ion", "Neutral Loss Associated Ions"};
        lipids = new String[][]{
                {"LMGL03010007", "TG(12:0/16:0/18:0)", "C24H66O6",
                        "[M+NH4]", "834.7723", "257.7732, 234.4356"},
                {"LMGL03010007", "TG(12:0/16:0/18:0)", "C24H66O4",
                        "[M+H+]", "834.7732", "257.7732, 345.6443, 654.3456"},
                {"LMGL03010007", "TG(12:0/16:0/18:0)", "C24H66O2",
                        "[M+NH4]", "834.7723", "257.7732"},
                {"LMGL03010007", "TG(12:0/16:0/18:0)", "C32H66O6",
                        "[M+H+]", "834.7732", "257.7732"},
                {"LMGL03010007", "TG(12:0/16:0/18:0)", "C32H66O6",
                        "[M+NH4]", "834.7723", "324.7732, 324.3453"}};
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

        tableModel = new DefaultTableModel(lipids, tableTitles) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setForeground(new Color(52, 94, 125));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        table.setForeground(new Color(52, 94, 125));
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setReorderingAllowed(false);
        table.setModel(tableModel);
        table.getColumnModel().getColumn(5).setPreferredWidth(200);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (table.getSelectedColumn() == 0) {
                    String urlstring = "https://lipidmaps.org/databases/lmsd/";
                    try {
                        url = new URL(urlstring + table.getValueAt(table.getSelectedRow(),
                                table.getSelectedColumn()));
                        if (Desktop.isDesktopSupported()) {
                            Desktop desktop = Desktop.getDesktop();
                            try {
                                desktop.browse(url.toURI());
                            } catch (IOException | URISyntaxException exception) {
                                exception.printStackTrace();
                            }
                        }
                    } catch (MalformedURLException exception) {
                        throw new RuntimeException(exception);
                    }
                }

                if (table.getSelectedColumn() == 2) {
                    JFrame window = new JFrame();
                    window.setLayout(new MigLayout());
                    window.setSize(750, 100);
                    window.setLocationRelativeTo(null);
                    JLabel label = new JLabel(table.getValueAt(table.getSelectedRow(),
                            table.getSelectedColumn()).toString());
                    label.setForeground(new Color(52, 94, 125));
                    label.setFont(new Font("Arial", Font.PLAIN, 14));
                    window.add(label, "center");
                    window.setVisible(true);
                }
            }
        });

        table.setRowHeight(table.getRowHeight() + 15);

        jScrollPane = new JScrollPane(table);
        jScrollPane.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        jScrollPane.setPreferredSize(new Dimension(1090, 500));
        jScrollPane.setBorder(new LineBorder(Color.WHITE, 1));

        subpanel1.add(jScrollPane, "center");
    }

    private String[][] convertToStringArray() {
        String[][] lipidsArray = new String[lipidsSet.size()][8];
        int index = 0;
        for (Lipid lipid : lipidsSet) {
            //lipidsArray[index][0] = lipid.get();
            //lipidsArray[index][1] = lipid.getAge();
            // todo
            index++;
        }
        return lipidsArray;
    }

    public void createButtons() {
        subpanel2.setBackground(Color.WHITE);
        subpanel2.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        subpanel2.setLayout(new MigLayout("", "25[grow, fill]25",
                "25[grow, fill]25[grow, fill]25"));

        configureComponents(searchButton);
        searchButton.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // todo
            }
        });

        configureComponents(exportButton);
        exportButton.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
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

        subpanel2.add(searchButton, "wrap");
        subpanel2.add(exportButton);
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
                updateUI_Ion(ionComboBox.getSelectedItem().toString());
            }
        });
        configureComponents(adductsScrollPane);
        configureComponents(adductsList);
        adductsList.setFixedCellHeight(30);
        adductsScrollPane.putClientProperty(FlatClientProperties.STYLE, "arc: 40");

        configureLabelComponents(PI_Label);
        configureLabelComponents(NLosses_Label);
        configureLabelComponents(ion_Label);
        configureLabelComponents(adducts_Label);

        inputSubpanel.add(PI_Label);
        inputSubpanel.add(ion_Label);
        inputSubpanel.add(adducts_Label, "wrap");
        inputSubpanel.add(PI_Input);
        inputSubpanel.add(ionComboBox);
        inputSubpanel.add(adductsScrollPane, "wrap, span 1 6");
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
        component.setFont(new Font("Arial", Font.BOLD, 16));
        component.setBackground(new Color(227, 235, 242));
        component.setForeground(new Color(52, 94, 125));
    }

    public static void configureLabelComponents(Component component) {
        component.setFont(new Font("Arial", Font.BOLD, 18));
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

    public void updateUI_Ion(String charge) {
        if (charge.equals("+  Positively Charged Adducts")) {
            String[] string = Adduct.getPositiveAdducts();
            updateListModel(string);
        } else if (charge.equals("-  Negatively Charged Adducts")) {
            String[] string = Adduct.getNegativeAdducts().toArray(new String[0]);
            updateListModel(string);
        } else {
            updateListModel(Adduct.getAllAdducts());
        }
        adductsScrollPane.revalidate();
        adductsScrollPane.repaint();
    }

    private void updateListModel(String[] data) {
        listModel.clear();
        for (String item : data) {
            listModel.addElement(item);
        }
    }

    public class CheckboxListCellRenderer extends JCheckBox implements ListCellRenderer {

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            setComponentOrientation(list.getComponentOrientation());
            setFont(list.getFont());
            setBackground(list.getBackground());
            setForeground(list.getForeground());
            setSelected(isSelected);
            setEnabled(list.isEnabled());

            setText(value == null ? "" : value.toString());

            return this;
        }
    }

}


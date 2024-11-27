package org.example.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;
import org.example.utils.CSVUtils;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BatchProcessingUI {

    // TODO: Make this a pop-up panel (JPanel) instead, so that it displays all of the different results and can be clicked through.

    /**
     * This class should appear when a file is uploaded by the user.
     */

    DefaultTableModel tableModel;
    JTable lipidTable;

    public static void main(String[] args) {
        new BatchProcessingUI();
    }

    public BatchProcessingUI() {
        FlatLightLaf.setup();
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        JFrame frame = new JFrame();
        frame.getContentPane().setBackground(new Color(195, 224, 229));
        frame.setLayout(new MigLayout("", "[]", "[]20[]"));
        frame.setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 400,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 400);
        frame.setLocationRelativeTo(null);
        frame.add(createLipidInfoHeaderPanel(), "wrap");
        frame.add(createLipidTablePanel());
        frame.setVisible(true);
    }

    public JPanel createLipidInfoHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(195, 224, 229));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        panel.setLayout(new MigLayout("", "[][]", "[]"));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 400),
                (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 775)));
        String[] tableTitles = new String[]{"Precursor Ion", "Neutral Loss Associated Ions"};
        String[][] lipidPrecursorIonAndNLIons = {{"656.5862", "411.3458, 439.3784, 467.4092"}};


        DefaultTableModel model = new DefaultTableModel(lipidPrecursorIonAndNLIons, tableTitles) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        JScrollPane jScrollPane = new JScrollPane(table);
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setForeground(new Color(65, 114, 159));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        table.getTableHeader().setReorderingAllowed(false);
        AdductTransformerUI.configureTable(table);
        table.setBorder(new LineBorder(Color.WHITE, 1));
        jScrollPane.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        jScrollPane.setBorder(new LineBorder(Color.WHITE, 1));
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        //panel.add(addExportButton(), "gapright 45");
        panel.add(jScrollPane, "center, grow");
        return panel;
    }

    public JPanel createLipidTablePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(195, 224, 229));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 400),
                (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 400)));
        String[] tableTitles = new String[]{"Compound Name", "Species Shorthand", "Compound Formula", "Compound Mass", "Adduct", "m/z", "CMM ID"};
        String[][] lipidPrecursorIonAndNLIons = {{"TG(16:0/16:0/16:0)", "TG 48:0"}};


        DefaultTableModel model = new DefaultTableModel(lipidPrecursorIonAndNLIons, tableTitles) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable lipidTable = new JTable(model);
        JScrollPane jScrollPane = new JScrollPane(lipidTable);
        lipidTable.getTableHeader().setBackground(Color.WHITE);
        lipidTable.getTableHeader().setForeground(new Color(65, 114, 159));
        lipidTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        lipidTable.getTableHeader().setReorderingAllowed(false);
        AdductTransformerUI.configureTable(lipidTable);
        lipidTable.setBorder(new LineBorder(Color.WHITE, 1));
        jScrollPane.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        jScrollPane.setBorder(new LineBorder(Color.WHITE, 1));
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setMinimumSize(new Dimension((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 500),
                (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 700)));
        jScrollPane.setPreferredSize(new Dimension((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 500),
                (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 700)));
        // TODO: TO GO BETWEEN THE DIFFERENT PAGES WITH ARROWS, CREATE AN ARRAY WITH THE NUMBER OF RECORDS AND THEN USE i-1 and i+1 TO DETERMINE WHAT INFO TO UPDATE THE TABLE WITH
        panel.add(jScrollPane, "center, grow");
        return panel;
    }

    public JButton addExportButton() {
        JButton exportButton = new JButton();
        exportButton.setBackground(Color.WHITE);
        exportButton.putClientProperty(FlatClientProperties.STYLE, "arc: 40");
        exportButton.setIcon(new ImageIcon("src/main/resources/Download_Icon.png"));
        exportButton.setBorder(new LineBorder(Color.white));
        exportButton.setHorizontalAlignment(SwingConstants.LEFT);
        LipidCalculatorUI.configureComponents(exportButton);
        exportButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(null, "Do you wish to export this information to CSV format?", "Export to CSV", JOptionPane.YES_NO_CANCEL_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                try {
                    CSVUtils csvUtils = new CSVUtils();
                    // csvUtils.createAndWriteCSV(lipidData);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Current data set is empty. Please introduce data before attempting to create a new file.");
                }
            } else if (choice == JOptionPane.NO_OPTION || choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION) {
                JOptionPane.showMessageDialog(null, "Operation cancelled.");
            } else {
                JOptionPane.showMessageDialog(null, "File already exists.");
            }
        });
        return exportButton;
    }
}
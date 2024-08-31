package org.example.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.math.NumberUtils;
import org.example.databases.Database;
import org.example.domain.LipidType;
import org.example.exceptions.FattyAcidCreation_Exception;
import org.example.exceptions.InvalidFormula_Exception;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;

public class CSVUtils {
    public void createAndWriteCSV(String[][] lipidData) throws IOException {
        if (!(lipidData == null) || !(lipidData.length == 0)) {
            File file = new File(System.getProperty("user.home"), "Lipids " + (10000 + (int) (Math.random() * 90000)) + ".csv");
            writeCSV(lipidData, file);
        }
    }

    private void writeCSV(String[][] lipidData, File file) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file.getAbsolutePath()));
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("CAS ID", "Compound Name", "Species Shorthand", "Compound Formula", "Compound Mass", "Adduct", "M/Z"))) {
            for (String[] lipidDataString : lipidData) {
                csvPrinter.printRecord(Arrays.asList(lipidDataString));
            }
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            } else {
                JOptionPane.showMessageDialog(null, "Desktop is not supported.");
            }
        }
    }

    public void createAndWriteCSVBatchProcessing(String[][] lipidData, String precursorIon) throws IOException {
        if (!(lipidData == null) || !(lipidData.length == 0)) {
            File file = new File(System.getProperty("user.home"), "Lipids " + precursorIon + " " + (10000 + (int) (Math.random() * 90000)) + ".csv");
            writeCSV(lipidData, file);
        }
    }

    public void readCSVForBatchProcessing(File file) throws IOException {
        try (Reader reader = new FileReader(file.getAbsolutePath())) {
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder().build();
            Iterable<CSVRecord> records = csvFormat.parse(reader);
            Set<Double> neutralLossAssociatedIons = new HashSet<>();
            Database database = new Database();

            for (CSVRecord record : records) {
                for (int i = 1; i < record.size(); i++) {
                    double number = NumberUtils.toDouble(record.get(i));
                    if (number != 0.0d) {
                        neutralLossAssociatedIons.add(number);
                    }
                }
                try {
                    createAndWriteCSVBatchProcessing(database.findLipidsCSVFormat(LipidType.TG, NumberUtils.toDouble(record.get(0)), neutralLossAssociatedIons), String.valueOf(NumberUtils.toDouble(record.get(0))));
                    neutralLossAssociatedIons.clear();
                } catch (SQLException | InvalidFormula_Exception | FattyAcidCreation_Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }
}

package org.example.utils;

import ceu.biolab.Adduct;
import ceu.biolab.IncorrectAdduct;
import ceu.biolab.IncorrectFormula;
import ceu.biolab.NotFoundElement;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class CSVUtils {
    private final Path fileDirectory;

    public CSVUtils() {
        try {
            fileDirectory = Files.createDirectories(Paths.get(System.getProperty("user.home") + "/lipids"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createAndWriteCSV(String[][] lipidData) {
        if (!(lipidData == null) && !(lipidData.length == 0)) {
            File file = new File(String.valueOf(fileDirectory), "Lipids " + (10000 + (int) (Math.random() * 90000)) + ".csv");
            writeCSV(lipidData, file);
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().open(file);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Failed to open the file: " + file.getAbsolutePath(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Desktop is not supported. The file was saved at: " + file.getAbsolutePath(), "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void writeCSV(String[][] lipidData, File file) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file.getAbsolutePath()));
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.builder().setHeader("Compound Name", "Species Shorthand", "Compound Formula", "Compound Mass", "Adduct", "M/Z", "CAS ID").build())) {
            for (String[] lipidDataString : lipidData) {
                csvPrinter.printRecord(Arrays.asList(lipidDataString));
            }
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(null, "An error occurred while writing to the CSV file "
                    + file.getAbsolutePath() + ". Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void createAndWriteCSVBatchProcessing(String[][] lipidData, String precursorIon) {
        if (!(lipidData == null)) {
            File file = new File(String.valueOf(fileDirectory), "Lipids " + precursorIon + " " + (10000 + (int) (Math.random() * 90000)) + ".csv");
            writeCSV(lipidData, file);
        }
    }

    public void readCSVAndWriteResultsToFile(File file, List<String> adducts, List<LipidType> lipidTypes) throws IOException {
        try (Reader reader = new FileReader(file.getAbsolutePath())) {
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder().build();
            Iterable<CSVRecord> records = csvFormat.parse(reader);
            Set<Double> neutralLossAssociatedIons = new HashSet<>();
            Database database = new Database();
            int numberOfRecords = 0;
            for (CSVRecord record : records) {
                for (int i = 1; i < record.size(); i++) {
                    double number = NumberUtils.toDouble(record.get(i));
                    if (number != 0.0d) {
                        neutralLossAssociatedIons.add(number);
                    }
                }
                try {
                    double precursorIon = NumberUtils.toDouble(record.get(0));
                    createAndWriteCSVBatchProcessing(database.findLipidsCSVFormat(precursorIon,
                            neutralLossAssociatedIons, lipidTypes, adducts), String.valueOf(precursorIon));
                    numberOfRecords++;
                    neutralLossAssociatedIons.clear();
                } catch (SQLException | InvalidFormula_Exception | FattyAcidCreation_Exception e) {
                    throw new RuntimeException(e);
                }
            }
            JOptionPane.showMessageDialog(null, numberOfRecords + " files were created in " + fileDirectory);
        }
    }

    /*public Map<String[], String[][]> readCSVAndSaveResultsAsSet(File file, String adduct) throws IOException {
        try (Reader reader = new FileReader(file.getAbsolutePath())) {
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder().build();
            Iterable<CSVRecord> records = csvFormat.parse(reader);
            Set<Double> neutralLossAssociatedIons = new HashSet<>();
            Database database = new Database();
            Map<String[], String[][]> lipidMap = new HashMap<>();

            for (CSVRecord record : records) {
                for (int i = 1; i < record.size(); i++) {
                    double number = NumberUtils.toDouble(record.get(i));
                    if (number != 0.0d) {
                        neutralLossAssociatedIons.add(number);
                    }
                }
                try {
                    String[] lipidRecordHeader = {String.valueOf(NumberUtils.toDouble(record.get(0)))};
                    String[][] lipidResultsStringArray = database.findLipidsCSVFormat(LipidType.TG, NumberUtils.toDouble(record.get(0)),
                            neutralLossAssociatedIons, adduct);
                    lipidMap.put(lipidRecordHeader, lipidResultsStringArray);
                    neutralLossAssociatedIons.clear();
                } catch (SQLException | InvalidFormula_Exception | FattyAcidCreation_Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return lipidMap;
        }
    }*/
}

package org.example.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CSVUtils {
    private static final String COUNTER_FILE = "counter.ser";
    private static final AtomicLong counter = readCounterFromFile();

    public void createAndWriteCSV(String[][] lipidData) throws IOException {
        if (lipidData == null || lipidData.length == 0) {
            throw new IllegalArgumentException("Current data set is empty.");
        }
        File file = new File(System.getProperty("user.home"), "lipidDataSet" + counter.getAndIncrement() + ".csv");
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
            saveCounterToFile(counter);
        }
    }

    private static AtomicLong readCounterFromFile() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(COUNTER_FILE))) {
            return (AtomicLong) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new AtomicLong(999);
        } // giving an exception. fix this.
    }

    private static void saveCounterToFile(AtomicLong counter) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(COUNTER_FILE))) {
            oos.writeObject(counter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readCSV(File file) throws IOException {
        // todo read CSV method for batch processing
    }

}

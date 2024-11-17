package org.example.databases;

import org.example.domain.*;
import org.example.exceptions.FattyAcidCreation_Exception;
import org.example.exceptions.InvalidFormula_Exception;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.swing.*;
import java.sql.*;
import java.util.*;

public class Database {
    public static Connection connection = null;
    public static DriverManagerDataSource dataSource = null;

    public Database() {
        try {
            String url = "jdbc:mysql://localhost:3306/compounds";
            String username = "root";
            String password = "schoenstatt37";
            connection = DriverManager.getConnection(url, username, password);

            dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver"); // TODO: Replace with org.sqlite.JDBC
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "An error occurred when establishing the database connection. Please try again.");
        }
    }

    public static Set<Double> calculateFattyAcidMassesFromNeutralLosses(LipidType lipidType, double precursorIon, Set<Double> neutralLossAssociatedIonMasses, String adduct) {
        Double[] fattyAcidMassesArray = new Double[neutralLossAssociatedIonMasses.size()];
        int i = 0;
        for (Double neutralLossAssociatedIonMass : neutralLossAssociatedIonMasses) {
            if (neutralLossAssociatedIonMass != 0.0d) {
                if (lipidType.equals(LipidType.TG) && adduct.equals("[M+NH4]+")) {
                    fattyAcidMassesArray[i] = precursorIon - neutralLossAssociatedIonMass - Adduct.getAdductMass(adduct) + PeriodicTable.elements_Map.get(Element.H);
                    //fattyAcidMassesArray[i] = precursorIon - neutralLossAssociatedIonMass - Adduct.getAdductMass(adduct) - PeriodicTable.elements_Map.get(Element.H);
                    System.out.println("1: " + fattyAcidMassesArray[i]);
                } else {
                    fattyAcidMassesArray[i] = precursorIon - neutralLossAssociatedIonMass - Adduct.getAdductMass(adduct);
                    System.out.println("2: " + fattyAcidMassesArray[i]);
                }
                i++;
            }
        }
        return orderSetSmallestToLargest(fattyAcidMassesArray);
    }

    public static Set<FattyAcid> getFattyAcidsFromDatabase(double fattyAcidMass) throws SQLException, InvalidFormula_Exception, FattyAcidCreation_Exception {
        // TODO: METER TOLERANCIA PARA MASS DELTA
        String query = "SELECT chain_id, num_carbons, double_bonds " +
                "FROM chains " +
                "WHERE mass <= ? + 0.05 " +
                "AND mass >= ? - 0.04 " +
                "AND oxidation = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setDouble(1, fattyAcidMass);
        statement.setDouble(2, fattyAcidMass);
        statement.setString(3, "");
        ResultSet resultSet = statement.executeQuery();
        Set<FattyAcid> fattyAcids = new LinkedHashSet<>();
        while (resultSet.next()) {
            String carbonAtoms = resultSet.getString("num_carbons");
            int doubleBonds = resultSet.getInt("double_bonds");
            if (!carbonAtoms.matches("[0-9]+")) {
                fattyAcids.add(new FattyAcid(carbonAtoms, doubleBonds));
            } else {
                fattyAcids.add(new FattyAcid(Integer.parseInt(carbonAtoms), doubleBonds));
            }
        }
        return fattyAcids;
    }

    public static Set<Double> orderSetSmallestToLargest(Double[] doubleList) {
        Arrays.sort(doubleList);
        return new LinkedHashSet<>(Arrays.asList(doubleList));
    }

    // TODO, FIX METHOD
    public String[][] findLipidsCSVFormat(LipidType lipidType, double precursorIon, Set<Double> neutralLossAssociatedIons, String adduct) throws SQLException, InvalidFormula_Exception, FattyAcidCreation_Exception {
        QueryParameters queryParameters = new QueryParameters();
        LinkedHashSet<MSLipid> lipidSet = queryParameters.returnSetOfLipidsFoundInDatabase(lipidType, precursorIon, neutralLossAssociatedIons, adduct);
        String[][] lipidData = new String[lipidSet.size()][7];
        // RE-DO THIS
        //MainPageUI.createLipidDataForTable(lipidSet, lipidData, adduct);
        return lipidData;
    }

}


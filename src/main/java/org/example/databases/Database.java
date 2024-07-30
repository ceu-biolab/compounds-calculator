package org.example.databases;

import org.example.domain.*;
import org.example.exceptions.FattyAcidCreation_Exception;
import org.example.exceptions.InvalidFormula_Exception;

import java.sql.*;
import java.util.*;

public class Database {
    private static Connection connection = null;
    private final String url = "jdbc:mysql://localhost:3306/compounds";
    private final String username = "root";
    private final String password = "schoenstatt37";

    public Database() {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Set<FattyAcid> getFattyAcidsFromDatabase(double fattyAcidMass) throws SQLException, InvalidFormula_Exception, FattyAcidCreation_Exception {
        String query = "SELECT chain_id, num_carbons, double_bonds " + "FROM chains " + "WHERE mass <= " + fattyAcidMass + " + 0.01 " + "AND mass >= " + fattyAcidMass + " - 0.01 " + "AND oxidation = ''";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        Set<FattyAcid> fattyAcids = new LinkedHashSet<>();

        while (resultSet.next()) {
            int carbonAtoms = resultSet.getInt("num_carbons");
            int doubleBonds = resultSet.getInt("double_bonds");
            fattyAcids.add(new FattyAcid(carbonAtoms, doubleBonds));
        }
        return fattyAcids;
    }

    public Set<Lipid> getLipidsFromDatabase(LipidType lipidType, double precursorIon, Set<Double> neutralLossAssociatedIonMasses) throws SQLException, FattyAcidCreation_Exception, InvalidFormula_Exception {
        Set<Double> fattyAcidMasses = calculateFattyAcidMasses(precursorIon, neutralLossAssociatedIonMasses);
        LipidSkeletalStructure lipidSkeletalStructure = new LipidSkeletalStructure(lipidType);
        Formula formula = new Formula(lipidSkeletalStructure.getFormula().toString());
        String query = "SELECT DISTINCT compound_name, formula FROM compounds " + "INNER JOIN compound_chain ON compounds.compound_id = compound_chain.compound_id " + "INNER JOIN chains ON chains.chain_id = compound_chain.chain_id " + "WHERE compounds.formula = ?";

        for (double fattyAcidMass : fattyAcidMasses) {
            FattyAcid fattyAcid = getFattyAcidsFromDatabase(fattyAcidMass).iterator().next();
            formula.addFattyAcidToFormula(fattyAcid);
            query += " AND compounds.compound_name LIKE '%" + fattyAcid + "%'";
        }
        query += ";";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, formula.toString());
            ResultSet resultSet = statement.executeQuery();
            Set<Lipid> lipids = new LinkedHashSet<>();

            while (resultSet.next()) {
                String compoundName = resultSet.getString("compound_name");
                try {
                    lipids.add(createLipidFromCompoundName(compoundName));
                } catch (InvalidFormula_Exception | FattyAcidCreation_Exception e) {
                    e.printStackTrace();
                }
            }
            return lipids;
        }
    }

    public Lipid createLipidFromCompoundName(String compoundName) throws InvalidFormula_Exception, FattyAcidCreation_Exception {
        List<String> array = new ArrayList<>(List.of(compoundName.split("\\(|\\)")));
        List<String> array2 = new ArrayList<>(List.of(array.get(1).split("\\/")));
        List<Integer> array3 = new ArrayList<>();
        LinkedHashSet fattyAcids = new LinkedHashSet<>();

        for (int i = 0; i <= array.size(); i++) {
            array3.add(Integer.parseInt(array2.get(i).split("\\:")[0]));
            array3.add(Integer.parseInt(array2.get(i).split("\\:")[1]));
        }

        for (int i = 0; i < array3.size(); i += 2) {
            fattyAcids.add(new FattyAcid(array3.get(i), array3.get(i + 1)));
        }

        LipidType lipidType = LipidType.valueOf(array.get(0));
        return new Lipid(fattyAcids, new LipidSkeletalStructure(lipidType));
    }

    public Set<Double> calculateFattyAcidMasses(double precursorIon, Set<Double> neutralLossAssociatedIonMasses) throws SQLException, FattyAcidCreation_Exception {
        Set<Double> fattyAcidMasses = new LinkedHashSet<>();
        Iterator<Double> iterator = neutralLossAssociatedIonMasses.iterator();

        while (iterator.hasNext()) {
            fattyAcidMasses.add(precursorIon - iterator.next() - PeriodicTable.NH3Mass);
        }

        return fattyAcidMasses;
    }
}


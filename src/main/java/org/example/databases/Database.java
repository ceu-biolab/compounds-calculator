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

    public static void main(String[] args) throws SQLException, InvalidFormula_Exception, FattyAcidCreation_Exception {
        Database database = new Database();
        Set<Double> fattyAcidMasses = new LinkedHashSet<>();
        fattyAcidMasses.add(467.4092d);
        fattyAcidMasses.add(439.3784d);
        fattyAcidMasses.add(411.3458d);
        System.out.println(database.getLipidsFromDatabase(LipidType.TG, 656.5862d, fattyAcidMasses));
    }

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

    public Set<MSLipid> getLipidsFromDatabase(LipidType lipidType, double precursorIon, Set<Double> neutralLossAssociatedIonMasses) throws SQLException, FattyAcidCreation_Exception, InvalidFormula_Exception {
        Set<Double> fattyAcidMasses = calculateFattyAcidMasses(precursorIon, neutralLossAssociatedIonMasses);
        LipidSkeletalStructure lipidSkeletalStructure = new LipidSkeletalStructure(lipidType);
        Formula formula = new Formula(lipidSkeletalStructure.getFormula().toString());
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT DISTINCT compounds.cas_id, compounds.compound_name, compounds.formula, compounds.mass, compound_chain.number_chains " +
                        "FROM compounds " +
                        "INNER JOIN compound_chain ON compounds.compound_id = compound_chain.compound_id " +
                        "INNER JOIN chains ON chains.chain_id = compound_chain.chain_id " +
                        "WHERE compounds.formula = ?");
        for (double fattyAcidMass : fattyAcidMasses) {
            FattyAcid fattyAcid = getFattyAcidsFromDatabase(fattyAcidMass).iterator().next();
            formula.addFattyAcidToFormula(fattyAcid);
            queryBuilder.append(" AND compounds.compound_name LIKE ?");
        }
        queryBuilder.append(";");
        String query = queryBuilder.toString();
        LinkedHashSet<MSLipid> lipidsWithInfo = new LinkedHashSet<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, formula.toString());

            int index = 2;
            for (double fattyAcidMass : fattyAcidMasses) {
                statement.setString(index++, "%" + getFattyAcidsFromDatabase(fattyAcidMass).iterator().next() + "%");
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String compoundNameDatabase = resultSet.getString("compound_name");
                    String casId = resultSet.getString("cas_id");
                    String formulaString = resultSet.getString("formula");
                    double mass = resultSet.getDouble("mass");
                    lipidsWithInfo.add(createLipidFromCompoundName(compoundNameDatabase, casId, formulaString, mass));
                }
            } catch (InvalidFormula_Exception | FattyAcidCreation_Exception e) {
                e.printStackTrace();
            }
        }
        return lipidsWithInfo;
    }

    public MSLipid createLipidFromCompoundName(String compoundNameDB, String casId, String formulaString, double mass) throws InvalidFormula_Exception, FattyAcidCreation_Exception {
        List<String> array = new ArrayList<>(List.of(compoundNameDB.split("\\(|\\)")));
        List<String> array2 = new ArrayList<>(List.of(array.get(1).split("\\/")));
        List<String> array3 = new ArrayList<>();
        LinkedHashSet fattyAcids = new LinkedHashSet<>();

        for (int i = 0; i <= array.size(); i++) {
            array3.add(array2.get(i).split("\\:")[0]);
            array3.add(array2.get(i).split("\\:")[1]);
        }

        for (int i = 0; i < array3.size(); i += 2) {
            if (!array3.get(i).contains("[a-zA-Z]+")) {
                fattyAcids.add(new FattyAcid(array3.get(i), Integer.parseInt(array3.get(i + 1))));
            }
        }

        LipidType lipidType = LipidType.valueOf(array.get(0));
        return new MSLipid(fattyAcids, new LipidSkeletalStructure(lipidType), compoundNameDB, casId, formulaString, mass);
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


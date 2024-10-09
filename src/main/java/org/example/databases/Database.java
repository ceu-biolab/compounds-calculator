package org.example.databases;

import org.example.domain.*;
import org.example.exceptions.FattyAcidCreation_Exception;
import org.example.exceptions.InvalidFormula_Exception;
import org.example.ui.MainPageUI;

import javax.swing.*;
import java.sql.*;
import java.util.*;

public class Database {
    private static Connection connection = null;

    public Database() {
        try {
            String url = "jdbc:mysql://localhost:3306/compounds";
            String username = "root";
            String password = "schoenstatt37";
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "An error occurred when establishing the database connection. Please try again.");
        }
    }

    public Set<Double> calculateFattyAcidMassesFromNeutralLosses(double precursorIon, Set<Double> neutralLossAssociatedIonMasses, String adduct) throws SQLException, FattyAcidCreation_Exception {
        Double[] fattyAcidMassesArray = new Double[neutralLossAssociatedIonMasses.size()];
        int i = 0;
        for (Double neutralLossAssociatedIonMass : neutralLossAssociatedIonMasses) {
            if (neutralLossAssociatedIonMass != 0.0d) {
                fattyAcidMassesArray[i] = precursorIon - neutralLossAssociatedIonMass - Adduct.getAdductMass(adduct);
                i++;
            }
        }
        return orderSetSmallestToLargest(fattyAcidMassesArray);
    }

    public Set<FattyAcid> getFattyAcidsFromDatabase(double fattyAcidMass) throws SQLException, InvalidFormula_Exception, FattyAcidCreation_Exception {
        // TODO METER TOLERANCIA PARA MASS DELTA
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

    public MSLipid createLipidFromDatabase(String compoundNameDB, String compoundID, String formulaString, double mass) throws InvalidFormula_Exception, FattyAcidCreation_Exception {
        List<String> compoundNamesInDatabase = new ArrayList<>(List.of(compoundNameDB.split("[()]")));
        LinkedHashSet<FattyAcid> fattyAcids = new LinkedHashSet<>();
        List<String> fattyAcidsOnly = getFattyAcidsOnly(compoundNamesInDatabase);
        for (int i = 0; i < fattyAcidsOnly.size(); i += 2) {
            if (!fattyAcidsOnly.get(i).matches("[a-zA-Z]+")) {
                fattyAcids.add(new FattyAcid(fattyAcidsOnly.get(i), Integer.parseInt(fattyAcidsOnly.get(i + 1))));
            }
        }
        LipidType lipidType = LipidType.valueOf(compoundNamesInDatabase.get(0).trim());
        return new MSLipid(fattyAcids, new LipidSkeletalStructure(lipidType), compoundNameDB, compoundID, formulaString, mass);
    }

    public Set<MSLipid> limitListOfLipidsAccordingToPrecursorIon(Set<MSLipid> msLipidSet, double precursorIon, String adduct) {
        Set<MSLipid> checkedLipidSet = new LinkedHashSet<>();
        for (MSLipid msLipid : msLipidSet) {
            if (valuesAreApproximate(msLipid.getMass(), adduct, precursorIon)) {
                checkedLipidSet.add(msLipid);
            }
        }
        return checkedLipidSet;
    }

    public boolean valuesAreApproximate(double lipidMass, String adduct, Double precursorIon) {
        int ppmIncrement = (int) Math.round(Math.abs(((lipidMass + Adduct.getAdductMass(adduct)) - precursorIon) * 1000000 / precursorIon));
        return ppmIncrement <= 10000;
    }

    public LinkedHashSet<MSLipid> checkForRepeatedLipids(Set<MSLipid> msLipidSet) throws FattyAcidCreation_Exception {
        LinkedHashSet<MSLipid> checkedLipidSet = new LinkedHashSet<>();
        Map<MSLipid, List<String>> lipidAndFattyAcidsMap = new HashMap<>();
        for (MSLipid msLipid : msLipidSet) {
            List<String> compoundNamesInDatabase = new ArrayList<>(List.of(msLipid.getCompoundName().split("[()]")));
            List<String> fattyAcidsOnly = getFattyAcidsOnly(compoundNamesInDatabase);
            lipidAndFattyAcidsMap.put(msLipid, fattyAcidsOnly);
        }
        for (MSLipid msLipid : msLipidSet) {
            List<String> fattyAcidsOfCurrentLipid = lipidAndFattyAcidsMap.get(msLipid);
            if (fattyAcidsOfCurrentLipid != null) {
                checkedLipidSet.add(msLipid);
                lipidAndFattyAcidsMap.values().removeIf(fattyAcids -> fattyAcids.equals(fattyAcidsOfCurrentLipid));
            }
        }
        return checkedLipidSet;
    }

    private static List<String> getFattyAcidsOnly(List<String> compoundNamesInDatabase) throws FattyAcidCreation_Exception {
        if (compoundNamesInDatabase.size() < 2) {
            throw new IllegalArgumentException("Invalid format.");
        }
        List<String> namesWithoutLipidType = new ArrayList<>(List.of(compoundNamesInDatabase.get(1).split("/")));
        List<String> fattyAcidsOnly = new ArrayList<>();
        for (String s : namesWithoutLipidType) {
            String[] splitArray = s.split(":");
            if (splitArray.length == 2) {
                fattyAcidsOnly.add(splitArray[0]);
                fattyAcidsOnly.add(splitArray[1]);
            } else {
                throw new FattyAcidCreation_Exception("Invalid fatty acid format.");
            }
        }
        return fattyAcidsOnly;
    }

    public Set<Double> orderSetSmallestToLargest(Double[] doubleList) {
        Arrays.sort(doubleList);
        return new LinkedHashSet<>(Arrays.asList(doubleList));
    }

    public String buildQuery() {

        return null;
    }

    /**
     * @param lipidType
     * @param precursorIonMZ
     * @param neutralLossAssociatedIonMZs
     * @return
     * @throws SQLException
     * @throws InvalidFormula_Exception
     * @throws FattyAcidCreation_Exception
     */
    public LinkedHashSet<MSLipid> getAllLipidsFromDatabase(LipidType lipidType, double precursorIonMZ, Set<Double> neutralLossAssociatedIonMZs) throws SQLException, InvalidFormula_Exception, FattyAcidCreation_Exception {
        // todo: Add adduct as a parameter for this method. Fix corresponding usages of method. Add a getAdducts() method in the MainPageUI class which allows us to access
        // the list of adducts anywhere (which is necessary for the CSVUtils class to perform the batch processing with the correct adducts.
        // -- Also, consider that if the adduct is a parameter then this method should be executed as many times as necessary (for each of the adducts)
        Set<Double> fattyAcidMasses;
        LinkedHashSet<MSLipid> lipidsWithInfo = new LinkedHashSet<>();
        String adduct = "[M+H]+";
        if (lipidType.equals(LipidType.TG)) {
            adduct = "[M+NH4]+";
        }

        System.out.println(neutralLossAssociatedIonMZs.size());
        fattyAcidMasses = calculateFattyAcidMassesFromNeutralLosses(precursorIonMZ, neutralLossAssociatedIonMZs, adduct);
        LipidSkeletalStructure lipidSkeletalStructure = new LipidSkeletalStructure(lipidType);
        Formula formulaSkeleton = new Formula(lipidSkeletalStructure.getFormula().toString());
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT DISTINCT compounds.compound_id, compounds.compound_name, compounds.formula, compounds.mass, compound_chain.number_chains " +
                        "FROM compounds " +
                        "INNER JOIN compound_chain ON compounds.compound_id = compound_chain.compound_id " +
                        "INNER JOIN chains ON chains.chain_id = compound_chain.chain_id " +
                        "WHERE compounds.formula = ? " +
                        "AND compounds.compound_name LIKE '" + lipidType + "%'");
        List<FattyAcid> fattyAcids = new ArrayList<>();
        for (double fattyAcidMass : fattyAcidMasses) {
            Iterator<FattyAcid> iterator = getFattyAcidsFromDatabase(fattyAcidMass).iterator();
            if (iterator.hasNext()) {
                FattyAcid fattyAcid = iterator.next();
                fattyAcids.add(fattyAcid);
                formulaSkeleton.addFattyAcidToFormula(fattyAcid);
                queryBuilder.append(" AND compounds.compound_name LIKE '%").append(fattyAcid).append("%' ");
            } else {
                System.err.println("No fatty acids found for mass: " + fattyAcidMass);
            }
        }
        int minimumFattyAcids = LipidTypeCharacteristics.getNumberOfFattyAcids(lipidType).getMinFAs();
        int maximumFattyAcids = LipidTypeCharacteristics.getNumberOfFattyAcids(lipidType).getMaxFAs();
        List<FattyAcid> repeatedFattyAcids = new ArrayList<>(fattyAcids);
        if (minimumFattyAcids == maximumFattyAcids) {
            switch (maximumFattyAcids) {
                case 1:
                    break;
                case 2:
                    // duplicar porque es lyso
                    if (fattyAcids.size() == 1) {
                        fattyAcids.add(fattyAcids.get(0));
                        formulaSkeleton.addFattyAcidToFormula(fattyAcids.get(0));
                        queryBuilder.append(" AND compounds.compound_name LIKE '%").append(fattyAcids.get(0)).append("%' ");
                    }
                    break;
                case 3:
                    lipidsWithInfo = getTGsWhenOneOrTwoFAs(formulaSkeleton, queryBuilder, fattyAcids, repeatedFattyAcids);
                    return lipidsWithInfo;
                case 4:
                    if (fattyAcids.size() == 3) {
                        // todo
                    } else {
                        // not right...
                        // getTGsWhenOneOrTwoFAs(formulaSkeleton, queryBuilder, fattyAcids, repeatedFattyAcids);
                    }
                    break;
            }
        } else {
            if (fattyAcids.size() == 1) {
                // ** Put 2 cases together here, create a UNION of both of these events so that the final list contains ALL the lipids, whether it be 1 or 2**/
            } else if (fattyAcids.size() == 2) {
                // ** ONLY OCCURS IF 2 NL IONS ARE GIVEN...
            }
            /** min 1, max 2 case
            // How do I know when it should be 1 and when it should be 2?...
            // It should just look for both of these cases then, first if there's only 1 FA then if there's 2 FA.
            If there's 2 NL ions, then its always 2 FAs, if there's only 1 NL ion, then its 1 or 2 FAs**/
        }
        queryBuilder.append(";");
        String query = queryBuilder.toString();
        try (ResultSet resultSet = callQuery(query, formulaSkeleton)) {
            return createLipidsFromQuery(resultSet);
        } catch (InvalidFormula_Exception | FattyAcidCreation_Exception e) {
            JOptionPane.showMessageDialog(null, "An error occurred when configuring the lipid structure. Please try again.");
        }
        if (neutralLossAssociatedIonMZs.size() == 2) {
            return checkForRepeatedLipids(limitListOfLipidsAccordingToPrecursorIon(lipidsWithInfo, precursorIonMZ, adduct));
        }
        return null;
    }

    private ResultSet callQuery(String query, Formula formulaSkeleton) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, formulaSkeleton.toString());
        return statement.executeQuery();
    }

    private LinkedHashSet<MSLipid> createLipidsFromQuery(ResultSet resultSet) throws SQLException, InvalidFormula_Exception, FattyAcidCreation_Exception {
        LinkedHashSet<MSLipid> lipidsWithInfo = new LinkedHashSet<>();
        while (resultSet.next()) {
            String compoundNameDatabase = resultSet.getString("compound_name");
            String compoundID = resultSet.getString("compound_id");
            String formulaString = resultSet.getString("formula");
            double mass = resultSet.getDouble("mass");
            lipidsWithInfo.add(createLipidFromDatabase(compoundNameDatabase, compoundID, formulaString, mass));
        }
        return lipidsWithInfo;
    }

    private LinkedHashSet<MSLipid> getTGsWhenOneOrTwoFAs(Formula formula, StringBuilder queryBuilder, List<FattyAcid> fattyAcids, List<FattyAcid> repeatedFattyAcids) throws InvalidFormula_Exception, SQLException, FattyAcidCreation_Exception {
        System.out.println(fattyAcids.size());
        if (fattyAcids.size() == 2) { // if a TG only has 2 neutral loss associated ions
            Formula secondFormula = new Formula(formula.toString());
            repeatedFattyAcids.add(fattyAcids.get(0));
            formula.addFattyAcidToFormula(fattyAcids.get(0));
            queryBuilder.append(" AND compounds.compound_name LIKE '%").append(repeatedFattyAcids.get(2));
            //  TODO replace with 2 queries and sum arrays after
            repeatedFattyAcids.add(fattyAcids.get(0));
            repeatedFattyAcids.add(fattyAcids.get(1));
            repeatedFattyAcids.add(fattyAcids.get(1));
            secondFormula.addFattyAcidToFormula(fattyAcids.get(1));
            StringBuilder secondQueryBuilder = new StringBuilder();
            secondQueryBuilder.append("SELECT DISTINCT compounds.compound_id, compounds.compound_name, compounds.formula, compounds.mass, compound_chain.number_chains " + "FROM compounds " + "INNER JOIN compound_chain ON compounds.compound_id = compound_chain.compound_id " + "INNER JOIN chains ON chains.chain_id = compound_chain.chain_id " + "WHERE compounds.formula = '").append(secondFormula).append("' AND compounds.compound_name LIKE '%").append(repeatedFattyAcids.get(3)).append("%' AND compounds.compound_name LIKE '%").append(repeatedFattyAcids.get(4)).append("%' AND compounds.compound_name LIKE '%").append(repeatedFattyAcids.get(5)).append("%'");
            //** replace queries num_carbon, etc.

            try {
                LinkedHashSet<MSLipid> lipidsWithInfo = createLipidsFromQuery(callQuery(queryBuilder.toString(), formula));
                System.out.println(lipidsWithInfo);
                LinkedHashSet<MSLipid> lipidsWithInfo2 = createLipidsFromQuery(callQuery(secondQueryBuilder.toString(), secondFormula));
                System.out.println(lipidsWithInfo2);
                LinkedHashSet<MSLipid> finalLipidSet = new LinkedHashSet<>();
                finalLipidSet.addAll(lipidsWithInfo);
                finalLipidSet.addAll(lipidsWithInfo2);
                return finalLipidSet;
            } catch (SQLException | FattyAcidCreation_Exception e) {
                throw new RuntimeException(e);
            }
        } else if (fattyAcids.size() == 1) { // if a TG only has 1 neutral loss associated ion
            fattyAcids.add(fattyAcids.get(0));
            formula.addFattyAcidToFormula(fattyAcids.get(0));
            fattyAcids.add(fattyAcids.get(0));
            formula.addFattyAcidToFormula(fattyAcids.get(0));
            queryBuilder.append(" AND compounds.compound_name LIKE '%").append(fattyAcids.get(0)).append("/").append(fattyAcids.get(0)).append("%' ");
            LinkedHashSet<MSLipid> finalLipidSet = createLipidsFromQuery(callQuery(queryBuilder.toString(), formula));
            System.out.println(finalLipidSet);
            //**  CAMBIAR TODOS LOS QUERIES AL MODELO RELACIONAL
            return finalLipidSet;
        }
        return null;
    }

    public String[][] findLipidsCSVFormat(LipidType lipidType, double precursorIon, Set<Double> neutralLossAssociatedIons) throws SQLException, InvalidFormula_Exception, FattyAcidCreation_Exception {
        Set<MSLipid> lipidSet = getAllLipidsFromDatabase(lipidType, precursorIon, neutralLossAssociatedIons);
        String[][] lipidData = new String[lipidSet.size()][7];
        MainPageUI.createLipidDataForTable(lipidSet, lipidData);

        return lipidData;
    }
}

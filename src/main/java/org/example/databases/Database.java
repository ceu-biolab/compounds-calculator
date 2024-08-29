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

    public Set<Double> calculateFattyAcidMasses(double precursorIon, Set<Double> neutralLossAssociatedIonMasses) throws SQLException, FattyAcidCreation_Exception {
        Double[] fattyAcidMassesArray = new Double[neutralLossAssociatedIonMasses.size()];
        int i = 0;
        for (Double neutralLossAssociatedIonMass : neutralLossAssociatedIonMasses) {
            if (neutralLossAssociatedIonMass != 0.0d) {
                fattyAcidMassesArray[i] = precursorIon - neutralLossAssociatedIonMass - PeriodicTable.NH3Mass;
                i++;
            }
        }
        return orderSetSmallestToLargest(fattyAcidMassesArray);
    }

    public Set<FattyAcid> getFattyAcidsFromDatabase(double fattyAcidMass) throws SQLException, InvalidFormula_Exception, FattyAcidCreation_Exception {
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

    public MSLipid createLipidFromCompoundName(String compoundNameDB, String casId, String formulaString, double mass) throws InvalidFormula_Exception, FattyAcidCreation_Exception {
        List<String> array = new ArrayList<>(List.of(compoundNameDB.split("[()]")));

        if (array.size() < 2) {
            throw new IllegalArgumentException("Invalid format.");
        }

        List<String> array2 = new ArrayList<>(List.of(array.get(1).split("\\/")));

        List<String> array3 = new ArrayList<>();
        LinkedHashSet<FattyAcid> fattyAcids = new LinkedHashSet<>();

        for (int i = 0; i < array2.size(); i++) {
            String[] splitArray = array2.get(i).split("\\:");
            if (splitArray.length == 2) {
                array3.add(splitArray[0]);
                array3.add(splitArray[1]);
            } else {
                throw new FattyAcidCreation_Exception("Invalid fatty acid format.");
            }
        }

        for (int i = 0; i < array3.size(); i += 2) {
            if (!array3.get(i).matches("[a-zA-Z]+")) {
                fattyAcids.add(new FattyAcid(array3.get(i), Integer.parseInt(array3.get(i + 1))));
            }
        }

        LipidType lipidType = LipidType.valueOf(array.get(0).trim());
        return new MSLipid(fattyAcids, new LipidSkeletalStructure(lipidType), compoundNameDB, casId, formulaString, mass);
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

    public Set<MSLipid> checkForRepeatedLipids(Set<MSLipid> msLipidSet) { //** Fix this. It should consider lipids with different orders as the same
        Set<MSLipid> checkedLipidSet = new LinkedHashSet<>();
        Set<String> lipidCompoundNames = new HashSet<>();

        for (MSLipid msLipid : msLipidSet) {
            if (lipidCompoundNames.add(msLipid.getCompoundName())) {
                checkedLipidSet.add(msLipid);
            }
        }
        return checkedLipidSet;
    }

    public Set<Double> orderSetSmallestToLargest(Double[] doubleList) {
        Arrays.sort(doubleList);
        return new LinkedHashSet<>(Arrays.asList(doubleList));
    }

    public Set<MSLipid> getAllLipidsFromDatabase(LipidType lipidType, double precursorIon, Set<Double> neutralLossAssociatedIonMasses) throws SQLException, InvalidFormula_Exception, FattyAcidCreation_Exception {
        Set<Double> fattyAcidMasses = new LinkedHashSet<>();
        fattyAcidMasses = calculateFattyAcidMasses(precursorIon, neutralLossAssociatedIonMasses);
        LipidSkeletalStructure lipidSkeletalStructure = new LipidSkeletalStructure(lipidType);
        Formula formula = new Formula(lipidSkeletalStructure.getFormula().toString());
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT DISTINCT compounds.cas_id, compounds.compound_name, compounds.formula, compounds.mass, compound_chain.number_chains " +
                        "FROM compounds " +
                        "INNER JOIN compound_chain ON compounds.compound_id = compound_chain.compound_id " +
                        "INNER JOIN chains ON chains.chain_id = compound_chain.chain_id " +
                        "WHERE compounds.formula = ? ");
        List<FattyAcid> fattyAcids = new ArrayList<>();
        for (double fattyAcidMass : fattyAcidMasses) {
            Iterator<FattyAcid> iterator = getFattyAcidsFromDatabase(fattyAcidMass).iterator();
            if (iterator.hasNext()) {
                FattyAcid fattyAcid = iterator.next();
                fattyAcids.add(fattyAcid);
                formula.addFattyAcidToFormula(fattyAcid);
                queryBuilder.append(" AND compounds.compound_name LIKE '%").append(fattyAcid).append("%' ");
            } else {
                System.err.println("No fatty acids found for mass: " + fattyAcidMass);
            }
        }
        String adduct = "[M+H]+";
        int minimumFattyAcids = LipidTypeCharacteristics.getNumberOfFattyAcids(lipidType).getMinFAs();
        int maximumFattyAcids = LipidTypeCharacteristics.getNumberOfFattyAcids(lipidType).getMaxFAs();
        List<FattyAcid> repeatedFattyAcids = new ArrayList<>(fattyAcids);
        if (minimumFattyAcids == maximumFattyAcids) {
            switch (maximumFattyAcids) {
                case 1:
                    break;
                case 2:
                    if (fattyAcids.size() == 1) {
                        fattyAcids.add(fattyAcids.get(0));
                        formula.addFattyAcidToFormula(fattyAcids.get(0));
                        queryBuilder.append(" AND compounds.compound_name LIKE '%").append(fattyAcids.get(0)).append("%' ");
                    }
                    break;
                case 3:
                    adduct = "[M+NH3]+";
                    if (fattyAcids.size() == 2) {
                        Formula secondFormula = new Formula(formula.toString());
                        repeatedFattyAcids.add(fattyAcids.get(0));
                        formula.addFattyAcidToFormula(fattyAcids.get(0));
                        queryBuilder.append(" AND compounds.compound_name LIKE '%").append(repeatedFattyAcids.get(2)).append("%' UNION ");
                        repeatedFattyAcids.add(fattyAcids.get(0));
                        repeatedFattyAcids.add(fattyAcids.get(1));
                        repeatedFattyAcids.add(fattyAcids.get(1));
                        secondFormula.addFattyAcidToFormula(fattyAcids.get(1));
                        queryBuilder.append("SELECT DISTINCT compounds.cas_id, compounds.compound_name, compounds.formula, compounds.mass, compound_chain.number_chains " + "FROM compounds " + "INNER JOIN compound_chain ON compounds.compound_id = compound_chain.compound_id " + "INNER JOIN chains ON chains.chain_id = compound_chain.chain_id " + "WHERE compounds.formula = '").append(secondFormula).append("' AND compounds.compound_name LIKE '%").append(repeatedFattyAcids.get(3)).append("%' AND compounds.compound_name LIKE '%").append(repeatedFattyAcids.get(4)).append("%' AND compounds.compound_name LIKE '%").append(repeatedFattyAcids.get(5)).append("%'");
                    } else if (fattyAcids.size() == 1) {
                        fattyAcids.add(fattyAcids.get(0));
                        formula.addFattyAcidToFormula(fattyAcids.get(0));
                        fattyAcids.add(fattyAcids.get(0));
                        formula.addFattyAcidToFormula(fattyAcids.get(0));
                        queryBuilder.append(" AND compounds.compound_name LIKE '%").append(fattyAcids.get(0)).append("/").append(fattyAcids.get(0)).append("%' ");
                    }
                    break;
                case 4:
                    if (fattyAcids.size() == 3) {
                        // todo

                    } else if (fattyAcids.size() == 2) {
                        Formula secondFormula = new Formula(formula.toString());
                        repeatedFattyAcids.add(fattyAcids.get(0));
                        formula.addFattyAcidToFormula(fattyAcids.get(0));
                        queryBuilder.append(" AND compounds.compound_name LIKE '%").append(repeatedFattyAcids.get(2)).append("%' UNION ");
                        repeatedFattyAcids.add(fattyAcids.get(0));
                        repeatedFattyAcids.add(fattyAcids.get(1));
                        repeatedFattyAcids.add(fattyAcids.get(1));
                        secondFormula.addFattyAcidToFormula(fattyAcids.get(1));
                        queryBuilder.append("SELECT DISTINCT compounds.cas_id, compounds.compound_name, compounds.formula, compounds.mass, compound_chain.number_chains " + "FROM compounds " + "INNER JOIN compound_chain ON compounds.compound_id = compound_chain.compound_id " + "INNER JOIN chains ON chains.chain_id = compound_chain.chain_id " + "WHERE compounds.formula = '").append(secondFormula).append("' AND compounds.compound_name LIKE '%").append(repeatedFattyAcids.get(3)).append("%' AND compounds.compound_name LIKE '%").append(repeatedFattyAcids.get(4)).append("%' AND compounds.compound_name LIKE '%").append(repeatedFattyAcids.get(5)).append("%'");
                    } else if (fattyAcids.size() == 1) {
                        fattyAcids.add(fattyAcids.get(0));
                        formula.addFattyAcidToFormula(fattyAcids.get(0));
                        fattyAcids.add(fattyAcids.get(0));
                        formula.addFattyAcidToFormula(fattyAcids.get(0));
                        queryBuilder.append(" AND compounds.compound_name LIKE '%").append(fattyAcids.get(0)).append("/").append(fattyAcids.get(0)).append("%' ");
                    }
                    break;
            }
        } else {
            // min 1, max 2 case
            // How do I know when it should be 1 and when it should be 2?...
            // It should just look for both of these cases then, first if there's only 1 FA then if there's 2 FA.
            // If there's 2 NL ions, then its always 2 FAs, if there's only 1 NL ion, then its 1 or 2 FAs
        }
        queryBuilder.append(";");
        String query = queryBuilder.toString();
        LinkedHashSet<MSLipid> lipidsWithInfo = new LinkedHashSet<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, formula.toString());

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
        } catch (NoSuchElementException exception) {
            System.err.println("No fatty acids found for the formula: " + formula);
        }
        if (neutralLossAssociatedIonMasses.size() == 2) {
            return checkForRepeatedLipids(limitListOfLipidsAccordingToPrecursorIon(lipidsWithInfo, precursorIon, adduct));
        } else {
            return lipidsWithInfo;
        }
    }

    public String[][] findLipidsCSVFormat(LipidType lipidType, double precursorIon, Set<Double> neutralLossAssociatedIons) throws SQLException, InvalidFormula_Exception, FattyAcidCreation_Exception {
        Set<MSLipid> lipidSet = getAllLipidsFromDatabase(lipidType, precursorIon, neutralLossAssociatedIons);
        String[][] lipidData = new String[lipidSet.size()][7];
        int i = 0;
        for (MSLipid lipid : lipidSet) {
            lipidData[i][0] = lipid.getCasID();
            lipidData[i][1] = lipid.getCompoundName();
            lipidData[i][2] = lipid.calculateSpeciesShorthand(lipid);
            lipidData[i][3] = lipid.getFormula();
            lipidData[i][4] = String.valueOf(lipid.getMass());
            lipidData[i][5] = "[M+NH4]+";
            lipidData[i][6] = String.valueOf(lipid.calculateMZWithAdduct("[M+NH3]+", 1));
            i++;
        }
        return lipidData;
    }
}

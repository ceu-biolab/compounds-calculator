package org.example.databases;

import org.example.domain.*;
import org.example.exceptions.FattyAcidCreation_Exception;
import org.example.exceptions.InvalidFormula_Exception;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.SQLException;
import java.util.*;

public class QueryParameters {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public QueryParameters() {
        new Database();
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(Database.dataSource);
    }

    public LinkedHashSet<MSLipid> findLipidsInDatabase(LipidType lipidType, double precursorIonMZ, Set<Double> neutralLossAssociatedIonMZs, String adduct) throws InvalidFormula_Exception, SQLException, FattyAcidCreation_Exception {
        Set<Double> fattyAcidMasses = Database.calculateFattyAcidMassesFromNeutralLosses(precursorIonMZ, neutralLossAssociatedIonMZs, adduct);
        LipidSkeletalStructure lipidSkeletalStructure = new LipidSkeletalStructure(lipidType);
        Formula formulaSkeleton = new Formula(lipidSkeletalStructure.getFormula().toString());

        List<FattyAcid> fattyAcids = new ArrayList<>();
        for (double fattyAcidMass : fattyAcidMasses) {
            Iterator<FattyAcid> iterator = Database.getFattyAcidsFromDatabase(fattyAcidMass).iterator();
            if (iterator.hasNext()) {
                FattyAcid fattyAcid = iterator.next();
                fattyAcids.add(fattyAcid);
                formulaSkeleton.addFattyAcidToFormula(fattyAcid);
            } else {
                System.err.println("No fatty acids found for mass: " + fattyAcidMass);
            }
        }

        StringBuilder queryBuilder = new StringBuilder(
                """
                        SELECT
                            cv.compound_id,
                            cv.compound_name,
                            cv.formula,
                            cv.mass,
                            cv.num_chains,
                            cv.number_carbons,
                            cv.double_bonds,
                            cv.lipid_type
                        FROM
                            (SELECT *
                             FROM compounds_view
                             WHERE lipid_type LIKE :lipidType
                             AND number_carbons = :totalNumCarbons
                             AND double_bonds = :totalDoubleBonds) AS cv
                        INNER JOIN
                            compound_chain ON cv.compound_id = compound_chain.compound_id
                        INNER JOIN
                            chains ON chains.chain_id = compound_chain.chain_id
                        """);
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("lipidType", "%" + lipidType + "(%");

        switch (lipidType) {
            case TG -> {
                return findTGsInDatabase(queryBuilder, paramMap, fattyAcids, lipidType, precursorIonMZ, adduct);
            }
        }
        return null;
    }

    public LinkedHashSet<MSLipid> findTGsInDatabase(StringBuilder queryBuilder, MapSqlParameterSource paramMap, List<FattyAcid> fattyAcidList, LipidType lipidType,
                                           double precursorIon, String adduct) {
        switch (fattyAcidList.size()) {
            case 1:
                queryBuilder.append("""
                        WHERE
                            (chains.num_carbons, chains.double_bonds) IN ((:numCarbons1, :doubleBonds1))
                            AND compound_chain.number_chains IN (3)
                        GROUP BY
                            cv.compound_id, cv.compound_name, cv.formula, cv.mass, cv.num_chains, cv.number_carbons, cv.double_bonds, cv.lipid_type
                        HAVING\s
                            SUM(CASE WHEN chains.num_carbons = :numCarbons1 AND chains.double_bonds = :doubleBonds1 THEN 1 ELSE 0 END) = 1;""");
                paramMap.addValue("totalNumCarbons", fattyAcidList.get(0).getCarbonAtoms() * 3);
                paramMap.addValue("totalDoubleBonds", fattyAcidList.get(0).getDoubleBonds() * 3);
                paramMap.addValue("numCarbons1", fattyAcidList.get(0).getCarbonAtoms());
                paramMap.addValue("doubleBonds1", fattyAcidList.get(0).getDoubleBonds());
                return new LinkedHashSet<>(namedJdbcTemplate.query(queryBuilder.toString(), paramMap, new MSLipidRowMapper()));
            case 2:
                queryBuilder.append("""
                        WHERE
                            (chains.num_carbons, chains.double_bonds) IN ((:numCarbons1, :doubleBonds1), (:numCarbons2, :doubleBonds2))
                            AND compound_chain.number_chains IN (1,2)
                        GROUP BY
                            cv.compound_id, cv.compound_name, cv.formula, cv.mass, cv.num_chains, cv.number_carbons, cv.double_bonds, cv.lipid_type
                        HAVING\s
                            SUM(CASE WHEN chains.num_carbons = :numCarbons1 AND chains.double_bonds = :doubleBonds1 THEN 1 ELSE 0 END) = 1
                            AND SUM(CASE WHEN chains.num_carbons = :numCarbons2 AND chains.double_bonds = :doubleBonds2 THEN 1 ELSE 0 END) = 1;""");
                fattyAcidList = predictTGMass(lipidType, precursorIon, fattyAcidList, adduct);
                paramMap.addValue("totalNumCarbons", fattyAcidList.get(0).getCarbonAtoms() + fattyAcidList.get(1).getCarbonAtoms() + fattyAcidList.get(2).getCarbonAtoms());
                paramMap.addValue("totalDoubleBonds", fattyAcidList.get(0).getDoubleBonds() + fattyAcidList.get(1).getDoubleBonds() + fattyAcidList.get(2).getDoubleBonds());
                paramMap.addValue("numCarbons1", fattyAcidList.get(0).getCarbonAtoms());
                paramMap.addValue("doubleBonds1", fattyAcidList.get(0).getDoubleBonds());
                paramMap.addValue("numCarbons2", fattyAcidList.get(1).getCarbonAtoms());
                paramMap.addValue("doubleBonds2", fattyAcidList.get(1).getDoubleBonds());
                return new LinkedHashSet<>(namedJdbcTemplate.query(queryBuilder.toString(), paramMap, new MSLipidRowMapper()));
            case 3:
                queryBuilder.append("""
                        WHERE
                            (chains.num_carbons, chains.double_bonds) IN ((:numCarbons1, :doubleBonds1), (:numCarbons2, :doubleBonds2), (:numCarbons3, :doubleBonds3))
                            AND compound_chain.number_chains IN (1)
                        GROUP BY
                            cv.compound_id, cv.compound_name, cv.formula, cv.mass, cv.num_chains, cv.number_carbons, cv.double_bonds, cv.lipid_type
                        HAVING\s
                            SUM(CASE WHEN chains.num_carbons = :numCarbons1 AND chains.double_bonds = :doubleBonds1 THEN 1 ELSE 0 END) = 1
                            AND SUM(CASE WHEN chains.num_carbons = :numCarbons2 AND chains.double_bonds = :doubleBonds2 THEN 1 ELSE 0 END) = 1
                            AND SUM(CASE WHEN chains.num_carbons = :numCarbons3 AND chains.double_bonds = :doubleBonds3 THEN 1 ELSE 0 END) = 1;""");
                paramMap.addValue("totalNumCarbons", fattyAcidList.get(0).getCarbonAtoms() + fattyAcidList.get(1).getCarbonAtoms() + fattyAcidList.get(2).getCarbonAtoms());
                paramMap.addValue("totalDoubleBonds", fattyAcidList.get(0).getDoubleBonds() + fattyAcidList.get(1).getDoubleBonds() + fattyAcidList.get(2).getDoubleBonds());
                paramMap.addValue("numCarbons1", fattyAcidList.get(0).getCarbonAtoms());
                paramMap.addValue("doubleBonds1", fattyAcidList.get(0).getDoubleBonds());
                paramMap.addValue("numCarbons2", fattyAcidList.get(1).getCarbonAtoms());
                paramMap.addValue("doubleBonds2", fattyAcidList.get(1).getDoubleBonds());
                paramMap.addValue("numCarbons3", fattyAcidList.get(2).getCarbonAtoms());
                paramMap.addValue("doubleBonds3", fattyAcidList.get(2).getDoubleBonds());
                return new LinkedHashSet<>(namedJdbcTemplate.query(queryBuilder.toString(), paramMap, new MSLipidRowMapper()));
        }
        return null;
    }

    private List<FattyAcid> predictTGMass(LipidType lipidType, double precursorIon, List<FattyAcid> fattyAcidList, String adduct) {
        LipidSkeletalStructure lipidSkeletalStructure = new LipidSkeletalStructure(lipidType);

        double predictedMass1 = lipidSkeletalStructure.getMass(lipidSkeletalStructure.getFormula()) + fattyAcidList.get(0).getMass(fattyAcidList.get(0).getFormula()) + fattyAcidList.get(0).getMass(fattyAcidList.get(0).getFormula()) +
                fattyAcidList.get(1).getMass(fattyAcidList.get(1).getFormula()) - 3 * PeriodicTable.waterMass;
        double predictedMass2 = lipidSkeletalStructure.getMass(lipidSkeletalStructure.getFormula()) + fattyAcidList.get(0).getMass(fattyAcidList.get(0).getFormula()) + fattyAcidList.get(1).getMass(fattyAcidList.get(1).getFormula()) +
                fattyAcidList.get(1).getMass(fattyAcidList.get(1).getFormula()) - 3 * PeriodicTable.waterMass;
        double expectedMass = precursorIon - Adduct.getAdductMass(adduct) - PeriodicTable.elements_Map.get(Element.H);
        double tolerance = 0.5d;

        if (Math.abs(predictedMass1 - expectedMass) < tolerance) {
            fattyAcidList.add(fattyAcidList.get(0));
            return fattyAcidList;
        } else if (Math.abs(predictedMass2 - expectedMass) < tolerance) {
            fattyAcidList.add(fattyAcidList.get(1));
            return fattyAcidList;
        }
        return null;
    }

    public static void main(String[] args) throws InvalidFormula_Exception, FattyAcidCreation_Exception, SQLException {
        QueryParameters queryParameters = new QueryParameters();
        /*List<FattyAcid> fattyAcidList = new ArrayList<>();
        fattyAcidList.add(new FattyAcid(10, 0));
        fattyAcidList.add(new FattyAcid(12, 0));
        System.out.println(queryParameters.predictTGMass(LipidType.TG, 600.4707, fattyAcidList, "[M+NH4]+"));*/

        Set<Double> neutralLossIons1 = new HashSet<>();
        neutralLossIons1.add(531.4924);
        Set<Double> neutralLossIons2 = new HashSet<>();
        neutralLossIons2.add(383.3159);
        neutralLossIons2.add(411.3476);
        Set<Double> neutralLossIons3 = new HashSet<>();
        neutralLossIons3.add(411.3473);
        neutralLossIons3.add(439.3789);
        Set<Double> neutralLossIons4 = new HashSet<>();
        neutralLossIons4.add(411.3458);
        neutralLossIons4.add(439.3784);
        neutralLossIons4.add(467.4092);
        Set<Double> neutralLossIons5 = new HashSet<>();
        neutralLossIons5.add(467.4080);
        neutralLossIons5.add(567.4420);
        neutralLossIons5.add(595.4714);

        System.out.println("TG(16:0/16:0/16:0): " + queryParameters.findLipidsInDatabase(LipidType.TG, 804.7470, neutralLossIons1, "[M+NH4]+"));
        System.out.println("TG(10:0/10:0/12:0): " + queryParameters.findLipidsInDatabase(LipidType.TG, 600.4707, neutralLossIons2, "[M+NH4]+"));
        System.out.println("TG(10:0/12:0/12:0): " + queryParameters.findLipidsInDatabase(LipidType.TG, 628.5521, neutralLossIons3, "[M+NH4]+"));
        System.out.println("TG(10:0/12:0/14:0): " + queryParameters.findLipidsInDatabase(LipidType.TG, 656.5862, neutralLossIons4, "[M+NH4]+"));
        System.out.println("TG(12:0/14:0/22:6): " + queryParameters.findLipidsInDatabase(LipidType.TG, 812.6760, neutralLossIons5, "[M+NH4]+"));
    }
}

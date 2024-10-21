package org.example.databases;

import org.example.domain.FattyAcid;
import org.example.domain.LipidSkeletalStructure;
import org.example.domain.LipidType;
import org.example.domain.MSLipid;
import org.example.exceptions.FattyAcidCreation_Exception;
import org.example.exceptions.InvalidFormula_Exception;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class MSLipidRowMapper implements RowMapper<MSLipid> {

    @Override
    public MSLipid mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        String compoundNameDatabase = resultSet.getString("compound_name");
        String compoundID = resultSet.getString("compound_id");
        String formulaString = resultSet.getString("formula");
        double mass = resultSet.getDouble("mass");
        try {
            return createLipidFromDatabase(compoundNameDatabase, compoundID, formulaString, mass);
        } catch (InvalidFormula_Exception | FattyAcidCreation_Exception e) {
            throw new SQLException("Error creating lipid from database", e);
        }
    }

    public MSLipid createLipidFromDatabase(String compoundNameDB, String compoundID, String formulaString, double mass)
            throws InvalidFormula_Exception, FattyAcidCreation_Exception {
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
}

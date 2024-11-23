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
import java.util.List;
import java.util.Objects;

public class MSLipidRowMapper implements RowMapper<MSLipid> {

    List<FattyAcid> fattyAcids;

    public MSLipidRowMapper(List<FattyAcid> fattyAcids) {
        this.fattyAcids = fattyAcids;
    }

    /**
     * Defines the RowMapper of MSLipids that correspond to data introduced by the user
     * @param resultSet Result set to be mapped of lipids found in the database according to user input
     * @param rowNum Current row number
     * @return MSLipid with attributes found in the database
     * @throws SQLException Error obtaining result set from database
     */
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

    /**
     * Creates an MSLipid object according to information obtained from the database: compound name, id, formula, and mass
     * @param compoundNameDB    Lipid name in database
     * @param compoundID        Lipid id in database
     * @param formulaString     Lipid formula in database
     * @param mass              Lipid mass in database
     * @return MSLipid object with attributes found in the database
     * @throws InvalidFormula_Exception Invalid formula format
     * @throws FattyAcidCreation_Exception Invalid fatty acid format
     */
    public MSLipid createLipidFromDatabase(String compoundNameDB, String compoundID, String formulaString, double mass)
            throws InvalidFormula_Exception, FattyAcidCreation_Exception {
        List<String> compoundNamesInDatabase = new ArrayList<>(List.of(compoundNameDB.split("[()]")));
        LipidType lipidType = LipidType.valueOf(compoundNamesInDatabase.get(0).trim());
        return new MSLipid(this.fattyAcids, new LipidSkeletalStructure(lipidType), compoundNameDB, compoundID, formulaString, mass);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MSLipidRowMapper that = (MSLipidRowMapper) o;
        return Objects.equals(fattyAcids, that.fattyAcids);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fattyAcids);
    }

    @Override
    public String toString() {
        return "MSLipidRowMapper{" +
                "fattyAcids=" + fattyAcids +
                '}';
    }
}

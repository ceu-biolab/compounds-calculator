package org.example.databases;

import org.example.domain.LipidType;
import org.example.domain.MSLipid;
import org.example.exceptions.FattyAcidCreation_Exception;
import org.example.exceptions.InvalidFormula_Exception;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class QueryParametersTest {
    QueryParameters queryParameters;

    @BeforeEach
    void setUp() {
        queryParameters = new QueryParameters();
    }

    @Test
    void returnSetOfLipidsFoundInDatabaseTG_3RepeatedFAs() throws SQLException, InvalidFormula_Exception, FattyAcidCreation_Exception {
        Set<Double> neutralLossAssociatedIonMZs = new LinkedHashSet<>();
        neutralLossAssociatedIonMZs.add(531.4924d);
        Set<MSLipid> actualLipidSet = queryParameters.returnSetOfLipidsFoundInDatabase(LipidType.TG, 804.7470d, neutralLossAssociatedIonMZs, "[M+NH4-H]+");
        assertTrue(actualLipidSet.toString().contains("TG(16:0/16:0/16:0)"));
    }

    @Test
    void returnSetOfLipidsFoundInDatabaseTG_2RepeatedFAs() throws SQLException, InvalidFormula_Exception, FattyAcidCreation_Exception {
        Set<Double> neutralLossAssociatedIonMZs = new LinkedHashSet<>();
        neutralLossAssociatedIonMZs.add(383.3159);
        neutralLossAssociatedIonMZs.add(411.3476);
        Set<MSLipid> actualLipidSet = queryParameters.returnSetOfLipidsFoundInDatabase(LipidType.TG, 600.4707d, neutralLossAssociatedIonMZs, "[M+NH4-H]+");
        assertTrue(actualLipidSet.toString().contains("TG(10:0/10:0/12:0)"));
    }

    @Test
    void returnSetOfLipidsFoundInDatabaseTG_NoRepeatedFAs() throws SQLException, InvalidFormula_Exception, FattyAcidCreation_Exception {
        Set<Double> neutralLossAssociatedIonMZs = new LinkedHashSet<>();
        neutralLossAssociatedIonMZs.add(411.3458);
        neutralLossAssociatedIonMZs.add(439.3784);
        neutralLossAssociatedIonMZs.add(467.4092);
        Set<MSLipid> actualLipidSet = queryParameters.returnSetOfLipidsFoundInDatabase(LipidType.TG, 656.5862d, neutralLossAssociatedIonMZs, "[M+NH4-H]+");
        assertTrue(actualLipidSet.toString().contains("TG(10:0/12:0/14:0)"));
    }

    @Test
    void returnSetOfLipidsFoundInDatabaseTG_4NLIons() throws SQLException, InvalidFormula_Exception, FattyAcidCreation_Exception {
        Set<Double> neutralLossAssociatedIonMZs = new LinkedHashSet<>();
        neutralLossAssociatedIonMZs.add(439.3783);
        neutralLossAssociatedIonMZs.add(491.4076);
        neutralLossAssociatedIonMZs.add(519.4411);
        neutralLossAssociatedIonMZs.add(547.4712);
        Set<MSLipid> actualLipidSet = queryParameters.returnSetOfLipidsFoundInDatabase(LipidType.TG, 736.6429d, neutralLossAssociatedIonMZs, "[M+NH4-H]+");
        assertTrue(actualLipidSet.toString().contains("TG(12:0/12:0/18:2(9Z,12Z))[iso3]"));
        // Not found in database: assertTrue(actualLipidSet.toString().contains("TG(10:0/14:0/18:2)"));
    }

    @Test
    void returnSetOfLipidsFoundInDatabaseTG_5NLIons() throws SQLException, InvalidFormula_Exception, FattyAcidCreation_Exception {
        Set<Double> neutralLossAssociatedIonMZs = new LinkedHashSet<>();
        neutralLossAssociatedIonMZs.add(439.3786);
        neutralLossAssociatedIonMZs.add(467.4099);
        neutralLossAssociatedIonMZs.add(495.4412);
        neutralLossAssociatedIonMZs.add(523.4725);
        neutralLossAssociatedIonMZs.add(551.5038);
        Set<MSLipid> actualLipidSet = queryParameters.returnSetOfLipidsFoundInDatabase(LipidType.TG, 740.6766d, neutralLossAssociatedIonMZs, "[M+NH4-H]+");
        assertTrue(actualLipidSet.toString().contains("TG(12:0/14:0/16:0)"));
        assertTrue(actualLipidSet.toString().contains("TG(10:0/14:0/18:0)"));
        assertTrue(actualLipidSet.toString().contains("TG(10:0/16:0/16:0)"));
    }

    @Test
    void returnSetOfLipidsFoundInDatabaseTG_6NLIons() throws SQLException, InvalidFormula_Exception, FattyAcidCreation_Exception {
        Set<Double> neutralLossAssociatedIonMZs = new LinkedHashSet<>();
        neutralLossAssociatedIonMZs.add(467.4088);
        neutralLossAssociatedIonMZs.add(491.4100);
        neutralLossAssociatedIonMZs.add(493.4250);
        neutralLossAssociatedIonMZs.add(519.4413);
        neutralLossAssociatedIonMZs.add(547.4726);
        neutralLossAssociatedIonMZs.add(575.5039);
        Set<MSLipid> actualLipidSet = queryParameters.returnSetOfLipidsFoundInDatabase(LipidType.TG, 764.6767d, neutralLossAssociatedIonMZs, "[M+NH4-H]+");
        // Not found in database: assertTrue(actualLipidSet.toString().contains("TG(10:0/16:0/18:2)"));
        assertTrue(actualLipidSet.toString().contains("TG(12:0/16:1(9Z)/16:1(9Z))[iso3]"));
        assertTrue(actualLipidSet.toString().contains("TG(12:0/14:0/18:2(9Z,12Z))[iso6]"));
    }

    @Test
    void findPossibleCombinationsOfFAsWhenCoelution() {

    }

    @Test
    void generateCombinations() {

    }
}
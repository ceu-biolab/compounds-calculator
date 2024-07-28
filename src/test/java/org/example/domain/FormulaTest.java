package org.example.domain;

import org.example.exceptions.FattyAcidCreation_Exception;
import org.example.exceptions.InvalidFormula_Exception;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.*;

class FormulaTest {
    private Formula formula;
    private Formula formulaH2O;
    private FattyAcid fattyAcid;
    private Lipid lipid;

    @BeforeEach
    void setUp() throws InvalidFormula_Exception, FattyAcidCreation_Exception {
        formula = new Formula("C23H44O5");
        formulaH2O = new Formula("C35H66O6");

        fattyAcid = new FattyAcid(12, 0);

        LinkedHashSet<FattyAcid> fattyAcids = new LinkedHashSet<>();
        fattyAcids.add(new FattyAcid(12, 0));
        fattyAcids.add(new FattyAcid(10, 0));
        fattyAcids.add(new FattyAcid(10, 0));
        lipid = new Lipid(fattyAcids, new LipidSkeletalStructure(LipidType.TG));
    }

    @Test
    void calculateMZ() {
        double expectedMZ = 582.485940 / 1;
        double actualMZ = formulaH2O.calculateMZ(lipid.calculateTotalMass(), 1);
        assertEquals(expectedMZ, actualMZ, 0.01);
    }

    @Test
    void removeH2OFromFormula() throws InvalidFormula_Exception {
        Formula expectedFormula = new Formula("C35H64O5");
        Formula actualFormula = formulaH2O.removeH2OFromFormula();
        assertEquals(expectedFormula.toString(), actualFormula.toString());
    }

    @Test
    void addFattyAcidToFormula() throws InvalidFormula_Exception {
        Formula expectedFormula = new Formula("C35H66O6");
        Formula actualFormula = formula.addFattyAcidToFormula(fattyAcid);
        assertEquals(expectedFormula.toString(), actualFormula.toString());
    }
}
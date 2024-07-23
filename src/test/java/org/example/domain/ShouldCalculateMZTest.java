package org.example.domain;

import org.example.exceptions.FattyAcidCreation_Exception;
import org.example.exceptions.InvalidFormula_Exception;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.*;

class ShouldCalculateMZTest {
    private Lipid lipid;
    private Formula formula;

    @BeforeEach
    void setUp() throws InvalidFormula_Exception, FattyAcidCreation_Exception {
        LinkedHashSet<FattyAcid> fattyAcids = new LinkedHashSet<>();
        fattyAcids.add(new FattyAcid(12, 0));
        fattyAcids.add(new FattyAcid(10, 0));
        fattyAcids.add(new FattyAcid(10, 0));
        lipid = new Lipid(fattyAcids, new LipidSkeletalStructure(LipidType.TG));
        formula = new Formula("C35H66O6");
    }

    @Test
    void calculateMZ() {
        double expectedMZ = 582.485940 / 1;
        double actualMZ = formula.calculateMZ(lipid.calculateTotalMass(), 1);
        assertEquals(expectedMZ, actualMZ, 0.01);
    }
}
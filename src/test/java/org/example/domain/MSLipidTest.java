package org.example.domain;

import org.example.exceptions.FattyAcidCreation_Exception;
import org.example.exceptions.InvalidFormula_Exception;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.*;

class MSLipidTest {
    private MSLipid msLipid = null;

    @BeforeEach
    void setUp() throws InvalidFormula_Exception, FattyAcidCreation_Exception {
        LinkedHashSet<FattyAcid> fattyAcids = new LinkedHashSet<>();
        fattyAcids.add(new FattyAcid(12, 0));
        fattyAcids.add(new FattyAcid(10, 0));
        fattyAcids.add(new FattyAcid(10, 0));
        msLipid = new MSLipid(fattyAcids, new LipidSkeletalStructure(LipidType.TG));
    }

    @Test
    void calculateTotalMassWithAdduct() {
        double expectedMass = 600.5198d;
        double actualMass = msLipid.calculateTotalMassWithAdduct("[M+NH4]+");
        assertEquals(expectedMass, actualMass, 0.001);
    }
}
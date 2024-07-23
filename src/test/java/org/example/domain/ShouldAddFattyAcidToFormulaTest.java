package org.example.domain;

import org.example.exceptions.FattyAcidCreation_Exception;
import org.example.exceptions.InvalidFormula_Exception;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShouldAddFattyAcidToFormulaTest {
    private Formula formula;
    private FattyAcid fattyAcid;
    @BeforeEach
    void setUp() throws InvalidFormula_Exception, FattyAcidCreation_Exception {
        formula = new Formula("C23H44O5");
        fattyAcid = new FattyAcid(12, 0);
    }

    @Test
    void addFattyAcidToFormula() throws InvalidFormula_Exception {
        Formula expectedFormula = new Formula("C35H66O6");
        Formula actualFormula = formula.addFattyAcidToFormula(fattyAcid);
        assertEquals(expectedFormula.toString(), actualFormula.toString());
    }
}
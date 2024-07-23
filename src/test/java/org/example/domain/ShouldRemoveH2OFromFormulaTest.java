package org.example.domain;

import org.example.exceptions.InvalidFormula_Exception;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShouldRemoveH2OFromFormulaTest {
    private Formula formula;

    @BeforeEach
    void setUp() throws InvalidFormula_Exception {
        formula = new Formula("C35H66O6");
    }

    @Test
    void removeH2OFromFormula() throws InvalidFormula_Exception {
        Formula expectedFormula = new Formula("C35H64O5");
        Formula actualFormula = formula.removeH2OFromFormula();
        assertEquals(expectedFormula.toString(), actualFormula.toString());
    }
}
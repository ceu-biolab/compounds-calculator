package org.example.domain;

import org.example.exceptions.InvalidFormula_Exception;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChemicalCompoundTest {
    private ChemicalCompound chemicalCompound;
    private Formula formula;

    @BeforeEach
    void setUp() throws InvalidFormula_Exception {
        chemicalCompound = new ChemicalCompound();
        formula = new Formula("C6H12O6");
    }

    @Test
    void getMass() {
        double expectedMass = (6 * 12.00000) + (12 * 1.00783) + (6 * 15.99491);
        double actualMass = chemicalCompound.getMass(this.formula);
        assertEquals(expectedMass, actualMass, 0.01);
    }
}
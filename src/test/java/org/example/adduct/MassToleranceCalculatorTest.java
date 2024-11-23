package org.example.adduct;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MassToleranceCalculatorTest {

    MassToleranceCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new MassToleranceCalculator();
    }

    @Test
    void calculateDelta() {
        assertEquals(1000.0, calculator.calculateDelta(ToleranceType.PPM, 100.0, 100.1), 1.0);
        assertEquals(0.5, calculator.calculateDelta(ToleranceType.Da, 200.5, 200.0), 1.0);
    }

    @Test
    void calculateTolerance() {
        assertEquals(0.1, calculator.calculateTolerance(ToleranceType.PPM, 100.0, 1000.0), 0.01);
        assertEquals(0.5, calculator.calculateTolerance(ToleranceType.Da, 200.5, 0.5), 0.01);
    }
}
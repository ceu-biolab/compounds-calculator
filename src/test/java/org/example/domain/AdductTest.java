package org.example.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdductTest {

    @Test
    void getAdductMass() {
        double expectedMass = 18.03382142d;
        double actualMass = Adduct.getAdductMass("[M+NH4]+");
        assertEquals(expectedMass, actualMass, 0.01);
    }
}
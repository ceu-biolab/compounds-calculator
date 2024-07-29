package org.example.databases;

import org.example.domain.FattyAcid;
import org.example.domain.Lipid;
import org.example.domain.LipidSkeletalStructure;
import org.example.domain.LipidType;
import org.example.exceptions.FattyAcidCreation_Exception;
import org.example.exceptions.InvalidFormula_Exception;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.offset;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {
    private Database database;
    private Set<Double> fattyAcidMasses;
    private LinkedHashSet<FattyAcid> fattyAcids;

    @BeforeEach
    void setUp() {
        database = new Database();
        fattyAcidMasses = new LinkedHashSet<>();
        fattyAcidMasses.add(467.4092);
        fattyAcidMasses.add(439.3784);
        fattyAcidMasses.add(411.3458);

        fattyAcids = new LinkedHashSet<>();
        try {
            fattyAcids.add(new FattyAcid(10, 0));
            fattyAcids.add(new FattyAcid(10, 0));
            fattyAcids.add(new FattyAcid(12, 0));
        } catch (FattyAcidCreation_Exception | InvalidFormula_Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getFattyAcidsFromDatabase() {
        try {
            Set<FattyAcid> expectedSet = new HashSet<>();
            expectedSet.add(new FattyAcid(10, 0));
            Set<FattyAcid> actualSet = database.getFattyAcidsFromDatabase(172.15045d);
            assertEquals(expectedSet.toString(), actualSet.toString());
        } catch (SQLException | InvalidFormula_Exception | FattyAcidCreation_Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void calculateFattyAcidMasses() {
        // ? todo: Method technically works but only if the values are in order... try stream() to order each Set
        try {
            Set<Double> expectedSet = new LinkedHashSet<>();
            expectedSet.add(172.15045);
            expectedSet.add(200.18125);
            expectedSet.add(228.21385);

            Set<Double> actualSet = database.calculateFattyAcidMasses(656.5862, fattyAcidMasses);
            Iterator<Double> expectedIterator = expectedSet.iterator();
            Iterator<Double> actualIterator = actualSet.iterator();
            while (expectedIterator.hasNext()) {
                assertThat(actualIterator.next()).isCloseTo(expectedIterator.next(), offset(2d));
            }
        } catch (SQLException | FattyAcidCreation_Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createLipidFromCompoundName() throws InvalidFormula_Exception, FattyAcidCreation_Exception {
        Lipid expectedLipid = new Lipid(fattyAcids, new LipidSkeletalStructure(LipidType.TG));
        Lipid actualLipid = database.createLipidFromCompoundName("TG(10:0/10:0/12:0)");
        assertEquals(expectedLipid.toString(), actualLipid.toString());
    }

    @Test
    void getLipidsFromDatabase() {

    }
}
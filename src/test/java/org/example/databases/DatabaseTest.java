package org.example.databases;

import org.example.domain.FattyAcid;
import org.example.domain.LipidType;
import org.example.domain.MSLipid;
import org.example.exceptions.FattyAcidCreation_Exception;
import org.example.exceptions.InvalidFormula_Exception;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.*;

import static org.assertj.core.api.Assertions.offset;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {
    private Database database;
    private Set<Double> fattyAcidMasses;
    private LinkedHashSet<FattyAcid> fattyAcids;
    private LinkedHashSet<FattyAcid> fattyAcidsIsoBranching;
    private LinkedHashSet<FattyAcid> fattyAcidsTG36;
    private Set<Double> neutralLossAssociatedIonMZs;

    @BeforeEach
    void setUp() {
        database = new Database();
        fattyAcidMasses = new LinkedHashSet<>();
        fattyAcidMasses.add(467.4092d);
        fattyAcidMasses.add(439.3784d);
        fattyAcidMasses.add(411.3458d);

        fattyAcids = new LinkedHashSet<>();
        fattyAcidsIsoBranching = new LinkedHashSet<>();
        fattyAcidsTG36 = new LinkedHashSet<>();
        try {
            fattyAcids.add(new FattyAcid(10, 0));
            fattyAcids.add(new FattyAcid(10, 0));
            fattyAcids.add(new FattyAcid(12, 0));

            fattyAcidsIsoBranching.add(new FattyAcid("i-12", 0));
            fattyAcidsIsoBranching.add(new FattyAcid(10, 0));
            fattyAcidsIsoBranching.add(new FattyAcid(10, 0));

            fattyAcidsTG36.add(new FattyAcid(10, 0));
            fattyAcidsTG36.add(new FattyAcid(12, 0));
            fattyAcidsTG36.add(new FattyAcid(14, 0));

            neutralLossAssociatedIonMZs = new LinkedHashSet<>();
            neutralLossAssociatedIonMZs.add(531.4924);
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
    void getAllLipidsFromDatabaseWithOnlyOneNeutralLossAssociatedIonMZ() {
        try {
            LinkedHashSet<MSLipid> expectedSet = new LinkedHashSet<>();
            expectedSet.add(database.createLipidFromDatabase("TG(16:0/16:0/16:0)", "18290", "C51H98O6", 806.736340868));
            expectedSet.add(database.createLipidFromDatabase("TG(i-16:0/i-16:0/16:0)", "44473", "C51H98O6", 806.736340876));
            expectedSet.add(database.createLipidFromDatabase("TG(16:0/16:0/i-16:0)", "59798", "C51H98O6", 806.736340876));
            expectedSet.add(database.createLipidFromDatabase("TG(i-16:0/16:0/16:0)", "60082", "C51H98O6", 806.736340876));

            LinkedHashSet<MSLipid> actualSet = database.getAllLipidsFromDatabase(LipidType.TG, 804.7470, neutralLossAssociatedIonMZs);
            System.out.println(actualSet.toString());
            assertEquals(expectedSet.toString(), actualSet.toString());
        } catch (SQLException | InvalidFormula_Exception | FattyAcidCreation_Exception e) {
            throw new RuntimeException(e);
        }
    }


}
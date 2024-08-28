package org.example.domain;

import org.example.exceptions.InvalidFormula_Exception;

import java.util.LinkedHashSet;

public class Lipid {
    private final LinkedHashSet<FattyAcid> fattyAcids;
    private final LipidSkeletalStructure lipidSkeletalStructure;

    public Lipid(LinkedHashSet<FattyAcid> fattyAcids, LipidSkeletalStructure lipidSkeletalStructure) {
        this.lipidSkeletalStructure = lipidSkeletalStructure;
        this.fattyAcids = fattyAcids;
    }

    public Formula calculateFormula() throws InvalidFormula_Exception {
        Formula formula = lipidSkeletalStructure.getFormula();
        for (FattyAcid fattyAcid : fattyAcids) {
            formula.addFattyAcidToFormula(fattyAcid);
        }
        return formula;
    }

    public double calculateTotalMass() {
        double mass = lipidSkeletalStructure.getMass(lipidSkeletalStructure.getFormula());
        for (FattyAcid fattyAcid : fattyAcids) {
            mass += fattyAcid.getMass(fattyAcid.getFormula());
        }
        return mass - (fattyAcids.size() * (PeriodicTable.waterMass));
    }

    public LinkedHashSet<FattyAcid> getFattyAcids() {
        return fattyAcids;
    }

    public LipidSkeletalStructure getLipidSkeletalStructure() {
        return lipidSkeletalStructure;
    }

    @Override
    public String toString() {
        return getLipidSkeletalStructure().getLipidType().toString() + fattyAcids.toString();
    }
}

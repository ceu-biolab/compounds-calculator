package org.example.domain;

import org.example.exceptions.InvalidFormula_Exception;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

public class Lipid {
    private final List<FattyAcid> fattyAcids;
    private final LipidSkeletalStructure lipidSkeletalStructure;

    public Lipid(List<FattyAcid> fattyAcids, LipidSkeletalStructure lipidSkeletalStructure) {
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

    public List<FattyAcid> getFattyAcids() {
        return fattyAcids;
    }

    public LipidSkeletalStructure getLipidSkeletalStructure() {
        return lipidSkeletalStructure;
    }

    public String calculateSpeciesShorthand(MSLipid lipid) {
        int carbonAtoms = 0;
        int doubleBonds = 0;

        for (FattyAcid fattyAcid : lipid.getFattyAcids()) {
            System.out.println("FA: " + fattyAcid);
            carbonAtoms += fattyAcid.getCarbonAtoms();
            doubleBonds += fattyAcid.getDoubleBonds();
        }
        return lipid.getLipidSkeletalStructure().getLipidType().toString() + " " + carbonAtoms + ":" + doubleBonds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lipid lipid = (Lipid) o;
        return Objects.equals(fattyAcids, lipid.fattyAcids) && Objects.equals(lipidSkeletalStructure, lipid.lipidSkeletalStructure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fattyAcids, lipidSkeletalStructure);
    }

    @Override
    public String toString() {
        return getLipidSkeletalStructure().getLipidType().toString() + fattyAcids.toString();
    }
}

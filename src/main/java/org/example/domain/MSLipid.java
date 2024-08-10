package org.example.domain;

import org.example.exceptions.FattyAcidCreation_Exception;
import org.example.exceptions.InvalidFormula_Exception;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MSLipid extends Lipid {
    public String casID;
    public String compoundName;
    public String formula;
    public double mass;

    public MSLipid(LinkedHashSet<FattyAcid> fattyAcids, LipidSkeletalStructure lipidSkeletalStructure, String compoundName, String casID,
                   String compoundNameMSLipid, double mass) {
        super(fattyAcids, lipidSkeletalStructure);
        this.compoundName = compoundName;
        this.casID = casID;
        this.formula = compoundNameMSLipid;
        this.mass = mass;
    }

    public double calculateTotalMassWithAdduct(String adduct) {
        double mass = calculateTotalMass();
        return mass + Adduct.getAdductMass(adduct);
    }

    public Formula addAdductToFormula(Formula formula, String adduct) throws InvalidFormula_Exception {
        // todo
        return new Formula("C");
    }

    public double calculateMZWithAdduct(String adduct, int charge) {
        return calculateTotalMassWithAdduct(adduct) / charge;
    }

    public Set<Lipid> getLipidsByMSData(double precursorIonMZ, List<FattyAcid> fattyAcids, Set<LipidSkeletalStructure> possibleHeads,
                                        Set<Adduct> possibleAdductsForPI, Set<Adduct> possibleAdductsForFAs) {
        Adduct adduct = possibleAdductsForPI.iterator().next();
        double adductPIon = precursorIonMZ - Adduct.getAdductMass(adduct.toString());
        for (FattyAcid fattyAcid : fattyAcids) {
            //todo
            //* require lipid type?
        }
        return null;
    }

    public Set<Lipid> getLipidsAccordingToInput(double precursorIonMZ, List<Double> fattyAcidLossValues) throws InvalidFormula_Exception, FattyAcidCreation_Exception {
        double adductWithPIon = precursorIonMZ - Adduct.getAdductMass("[M+NH4]+");
        List<Double> fattyAcidValues = new ArrayList<>();
        LinkedHashSet<FattyAcid> fattyAcids = new LinkedHashSet<>();
        Set<Lipid> lipids = new LinkedHashSet<>();

        for (Double fattyAcidLossValue : fattyAcidLossValues) {
            fattyAcidValues.add(adductWithPIon - fattyAcidLossValue);
            //getFattyAcids().add(FattyAcid.getFattyAcidByMass());
        }

        fattyAcids.add(new FattyAcid(10, 0)); // * temporary code just for test
        fattyAcids.add(new FattyAcid(12, 0));
        fattyAcids.add(new FattyAcid(14, 0));

        lipids.add(new Lipid(fattyAcids, new LipidSkeletalStructure(LipidType.TG)));
        return lipids;
    }

    public String getCasID() {
        return casID;
    }

    public String getCompoundName() {
        return compoundName;
    }

    public String getFormula() {
        return formula;
    }

    public double getMass() {
        return mass;
    }

    @Override
    public String toString() {
        return "{" + compoundName + ", " + casID + ", " + formula + ", " + mass + "}";
    }
}

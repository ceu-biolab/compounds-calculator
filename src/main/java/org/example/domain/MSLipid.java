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

    public double calculateMZWithAdduct(String adduct, int charge) {
        return calculateTotalMassWithAdduct(adduct) / charge;
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

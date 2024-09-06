package org.example.domain;

import java.text.DecimalFormat;
import java.util.LinkedHashSet;

public class MSLipid extends Lipid {
    public String compoundID;
    public String compoundName;
    public String formula;
    public double mass;

    public MSLipid(LinkedHashSet<FattyAcid> fattyAcids, LipidSkeletalStructure lipidSkeletalStructure, String compoundName, String compoundID,
                   String compoundNameMSLipid, double mass) {
        super(fattyAcids, lipidSkeletalStructure);
        this.compoundName = compoundName;
        this.compoundID = compoundID;
        this.formula = compoundNameMSLipid;
        this.mass = mass;
    }

    public double calculateTotalMassWithAdduct(String adduct) {
        double mass = calculateTotalMass();
        return mass + Adduct.getAdductMass(adduct);
    }

    public String calculateMZWithAdduct(String adduct, int charge) {
        DecimalFormat numberFormat = new DecimalFormat("#.0000");
        return numberFormat.format(calculateTotalMassWithAdduct(adduct) / charge);
    }

    public String getCompoundID() {
        return compoundID;
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
        return "{" + compoundName + ", " + compoundID + ", " + formula + ", " + mass + "}";
    }
}

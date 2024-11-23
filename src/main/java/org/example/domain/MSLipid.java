package org.example.domain;

import java.text.DecimalFormat;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

public class MSLipid extends Lipid {
    public String compoundID;
    public String compoundName;
    public String formula;
    public double mass;

    public MSLipid(List<FattyAcid> fattyAcids, LipidSkeletalStructure lipidSkeletalStructure, String compoundName, String compoundID,
                   String compoundNameMSLipid, double mass) {
        super(fattyAcids, lipidSkeletalStructure);
        this.compoundName = compoundName;
        this.compoundID = compoundID;
        this.formula = compoundNameMSLipid;
        this.mass = mass;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MSLipid msLipid = (MSLipid) o;
        return Double.compare(mass, msLipid.mass) == 0 && Objects.equals(compoundID, msLipid.compoundID) && Objects.equals(compoundName, msLipid.compoundName) && Objects.equals(formula, msLipid.formula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(compoundID, compoundName, formula, mass);
    }

    @Override
    public String toString() {
        return "{" + compoundName + ", " + compoundID + ", " + formula + ", " + mass + "}";
    }
}

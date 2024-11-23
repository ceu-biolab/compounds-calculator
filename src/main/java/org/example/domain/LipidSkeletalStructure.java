package org.example.domain;

import java.util.Objects;

/**
 * Defines the base skeletal structure of a lipid as the skeleton of the lipid head group defined in LipidTypeCharacteristics.
 * @author pilarbourg
 */
public class LipidSkeletalStructure extends ChemicalCompound {
    private final LipidType lipidType;

    /**
     * Class constructor which defines the lipidType, obtains the formula according to the lipid type, and
     * calculates the mass. For example, a triglyceride has a lipidType of TG and begins with the formula and mass
     * of a glycerol, which is the basic lipid head structure of a triglyceride. The formula and mass then change
     * once fatty acids are added to form the lipid.
     * @param lipidType
     */
    public LipidSkeletalStructure(LipidType lipidType) {
        this.lipidType = lipidType;
        this.formula = LipidTypeCharacteristics.lipidHeadStructure.get(lipidType).getFormula();
        mass = getMass(this.formula);
    }

    /**
     * Gets the lipid type that corresponds to a lipid skeletal structure object.
     * @return LipidType object.
     */
    public LipidType getLipidType() {
        return lipidType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LipidSkeletalStructure that = (LipidSkeletalStructure) o;
        return lipidType == that.lipidType;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(lipidType);
    }

    /**
     * Returns the lipid's skeletal structure as a string object.
     * @return String representation of lipidSkeletalStructure attributes.
     */
    @Override
    public String toString() {
        return "LipidSkeletalStructure{" +
                "lipidType=" + lipidType +
                ", formula=" + formula +
                ", mass=" + mass +
                '}';
    }
}
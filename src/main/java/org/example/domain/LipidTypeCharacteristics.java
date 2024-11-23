package org.example.domain;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LipidTypeCharacteristics extends ChemicalCompound {
    public static final Map<LipidType, LipidCharacteristics> lipidHeadStructure = new HashMap<>();
    protected LipidType lipidType;
    protected LipidCharacteristics lipidCharacteristics;

    static {
        try {
            lipidHeadStructure.put(LipidType.CE, new LipidCharacteristics(new Formula("C27H46O"), 1));
            lipidHeadStructure.put(LipidType.MG, new LipidCharacteristics(new Formula("C3H8O3"), 1));
            lipidHeadStructure.put(LipidType.DG, new LipidCharacteristics(new Formula("C3H8O3"), 2));
            lipidHeadStructure.put(LipidType.TG, new LipidCharacteristics(new Formula("C3H8O3"), 3));
            lipidHeadStructure.put(LipidType.PA, new LipidCharacteristics(new Formula("C3H9O6P"), 1, 2));
            lipidHeadStructure.put(LipidType.PC, new LipidCharacteristics(new Formula("C8H20NO6P"), 1, 2));
            lipidHeadStructure.put(LipidType.PE, new LipidCharacteristics(new Formula("C5H14NO6P"), 1, 2));
            lipidHeadStructure.put(LipidType.PI, new LipidCharacteristics(new Formula("C9H19O11P"), 1, 2));
            lipidHeadStructure.put(LipidType.PG, new LipidCharacteristics(new Formula("C6H15O8P"), 1, 2));
            lipidHeadStructure.put(LipidType.PS, new LipidCharacteristics(new Formula("C6H14NO8P"), 1, 2));
            lipidHeadStructure.put(LipidType.CL, new LipidCharacteristics(new Formula("C9H22O13P2"), 4));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid lipid structure. Please try again.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LipidTypeCharacteristics that = (LipidTypeCharacteristics) o;
        return lipidType == that.lipidType && Objects.equals(lipidCharacteristics, that.lipidCharacteristics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lipidType, lipidCharacteristics);
    }

    @Override
    public String toString() {
        return "LipidTypeCharacteristics{" +
                "lipidType=" + lipidType +
                ", lipidCharacteristics=" + lipidCharacteristics.toString() +
                '}';
    }
}

package org.example.domain;

import java.util.HashMap;
import java.util.Map;

public class LipidTypeCharacteristics extends ChemicalCompound {
    public static final Map<LipidType, LipidCharacteristics> lipidHeadStructure = new HashMap<LipidType, LipidCharacteristics>();
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
            e.printStackTrace();
        }
    }

    public static LipidCharacteristics getNumberOfFattyAcids(LipidType lipidType) {
        return lipidHeadStructure.get(lipidType);
    }

    @Override
    public String toString() {
        return "LipidTypeCharacteristics{" +
                "lipidType=" + lipidType +
                ", lipidCharacteristics=" + lipidCharacteristics.toString() +
                '}';
    }
}

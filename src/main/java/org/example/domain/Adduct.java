package org.example.domain;

import java.util.*;

public class Adduct {
    protected static final Map<String, Double> adductMap = new LinkedHashMap<>();
    protected static final Map<String, Double> adductMap_Pos = new LinkedHashMap<>();
    protected static final Map<String, Double> adductMap_Neg = new LinkedHashMap<>();

    static {
        adductMap_Pos.put("None", 0d);
        adductMap_Pos.put("[M-H2O+H]+", -18.01056 + PeriodicTable.elements_Map.get(Element.H) - PeriodicTable.electronMass);
        adductMap_Pos.put("[M+H]+", 1.00783d - PeriodicTable.electronMass);
        adductMap_Pos.put("[M+NH4]+", 18.03437d - PeriodicTable.electronMass);
        adductMap_Pos.put("[M+C2H6N2+H]+", 58.05310d + PeriodicTable.elements_Map.get(Element.H) - PeriodicTable.electronMass);
        adductMap_Pos.put("[M+Na]+", 22.98977d - PeriodicTable.electronMass);
        adductMap_Pos.put("[M+K]+", 38.96371d - PeriodicTable.electronMass);
        adductMap_Pos.put("[M+Pb]+", 34.0239d - PeriodicTable.electronMass);
    }

    static {
        adductMap_Neg.put("None", 0d);
        adductMap_Neg.put("[M-H]-", -1.00783d + PeriodicTable.electronMass);
        adductMap_Neg.put("[M+Cl]-", 34.96885 + PeriodicTable.electronMass);
        adductMap_Neg.put("[M+HCOOH-H]-", 46.00548 - PeriodicTable.elements_Map.get(Element.H) + PeriodicTable.electronMass);
        adductMap_Neg.put("[M+CH3COOH-H]-", 60.02113 - PeriodicTable.elements_Map.get(Element.H) + PeriodicTable.electronMass);
    }

    public static double getAdductMass(String adduct) {
        for (String string : getAllAdducts()) {
            if (string.equals(adduct)) {
                return adductMap.get(string);
            }
        }
        return 0;
    }

    public static List<String> getPositiveAdducts() {
        return new ArrayList<>(adductMap_Pos.keySet());
    }

    public static List<String> getNegativeAdducts() {
        return new ArrayList<>(adductMap_Neg.keySet());
    }

    public static String[] getAllAdducts() {
        adductMap.putAll(adductMap_Pos);
        adductMap.putAll(adductMap_Neg);
        return adductMap.keySet().toArray(new String[0]);
    }

}
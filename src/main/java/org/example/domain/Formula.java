package org.example.domain;

import org.example.exceptions.InvalidFormula_Exception;

import javax.swing.*;
import java.util.*;

public class Formula {
    private final TreeMap<Element, Integer> mapFormula = new TreeMap<>();

    public Formula(String formula) throws InvalidFormula_Exception {
        createFormulaFromString(formula);
    }

    public void createFormulaFromString(String formula) throws IllegalStateException {
        try {
            List<String> array = new ArrayList<>(Arrays.asList(formula.split("(?<=[0-9])(?=[a-zA-Z])")));
            for (String element : array) {
                String[] splitArray = element.split("(?<=[a-zA-Z])(?=[0-9])");
                mapFormula.put(Element.toElement(splitArray[0]), Integer.valueOf(splitArray[1]));
            }
        } catch (IllegalArgumentException | NullPointerException | ArrayIndexOutOfBoundsException e) {
            List<String> array = new ArrayList<>(Arrays.asList(formula.split("(?<=[0-9])(?=[a-zA-Z])|(?=[A-Z])")));
            for (String element : array) {
                String[] splitArray = element.split("(?<=[a-zA-Z])(?=[0-9])");
                if (splitArray.length == 1) {
                    mapFormula.put(Element.toElement(splitArray[0]), 1);
                } else {
                    mapFormula.put(Element.toElement(splitArray[0]), Integer.valueOf(splitArray[1]));
                }
            }
        }
    }

    public double calculateMZ(double exactLipidMass, int charge) {
        return (exactLipidMass / charge);
    }

    public Set<Element> getElements() {
        return mapFormula.keySet();
    }

    public int getElementQuantity(Element c) {
        return mapFormula.get(c);
    }

    public Formula removeH2OFromFormula() throws InvalidFormula_Exception {
        try {
            mapFormula.replace(Element.H, mapFormula.get(Element.H) - 2);
            mapFormula.replace(Element.O, mapFormula.get(Element.O) - 1);
        } catch (ArrayIndexOutOfBoundsException exception) {
            JOptionPane.showMessageDialog(null, "An error occurred while creating the lipid's formula. Please try again.");
        }
        return new Formula(mapToString(mapFormula));
    }

    public Formula addFattyAcidToFormula(FattyAcid fattyAcid) throws InvalidFormula_Exception {
        Formula formula = fattyAcid.getFormula();
        mapFormula.replace(Element.C, mapFormula.get(Element.C) + formula.getElementQuantity(Element.C));
        mapFormula.replace(Element.H, mapFormula.get(Element.H) + formula.getElementQuantity(Element.H));
        mapFormula.replace(Element.O, mapFormula.get(Element.O) + formula.getElementQuantity(Element.O));
        removeH2OFromFormula();
        return new Formula(mapToString(mapFormula));
    }

    public static String mapToString(TreeMap<Element, Integer> mapFormula) {
        StringBuilder formula = new StringBuilder();
        for (Map.Entry<Element, Integer> entry : mapFormula.entrySet()) {
            if (entry.getValue() == 1) {
                formula.append(entry.getKey().name());
            } else {
                formula.append(entry.getKey().name()).append(entry.getValue());
            }
        }
        return formula.toString();
    }

    @Override
    public String toString() {
        return mapToString(mapFormula);
    }
}

package org.example.domain;

import org.example.exceptions.InvalidFormula_Exception;

import javax.swing.*;
import java.util.*;

public class Formula {
    private final TreeMap<Element, Integer> mapFormula = new TreeMap<>();

    public Formula(String formula) throws InvalidFormula_Exception {
        createFormulaFromString(formula);
    }

    /**
     * Defines the formula of a chemical compound via a tree map structure that defines elements as the keys and
     * the number of elements as the values.
     *
     * @param formula String representation of the formula that will be produced.
     */
    public void createFormulaFromString(String formula) {
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

    /**
     * Calculates the mass-to-charge ratio according to the lipid mass divided by the charge of the adduct.
     *
     * @param exactLipidMass Lipid mass as a double.
     * @param charge         Adduct charge as an int.
     * @return Division of the mass by the charge to provide the m/z value as a double.
     */
    public double calculateMZ(double exactLipidMass, int charge) {
        return (exactLipidMass / charge);
    }

    /**
     * Gets the element objects found in the formula map. (ie. H, C, and O)
     *
     * @return Set of keys found in the map formula (since the keys correspond to the elements).
     */
    public Set<Element> getElements() {
        return mapFormula.keySet();
    }

    /**
     * Determines the number of atoms that correspond to a specific element in a formula. (ie. 2 Hydrogen atoms in H2O)
     *
     * @param element Element object to be searched for in the formula map.
     * @return Integer value of the number of atoms of an element.
     */
    public int getElementQuantity(Element element) {
        return mapFormula.get(element);
    }

    /**
     * Removes a molecule of water from the formula map by removing 2 Hydrogen atoms and 1 Oxygen atom.
     *
     * @return New formula object with 2 less Hydrogen atoms and 1 less Oxygen atom.
     * @throws InvalidFormula_Exception Invalid formula format.
     */
    public Formula removeH2OFromFormula() throws InvalidFormula_Exception {
        try {
            mapFormula.replace(Element.H, mapFormula.get(Element.H) - 2);
            mapFormula.replace(Element.O, mapFormula.get(Element.O) - 1);
        } catch (ArrayIndexOutOfBoundsException exception) {
            JOptionPane.showMessageDialog(null, "An error occurred while creating the lipid's formula. Please try again.");
        }
        return new Formula(mapToString(mapFormula));
    }

    /**
     * Adds a fatty acid's elements to the formula map while removing a water molecule (2 Hydrogens and 1 Oxygen)
     * from the formula map. The addition of a fatty acid is a condensation reaction which releases a molecule of
     * water so the removal of these elements must occur at the same time as the addition of the fatty acid's
     * elements.
     *
     * @param fattyAcid Fatty acid object.
     * @return New formula object with the elements of the fatty acid object added and one molecule of water removed.
     * @throws InvalidFormula_Exception Invalid formula format.
     */
    public Formula addFattyAcidToFormula(FattyAcid fattyAcid) throws InvalidFormula_Exception {
        Formula formula = fattyAcid.getFormula();
        mapFormula.replace(Element.C, mapFormula.get(Element.C) + formula.getElementQuantity(Element.C));
        mapFormula.replace(Element.H, mapFormula.get(Element.H) + formula.getElementQuantity(Element.H));
        mapFormula.replace(Element.O, mapFormula.get(Element.O) + formula.getElementQuantity(Element.O));
        removeH2OFromFormula();
        return new Formula(mapToString(mapFormula));
    }

    /**
     * Converts map of elements that represents the formula into a string object.
     *
     * @param mapFormula Tree map of elements and the number of each element in the formula.
     * @return String representation of compound's formula.
     */
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Formula formula = (Formula) o;
        return Objects.equals(mapFormula, formula.mapFormula);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mapFormula);
    }

    /**
     * Defines formula object as a string.
     *
     * @return String representation of compound's formula.
     */
    @Override
    public String toString() {
        return mapToString(mapFormula);
    }
}

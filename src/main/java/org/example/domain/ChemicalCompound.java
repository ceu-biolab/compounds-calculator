package org.example.domain;

import java.util.Objects;

public class ChemicalCompound {
    protected Formula formula;
    protected double mass;

    /**
     * Calculates the mass of a chemical compound according to its formula by adding the mass of the elements together
     *
     * @param formula Formula object which defines the formula of a chemical compound
     * @return Mass of chemical compound as a double.
     */
    public double getMass(Formula formula) {
        mass = 0d;
        for (Element e : formula.getElements()) {
            for (Element e_table : PeriodicTable.elements_Map.keySet()) {
                if (e.equals(e_table)) {
                    mass += formula.getElementQuantity(e) * PeriodicTable.elements_Map.get(e);
                }

            }
        }
        return mass;
    }

    /**
     * Returns the formula attribute of a chemical compound
     *
     * @return Formula object of chemical compound
     */
    public Formula getFormula() {
        return formula;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChemicalCompound that = (ChemicalCompound) o;
        return Double.compare(mass, that.mass) == 0 && Objects.equals(formula, that.formula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formula, mass);
    }

    @Override
    public String toString() {
        return "ChemicalCompound{" +
                "formula=" + formula +
                ", mass=" + mass +
                '}';
    }
}

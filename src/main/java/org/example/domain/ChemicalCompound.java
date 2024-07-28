package org.example.domain;

public class ChemicalCompound {
    protected Formula formula;
    protected double mass;

    public double getMass(Formula formula) {
        mass = 0d;
        for (Element e : formula.getElements()) {
            for (Element e_table : PeriodicTable.elements_Map.keySet()) {
                if (e.equals(e_table)) {
                    mass = mass + formula.getElementQuantity(e) * PeriodicTable.elements_Map.get(e);
                }

            }
        }
        return mass;
    }

    public Formula getFormula() {
        return formula;
    }

}

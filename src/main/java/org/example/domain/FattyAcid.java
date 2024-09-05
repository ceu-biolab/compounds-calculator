package org.example.domain;

import org.example.exceptions.FattyAcidCreation_Exception;
import org.example.exceptions.InvalidFormula_Exception;

public class FattyAcid extends ChemicalCompound {

    private final int carbonAtoms;
    private final int doubleBonds;

    public FattyAcid(Integer carbonAtoms, Integer doubleBonds) throws FattyAcidCreation_Exception, InvalidFormula_Exception {
        super();
        this.carbonAtoms = carbonAtoms;
        this.doubleBonds = doubleBonds;
        this.formula = new Formula(chainToString());
        this.mass = getMass(this.formula);
    }

    public FattyAcid(String carbonAtoms, Integer doubleBonds) throws FattyAcidCreation_Exception, InvalidFormula_Exception {
        super();
        this.carbonAtoms = Integer.parseInt(carbonAtoms.replaceAll("[^0-9]", ""));
        this.doubleBonds = doubleBonds;
        this.formula = new Formula(chainToString());
        this.mass = getMass(this.formula);
    }

    public String chainToString() {
        return "C" + carbonAtoms + "H" + (2 * carbonAtoms - (doubleBonds * 2)) + "O" + 2;
    }

    public int getCarbonAtoms() {
        return carbonAtoms;
    }

    public int getDoubleBonds() {
        return doubleBonds;
    }

    @Override
    public String toString() {
        return carbonAtoms + ":" + doubleBonds;
    }
}
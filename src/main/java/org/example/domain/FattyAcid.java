package org.example.domain;

import org.example.exceptions.FattyAcidCreation_Exception;
import org.example.exceptions.InvalidFormula_Exception;

import java.sql.*;
import java.util.Map;
import java.util.TreeMap;

public class FattyAcid extends ChemicalCompound {

    private final int carbonAtoms;
    private final int doubleBonds;

    public FattyAcid(Integer carbonAtoms, Integer doubleBonds) throws FattyAcidCreation_Exception, InvalidFormula_Exception {
        super();
        this.carbonAtoms = carbonAtoms;
        this.doubleBonds = doubleBonds;
        ensureValidFattyAcid(doubleBonds);
    }

    public FattyAcid(String carbonAtoms, Integer doubleBonds) throws FattyAcidCreation_Exception, InvalidFormula_Exception {
        super();
        this.carbonAtoms = Integer.parseInt(carbonAtoms.replaceAll("[^0-9]", ""));
        this.doubleBonds = doubleBonds;
        ensureValidFattyAcid(doubleBonds);
    }

    public static FattyAcid createFattyAcid(Object carbonAtoms, int doubleBonds) throws FattyAcidCreation_Exception, InvalidFormula_Exception {
        if (carbonAtoms instanceof Integer) {
            return new FattyAcid((Integer) carbonAtoms, doubleBonds);
        } else if (carbonAtoms instanceof String) {
            return new FattyAcid((String) carbonAtoms, doubleBonds);
        } else {
            throw new InvalidFormula_Exception("Invalid type for carbonAtoms. Must be Integer or String.");
        }
    }

    public void ensureValidFattyAcid(int doubleBonds) throws FattyAcidCreation_Exception, InvalidFormula_Exception {
        if (doubleBonds > this.carbonAtoms - 1)
            throw new FattyAcidCreation_Exception(
                    "Double bonds of the molecule must be at least lower than the number of Carbon atoms-1.");
        if (doubleBonds < 0 || this.carbonAtoms < 2)
            throw new FattyAcidCreation_Exception(
                    "The fatty acid can't have a negative number of double bonds or less than 3 carbon atoms.");
        if (this.carbonAtoms > 36)
            throw new FattyAcidCreation_Exception("The fatty acid can't have more than 36 carbon atoms.");
        if (doubleBonds > 6)
            throw new FattyAcidCreation_Exception("The fatty acid can't have more than 6 double bonds.");
        Map<Element, Integer> fattyAcidChain = new TreeMap<Element, Integer>();
        fattyAcidChain.put(Element.C, this.carbonAtoms);
        fattyAcidChain.put(Element.H, 2 * this.carbonAtoms - (doubleBonds * 2));
        fattyAcidChain.put(Element.O, 2);
        this.formula = new Formula(chainToString());
        this.mass = getMass(this.formula);
    }

    public String chainToString() {
        return "C" + carbonAtoms + "H" + (2 * carbonAtoms - (doubleBonds * 2)) + "O" + 2;
    }

    public Formula getFormula() {
        return formula;
    }

    @Override
    public String toString() {
        return carbonAtoms + ":" + doubleBonds;
    }
}
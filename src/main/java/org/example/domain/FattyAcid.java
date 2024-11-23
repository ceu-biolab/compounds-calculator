package org.example.domain;

import org.example.exceptions.FattyAcidCreation_Exception;
import org.example.exceptions.InvalidFormula_Exception;

import java.util.Objects;

public class FattyAcid extends ChemicalCompound {

    private final int carbonAtoms;
    private final int doubleBonds;

    /**
     * Constructor for the creation of a fatty acid object with no iso-branching. (ie. 12:0)
     * @param carbonAtoms   Integer value of the number of carbon atoms that correspond to the fatty acid (ie. 12)
     * @param doubleBonds   Integer value of the number of double bonds that the fatty acid has (ie. 0)
     * @throws FattyAcidCreation_Exception  Invalid fatty acid creation format.
     * @throws InvalidFormula_Exception Invalid formula format.
     */
    public FattyAcid(Integer carbonAtoms, Integer doubleBonds) throws FattyAcidCreation_Exception, InvalidFormula_Exception {
        super();
        this.carbonAtoms = carbonAtoms;
        this.doubleBonds = doubleBonds;
        this.formula = new Formula(chainToString());
        this.mass = getMass(this.formula);
    }

    /**
     * Constructor for the creation of a fatty acid object with iso-branching. (ie. i-12:0)
     * @param carbonAtoms   String of carbon atoms that correspond to the fatty acid (ie. i-12)
     * @param doubleBonds   Integer value of the number of double bonds that the fatty acid has (ie. 0)
     * @throws FattyAcidCreation_Exception  Invalid fatty acid creation format.
     * @throws InvalidFormula_Exception Invalid formula format.
     */
    public FattyAcid(String carbonAtoms, Integer doubleBonds) throws FattyAcidCreation_Exception, InvalidFormula_Exception {
        super();
        this.carbonAtoms = Integer.parseInt(carbonAtoms.replaceAll("[^0-9]", ""));
        this.doubleBonds = doubleBonds;
        this.formula = new Formula(chainToString());
        this.mass = getMass(this.formula);
    }

    /**
     * Defines the formula of the fatty acid as a String object.
     * @return String representing formula of fatty acid.
     */
    public String chainToString() {
        return "C" + carbonAtoms + "H" + (2 * carbonAtoms - (doubleBonds * 2)) + "O" + 2;
    }

    /**
     * Gets the number of carbon atoms a fatty acid has.
     * @return Integer value of number of carbon atoms a fatty acid has.
     */
    public int getCarbonAtoms() {
        return carbonAtoms;
    }

    /**
     * Gets the number of double bonds a fatty acid has.
     * @return Integer value of number of double bonds a fatty acid has.
     */
    public int getDoubleBonds() {
        return doubleBonds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FattyAcid fattyAcid = (FattyAcid) o;
        return carbonAtoms == fattyAcid.carbonAtoms && doubleBonds == fattyAcid.doubleBonds;
    }

    @Override
    public int hashCode() {
        return Objects.hash(carbonAtoms, doubleBonds);
    }

    /**
     * String representation of fatty acid in the format A:B. (ie. 12:0)
     * @return String object with carbon atoms and double bonds separated by a colon.
     */
    @Override
    public String toString() {
        return carbonAtoms + ":" + doubleBonds;
    }
}
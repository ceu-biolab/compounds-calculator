package org.example.domain;

import org.example.exceptions.InvalidFormula_Exception;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MSLipid extends Lipid {
    public MSLipid(LinkedHashSet<FattyAcid> fattyAcids, LipidSkeletalStructure lipidSkeletalStructure) {
        super(fattyAcids, lipidSkeletalStructure);
    }

    public double calculateTotalMassWithAdduct(String adduct) {
        double mass = calculateTotalMass();
        return mass + Adduct.getAdductMass(adduct);
    }

    public Formula addAdductToFormula(Formula formula, String adduct) throws InvalidFormula_Exception {
        // todo
        return new Formula("C");
    }

    public double calculateMZWithAdduct(String adduct, int charge) {
        return calculateTotalMassWithAdduct(adduct) / charge;
    }

    /*
     * This method calculates the potential lipids corresponding to the precursor ion and the FAs assumming that TGs ionizes with M+NH4 but the FAs are ionized with M+Hs
     */
    public Set<Lipid> getTGsByMSData(double precursorIon, List<Double> FAs) {
        return null;
    }

    /*
     * This method calculates the potential lipids corresponding to the precursor ion and the FAs assumming the same ionization
     */
    public Set<Lipid> getLipidsByMSData(double precursorIon, List<Double> FAs) {
        return null;
    }

    /*
     * This method calculates the potential lipids corresponding to the precursor ion and the FAs assumming the same ionization
     */
    public Set<Lipid> getLipidsByMSData(double precursorIon, List<Double> FAs, Set<Adduct> possibleAdductsForPI, Set<Adduct> possibleAdductsForFAs) {
        return null;
    }

    /*
     * This method calculates the potential lipids corresponding to the precursor ion and the FAs assumming the same ionization
     */
    public Set<Lipid> getLipidsByMSData(double precursorIonMZ, List<Double> fattyAcids, Set<LipidSkeletalStructure> possibleHeads,
                                        Set<Adduct> possibleAdductsForPI, Set<Adduct> possibleAdductsForFAs) {

        return null;
    }

}

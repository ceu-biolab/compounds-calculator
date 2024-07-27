package org.example.domain;

import com.itextpdf.layout.element.Link;
import org.example.exceptions.FattyAcidCreation_Exception;
import org.example.exceptions.InvalidFormula_Exception;

import java.util.ArrayList;
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
    public Set<Lipid> getLipidsByMSData(double precursorIonMZ, List<FattyAcid> fattyAcids, Set<LipidSkeletalStructure> possibleHeads,
                                        Set<Adduct> possibleAdductsForPI, Set<Adduct> possibleAdductsForFAs) {
        Adduct adduct = possibleAdductsForPI.iterator().next();
        double adductPIon = precursorIonMZ - Adduct.getAdductMass(adduct.toString());
        for (FattyAcid fattyAcid : fattyAcids) {

        }
        return null;
    }

    public Set<Lipid> getLipidsAccordingToInput(double precursorIonMZ, List<Double> fattyAcidLossValues) throws InvalidFormula_Exception, FattyAcidCreation_Exception {
        double adductWithPIon = precursorIonMZ - Adduct.getAdductMass("[M+NH4]+");
        List<Double> fattyAcidValues = new ArrayList<>();
        LinkedHashSet<FattyAcid> fattyAcids = new LinkedHashSet<>();
        Set<Lipid> lipids = new LinkedHashSet<>();

        for (Double fattyAcidLossValue : fattyAcidLossValues) {
            fattyAcidValues.add(adductWithPIon - fattyAcidLossValue);
            //getFattyAcids().add(FattyAcid.getFattyAcidByMass());
        }

        fattyAcids.add(new FattyAcid(10,0)); // * temporary code just for test
        fattyAcids.add(new FattyAcid(12,0));
        fattyAcids.add(new FattyAcid(14,0));

        lipids.add(new Lipid(fattyAcids, new LipidSkeletalStructure(LipidType.TG)));
        return lipids;
    }

    public static void main(String[] args) {
        try {
            LinkedHashSet<FattyAcid> fattyAcids = new LinkedHashSet<>();
            fattyAcids.add(new FattyAcid(2, 0));
            fattyAcids.add(new FattyAcid(2, 0));
            fattyAcids.add(new FattyAcid(2, 0));
            MSLipid msLipid = new MSLipid(fattyAcids, new LipidSkeletalStructure(LipidType.TG));
            List<Double> fattyAcidLossValues = new ArrayList<>();
            fattyAcidLossValues.add(411.3458);
            fattyAcidLossValues.add(439.3784);
            fattyAcidLossValues.add(467.4092);

            Set<Lipid> lipidsSet = msLipid.getLipidsAccordingToInput(656.5862, fattyAcidLossValues);
            System.out.println(lipidsSet.toString());

        } catch (FattyAcidCreation_Exception e) {
            throw new RuntimeException(e);
        } catch (InvalidFormula_Exception e) {
            throw new RuntimeException(e);
        }

    }

}

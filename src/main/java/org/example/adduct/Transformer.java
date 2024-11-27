package org.example.adduct;

import ceu.biolab.*;

import static org.example.adduct.AdductsLists.MAPADDUCTS;

public class Transformer {

    /**
     * Returns the ppm difference between measured mass and theoretical mass
     *
     * @param measuredMass    Mass measured by MS
     * @param theoreticalMass Theoretical mass of the compound
     */
    public static int calculatePPMIncrement(Double measuredMass, Double theoreticalMass) {
        int ppmIncrement;
        ppmIncrement = (int) Math.round(Math.abs((measuredMass - theoreticalMass) * 1000000
                / theoreticalMass));
        return ppmIncrement;
    }

    /**
     * Set the relative percentage difference between measured value and
     * theoretical value
     *
     * @param experimentalRMT RMT in CEMS experiment
     * @param theoreticalRMT  RMT in CEMS experiment
     * @return
     */
    public static Integer calculatePercentageError(Double experimentalRMT, Double theoreticalRMT) {
        int RMTError;
        RMTError = (int) Math.round(Math.abs((experimentalRMT - theoreticalRMT) / theoreticalRMT * 100));
        return RMTError;
    }

    /**
     * Calculate the delta to search based on the mass, the tolerance Mode and
     * the tolerance
     *
     * @param massToSearch  Mass to search to calculate delta based on the
     *                      tolerance
     * @param toleranceMode 0 (ppm) or 1 (mDa)
     * @param tolerance     Tolerance value
     * @return the mass difference within the tolerance respecting to the
     * massToSearch
     */
    public static Double calculateDeltaPPM(Double massToSearch, Integer toleranceMode,
                                           Double tolerance) {
        //String toleranceModeString = DataFromInterfacesUtilities.toleranceTypeToString(toleranceMode);
        //return calculateDeltaPPM(massToSearch, toleranceModeString, tolerance);
        // TODO
        return null;
    }


    /**
     * Calculate the delta to search based on the RMT, the RMT Tolerance Mode
     * and the RMT Tolerance
     *
     * @param valueToSearch  Mass RMT search to calculate delta based on the RMT
     *                       tolerance. If null, the delta will be very high (5000) to search into all
     *                       masses
     * @param toleranceType  % or abs
     * @param valueTolerance RMTTolerance value
     * @return the mass difference within the tolerance respecting to the
     * Relative migration time
     */
    public static Double calculateDeltaPercentage(Double valueToSearch, ToleranceType toleranceType,
                                                  Double valueTolerance) {
        if (valueToSearch == null) {
            return 5000d;
        }
        Double delta;
        switch (toleranceType) {
            case Da:
                delta = valueToSearch * (valueTolerance / 100);
                break;
            case PPM:
                delta = valueTolerance;
                break;
            default:
                delta = valueTolerance;
                break;
        }
        return delta;
    }

    /**
     * Calculate the mass to search depending on the adduct hypothesis
     *
     * @param experimentalMass Experimental mass of the compound
     * @param adduct           adduct name (M+H, 2M+H, M+2H, etc.)
     * @return the mass difference within the tolerance respecting to the
     * massToSearch
     */
    public static Double getMonoisotopicMassFromMZ(Double experimentalMass, String adduct) {
        Adduct adductObj = MAPADDUCTS.get(adduct);

        if (adductObj == null) {
            return getMonoMassFromSingleChargedMZ(experimentalMass, org.example.domain.Adduct.getAdductMass(adduct));
        }

        double adductValue = adductObj.getAdductMass();
        int charge = adductObj.getAdductCharge();
        int multimer = adductObj.getMultimer();

        if (charge == 1 && multimer == 1) { // Default case: Monomer with Charge +/- 1
            return getMonoMassFromSingleChargedMZ(experimentalMass, adductValue);
        }

        if (multimer > 1) { // Dimer or Trimer with a charge of +/- 2 or +/- 3
            return getMonoMassFromMultimerMZ(experimentalMass, adductValue, multimer);
        } else { // Monomer with a specified charge of +/- 2 or +/- 3
            return getMonoMassFromMultiChargedMZ(experimentalMass, adductValue, charge);
        }
    }

    public static Double getMonoMassFromSingleChargedMZ(Double experimentalMass, Double adductValue) {
        return experimentalMass - adductValue;
    }

    private static Double getMonoMassFromMultiChargedMZ(double experimentalMass, double adductValue, int charge) {
        double result = experimentalMass;
        result -= adductValue;
        result *= charge;
        return result;
    }

    private static Double getMonoMassFromMultimerMZ(double experimentalMass, double adductValue, int numberAtoms) {
        double result = experimentalMass;
        result -= adductValue;
        result /= numberAtoms;
        return result;
    }

    public static Double getMZFromSingleChargedMonoMass(Double monoisotopic_weight, Double adductValue) {
        return monoisotopic_weight + adductValue;
    }

    private static Double getMZFromMultiChargedMonoMass(double monoisotopicWeight, double adductValue, int charge) {
        double result = monoisotopicWeight;
        result /= charge;
        result += adductValue;
        return result;
    }

    private static Double getMZFromMultimerMonoMass(double monoisotopicWeight, double adductValue, int numberMultimers) {
        double result = monoisotopicWeight;
        result *= numberMultimers;
        result += adductValue;
        return result;
    }

    /**
     * Calculate the adduct Mass based on the monoisotopic weight
     *
     * @param monoisotopic_weight Experimental mass of the compound
     * @param adduct              adduct name (M+H, 2M+H, M+2H, etc..)
     * @return the mass difference within the tolerance respecting to the massToSearch
     */
    public static Double getMassOfAdductFromMonoMass(Double monoisotopic_weight, String adduct) {
        Adduct adductObj = MAPADDUCTS.get(adduct);

        if (adductObj == null) { // Adduct not found in map
            return getMZFromSingleChargedMonoMass(monoisotopic_weight, org.example.domain.Adduct.getAdductMass(adduct));
        }

        double adductValue = adductObj.getAdductMass();

        int charge = adductObj.getAdductCharge();
        int multimer = adductObj.getMultimer();

        if (charge == 1 && multimer == 1) { // Default case: Monomer with Charge +/- 1
            return getMZFromSingleChargedMonoMass(monoisotopic_weight, adductValue);
        }

        if (multimer > 1) { // Dimer or Trimer with a charge of +/- 2 or +/- 3
            return getMZFromMultimerMonoMass(monoisotopic_weight, adductValue, multimer);
        } else { // Monomer with a specified charge of +/- 2 or +/- 3
            return getMZFromMultiChargedMonoMass(monoisotopic_weight, adductValue, charge);
        }
    }
}

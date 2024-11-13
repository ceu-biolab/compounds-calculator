package org.example.adduct;

import ceu.biolab.Adduct;
import org.example.domain.Element;
import org.example.domain.PeriodicTable;

/**
 * @author: San Pablo-CEU, Alberto Gil de la Fuente
 * @version: 4.0, 20/07/2016
 */
public class Transformer {
    private final static double MDA_TO_DA = 0.001d;
    private final static double PPM_TO_DA = 0.000001d;

    /**
     * Calculate the MZ if the user has introduced the neutral mass based on a
     * protonated or deprotonated calcuation
     *
     * @param inputMass
     * @param massesMode
     * @param ionizationMode
     * @return
     */
    public static Double calculateMZFromNeutralMass(Double inputMass, int massesMode, int ionizationMode) {
        Double mzInputMass = inputMass;
        if (massesMode == 0 && ionizationMode == 1) {
            mzInputMass = inputMass + PeriodicTable.elements_Map.get(Element.H);
        } else if (massesMode == 0 && ionizationMode == 2) {
            mzInputMass = inputMass - PeriodicTable.elements_Map.get(Element.H);
        }
        return mzInputMass;
    }

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
     * @param ValueToSearch      Mass RMT search to calculate delta based on the RMT
     *                           tolerance. If null, the delta will be very high (5000) to search into all
     *                           masses
     * @param ValueToleranceMode % or abs
     * @param ValueTolerance     RMTTolerance value
     * @return the mass difference within the tolerance respecting to the
     * Relative migration time
     */
    public static Double calculateDeltaPercentage(Double ValueToSearch, String ValueToleranceMode,
                                                  Double ValueTolerance) {
        if (ValueToSearch == null) {
            return 5000d;
        }
        Double delta;
        switch (ValueToleranceMode) {
            // Case mDa
            case "percentage":
                delta = ValueToSearch * (ValueTolerance / 100);
                break;
            // Case ppm
            case "absolute":
                delta = ValueTolerance;
                break;
            default:
                delta = ValueTolerance;
                break;
        }
        return delta;
    }


    /**
     * Calculate the mass to search depending on the adduct hypothesis
     *
     * @param experimentalMass Experimental mass of the compound
     * @param adduct           adduct name (M+H, 2M+H, M+2H, etc.)
     * @param adductValue      numeric value of the adduct (1.0073, etc.)
     * @return the mass difference within the tolerance respecting to the
     * massToSearch
     */
    public static Double getMonoisotopicMassFromMZ(Double experimentalMass, String adduct, Double adductValue) {
        Adduct adductObj = AdductsLists.MAPADDUCTS.get(adduct);

        if (adductObj == null) { // Adduct not found in map
            return getDefaultMassFromMZ(experimentalMass, adductValue);
        }

        int charge = adductObj.getAdductCharge();
        int multimer = adductObj.getMultimer();

        if (charge == 1 && multimer == 1) { // Default case: Monomer with Charge +/- 1
            return getDefaultMassFromMZ(experimentalMass, adductValue);
        }

        if (multimer > 1) { // Dimer or Trimer with a charge of +/- 2 or +/- 3
            return getMultimerOriginalMass(experimentalMass, adductValue, multimer);
        } else { // Monomer with a specified charge of +/- 2 or +/- 3
            return getChargedOriginalMass(experimentalMass, adductValue, charge);
        }
    }

    /**
     * Calculate the monoisotopic mass based on the adduct mass, without knowing
     * the value of the adduct.
     *
     * @param mz
     * @param adduct
     * @return
     */
    public static Double getMonoisotopicMassFromMZ(Double mz, String adduct) {
        // TODO: Double adductValue = getAdductValue(adduct, ionizationMode);
        Double adductValue = 0d;
        return getMonoisotopicMassFromMZ(mz, adduct, adductValue);
    }

    public static Double getDefaultMassFromMZ(Double experimentalMass, Double adductValue) {
        return experimentalMass + adductValue;
    }

    public static Double getDefaultMassFromMonoWeight(Double monoisotopic_weight, Double adductValue) {
        return monoisotopic_weight - adductValue;
    }

    private static Double getChargedOriginalMass(double experimentalMass, double adductValue, int charge) {
        double result = experimentalMass;
        result += adductValue;
        result *= charge;
        return result;
    }

    private static Double getMultimerOriginalMass(double experimentalMass, double adductValue, int numberAtoms) {
        double result = experimentalMass;
        result += adductValue;
        result /= numberAtoms;
        return result;
    }

    private static Double getChargedAdductMass(double monoisotopicWeight, double adductValue, int charge) {
        double result = monoisotopicWeight;
        result /= charge;
        result -= adductValue;
        return result;
    }

    private static Double getMultimerAdductMass(double monoisotopicWeight, double adductValue, int numberAtoms) {
        double result = monoisotopicWeight;
        result *= numberAtoms;
        result -= adductValue;
        return result;
    }

    /**
     * Calculate the adduct Mass based on the monoisotopic weight, without
     * knowing the value of the adduct.
     *
     * @param monoisotopic_weight Experimental mass of the compound
     * @param adduct              adduct name (M+H, 2M+H, M+2H, etc..)
     * @param ionizationMode      positive, negative or neutral
     * @return the mass difference within the tolerance respecting to the massToSearch
     */
    public static Double getMassOfAdductFromMonoWeight(Double monoisotopic_weight, String adduct, int ionizationMode) {
        Adduct adductObj = AdductsLists.MAPADDUCTS.get(adduct);
        // TODO: Double adductValue = getAdductValue(adduct, ionizationMode);
        Double adductValue = 0d;

        if (adductObj == null) { // Adduct not found in map
            return getDefaultMassFromMonoWeight(monoisotopic_weight, adductValue);
        }

        int charge = adductObj.getAdductCharge();
        int multimer = adductObj.getMultimer();

        if (charge == 1 && multimer == 1) { // Default case: Monomer with Charge +/- 1
            return getDefaultMassFromMonoWeight(monoisotopic_weight, adductValue);
        }

        if (multimer > 1) { // Dimer or Trimer with a charge of +/- 2 or +/- 3
            return getMultimerOriginalMass(monoisotopic_weight, adductValue, multimer);
        } else { // Monomer with a specified charge of +/- 2 or +/- 3
            return getChargedOriginalMass(monoisotopic_weight, adductValue, charge);
        }
    }

    /**
     * Calculate the adduct Mass based on the monoisotopic weight
     *
     * @param monoisotopic_weight Experimental mass of the compound
     * @param adduct              adduct name (M+H, 2M+H, M+2H, etc..)
     * @param adductValue         numeric value of the adduct (1.0073, etc.)
     * @return the mass difference within the tolerance respecting to the massToSearch
     */
    public static Double getMassOfAdductFromMonoWeight(Double monoisotopic_weight, String adduct, Double adductValue) {
        getMonoisotopicMassFromMZ(monoisotopic_weight, adduct, adductValue);

        Adduct adductObj = AdductsLists.MAPADDUCTS.get(adduct);
        // TODO: Double adductValue = getAdductValue(adduct, ionizationMode);

        if (adductObj == null) { // Adduct not found in map
            return getDefaultMassFromMonoWeight(monoisotopic_weight, adductValue);
        }

        int charge = adductObj.getAdductCharge();
        int multimer = adductObj.getMultimer();

        if (charge == 1 && multimer == 1) { // Default case: Monomer with Charge +/- 1
            return getDefaultMassFromMonoWeight(monoisotopic_weight, adductValue);
        }

        if (multimer > 1) { // Dimer or Trimer with a charge of +/- 2 or +/- 3
            return getMultimerOriginalMass(monoisotopic_weight, adductValue, multimer);
        } else { // Monomer with a specified charge of +/- 2 or +/- 3
            return getChargedOriginalMass(monoisotopic_weight, adductValue, charge);
        }
    }
}

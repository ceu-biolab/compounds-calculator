package org.example.adduct;

public class MassToleranceCalculator {

    /**
     * Calculates the difference between the theoretical mass and the experimental (or observed) mass according
     * to the tolerance type (either PPM or Da).
     *
     * @param toleranceType
     * @param experimentalMass
     * @param theoreticalMass
     * @return
     */
    public double calculateDelta(ToleranceType toleranceType, double experimentalMass, double theoreticalMass) {
        switch (toleranceType) {
            case PPM:
                return (Math.abs(theoreticalMass - experimentalMass) / theoreticalMass) * Math.pow(10, 6);
            case Da:
                return Math.abs(theoreticalMass - experimentalMass);
            default:
                throw new IllegalArgumentException("Invalid tolerance type");
        }
    }

    /**
     * Realizes the opposite operation of the calculateDelta() method to calculate the tolerance when given a mass.
     *
     * @param toleranceType PPM or Da
     * @param mass
     * @param tolerance
     * @return
     */
    public double calculateTolerance(ToleranceType toleranceType, double mass, double tolerance) {
        switch (toleranceType) {
            case PPM:
                return mass * tolerance / Math.pow(10, 6);
            case Da:
                return tolerance;
            default:
                throw new IllegalArgumentException("Invalid tolerance type");
        }
    }
}

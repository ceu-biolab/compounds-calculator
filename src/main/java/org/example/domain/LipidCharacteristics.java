package org.example.domain;

public class LipidCharacteristics {
    private final Formula formula;
    private final int minFAs;
    private final int maxFAs;

    public LipidCharacteristics(Formula formula, int minFAs, int maxFAs) {
        this.formula = formula;
        this.minFAs = minFAs;
        this.maxFAs = maxFAs;
    }

    public LipidCharacteristics(Formula formula, int numExactFAs) {
        this.formula = formula;
        this.minFAs = numExactFAs;
        this.maxFAs = numExactFAs;
    }

    public int getMinFAs() {
        return minFAs;
    }

    public int getMaxFAs() {
        return maxFAs;
    }

    @Override
    public String toString() {
        return "LipidCharacteristics{" +
                "formula=" + formula.toString() +
                '}';
    }

    public Formula getFormula() {
        return formula;
    }
}

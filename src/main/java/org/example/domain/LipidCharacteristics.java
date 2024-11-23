package org.example.domain;

import java.util.Objects;

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

    public Formula getFormula() {
        return formula;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LipidCharacteristics that = (LipidCharacteristics) o;
        return minFAs == that.minFAs && maxFAs == that.maxFAs && Objects.equals(formula, that.formula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formula, minFAs, maxFAs);
    }

    @Override
    public String toString() {
        return "LipidCharacteristics{" +
                "formula=" + formula.toString() +
                '}';
    }

}

package org.example.domain;

import org.example.exceptions.InvalidFattyAcidSize_Exception;

public enum LipidType {
    /**
     * CE, MG: 1
     * DG, PA, PC, PE, PI, PG, PS: 2
     * TG: 3
     * CL: 4
     **/
    CE, CER, DG, MG, PA, PC, PE, PI, PG, PS, SM, TG, CL;
    // ** CER y SM A FUTURO, el FA ES DIFERENTE

    public static LipidType verifyFattyAcidType(LipidSkeletalStructure head, int numOfFAs) throws InvalidFattyAcidSize_Exception {
        switch (head.getLipidType()) {
            case CE, MG -> {
                if (numOfFAs != 1) {
                    throw new InvalidFattyAcidSize_Exception("Invalid number of FAs corresponding to lipid type.");
                }
                if (head.getLipidType() == CE) {
                    return CE;
                } else {
                    return MG;
                }
            }
            case DG, PA, PC, PE, PI, PG, PS -> {
                if (numOfFAs != 2) {
                    throw new InvalidFattyAcidSize_Exception("Invalid number of FAs corresponding to lipid type.");
                }
                if (head.getLipidType() == DG) {
                    return DG;
                } else if (head.getLipidType() == PA) {
                    return PA;
                } else if (head.getLipidType() == PC) {
                    return PC;
                } else if (head.getLipidType() == PE) {
                    return PE;
                } else if (head.getLipidType() == PI) {
                    return PI;
                } else if (head.getLipidType() == PG) {
                    return PG;
                } else {
                    return PS;
                }
            }
            case TG -> {
                if (numOfFAs != 3) {
                    throw new InvalidFattyAcidSize_Exception("Invalid number of FAs corresponding to lipid type.");
                }
                return TG;
            }
            case CL -> {
                if (numOfFAs != 4) {
                    throw new InvalidFattyAcidSize_Exception("Invalid number of FAs corresponding to lipid type.");
                }
                return CL;
            }
        }
        return null;
    }

}

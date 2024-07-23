package org.example.domain;

import org.example.exceptions.InvalidFattyAcidSize_Exception;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class LipidLipidMaps extends Lipid {     // todo LipidLipidMaps class
    private Set<String> lipidMapsIDs;
    private String commonName;
    private String systematicName;

    public LipidLipidMaps(LinkedHashSet<FattyAcid> fattyAcids, String commonName, String systematicName,
                          Set<String> lipidMapsIDs, LipidSkeletalStructure lipidTypeCharacteristics) {
        super(fattyAcids, lipidTypeCharacteristics);
        this.lipidMapsIDs = lipidMapsIDs;
        this.commonName = commonName;
        this.systematicName = systematicName;
    }

    public Set<String> getLipidMapsIDs() {
        return lipidMapsIDs;
    }

    public String getCommonName() {
        return commonName;
    }

    public String getSystematicName() {
        return systematicName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LipidLipidMaps that = (LipidLipidMaps) o;
        return lipidMapsIDs.equals(that.lipidMapsIDs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lipidMapsIDs);
    }

    //comparator

}

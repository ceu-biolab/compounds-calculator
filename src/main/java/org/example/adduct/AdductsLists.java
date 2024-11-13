package org.example.adduct;


import ceu.biolab.Adduct;
import ceu.biolab.IncorrectAdduct;
import ceu.biolab.IncorrectFormula;
import ceu.biolab.NotFoundElement;

import java.util.*;
import javax.faces.model.SelectItem;

public class AdductsLists {

    public static final List<SelectItem> LISTNEUTRALMODESFORCEMS;

    static {
        List<SelectItem> listNeutralModesTMP = new LinkedList<>();
        listNeutralModesTMP.add(new SelectItem(0, "Neutral"));
        LISTNEUTRALMODESFORCEMS = Collections.unmodifiableList(listNeutralModesTMP);
    }

    public static final List<SelectItem> LISTNEUTRALMODES;

    static {
        List<SelectItem> listNeutralModesTMP = new LinkedList<>();
        listNeutralModesTMP.add(new SelectItem(0, "Neutral"));
        listNeutralModesTMP.add(new SelectItem(1, "Positive Mode"));
        listNeutralModesTMP.add(new SelectItem(2, "Negative Mode"));
        LISTNEUTRALMODES = Collections.unmodifiableList(listNeutralModesTMP);
    }

    public static final List<SelectItem> LISTIONIZEDMODES;

    static {
        List<SelectItem> listIonizedModesTMP = new LinkedList<>();
        listIonizedModesTMP.add(new SelectItem(1, "Positive Mode"));
        listIonizedModesTMP.add(new SelectItem(2, "Negative Mode"));
        LISTIONIZEDMODES = Collections.unmodifiableList(listIonizedModesTMP);
    }


    public static final Map<String, Adduct> MAPADDUCTS;

    static {
        try {
            // TODO: Check charges for each adduct
            // Positive adducts
            MAPADDUCTS = new HashMap<String, Adduct>();
            MAPADDUCTS.put("[M+H]+", new Adduct("[M+H]+"));
            MAPADDUCTS.put("[M+2H]2+", new Adduct("[M+2H]2+")); // CHARGE_2TMP
            MAPADDUCTS.put("[2M+H]+", new Adduct("[2M+H]+"));
            MAPADDUCTS.put("[M+2H]+", new Adduct("[M+2H]2+"));
            MAPADDUCTS.put("[M+Na]+", new Adduct("[M+Na]+"));
            MAPADDUCTS.put("[M+K]+", new Adduct("[M+K]+"));
            MAPADDUCTS.put("[M+NH4]+", new Adduct("[M+NH4]+"));
            MAPADDUCTS.put("[M+H-H2O]+", new Adduct("[M+H-H2O]+"));
            MAPADDUCTS.put("[M+H+NH4]2+", new Adduct("[M+H+NH4]2+")); // CHARGE_2TMP
            MAPADDUCTS.put("[2M+Na]+", new Adduct("[2M+Na]+"));
            MAPADDUCTS.put("[M+H+HCOONa]+", new Adduct("[M+H+HCOONa]+"));
            MAPADDUCTS.put("[2M+H-H2O]+", new Adduct("[2M+H-H2O]+"));
            MAPADDUCTS.put("[M+3H]3+", new Adduct("[M+3H]3+"));
            MAPADDUCTS.put("[M+2H+Na]2+", new Adduct("[M+2H+Na]2+")); // CHARGE_2TMP
            MAPADDUCTS.put("[M+H+2K]2+", new Adduct("[M+H+2K]2+"));
            MAPADDUCTS.put("[M+H+2Na]2+", new Adduct("[M+H+2Na]2+"));
            MAPADDUCTS.put("[M+3Na]3+", new Adduct("[M+3Na]3+"));
            MAPADDUCTS.put("[M+H+Na]2+", new Adduct("[M+H+Na]2+")); // CHARGE_2TMP
            MAPADDUCTS.put("[M+H+K]2+", new Adduct("[M+H+K]2+")); // CHARGE_2TMP
            MAPADDUCTS.put("[M+ACN+2H]2+", new Adduct("[M+ACN+2H]2+")); // CHARGE_2TMP
            MAPADDUCTS.put("[M+2Na]2+", new Adduct("[M+2Na]2+")); // CHARGE_2TMP
            MAPADDUCTS.put("[M+2ACN+2H]2+", new Adduct("[M+2ACN+2H]2+")); // CHARGE_2TMP
            MAPADDUCTS.put("[M+3ACN+2H]2+", new Adduct("[M+3ACN+2H]2+")); // CHARGE_2TMP
            MAPADDUCTS.put("[M+CH3OH+H]+", new Adduct("[M+CH3OH+H]+"));
            MAPADDUCTS.put("[M+ACN+H]+", new Adduct("[M+ACN+H]+"));
            MAPADDUCTS.put("[M+2Na-H]+", new Adduct("[M+2Na-H]+"));
            MAPADDUCTS.put("[M+IsoProp+H]+", new Adduct("[M+IsoProp+H]+"));
            MAPADDUCTS.put("[M+ACN+Na]+", new Adduct("[M+ACN+Na]+"));
            MAPADDUCTS.put("[M+2K-H]+", new Adduct("[M+2K-H]+"));
            MAPADDUCTS.put("[M+DMSO+H]+", new Adduct("[M+DMSO+H]+"));
            MAPADDUCTS.put("[M+2ACN+H]+", new Adduct("[M+2ACN+H]+"));
            MAPADDUCTS.put("[M+IsoProp+Na+H]+", new Adduct("[M+IsoProp+Na+H]+"));
            MAPADDUCTS.put("[2M+NH4]+", new Adduct("[2M+NH4]+"));
            MAPADDUCTS.put("[2M+K]+", new Adduct("[2M+K]+"));
            MAPADDUCTS.put("[2M+ACN+H]+", new Adduct("[2M+ACN+H]+"));
            MAPADDUCTS.put("[2M+ACN+Na]+", new Adduct("[2M+ACN+Na]+"));
            MAPADDUCTS.put("[3M+H]+", new Adduct("[3M+H]+"));
            MAPADDUCTS.put("[3M+Na]+", new Adduct("[3M+Na]+"));
            MAPADDUCTS.put("[M+H-2H2O]+", new Adduct("[M+H-2H2O]+"));
            MAPADDUCTS.put("[M+NH4-H2O]+", new Adduct("[M+NH4-H2O]+"));
            MAPADDUCTS.put("[M+Li]+", new Adduct("[M+Li]+"));
            MAPADDUCTS.put("[2M+2H+3H2O]+", new Adduct("[2M+2H+3H2O]+")); // Check charge
            MAPADDUCTS.put("[M+H+CH3COOH]+", new Adduct("[M+H+CH3COOH]+"));
            MAPADDUCTS.put("[M+H+CH3COONa]+", new Adduct("[M+H+CH3COONa]+"));
            MAPADDUCTS.put("[M+F+H]+", new Adduct("[M+F+H]+"));
            MAPADDUCTS.put("[M-2H]2-", new Adduct("[M-2H]2-")); // CHARGE_2TMP (negative charge)
            MAPADDUCTS.put("[2M-H]+", new Adduct("[2M-H]+"));
            MAPADDUCTS.put("[2M+HCOOH-H]+", new Adduct("[2M+HCOOH-H]+"));
            MAPADDUCTS.put("[2M+CH3COOH-H]+", new Adduct("[2M+CH3COOH-H]+"));
            MAPADDUCTS.put("[3M-H]+", new Adduct("[3M-H]+"));
            MAPADDUCTS.put("[M+2H+Na]3+", new Adduct("[M+2H+Na]3+"));
            MAPADDUCTS.put("[M+H+2K]3+", new Adduct("[M+H+2K]3+"));
            MAPADDUCTS.put("[M+H+2Na]3+", new Adduct("[M+H+2Na]3+"));
            MAPADDUCTS.put("[M-3H]3+", new Adduct("[M-3H]3+"));

            // Negative adducts
            MAPADDUCTS.put("[M-H]-", new Adduct("[M-H]-"));
            MAPADDUCTS.put("[M+Cl]-", new Adduct("[M+Cl]-"));
            MAPADDUCTS.put("[M+HCOOH-H]-", new Adduct("[M+HCOOH-H]-"));
            MAPADDUCTS.put("[M-H-H2O]-", new Adduct("[M-H-H2O]-"));
            MAPADDUCTS.put("[M-H+HCOONa]-", new Adduct("[M-H+HCOONa]-"));
            MAPADDUCTS.put("[M-H+CH3COONa]-", new Adduct("[M-H+CH3COONa]-"));
            MAPADDUCTS.put("[2M-H]-", new Adduct("[2M-H]-"));
            MAPADDUCTS.put("[M-3H]-", new Adduct("[M-3H]-"));
            MAPADDUCTS.put("[M-2H]-", new Adduct("[M-2H]-"));
            MAPADDUCTS.put("[M+Na-2H]-", new Adduct("[M+Na-2H]-"));
            MAPADDUCTS.put("[M+K-2H]-", new Adduct("[M+K-2H]-"));
            MAPADDUCTS.put("[M+CH3COOH-H]-", new Adduct("[M+CH3COOH-H]-"));
            MAPADDUCTS.put("[M+Br]-", new Adduct("[M+Br]-"));
            MAPADDUCTS.put("[M+TFA-H]-", new Adduct("[M+TFA-H]-"));
            MAPADDUCTS.put("[2M+HCOOH-H]-", new Adduct("[2M+HCOOH-H]-"));
            MAPADDUCTS.put("[2M+CH3COOH-H]-", new Adduct("[2M+CH3COOH-H]-"));
            MAPADDUCTS.put("[3M-H]-", new Adduct("[3M-H]-"));
            MAPADDUCTS.put("[M+F]-", new Adduct("[M+F]-"));
            MAPADDUCTS.put("[M+F+H]-", new Adduct("[M+F+H]-"));
        } catch (IncorrectAdduct | NotFoundElement | IncorrectFormula e) {
            throw new RuntimeException(e);
        }
    }

    public static final Map<String, String> MAPNEUTRALADDUCTS;

    static {
        Map<String, String> mapNeutralAdductsTMP = new LinkedHashMap<>();
        mapNeutralAdductsTMP.put("M", "0");
        MAPNEUTRALADDUCTS = Collections.unmodifiableMap(mapNeutralAdductsTMP);
    }

    public static final Map<String, List<String>> MAPPOSITIVEADDUCTFRAGMENT;

    static {
        Map<String, List<String>> mapMZPositiveAdductsTMP = new LinkedHashMap<>();

        List<String> possibleParents = new LinkedList<>();
        possibleParents.add("M+H");
        possibleParents.add("M+2H");
        possibleParents.add("M+3H");
        possibleParents.add("M+H-H2O");
        possibleParents.add("M+H+NH4");
        possibleParents.add("M+H+HCOONa");
        possibleParents.add("M+2H+Na");
        possibleParents.add("M+H+2K");
        possibleParents.add("M+H+2Na");
        possibleParents.add("M+H+Na");
        possibleParents.add("M+H+K");
        possibleParents.add("3M+H");
        possibleParents.add("2M+H");
        possibleParents.add("2M+H-H2O");
        possibleParents.add("M+ACN+2H");
        possibleParents.add("M+2ACN+2H");
        possibleParents.add("M+3ACN+2H");
        possibleParents.add("M+CH3OH+H");
        possibleParents.add("M+ACN+H");
        possibleParents.add("M+IsoProp+H");
        possibleParents.add("M+DMSO+H");
        possibleParents.add("M+2ACN+H");
        possibleParents.add("M+IsoProp+Na+H");
        possibleParents.add("2M+ACN+H");
        possibleParents.add("M+H-2H2O");
        possibleParents.add("2M+2H+3H2O");
        possibleParents.add("M+H+CH3COOH");
        possibleParents.add("M+H+CH3COONa");
        mapMZPositiveAdductsTMP.put("M+H", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+2H");
        possibleParents.add("M+2H+Na");
        possibleParents.add("M+ACN+2H");
        possibleParents.add("M+2ACN+2H");
        possibleParents.add("M+3ACN+2H");
        mapMZPositiveAdductsTMP.put("M+2H", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+Na");
        possibleParents.add("2M+Na");
        possibleParents.add("M+2H+Na");
        possibleParents.add("M+H+2Na");
        possibleParents.add("M+3Na");
        possibleParents.add("M+H+Na");
        possibleParents.add("M+2Na");
        possibleParents.add("M+2Na-H");
        possibleParents.add("M+ACN+Na");
        possibleParents.add("M+IsoProp+Na+H");
        possibleParents.add("2M+ACN+Na");
        mapMZPositiveAdductsTMP.put("M+Na", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+K");
        possibleParents.add("M+H+2K");
        possibleParents.add("M+H+K");
        possibleParents.add("M+2K-H");
        possibleParents.add("2M+K");
        mapMZPositiveAdductsTMP.put("M+K", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+NH4");
        possibleParents.add("M+H+NH4");
        possibleParents.add("2M+NH4");
        mapMZPositiveAdductsTMP.put("M+NH4", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("2M+H-H2O");
        possibleParents.add("M+H-H2O");
        mapMZPositiveAdductsTMP.put("M+H-H2O", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+H+NH4");
        mapMZPositiveAdductsTMP.put("M+H+NH4", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+H+HCOONa");
        mapMZPositiveAdductsTMP.put("M+H+HCOONa", possibleParents);

        //mapMZPositiveAdductsTMP.put("2M+H-H2O", "17.0032");
        possibleParents = new LinkedList<>();
        possibleParents.add("M+3H");
        mapMZPositiveAdductsTMP.put("M+3H", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+2H+Na");
        mapMZPositiveAdductsTMP.put("M+2H+Na", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+H+2K");
        mapMZPositiveAdductsTMP.put("M+H+2K", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+H+2Na");
        mapMZPositiveAdductsTMP.put("M+H+2Na", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+3Na");
        mapMZPositiveAdductsTMP.put("M+3Na", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+2H+Na");
        possibleParents.add("M+H+2Na");
        possibleParents.add("M+H+Na");
        mapMZPositiveAdductsTMP.put("M+H+Na", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+H+2K");
        possibleParents.add("M+H+K");
        mapMZPositiveAdductsTMP.put("M+H+K", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+3ACN+2H");
        possibleParents.add("M+2ACN+2H");
        possibleParents.add("M+ACN+2H");
        mapMZPositiveAdductsTMP.put("M+ACN+2H", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+3Na");
        possibleParents.add("M+2Na");
        possibleParents.add("M+H+2Na");
        possibleParents.add("M+2Na-H");
        mapMZPositiveAdductsTMP.put("M+2Na", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+3ACN+2H");
        possibleParents.add("M+2ACN+2H");
        mapMZPositiveAdductsTMP.put("M+2ACN+2H", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+3ACN+2H");
        mapMZPositiveAdductsTMP.put("M+3ACN+2H", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+CH3OH+H");
        mapMZPositiveAdductsTMP.put("M+CH3OH+H", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+ACN+2H");
        possibleParents.add("M+ACN+H");
        possibleParents.add("M+2ACN+2H");
        possibleParents.add("M+3ACN+2H");
        possibleParents.add("M+2ACN+H");
        possibleParents.add("2M+ACN+H");
        mapMZPositiveAdductsTMP.put("M+ACN+H", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+2Na-H");
        mapMZPositiveAdductsTMP.put("M+2Na-H", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+IsoProp+Na+H");
        possibleParents.add("M+IsoProp+H");
        mapMZPositiveAdductsTMP.put("M+IsoProp+H", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("2M+ACN+Na");
        possibleParents.add("M+ACN+Na");
        mapMZPositiveAdductsTMP.put("M+ACN+Na", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+2K-H");
        mapMZPositiveAdductsTMP.put("M+2K-H", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+DMSO+H");
        mapMZPositiveAdductsTMP.put("M+DMSO+H", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+3ACN+2H");
        possibleParents.add("M+2ACN+2H");
        possibleParents.add("M+2ACN+H");
        mapMZPositiveAdductsTMP.put("M+2ACN+H", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+IsoProp+Na+H");
        mapMZPositiveAdductsTMP.put("M+IsoProp+Na+H", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+H-2H2O");
        mapMZPositiveAdductsTMP.put("M+H-2H2O", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+NH4-H2O");
        mapMZPositiveAdductsTMP.put("M+NH4-H2O", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+Li");
        mapMZPositiveAdductsTMP.put("M+Li", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("2M+2H+3H2O");
        mapMZPositiveAdductsTMP.put("2M+2H+3H2O", possibleParents);

        MAPPOSITIVEADDUCTFRAGMENT = Collections.unmodifiableMap(mapMZPositiveAdductsTMP);
    }

    public static final Map<String, List<String>> MAPNEGATIVEADDUCTFRAGMENT;

    static {
        Map<String, List<String>> mapMZNegativeAdductsTMP = new LinkedHashMap<>();

        List<String> possibleParents = new LinkedList<>();
        possibleParents.add("M-H");
        possibleParents.add("M+HCOOH-H");
        possibleParents.add("M-H-H2O");
        possibleParents.add("M-H+HCOONa");
        possibleParents.add("M-H+CH3COONa");
        possibleParents.add("2M-H");
        possibleParents.add("M+CH3COOH-H");
        possibleParents.add("M+TFA-H");
        possibleParents.add("2M+HCOOH-H");
        possibleParents.add("2M+CH3COOH-H");
        possibleParents.add("3M-H");
        possibleParents.add("M-2H");
        possibleParents.add("M+Na-2H");
        possibleParents.add("M+K-2H");
        possibleParents.add("M-3H");
        mapMZNegativeAdductsTMP.put("M-H", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+Cl");
        mapMZNegativeAdductsTMP.put("M+Cl", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("2M+HCOOH-H");
        possibleParents.add("M+HCOOH-H");
        mapMZNegativeAdductsTMP.put("M+HCOOH-H", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M-H-H2O");
        mapMZNegativeAdductsTMP.put("M-H-H2O", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M-H+HCOONa");
        mapMZNegativeAdductsTMP.put("M-H+HCOONa", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M-3H");
        mapMZNegativeAdductsTMP.put("M-3H", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+Na-2H");
        possibleParents.add("M-2H");
        possibleParents.add("M+K-2H");
        mapMZNegativeAdductsTMP.put("M-2H", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+Na-2H");
        mapMZNegativeAdductsTMP.put("M+Na-2H", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+K-2H");
        mapMZNegativeAdductsTMP.put("M+K-2H", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+CH3COOH-H");
        mapMZNegativeAdductsTMP.put("M+CH3COOH-H", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+Br");
        mapMZNegativeAdductsTMP.put("M+Br", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+TFA-H");
        mapMZNegativeAdductsTMP.put("M+TFA-H", possibleParents);

        possibleParents = new LinkedList<>();
        possibleParents.add("M+F");
        mapMZNegativeAdductsTMP.put("M+F", possibleParents);

        MAPNEGATIVEADDUCTFRAGMENT = Collections.unmodifiableMap(mapMZNegativeAdductsTMP);
    }
}

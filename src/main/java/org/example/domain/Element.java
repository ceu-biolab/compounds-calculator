package org.example.domain;

public enum Element {
    C, H, N, O, P, S, Cl, F, Li, Na, K, Rb, Cs, Fr, Be, Mg, Ca, Sr, Ba, Ra, Sc, Y, Lu, Lr, Ti, Zr, Hf, Rf, V, Nb, Ta,
    Db, Cr, Mo, W, Sg, Mn, Tc, Re, Bh, Fe, Ru, Os, Hs, Co, Rh, Ir, Mt, Ni, Pd, Pt, Ds, Cu, Ag, Au, Rg, Zn, Cd, Hg, Cn,
    B, Al, In, Tl, Nh, Si, Ge, Sn, Pb, Fl, As, Sb, Bi, Mc, Se, Po, Lv, Br, I, At, Ts, He, Ne, Ar, Ga, Kr, Te, Xe, La, Ce,
    Pr, Nd, Pm, Sm, Eu, Gd, Tb, Dy, Ho, Er, Tm, Yb, Rn, Ac, Th, Pa, U, Np, Pu, Am, Cm, Bk, Cf, Es, Fm, Md, No, Uuo;

    public static Element toElement(String element) {
        return Element.valueOf(element);
    }
}

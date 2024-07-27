package org.example.domain;

import java.util.Map;
import java.util.HashMap;

public class PeriodicTable {

    public static final Map<Element, Double> elements_Map = new HashMap<Element, Double>();
    public static final double electronMass = 0.00054858d;
    public static final double waterMass = 18.01056d;
    public static final double protonMass = 1.00783 - electronMass;

    static {
        elements_Map.put(Element.H, 1.00783);
        elements_Map.put(Element.He, 4.002603d);
        elements_Map.put(Element.Li, 7.016005d);
        elements_Map.put(Element.Be, 9.012183d);
        elements_Map.put(Element.B, 11.009305d);
        elements_Map.put(Element.C, 12d);
        elements_Map.put(Element.N, 14.003074d);
        elements_Map.put(Element.O, 15.99491);
        elements_Map.put(Element.F, 18.998403d);
        elements_Map.put(Element.Ne, 19.992439d);
        elements_Map.put(Element.Na, 22.989770d);
        elements_Map.put(Element.Mg, 23.985045d);
        elements_Map.put(Element.Al, 26.981541d);
        elements_Map.put(Element.Si, 27.976928d);
        elements_Map.put(Element.P, 30.973763d);
        elements_Map.put(Element.S, 31.972072d);
        elements_Map.put(Element.Cl, 34.968853d);
        elements_Map.put(Element.Ar, 39.962383d);
        elements_Map.put(Element.K, 38.963708d);
        elements_Map.put(Element.Ca, 39.962591d);
        elements_Map.put(Element.Sc, 44.955914d);
        elements_Map.put(Element.Ti, 47.947947d);
        elements_Map.put(Element.V, 50.943963d);
        elements_Map.put(Element.Cr, 51.940510d);
        elements_Map.put(Element.Mn, 54.938046d);
        elements_Map.put(Element.Fe, 55.934939d);
        elements_Map.put(Element.Ni, 57.935347d);
        elements_Map.put(Element.Co, 58.933198d);
        elements_Map.put(Element.Cu, 62.929599d);
        elements_Map.put(Element.Zn, 63.929145d);
        elements_Map.put(Element.Ga, 68.925581d);
        elements_Map.put(Element.Ge, 73.921179d);
        elements_Map.put(Element.As, 74.921596d);
        elements_Map.put(Element.Se, 79.916521d);
        elements_Map.put(Element.Br, 78.918336d);
        elements_Map.put(Element.Kr, 83.911506d);
        elements_Map.put(Element.Rb, 84.911800d);
        elements_Map.put(Element.Sr, 87.905625d);
        elements_Map.put(Element.Y, 88.905856d);
        elements_Map.put(Element.Zr, 89.904708d);
        elements_Map.put(Element.Nb, 92.906378d);
        elements_Map.put(Element.Mo, 97.905405d);
        elements_Map.put(Element.Tc, 98.0d);
        elements_Map.put(Element.Ru, 101.904348d);
        elements_Map.put(Element.Rh, 102.905503d);
        elements_Map.put(Element.Pd, 105.903475d);
        elements_Map.put(Element.Ag, 106.905095d);
        elements_Map.put(Element.Cd, 113.903361d);
        elements_Map.put(Element.In, 114.903875d);
        elements_Map.put(Element.Sn, 118.710d);
        elements_Map.put(Element.Sb, 120.903824d);
        elements_Map.put(Element.Te, 129.906229d);
        elements_Map.put(Element.I, 126.904477d);
        elements_Map.put(Element.Xe, 131.293d);
        elements_Map.put(Element.Cs, 132.905433d);
        elements_Map.put(Element.Ba, 137.905236d);
        elements_Map.put(Element.La, 138.907114d);
        elements_Map.put(Element.Ce, 139.905442d);
        elements_Map.put(Element.Pr, 140.907657d);
        elements_Map.put(Element.Nd, 141.907731d);
        elements_Map.put(Element.Pm, 145.0d);
        elements_Map.put(Element.Sm, 151.919741d);
        elements_Map.put(Element.Eu, 152.921243d);
        elements_Map.put(Element.Gd, 157.924111d);
        elements_Map.put(Element.Tb, 158.92535d);
        elements_Map.put(Element.Dy, 163.929183d);
        elements_Map.put(Element.Ho, 164.930332d);
        elements_Map.put(Element.Er, 165.930305d);
        elements_Map.put(Element.Tm, 168.934225d);
        elements_Map.put(Element.Yb, 173.938873d);
        elements_Map.put(Element.Lu, 174.940785d);
        elements_Map.put(Element.Hf, 179.946561d);
        elements_Map.put(Element.Ta, 180.948014d);
        elements_Map.put(Element.W, 183.950953d);
        elements_Map.put(Element.Re, 186.955765d);
        elements_Map.put(Element.Os, 191.961487d);
        elements_Map.put(Element.Ir, 192.962942d);
        elements_Map.put(Element.Pt, 194.964785d);
        elements_Map.put(Element.Au, 196.966560d);
        elements_Map.put(Element.Hg, 201.970632d);
        elements_Map.put(Element.Tl, 204.974410d);
        elements_Map.put(Element.Pb, 207.976641d);
        elements_Map.put(Element.Bi, 208.980388d);
        elements_Map.put(Element.Po, 209.0d);
        elements_Map.put(Element.At, 210.0d);
        elements_Map.put(Element.Rn, 222.0d);
        elements_Map.put(Element.Fr, 223.0d);
        elements_Map.put(Element.Ra, 226.0d);
        elements_Map.put(Element.Ac, 227.0d);
        elements_Map.put(Element.Th, 232.038054d);
        elements_Map.put(Element.Pa, 231.03588d);
        elements_Map.put(Element.U, 238.050786d);
        elements_Map.put(Element.Np, 237.0d);
        elements_Map.put(Element.Pu, 244.0d);
        elements_Map.put(Element.Am, 243.0d);
        elements_Map.put(Element.Cm, 247.0d);
        elements_Map.put(Element.Bk, 247.0d);
        elements_Map.put(Element.Cf, 251.0d);
        elements_Map.put(Element.Es, 252.0d);
        elements_Map.put(Element.Fm, 257.0d);
        elements_Map.put(Element.Md, 258.0d);
        elements_Map.put(Element.No, 259.0d);
        elements_Map.put(Element.Lr, 266.0d);
        elements_Map.put(Element.Rf, 267.0d);
        elements_Map.put(Element.Db, 268.0d);
        elements_Map.put(Element.Sg, 269.0d);
        elements_Map.put(Element.Bh, 270.0d);
        elements_Map.put(Element.Hs, 269.0d);
        elements_Map.put(Element.Mt, 278.0d);
        elements_Map.put(Element.Ds, 281.0d);
        elements_Map.put(Element.Rg, 282.0d);
        elements_Map.put(Element.Cn, 285.0d);
        elements_Map.put(Element.Nh, 286.0d);
        elements_Map.put(Element.Fl, 289.0d);
        elements_Map.put(Element.Mc, 289.0d);
        elements_Map.put(Element.Lv, 293.0d);
        elements_Map.put(Element.Ts, 294.0d);
        elements_Map.put(Element.Uuo, 294.0d);
    }

}
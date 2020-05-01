package com.lefc.jambly;

import java.util.ArrayList;

public class ProdParser {

    private static Record rec;

    /*FUNZIONE PER LA GESTIONE DEI MODIFICATORII*/
    public static void mods(String mod_opt) {
        Support.sepWordWS(mod_opt);
        CheckClass.checkMod();
    }

    /*FUNZIONE CHE GESTISCE LA DICHIARAZIONE DEGLI ARRAY*/
    public static void declArr(String vdi, String vi) {
        ArrayList listValue = new ArrayList();

        vdi = vdi.replace("[]", "");
        rec = SymbolTable.getCurrRec(vdi);

        String elem;
        int i;
        if (vi.contains("new")) {
            i = vi.lastIndexOf(",");

            while (i != -1 || !vi.equals("")) {

                if (vi.startsWith(" ", i + 1)) {
                    elem = vi.substring(i + 2, vi.length());  //recupero valori numerici
                } else {
                    elem = vi.substring(i + 1, vi.length());  //recupero valori numerici
                }

                listValue.add(elem);

                if (i != -1) {
                    vi = vi.substring(vi.indexOf("]") + 1, i);
                    i = vi.lastIndexOf(",");
                } else {
                    vi = "";
                }
            }
            rec.setList(listValue);
        } else {
            i = vi.lastIndexOf(",");

            while (i != -1 || !vi.equals("")) {
                if (vi.startsWith(" ", i + 1)) {
                    elem = vi.substring(i + 2, vi.length());  //recupero valori numerici
                } else {
                    elem = vi.substring(i + 1, vi.length());  //recupero valori numerici
                }

                listValue.add(elem);
                if (i != -1) {
                    vi = vi.substring(0, i);
                    i = vi.lastIndexOf(",");
                } else {
                    vi = "";
                }
            }
            rec.setValue(listValue.size());
            rec.setList(listValue);
        }
    }
}
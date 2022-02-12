package com.lefc.jambly;

import java.util.ArrayList;
import java.util.List;

import static com.lefc.jambly.Support.getArrList;

public class ProdParser {

    public void modifierHandler(String mod_opt) {
        Support.sepWordWS(mod_opt);
        CUP$parser$actions.checkFlag = new CheckClass().checkModifiers(getArrList());
    }

    public static void arrayDeclarationHandler(String vdi, String vi) {
        List<String> listValue = new ArrayList<>();

        vdi = vdi.replace("[]", "");
        Record rec = SymbolTable.getCurrRec(vdi);

        String elem;
        int i;
        if (vi.contains("new")) {
            i = vi.lastIndexOf(",");
            while (i != -1 || !vi.equals("")) {
                if (vi.startsWith(" ", i + 1)) {
                    elem = vi.substring(i + 2);
                } else {
                    elem = vi.substring(i + 1);
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
                    elem = vi.substring(i + 2);
                } else {
                    elem = vi.substring(i + 1);
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
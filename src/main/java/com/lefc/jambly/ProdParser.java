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
        Record record = SymbolTable.getCurrRec(vdi.replace("[]", ""));

        List<String> listValue = new ArrayList<>();

        int i = vi.lastIndexOf(",");
        while (i != -1 || !vi.equals("")) {
            listValue.add((vi.startsWith(" ", i + 1)) ? vi.substring(i + 2) : vi.substring(i + 1));

            if (vi.contains("new")) {
                if (i != -1) {
                    vi = vi.substring(vi.indexOf("]") + 1, i);
                    i = vi.lastIndexOf(",");
                    continue;
                }
            } else {
                if (i != -1) {
                    vi = vi.substring(0, i);
                    i = vi.lastIndexOf(",");
                    continue;
                }
            }
            vi = "";
        }
        if (!vi.contains("new"))
            record.setValue(listValue.size());

        record.setList(listValue);
    }
}
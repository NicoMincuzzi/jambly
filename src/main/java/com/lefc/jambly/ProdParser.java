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
        int lastCommaInSentence = vi.lastIndexOf(",");
        while (lastCommaInSentence != -1 || !vi.equals("")) {
            int index = (vi.startsWith(" ", lastCommaInSentence + 1))
                    ? lastCommaInSentence + 2
                    : lastCommaInSentence + 1;

            String result = vi.substring(index);
            listValue.add(result);

            if (vi.contains("new")) {
                if (lastCommaInSentence != -1) {
                    vi = vi.substring(vi.indexOf("]") + 1, lastCommaInSentence);
                    lastCommaInSentence = vi.lastIndexOf(",");
                    continue;
                }
            } else {
                if (lastCommaInSentence != -1) {
                    vi = vi.substring(0, lastCommaInSentence);
                    lastCommaInSentence = vi.lastIndexOf(",");
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
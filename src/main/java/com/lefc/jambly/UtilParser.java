package com.lefc.jambly;

import java.util.HashMap;
import java.util.ListIterator;

public class UtilParser {
    private static int countBrace;

    public static void newScope() {
        countBrace = countBrace + 1;
        SymbolTable.createTable();
    }

    public static void delScope() {
        ListIterator<HashMap<String, Record>> listItMap = SymbolTable.getArrMap().listIterator(countBrace);

        if (listItMap.hasPrevious()) {
            listItMap.previous();
            listItMap.remove();
        }
        countBrace = countBrace - 1;
    }
}

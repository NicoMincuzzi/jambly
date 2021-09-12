package com.lefc.jambly;

import java.util.HashMap;
import java.util.ListIterator;

import static com.lefc.jambly.SymbolTable.createTable;
import static com.lefc.jambly.SymbolTable.getArrMap;

public class UtilParser {
    private static int countBrace;

    public static void newScope() {
        countBrace += 1;
        createTable();
    }

    public static void delScope() {
        ListIterator<HashMap<String, Record>> iterator = getArrMap().listIterator(countBrace);

        if (iterator.hasPrevious()) {
            iterator.previous();
            iterator.remove();
        }
        countBrace = countBrace - 1;
    }
}

package com.lefc.jambly;

import static com.lefc.jambly.SymbolTable.createTable;

public class UtilParser {
    private static int countBrace;

    public static void newScope() {
        countBrace += 1;
        createTable();
    }

    public static void delScope() {
        SymbolTable.retrieveAll().remove(countBrace - 1);
        countBrace = countBrace - 1;
    }
}

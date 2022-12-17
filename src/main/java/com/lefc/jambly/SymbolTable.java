package com.lefc.jambly;

import java.util.*;

public class SymbolTable {

    private static final List<Map<String, Record>> listTable = new ArrayList<>();
    private static Map<String, Record> table;

    /*METODO PER LA RESTITUZIONE DEL CORRENTE RECORD*/
    public static Record getCurrRec(String key) {
        return table.get(key);
    }

    public static void createTable() {
        table = new HashMap<>();
        listTable.add(table);
    }

    /*METODO CHE PERMETTE DI OTTENERE LA TABELLA CORRENTE*/
    public static Map<String, Record> getTable() {
        return table;
    }

    /*METODO CHE PERMETTE DI OTTENERE ARRAYLIST CONTENENTE TUTTE LE TABELLE*/
    public static List<Map<String, Record>> retrieveAll() {
        return listTable;
    }

    public static Record retrieveVariableInsideScope(String variable) {
        if (table.containsKey(variable))
            return table.get(variable);

        for(int i = listTable.size() - 1; i >= 0; i--){
            table = listTable.get(i);
            if (table.containsKey(variable)) {
                return table.get(variable);
            }
        }

        CUP$parser$actions.Err_War = "ERROR: la variabile non e' stata dichiarata in nessun scope!\n";
        CUP$parser$actions.checkFlag = true;
        return new Record();
    }

    public static void addVariableToTable(String variable) {
        if (table.containsKey(variable)) {
            CUP$parser$actions.Err_War = "ERROR: la variabile e' stata gia' dichiarata!\n";
            CUP$parser$actions.checkFlag = true;
            return;
        }
        table.put(variable, new Record());
    }
}

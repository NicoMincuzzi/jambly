package com.lefc.jambly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

public class SymbolTable {

    private static ArrayList<HashMap<String, Record>> listTable = new ArrayList<>();
    private static HashMap<String, Record> table;

    /*METODO PER LA RESTITUZIONE DEL CORRENTE RECORD*/
    public static Record getCurrRec(String key) {
        return table.get(key);
    }

    /*METODO PER LA CREAZIONE DI UNA NUOVA TABELLA*/
    public static void createTable() {
        table = new HashMap<>();
        listTable.add(table);
    }

    /*METODO CHE PERMETTE DI OTTENERE LA TABELLA CORRENTE*/
    public static HashMap<String, Record> getTable() {
        return table;
    }

    /*METODO CHE PERMETTE DI OTTENERE ARRAYLIST CONTENENTE TUTTE LE TABELLE*/
    public static ArrayList<HashMap<String, Record>> getArrMap() {
        return listTable;
    }

    public static Record retrieveVariableInsideScope(String variable) {
        Record rec = new Record();
        boolean flagT = false;

        if (table.containsKey(variable)) {
            rec = table.get(variable);
        } else {
            int h = listTable.size() - 1;
            ListIterator<HashMap<String, Record>> listItMap = listTable.listIterator(h);
            while (listItMap.hasPrevious() && !flagT) {
                table = listItMap.previous();
                if (table.containsKey(variable)) {
                    flagT = true;
                    rec = table.get(variable);
                }
                h--;
                listItMap = listTable.listIterator(h);
            }
            if (!flagT) {
                CUP$parser$actions.Err_War = "ERROR: la variabile non e' stata dichiarata in nessun scope!\n";
                CUP$parser$actions.checkFlag = true;
            }
        }
        return rec;
    }

    public static void addVariableToTable(String variable) {
        if (table.containsKey(variable)) {
            CUP$parser$actions.Err_War = "ERROR: la variabile e' stata gia' dichiarata!\n";
            CUP$parser$actions.checkFlag = true;
        } else {
            table.put(variable, new Record(variable));
        }
    }
}

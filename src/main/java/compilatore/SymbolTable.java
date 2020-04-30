package compilatore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;

public class SymbolTable {

    private static ArrayList<HashMap> listTable = new ArrayList<>();
    private static HashMap<String, Record> table;
    private static ListIterator<HashMap> listItMap;

    /*METODO PER LA RESTITUZIONE DEL CORRENTE RECORD*/
    public static Record getCurrRec(String key) {
        Record rec = table.get(key);
        return rec;
    }

    /*METODO PER LA CREAZIONE DI UNA NUOVA TABELLA*/
    public static void createTable() {
        table = new HashMap<>();
        listTable.add(table);
    }

    /*METODO CHE PERMETTE DI OTTENERE LA TABELLA CORRENTE*/
    public static HashMap getTable() {
        return table;
    }

    /*METODO CHE PERMETTE DI OTTENERE ARRAYLIST CONTENENTE TUTTE LE TABELLE*/
    public static ArrayList getArrMap() {
        return listTable;
    }


    /*METODO DI SUPPORTO PER LA STAMPA DEI CAMPI DI UN RECORD, OSSIA DI UNA ENTRY*/
    public static void stampaCampi() {
        Iterator<String> it = table.keySet().iterator();

        while (it.hasNext()) { //ci chiediamo se è presente la prossima voce della tabella
            Record rec = table.get(it.next()); //nel caso positivo, cerchiamo di ottenerla
            rec.showField();
        }
    }

    /*METODO CHE INDICA SE UNA VARIABILE È PRESENTE O MENO ALL'INTERNO DI UNO SCOPE*/
    public static Record checkVScope(String lhs) {
        Record rec = new Record();
        boolean flagT = false;

        if (table.containsKey(lhs)) {
            rec = table.get(lhs);
        } else {
            int h = listTable.size() - 1;
            listItMap = listTable.listIterator(h);
            while (listItMap.hasPrevious() && flagT == false) {
                table = listItMap.previous();
                if (table.containsKey(lhs)) {
                    flagT = true;
                    rec = table.get(lhs);
                }
                h--;
                listItMap = listTable.listIterator(h);
            }
            if (flagT == false) {
                CUP$parser$actions.Err_War = "ERROR: la variabile non e' stata dichiarata in nessun scope!\n";
                CUP$parser$actions.checkFlag = true;
            }
        }
        return rec;
    }

    /*METODO CHE VERIFICA SE UNA VARIABILE È PRESENTE IN TABELLA, IN CASO NEGATIVO VIENE AGGIUNTA*/
    public static void addTb(String var) {

        if (table.containsKey(var)) {
            CUP$parser$actions.Err_War = "ERROR: la variabile e' stata gia' dichiarata!\n";
            CUP$parser$actions.checkFlag = true;
        } else {
            table.put(var, new Record(var));
        }
    }
}

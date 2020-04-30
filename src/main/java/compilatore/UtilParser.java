package compilatore;

import java.util.HashMap;
import java.util.ListIterator;

public class UtilParser {
    private static int countBrace;
    private static int countBrace2;

    public static int getcountBrace() {
        return countBrace;
    }

    public static int getcountBrace2() {
        return countBrace2;
    }

    public static void addBr() {
        countBrace2++;
    }

    public static void decrBr() {
        countBrace2--;
    }

    public static void newScope() {
        countBrace = countBrace + 1;
        SymbolTable.createTable();
    }

    public static void delScope() {
        ListIterator<HashMap> listItMap = SymbolTable.getArrMap().listIterator(countBrace);

        if (listItMap.hasPrevious()) {

            listItMap.previous();
            listItMap.remove();

        }
        /*STAMPA DI TEST CHE VERIFICA IL CORRETTO FUNZIONAMENTO DEL METODO delScope()*/
        //System.out.println("TABELLE PRESENTI(OUTPUT): "+SymbolTable.getArrMap().size());
        countBrace = countBrace - 1;
    }
}

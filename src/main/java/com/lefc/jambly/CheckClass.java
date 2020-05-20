package com.lefc.jambly;

import java.util.ListIterator;

public class CheckClass {

    private static String str;

    /*METODO PER IL CONTROLLO SUI MODIFICATORI*/
    public static void checkMod() {
        ListIterator<String> it = Support.getArrList().listIterator();

        if (Support.getArrList().size() <= 3 && Support.getArrList().size() > 0) {
            String m1 = it.next();
            String m2;
            while (it.hasNext()) {
                m2 = it.next();
                if (m1.equals(m2) || m1.equals("public") && m2.equals("private") ||
                        m1.equals("private") && m2.equals("public")) {
                    CUP$parser$actions.Err_War = "Error: Modificatori uguali o dello stesso tipo!\n";
                    CUP$parser$actions.checkFlag = true;
                }
            }

            if (Support.getArrList().size() > 2) {
                it = Support.getArrList().listIterator(1);
                m1 = it.next();
                m2 = it.next();
                if (m1.equals(m2) || m1.equals("public") && m2.equals("private") ||
                        m1.equals("private") && m2.equals("public")) {
                    CUP$parser$actions.Err_War = "Error: Modificatori uguali o dello stesso tipo!\n";
                    CUP$parser$actions.checkFlag = true;
                }
            }
        } else {
            CUP$parser$actions.Err_War = "Error: almeno un modificatore/al massimo tre modificatori!\n";
            CUP$parser$actions.checkFlag = true;
        }

    }

    /*METODO PER IL CONTROLLO DEL TIPO NELLE ESPRESSIONI*/
    public static String checkTypeMD(String type1, String type2) {
        if (!type1.equals(type2)) {
            if (type1.equals("INTEGER") && type2.equals("DOUBLE") ||
                    type1.equals("DOUBLE") && type2.equals("INTEGER")) {
                CUP$parser$actions.Err_War = "WARNING: tipi differenti! Si provvederà ad effettuare un cast per la risoluzione del problema!\n";
                str = "DOUBLE";
                CUP$parser$actions.flagWarn = true;

            } else {
                CUP$parser$actions.Err_War = "ERROR: non e' possibile effettuare tale operazione tra " +
                        type1 + " e " + type2 + "\n";
                CUP$parser$actions.checkFlag = true;
            }
        } else {
            str = type1;
        }
        return str;
    }

    /*METODO PER IL CONTROLLO DI TIPO IN UNA ASSEGNAZIONE*/
    public static String checkTypeA(String type1, String type2, String testo2) {

        if (testo2.contains("new") && !type1.equals(type2)) {
            CUP$parser$actions.Err_War = "Error: non è possibile assegnare " +
                    type2 + " a " + type1 + "!\n";
            CUP$parser$actions.checkFlag = true;

        } else if (!type1.equals(type2)) {
            if (type1.equals("DOUBLE") && type2.equals("INTEGER")) {
                CUP$parser$actions.Err_War = "WARNING: tipi differenti! Si provederà ad effettuare" +
                        " un cast per la risoluzione del problema!\n";
                str = "DOUBLE";
                CUP$parser$actions.flagWarn = true;
            } else if (type1.equals("INTEGER") && type2.equals("DOUBLE")) {
                CUP$parser$actions.Err_War = "ERROR: assegnazione di tipi differenti! "
                        + "Verrà effettuata un'approssimazione per difetto!\n";
                str = "INTEGER";
                CUP$parser$actions.checkFlag = true;

            } else if (type1.equals("STRING") || type2.equals("STRING")) {
                str = ""; //controllare la fermata
                CUP$parser$actions.Err_War = "ERROR: assegnazione di tipi differenti!\n";
                CUP$parser$actions.checkFlag = true;
            } else {
                str = "";
            }
        } else {
            str = type1;
        }
        return str;
    }

    /*METODO PER IL CONTROLLO DEL TIPO NELLE RELAZIONI <, >, <=, >=*/
    public static void checkOpCond(String type1, String type2) {

        if (type1.equals("STRING") || type2.equals("STRING") ||
                type1.equals("BOOLEAN") || type2.equals("BOOLEAN")) {
            CUP$parser$actions.Err_War = "ERROR: tipi degli operandi errati per un operatore binario!\n";
            CUP$parser$actions.checkFlag = true;
        }
    }

    /*METODO PER IL CONTROLLO DEL TIPO ==, != */
    public static void checkEQNEQ(String type1, String type2) {

        if (type1.equals("STRING") && !type2.equals("STRING") ||
                !type1.equals("STRING") && type2.equals("STRING") ||
                type1.equals("BOOLEAN") && !type2.equals("BOOLEAN") ||
                !type1.equals("BOOLEAN") && type2.equals("BOOLEAN")) {
            CUP$parser$actions.Err_War = "ERROR: tipi degli operandi errati per un operatore binario!\n";
            CUP$parser$actions.checkFlag = true;
        }
    }
}

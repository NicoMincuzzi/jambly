package com.lefc.jambly;

import java.util.List;

import static java.util.Arrays.asList;


public class CheckClass {

    public static final int MAX_MODIFIERS_NUMBER = 3;
    public static final int MIN_MODIFIERS_NUMBER = 1;

    public boolean checkModifiers(List<String> modifiers) {
        boolean result = CUP$parser$actions.checkFlag;
        if (modifiers.size() > MAX_MODIFIERS_NUMBER || modifiers.size() < MIN_MODIFIERS_NUMBER) {
            CUP$parser$actions.Err_War = "Error: almeno un modificatore/al massimo tre modificatori!\n";
            result = true;
        }

        if (modifiers.contains("private") && modifiers.contains("public")) {
            CUP$parser$actions.Err_War = "Error: Modificatori uguali o dello stesso tipo!\n";
            result = true;
        }
        return result;
    }


    public static String checkExpressionType(String leftType, String rightType) {
        if (!leftType.equals(rightType)) {
            if (asList("INTEGER", "DOUBLE").contains(leftType) && asList("INTEGER", "DOUBLE").contains(rightType)) {
                CUP$parser$actions.Err_War = "WARNING: tipi differenti! Si provvederà ad effettuare un cast per la risoluzione del problema!\n";
                CUP$parser$actions.flagWarn = true;
                return "DOUBLE";
            }
            CUP$parser$actions.Err_War = "ERROR: non e' possibile effettuare tale operazione tra " + leftType + " e " + rightType + "\n";
            CUP$parser$actions.checkFlag = true;
            return null;
        }
        return leftType;
    }

    /*METODO PER IL CONTROLLO DI TIPO IN UNA ASSEGNAZIONE*/
    public static String checkTypeA(String leftType, String rightType, String assignmentStatement) {
        if (leftType.equals(rightType)) {
            return leftType;
        }

        if (assignmentStatement.contains("new")) {
            CUP$parser$actions.Err_War = "Error: non è possibile assegnare " + rightType + " a " + leftType + "!\n";
            CUP$parser$actions.checkFlag = true;
            return null;
        }

        if (leftType.equals("DOUBLE") && rightType.equals("INTEGER")) {
            CUP$parser$actions.Err_War = "WARNING: tipi differenti! Si provederà ad effettuare" +
                    " un cast per la risoluzione del problema!\n";
            CUP$parser$actions.flagWarn = true;
            return "DOUBLE";
        }

        if (leftType.equals("INTEGER") && rightType.equals("DOUBLE")) {
            CUP$parser$actions.Err_War = "ERROR: assegnazione di tipi differenti! "
                    + "Verrà effettuata un'approssimazione per difetto!\n";
            CUP$parser$actions.checkFlag = true;
            return "INTEGER";
        }
        if (leftType.equals("STRING") || rightType.equals("STRING")) {
            CUP$parser$actions.Err_War = "ERROR: assegnazione di tipi differenti!\n";
            CUP$parser$actions.checkFlag = true;
            return "";
        }
        return "";
    }

    /*METODO PER IL CONTROLLO DEL TIPO NELLE RELAZIONI <, >, <=, >=*/
    public static boolean checkOpCond(String type1, String type2) {
        List<String> checkValue = asList("STRING", "BOOLEAN");
        if (checkValue.contains(type1) || checkValue.contains(type2)) {
            CUP$parser$actions.Err_War = "ERROR: tipi degli operandi errati per un operatore binario!\n";
            return true;
        }
        return false;
    }

    public static void isValidEqualsOrNotEqualsComparison(String type1, String type2) {
        if (type1.equals("STRING") && !type2.equals("STRING") ||
                !type1.equals("STRING") && type2.equals("STRING") ||
                type1.equals("BOOLEAN") && !type2.equals("BOOLEAN") ||
                !type1.equals("BOOLEAN") && type2.equals("BOOLEAN")) {
            CUP$parser$actions.Err_War = "ERROR: tipi degli operandi errati per un operatore binario!\n";
            CUP$parser$actions.checkFlag = true;
        }
    }
}

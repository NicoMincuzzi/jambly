package com.lefc.jambly;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Support {

    private static List<String> listMod;
    private static int numErr;

    public static List<String> getArrList() {
        return listMod;
    }

    /*Funzione che permette di dividere parole separate da spazi*/
    public static void sepWordWS(String s) {
        listMod = Arrays.stream(s.split(" ")).collect(Collectors.toList());
    }

    public static String getVar(String varad) {
        if (varad.contains("[]")) {
            return varad.substring(0, varad.indexOf("[]"));
        }
        return varad.substring(0, varad.indexOf("=") - 1);
    }

    public static String calcOffset(String espr, String type) {
        switch (type) {
            case "INTEGER":
                return String.valueOf(Integer.parseInt(espr) * 4);
            case "DOUBLE":
                return String.valueOf(Integer.parseInt(espr) * 8);
            default:
                break;
        }
        return String.valueOf(0);
    }

    public static void countExit(int errorCount) {
        numErr = errorCount;
    }

    public static int getnumErr() {
        return numErr;
    }
}
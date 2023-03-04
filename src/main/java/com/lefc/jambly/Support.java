package com.lefc.jambly;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Support {
    private static List<String> listMod;

    public static List<String> getArrList() {
        return listMod;
    }

    /*Funzione che permette di dividere parole separate da spazi*/
    public static void sepWordWS(String s) {
        listMod = Arrays.stream(s.split(" ")).collect(toList());
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
}
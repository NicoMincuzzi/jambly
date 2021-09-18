package com.lefc.jambly;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Support {

    private static List<String> listMod;
    private static int numErr;

    public static List<String> getArrList() {
        return listMod;
    }

    /*Funzione che permette di dividere parole separate da spazi*/
    public static void sepWordWS(String s) {
        listMod = new ArrayList<>();
        String str;
        int i = 0;
        while (i != -1) {
            i = s.lastIndexOf(" ");
            str = s.substring(i + 1);
            listMod.add(str);
            if (i != -1) {
                s = s.substring(0, i);
            }
        }
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

    public static void writeFile(boolean flag, String translation) throws IOException {
        FileWriter file = new FileWriter("FileTrad.txt", true);
        if (flag) {
            file.write(translation);
            file.flush();
            return;
        }
        file.write(translation);
        file.close();
    }

    public static void countExit(int errorCount) {
        numErr = errorCount;
    }

    public static int getnumErr() {
        return numErr;
    }
}
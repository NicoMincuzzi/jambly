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
        int i = 0;
        listMod = new ArrayList<>();
        String str;
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
        String str;
        if (varad.contains("[]")) {
            str = varad.substring(0, varad.indexOf("[]"));
        } else {
            str = varad.substring(0, varad.indexOf("=") - 1);
        }
        return str;
    }

    public static String calcOffset(String espr, String tipo) {
        int i = 0;
        switch (tipo) {
            case "INTEGER":
                i = Integer.parseInt(espr) * 4;
                break;
            case "DOUBLE":
                i = Integer.parseInt(espr) * 8;
                break;
            default:
                System.out.println("ERROR: tipo non supportato!");
                break;
        }

        return String.valueOf(i);
    }

    public static void writeFile(boolean flag, String TRAD) throws IOException {
        FileWriter file = new FileWriter("FileTrad.txt", true);
        if (flag) {
            file.write(TRAD);
            file.flush();
        } else {
            file.write(TRAD);
            file.close();
        }
    }

    public static void countExit(int errorCount) {
        numErr = errorCount;
    }

    public static int getnumErr() {
        return numErr;
    }
}
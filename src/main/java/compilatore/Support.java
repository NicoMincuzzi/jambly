package compilatore;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Support {
    private static ArrayList<String> listMod;
    private static int I;
    private static double D;
    private static Integer it;
    private static String str;
    private static int numErr;

    public static ArrayList getArrList() {
        return listMod;
    }

    public static int changeStrInt(String espr) {
        try {
            it = new Integer(espr);
            I = it.intValue();
        } catch (Exception e) {
        }
        return I;
    }

    /*Funzione che permette di dividere parole separate da spazi*/
    public static void sepWordWS(String s) {
        I = 0;
        listMod = new ArrayList<>();
        while (I != -1) {
            I = s.lastIndexOf(" ");
            str = s.substring(I + 1, s.length());
            listMod.add(str);

            if (I != -1) {
                s = s.substring(0, I);
            }
        }
    }


    public static String getVar(String varad) {
        if (varad.contains("[]")) {
            str = varad.substring(0, varad.indexOf("[]"));
        } else {
            str = varad.substring(0, varad.indexOf("=") - 1);
        }
        return str;
    }

    public static String calcOffset(String espr, String tipo) {
        I = 0;
        switch (tipo) {
            case "INTEGER":
                I = changeStrInt(espr) * 4;
                break;
            case "DOUBLE":
                I = changeStrInt(espr) * 8;
                break;
            default:
                System.out.println("ERROR: tipo non supportato!");
                break;
        }

        str = String.valueOf(I);
        return str;
    }

    public static void writeFile(boolean FLAG, String TRAD) throws IOException {
        FileWriter file = new FileWriter("FileTrad.txt", true);
        if (FLAG == true) {
            file.write(TRAD);
            file.flush();
        } else {
            file.write(TRAD);
            file.close();
        }
    }

    public static void writeFileErr(String TRAD) throws IOException {
        FileWriter file = new FileWriter("FileErr.txt", true);
        file.write(TRAD);
        file.flush();
    }

    public static void countExit(int cont_errori) {
        numErr = cont_errori;
    }

    public static int getnumErr() {
        return numErr;
    }
}
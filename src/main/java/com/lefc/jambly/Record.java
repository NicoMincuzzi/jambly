package com.lefc.jambly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Record {
    private String name;
    private String modifier = "null";
    private Object value; //valore della variabile(può essere int, double o String) o dimensione vettore all'occorenza
    private String type;
    private List<String> list;
    private String register; //nome del registro che utilizzeremo in Assembly
    private String regDim;
    private boolean FA;
    private HashMap<String, String> TabArrTemp = new HashMap<>();

    /*METODI CHE PERMETTONO DI OTTENERE I VARI ELEMENTI DEL RECORD*/
    public String getName() {
        return name;
    }

    public String getModifier() {
        return modifier;
    }

    public Object getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public List<String> getList() {
        return list;
    }

    public String getRegister() {
        return register;
    }

    public HashMap getTabRegArr() {
        return TabArrTemp;
    }

    public void setName(String N) {
        name = N;
    }

    public void setModifier(String M) {
        modifier = M;
    }

    public void setValue(Object V) {
        value = V;
    }

    public void setType(String T) {
        type = T;
    }

    public void setList(List<String> listValue) {
        list = listValue;
        FA = !list.isEmpty();
    }

    public void setRegister(String R) {
        register = R;
    }

    public void setRegDim(String RD) {
        regDim = RD;
    }

    public boolean setArrayList(String value, int index) {
        try {
            list.add(index, value);
            return index + 1 != list.size();
        } catch (Exception e) {
            if (index == 0) {
                list.add(value);
                return false;
            } else {
                list.add(value);
                CUP$parser$actions.Err_War = "WARNING: array vuoto!!! L'elemento è stato inserito nella prima posizione libera!\n";
                CUP$parser$actions.flagWarn = true;
                return false;
            }
        }
    }

    /*COSTRUTTORI DELLA CLASSE RECORD*/
    public Record() {
    }

    public Record(String N) {
        name = N;
    }

    //Record per la gestione di vettori
    public Record(String T, ArrayList L, int D, String N) {
        type = T;
        value = D;
        list = L;
        name = N;
        FA = true;
    }

    //Record per la gestione di vettori
    public Record(String T, ArrayList L, int D, String N, String R) {
        type = T;
        value = D;
        list = L;
        name = N;
        register = R;
        FA = true;
    }

    //Record per la gestione di vettori
    public Record(String T, ArrayList L, String D, String N) {
        type = T;
        value = D;
        list = L;
        name = N;
        FA = true;
    }

    //Record per la gestione delle variabili
    public Record(String T, String V, String N) {
        type = T;
        value = V;
        name = N;
        FA = false;
    }

    public Record(String T, String N) {
        type = T;
        value = 0;
        name = N;
        FA = false;
    }

    public Record(String T, String N, String R, int V) {
        //il quarto parametro può essere un qualsiasi intero
        type = T;
        value = 0;
        name = N;
        register = R;
        FA = false;
    }

    /*METODO CHE PERMETTE L'ELIMINAZIONE DI UN ELEMENTO DEL RECORD*/
    public void remElem(int index) {
        list.remove(index);
    }
}

package com.lefc.jambly;

import java.util.HashMap;
import java.util.List;

public class Record {
    private String modifier = "null";
    private Object value; //valore della variabile(può essere int, double o String) o dimensione vettore all'occorenza
    private String type;
    private List<String> list;
    private String assemblyRegister;
    private final HashMap<String, String> TabArrTemp = new HashMap<>();

    public Record() {
    }

    public Record(String type, String register) {
        this.type = type;
        value = 0;
        assemblyRegister = register;
    }

    public Record(String type, List<String> L, int value, String register) {
        this.type = type;
        this.value = value;
        list = L;
        assemblyRegister = register;
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
        return assemblyRegister;
    }

    public HashMap<String, String> getTabRegArr() {
        return TabArrTemp;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setList(List<String> listValue) {
        list = listValue;
    }

    public void setRegister(String R) {
        assemblyRegister = R;
    }

    public void setAssemblyRegister(String R) {
        assemblyRegister = R;
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

    /*METODO CHE PERMETTE L'ELIMINAZIONE DI UN ELEMENTO DEL RECORD*/
    public void remElem(int index) {
        list.remove(index);
    }
}

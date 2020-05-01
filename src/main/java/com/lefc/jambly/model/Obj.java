package com.lefc.jambly.model;

public class Obj {
    public String testo;
    public String tipo;
    public String genere;
    public int linea;

    public Obj(String testo, String tipo, String genere) {
        this.testo = testo;
        this.tipo = tipo;
        this.genere = genere;
    }

    public Obj(String testo, String tipo, String genere, int linea) {
        this.testo = testo;
        this.tipo = tipo;
        this.genere = genere;
        this.linea = linea;
    }

    public Obj(String testo, String tipo) {
        this.testo = testo;
        this.tipo = tipo;
        this.genere = "";
    }

    public Obj(String testo, String tipo, int linea) {
        this.testo = testo;
        this.tipo = tipo;
        this.genere = "";
        this.linea = linea;
    }


    public Obj(String testo) {
        this.testo = testo;
        this.tipo = "";
        this.genere = "";
    }
}
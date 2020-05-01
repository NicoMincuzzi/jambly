package com.lefc.jambly;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class UtilScanner {
    private int line;
    private int pos;

    public UtilScanner() {
        this.line = 0;
        this.pos = 0;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getLine() {
        return line;
    }

    public int getPos() {
        return pos;
    }

    public void getUtil() throws FileNotFoundException {
        FileReader fr2 = new FileReader(Compilatore.getPath());
        ExprLex scanner = new ExprLex(fr2);

        java_cup.runtime.Symbol t;
        java_cup.runtime.Symbol end = new java_cup.runtime.Symbol(sym.EOF);

        boolean flag = true;
        int pos = 0;
        try {
            while ((t = scanner.next_token()) != null && flag == true) {
                if (t.toString().compareTo(end.toString()) != 0) {
                    setLine(scanner.getLine());
                    setPos(scanner.getCurrentPos());
                } else {
                    flag = false;
                }
            }
        } catch (IOException ioe) {
            System.out.println("token a null");
        }
    }
}

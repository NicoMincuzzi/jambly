package com.lefc.jambly;

import java_cup.runtime.Symbol;

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
        FileReader fileReader = new FileReader(Compilatore.getPath());
        Scanner scanner = new Scanner(fileReader);

        Symbol t;
        Symbol end = new Symbol(sym.EOF);

        boolean flag = true;
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

package com.lefc.jambly;

import static java.util.Arrays.asList;

public class PrintText implements Comparable<PrintText> {
    private String message;
    private String wrongString;
    private int pos;

    public PrintText() {
        message = "";
        wrongString = "";
        pos = -1;
    }

    @Override
    public int compareTo(PrintText o) {
        if (this.getPos() < o.getPos()) {
            return -1;  //ordinamento in base al n di riga in modo da averli come nel source
        }
        return (this.getPos() == o.getPos()) ? 0 : 1;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getString() {
        return wrongString;
    }

    public int getPos() {
        return pos;
    }

    public void setString(String s) {
        this.wrongString = s;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void textAndLine(int linea, char[] zzBuffer) {
        int start = 0;
        int counter = 0;

        while (counter != linea - 1) {
            if (zzBuffer[start] == '\n') {
                counter++;
            }
            start++;
        }

        StringBuilder load = new StringBuilder(String.valueOf(zzBuffer[start + 1]));
        readCharUntilTheNextRow(zzBuffer, load, start);
        wrongString = load.toString().trim();
        pos = linea;
    }

    public void textAndLine(int currPosition, int linea, char[] zzBuffer) {
        //capisce che è avvenuto l'errore solo una volta superato e quindi verrà restituito il token sbagliato
        //per soddisfare la sintassi o il lessico
        StringBuilder load = new StringBuilder();

        if (currPosition == 0) {
            readCharUntilTheNextRow(zzBuffer, load, currPosition);
            this.wrongString = load.toString();
            this.pos = linea;
            return;
        }

        //nel caso in cui l'err si sia verificato a metà e l'err è stato rilevato dopo
        //controlla se c'è un tab o uno spazio vuoto
        int start = currPosition - 1;
        int line = linea;
        if (asList('\t', ' ').contains(zzBuffer[start])) {   //torno indietro assicurandomi di restare nel codice e di non sforare a -1

            start = moveToLastValidChar(zzBuffer, start);

            if (checkErrorInTheBeginingAndInSameRow(linea, zzBuffer, currPosition, load, start - 1)) return;

            //errore all'interno della riga
            if (zzBuffer[start] != '\n' && zzBuffer[start] != '\r') {
                while (zzBuffer[start - 1] != '\n' && start != 1) {
                    start--;
                }
                readCharUntilTheNextRow(zzBuffer, load, start);
                this.wrongString = load.toString().trim();
                this.pos = line;
                return;
            }

            line = checkErrorInPrevRow(zzBuffer, load, start, line);
            this.wrongString = load.toString().trim();
            this.pos = line;
            return;
        }

        if (zzBuffer[start] == '\n' || zzBuffer[start] == '\r') {
            start--;
            line--;
            while (asList('\t', ' ', '\n', '\r').contains(zzBuffer[start]) && start - 1 != 0) {
                if (zzBuffer[start] == '\n')
                    line--;
                start--;
            }

            if ((start - 1) == 0) {
                while (asList('\t', ' ', '\n', '\r').contains(zzBuffer[start])) {
                    start++;
                }
                readCharUntilTheNextRow(zzBuffer, load, start);
                this.wrongString = load.toString().trim();
                this.pos = line;
                return;
            }
            while (zzBuffer[start] != '\n') {
                load.append(zzBuffer[start]);
                start--;
            }
            load.reverse();

            this.wrongString = load.toString().trim();
            this.pos = line;
            return;
        }

        while (zzBuffer[start - 1] != '\n')
            start--;
        readCharUntilTheNextRow(zzBuffer, load, start);
        this.wrongString = load.toString().trim();
        this.pos = line;
    }

    private static void readCharUntilTheNextRow(char[] zzBuffer, StringBuilder load, int start) {
        //leggi fino alla prossima riga dove una riga è \n .... \n
        while (zzBuffer[start] != '\n') {
            load.append(zzBuffer[start]);
            start++;
        }
    }

    private static int moveToLastValidChar(char[] zzBuffer, int start) {
        while (asList('\t', ' ').contains(zzBuffer[start]) && start - 1 != 0) {
            start--;
        }
        return start;
    }

    private boolean checkErrorInTheBeginingAndInSameRow(int linea, char[] zzBuffer, int end, StringBuilder load, int start) {
        if (start != 0) {
            return false;
        }
        readCharUntilTheNextRow(zzBuffer, load, end);
        this.wrongString = load.toString();
        this.pos = linea;
        return true;
    }

    private static int checkErrorInPrevRow(char[] zzBuffer, StringBuilder load, int start, int line) {
        if (zzBuffer[start] != '\n' && zzBuffer[start] != '\r') {
            return line;
        }
        start--;
        line--;

        //va indietro finchè non trova un carattere o se finisce il file
        while (asList('\t', ' ', '\n', '\r').contains(zzBuffer[start]) && start - 1 != 0) {
            if (zzBuffer[start] == '\n')
                line--;
            start--;
        }

        //errore programma inizia diverse righe dopo
        if ((start - 1) == 0) {
            while (asList('\t', ' ', '\n', '\r').contains(zzBuffer[start])) {
                start++;
            }
            readCharUntilTheNextRow(zzBuffer, load, start);
            return line;
        }

        //errore riga precedente
        load.append(zzBuffer[start]);
        start--;
        //carico la riga al contrario
        while (zzBuffer[start] != '\n') {
            load.append(zzBuffer[start]);
            start--;
        }
        load.reverse(); //qui la mostro nel giusto verso
        return line;
    }
}

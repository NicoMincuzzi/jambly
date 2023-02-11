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
        int point = 0;

        while (point != linea - 1) {
            if (zzBuffer[start] == '\n') {
                point++;
            }
            start++;
        }
        start++;

        StringBuilder load = new StringBuilder();
        load.append(zzBuffer[start]);
        while (zzBuffer[start] != '\n') {
            load.append(zzBuffer[start]);
            start++;
        }
        wrongString = load.toString().trim().replaceAll("\n", "");
        pos = linea;
    }

    public void textAndLine(int pos_attuale, int linea, char[] zzBuffer) {
        //capisce che è avvenuto l'errore solo una volta superato e quindi verrà restituito il token sbagliato
        //per soddisfare la sintassi o il lessico
        int start = pos_attuale - 1;
        int end = pos_attuale;
        int line = linea;

        StringBuilder load = new StringBuilder();

        if (end == 0) {
            while (zzBuffer[end] != '\n') {
                load.append(zzBuffer[end]);
                end++;
            }
            this.wrongString = load.toString();
            this.pos = line;
            return;
        }

        //nel caso in cui l'err si sia verificato a metà e l'err è stato rilevato dopo
        //controlla se c'è un tab o uno spazio vuoto
        boolean flag = false;
        if (zzBuffer[start] == '\t' || zzBuffer[start] == ' ') {   //torno indietro assicurandomi di restare nel codice e di non sfornare a -1
            while ((zzBuffer[start] == '\t' || zzBuffer[start] == ' ') && start - 1 != 0) { //diminuisco il valore di start
                start--;
            }
            //controllo errore inizio programma stessa riga
            if (start - 1 == 0) {
                flag = true;

                while (zzBuffer[end] != '\n') {
                    load.append(zzBuffer[end]);
                    end++;
                }
            } else {
                //errore all'interno della riga
                if (zzBuffer[start] != '\n' && zzBuffer[start] != '\r') {
                    while (zzBuffer[start - 1] != '\n' && start != 1) {
                        start--;
                    }
                    //leggi fino alla prossima riga dove una riga è \n .... \n
                    while (zzBuffer[start] != '\n') {
                        load.append(zzBuffer[start]);
                        start++;
                    }
                } else {
                    //errore nella riga precedente
                    if (zzBuffer[start] == '\n' || zzBuffer[start] == '\r') {
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
                            while (zzBuffer[start] != '\n') {
                                load.append(zzBuffer[start]);
                                start++;
                            }
                        } else {
                            //errore riga precedente
                            load.append(zzBuffer[start]);
                            start--;
                            //carico la riga al contrario
                            while (zzBuffer[start] != '\n') {
                                load.append(zzBuffer[start]);
                                start--;
                            }
                            load.reverse(); //qui la mostro nel giusto verso
                        }
                    }
                }
            }
            setString(load.toString().trim().replaceAll("\n", ""));
            setPos((flag) ? linea : line);
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
                flag = true;
                while (asList('\t', ' ', '\n', '\r').contains(zzBuffer[start])) {
                    start++;
                }
                while (zzBuffer[start] != '\n') {
                    load.append(zzBuffer[start]);
                    start++;
                }
            } else {
                while (zzBuffer[start] != '\n') {
                    load.append(zzBuffer[start]);
                    start--;
                }
                load.reverse();
            }
        } else {
            while (zzBuffer[start - 1] != '\n')
                start--;
            while (zzBuffer[start] != '\n') {
                load.append(zzBuffer[start]);
                start++;
            }
        }
        setString(load.toString().trim().replaceAll("\n", ""));
        setPos((flag) ? linea : line);
    }
}
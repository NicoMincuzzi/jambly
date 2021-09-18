package com.lefc.jambly;

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
        //confronto tra ogg chiamante e l'ogg passato
        if (this.getPos() < o.getPos()) {
            return -1;  //ordinamento in base al n di riga in modo da averli come nel source
        } else {
            return (this.getPos() == o.getPos()) ? 0 : 1;
        }
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
        StringBuilder load = new StringBuilder("");
        String wrongStr;
        int start = 0;
        int point = 0;

        while (point != linea - 1) {
            if (zzBuffer[start] == '\n')
                point++;
            start++;
        }
        start++;
        load.append(zzBuffer[start]);
        while (zzBuffer[start] != '\n') {
            load.append(zzBuffer[start]);
            start++;
        }
        wrongStr = load.toString();
        wrongStr = wrongStr.trim();
        wrongStr = wrongStr.replaceAll("\n", "");
        setString(wrongStr);
        setPos(linea);
    }

    public void textAndLine(int pos_attuale, int linea, char[] zzBuffer) {
        //capisce che è avvenuto l'errore solo una volta superato e quindi verrà restituito il token sbagliato
        //per soddisfare la sintassi o il lessico
        int start = pos_attuale - 1;
        int end = pos_attuale;
        int line = linea;
        int rif = 0;
        boolean flag = false;

        StringBuilder load = new StringBuilder("");

        //controllo errore prima parola inizio programma
        //errore prima stringa della prima parola nel caso in cui il codice è all'inizio
        if (end == 0) {
            while (zzBuffer[end] != '\n') {
                load.append(zzBuffer[end]); //carica carattere per carattere essendo un vettore di char[]
                end++;
            }
        } else {
            //nel caso in cui l'err si sia verificato a metà e l'err è stato rilevato dopo
            //controlla se c'è un tab o uno spazio vuoto
            if (zzBuffer[start] == '\t' || zzBuffer[start] == ' ') {   //torno indietro assicurandomi di restare nel codice e di non sfornare a -1
                while ((zzBuffer[start] == '\t' || zzBuffer[start] == ' ') && (start - 1) != rif) { //diminuisco il valore di start
                    start--;
                }
                //controllo errore inizio programma stessa riga
                if (start - 1 == rif) {
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
                            while ((zzBuffer[start] == '\t' || zzBuffer[start] == ' ' ||
                                    zzBuffer[start] == '\n' || zzBuffer[start] == '\r') && start - 1 != rif) {
                                if (zzBuffer[start] == '\n')
                                    line--;
                                start--;
                            }
                            //errore programma inizia diverse righe dopo
                            if ((start - 1) == rif) {
                                while (zzBuffer[start] == '\t' || zzBuffer[start] == ' ' ||
                                        zzBuffer[start] == '\n' || zzBuffer[start] == '\r') {
                                    start++;
                                }
                                while (zzBuffer[start] != '\n') {
                                    load.append(zzBuffer[start]);
                                    start++;
                                }
                            } else
                            //errore riga precedente
                            {
                                load.append(zzBuffer[start]);
                                start--;
                                while (zzBuffer[start] != '\n') //carico la riga al contrario
                                {
                                    load.append(zzBuffer[start]);
                                    start--;
                                }
                                load.reverse(); //qui la mostro nel giusto verso
                            }
                        }
                    }
                }
            } else {
                if (zzBuffer[start] == '\n' || zzBuffer[start] == '\r') {
                    start--;
                    line--;
                    while ((zzBuffer[start] == '\t' || zzBuffer[start] == ' ' ||
                            zzBuffer[start] == '\n' || zzBuffer[start] == '\r') && start - 1 != rif) {
                        if (zzBuffer[start] == '\n')
                            line--;
                        start--;
                    }

                    if ((start - 1) == rif) {
                        flag = true;
                        while (zzBuffer[start] == '\t' || zzBuffer[start] == ' ' ||
                                zzBuffer[start] == '\n' || zzBuffer[start] == '\r') {
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
            }
        }

        setString(load.toString().trim().replaceAll("\n", ""));
        if (flag)
            setPos(linea);
        else
            setPos(line);
    }
}
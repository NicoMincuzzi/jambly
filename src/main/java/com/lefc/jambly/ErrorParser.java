package com.lefc.jambly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ErrorParser {

    public void print_error(ArrayList<PrintText> error, int cont_errori) throws IOException {
        Iterator<PrintText> err_list = error.iterator();

        Support.writeFileErr("ERRORI TROVATI: " + cont_errori + "\n");
        Support.countExit(cont_errori);
        while (err_list.hasNext()) {
            PrintText stampa = err_list.next();
            Support.writeFileErr("\n" + stampa.getMessage() + "OUTPUT ERROR:\n" + stampa.getString() + "\nRiga n." + stampa.getPos() + "\n");
        }
    }

    //tutti i metodi successivi si appoggiano alla seconda report_error passando il mess e il tipo di ogg
    //per stampare la linea di un determinato terminale o non terminale
    public void report_error(int errorCounter, ArrayList<PrintText> error, Scanner scan, String message, int line) {
        errorCounter++;
        PrintText output = scan.text(line);

        output.setMessage(message);
        error.add(output);
    }

    //chiamata in auto da syntax_error e fatal_error, per il controllo era necessario fare un override
    //crea oggetti di tipo PrintText
    /*public void report_error(String message, Object info) {
        cont_errori++; //incremento degli errori
        int pos = scan.getPos_vir();

        //il null serve per la syntax_error e per la fatal_error
        if (info == null) {
            try {
                output = scan.text(); //ottengo pt della funzione text()

                if (!fatal) {
                    output.setMessage(message);
                    error.add(output);
                } else { //caso in cui viene superata la fine del file
                    output.setMessage(message);
                    if (output.getPos() >= pos)  //getPos() posizione riferita all'oggetto errato
                        output.setString("");
                    fatal = false;
                    error.add(output);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            if (info instanceof Obj) {
                try {
                    output = scan.text();
                    output.setMessage(message);
                    error.add(output);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }*/
}

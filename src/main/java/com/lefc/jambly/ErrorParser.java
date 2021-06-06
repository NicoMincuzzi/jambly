package com.lefc.jambly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ErrorParser {

    public void print_error(ArrayList<PrintText> error, int errorCounter) throws IOException {
        Iterator<PrintText> iterator = error.iterator();

        Support.writeFileErr("ERRORI TROVATI: " + errorCounter + "\n");
        Support.countExit(errorCounter);
        while (iterator.hasNext()) {
            PrintText stampa = iterator.next();
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
}

package com.lefc.jambly;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ErrorParser {

    public void print_error(List<PrintText> error, int errorCounter) throws IOException {
        Iterator<PrintText> iterator = error.iterator();

        writeFileErr("ERRORI TROVATI: " + errorCounter + "\n");
        Support.countExit(errorCounter);
        while (iterator.hasNext()) {
            PrintText text = iterator.next();
            writeFileErr("\n" + text.getMessage() + "OUTPUT ERROR:\n" + text.getString() + "\nRiga n." + text.getPos() + "\n");
        }
    }

    public static void writeFileErr(String msg) throws IOException {
        FileWriter file = new FileWriter("FileErr.txt", true);
        file.write(msg);
        file.flush();
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

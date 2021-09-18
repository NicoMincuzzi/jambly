package com.lefc.jambly;

import com.lefc.jambly.repository.SemanticErrorRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ErrorParser {

    private final SemanticErrorRepository semanticError;

    public ErrorParser(SemanticErrorRepository semanticError) {
        this.semanticError = semanticError;
    }

    public void print_error(List<PrintText> error, int errorCounter) throws IOException {
        semanticError.writeFileErr("ERRORI TROVATI: " + errorCounter + "\n");
        Support.countExit(errorCounter);
        for (PrintText text : error) {
            semanticError.writeFileErr("\n" + text.getMessage() + "OUTPUT ERROR:\n" + text.getString() + "\nRiga n." + text.getPos() + "\n");
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

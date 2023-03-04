package com.lefc.jambly;

import java.util.List;

public class ErrorFormatter {

    public String print(List<PrintText> error, int errorCounter) {
        String result = "ERRORI TROVATI: " + errorCounter + "\n";

        for (PrintText text : error) {
            result = result.concat("\n" + text.getMessage() + "OUTPUT ERROR:\n" + text.getString() + "\nRiga n." + text.getPos() + "\n");
        }
        return result;
    }
}

package com.lefc.jambly;

import java.util.List;

public class ErrorParser {
    private String result;

    public String getResult() {
        return result;
    }

    public void print(List<PrintText> error, int errorCounter) {
        result = "ERRORI TROVATI: " + errorCounter + "\n";

        for (PrintText text : error) {
            result = result.concat("\n" + text.getMessage() + "OUTPUT ERROR:\n" + text.getString() + "\nRiga n." + text.getPos() + "\n");
        }
    }
}

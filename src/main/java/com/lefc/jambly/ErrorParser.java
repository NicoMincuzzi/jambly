package com.lefc.jambly;

import com.lefc.jambly.repository.SemanticErrorRepository;

import java.io.IOException;
import java.util.List;

public class ErrorParser {
    private String result;
    private final SemanticErrorRepository semanticError;

    public ErrorParser(SemanticErrorRepository semanticError) {
        this.semanticError = semanticError;
    }

    public String getResult() {
        return result;
    }

    public void print(List<PrintText> error, int errorCounter) throws IOException {
        //TODO remove the usage of ErrorFile
        semanticError.writeFileErr("ERRORI TROVATI: " + errorCounter + "\n");

        result = "ERRORI TROVATI: " + errorCounter + "\n";

        Support.countExit(errorCounter);

        for (PrintText text : error) {
            result = result.concat("\n" + text.getMessage() + "OUTPUT ERROR:\n" + text.getString() + "\nRiga n." + text.getPos() + "\n");
        }
    }
}

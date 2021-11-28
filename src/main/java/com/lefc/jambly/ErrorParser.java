package com.lefc.jambly;

import com.lefc.jambly.repository.SemanticErrorRepository;

import java.io.IOException;
import java.util.List;

public class ErrorParser {

    private final SemanticErrorRepository semanticError;

    public ErrorParser(SemanticErrorRepository semanticError) {
        this.semanticError = semanticError;
    }

    public void print(List<PrintText> error, int errorCounter) throws IOException {
        semanticError.writeFileErr("ERRORI TROVATI: " + errorCounter + "\n");
        Support.countExit(errorCounter);
        for (PrintText text : error) {
            semanticError.writeFileErr("\n" + text.getMessage() + "OUTPUT ERROR:\n" + text.getString() + "\nRiga n." + text.getPos() + "\n");
        }
    }
}

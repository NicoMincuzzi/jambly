package com.lefc.jambly.domain;

import com.lefc.jambly.PrintText;
import com.lefc.jambly.Scanner;

public class Bracket {
    private final Scanner scanner;

    public Bracket(Scanner scanner) {
        this.scanner = scanner;
    }

    public boolean isValid() {
        return scanner.bracketCounter == 0;
    }

    public PrintText buildError() {
        int numberOfBrackets = scanner.bracketCounter;

        PrintText printText = new PrintText();
        if (numberOfBrackets > 0) {
            printText.setString("Parentesi in difetto");
            printText.setPos((scanner.get_close_par() != 0) ? scanner.get_close_par() : scanner.get_open_par());
        } else {
            printText.setString("Parentesi in eccesso");
            printText.setPos(scanner.get_open_par());
        }
        return printText;
    }
}

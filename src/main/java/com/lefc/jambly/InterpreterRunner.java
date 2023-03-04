package com.lefc.jambly;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Iterator;

import static java.util.Collections.sort;

public class InterpreterRunner {
    public static String TRANSLATION_RESULT = "";
    private Scanner scanner;
    private parser parser;
    private ErrorParser errorParser;

    public String run(String file) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(file.getBytes()));
        this.scanner = scanner;
        parser parser = new parser(scanner);
        this.parser = parser;
        new CUP$parser$actions(parser);

        String result = null;
        try {
            parser.parse();
            checkBrackets();
            sort(parser.error);
            remove();   //per errori al di fuori del source program
            errorParser = new ErrorParser();
            errorParser.print(this.parser.error, this.parser.cont_errori);
            result = buildResult(this.parser.cont_errori);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            TRANSLATION_RESULT = "";
            CUP$parser$actions.countReg = 0;
            CUP$parser$actions.countRegFP = 0;
            CUP$parser$actions.countRTemp = 0;
            CUP$parser$actions.FlagSyn = false;
        }
        return result;
    }

    private void checkBrackets() {
        int numberOfBrackets = scanner.countBrace2;
        if (numberOfBrackets == 0) {
            return;
        }

        PrintText printText = new PrintText();
        if (numberOfBrackets > 0) {
            printText.setString("Parentesi in difetto");
            printText.setPos((scanner.get_close_par() != 0) ? scanner.get_close_par() : scanner.get_open_par());
        } else {
            printText.setString("Parentesi in eccesso");
            printText.setPos(scanner.get_open_par());
        }
        parser.error.add(printText);
    }

    private void remove() {
        int pos = scanner.getPos_vir();
        Iterator<PrintText> I = parser.error.iterator();
        while (I.hasNext()) {
            if (I.next().getPos() >= pos) {
                I.remove();
                return;
            }
        }
    }

    private String buildResult(int errorCounter) {
        if (errorCounter == 0) {
            return TRANSLATION_RESULT + ("\nCompilazione avvenuta correttamente!" + " Non si sono verificati errori sintattici!");
        }
        if (CUP$parser$actions.FlagSyn || errorCounter > 5) {
            return errorParser.getResult();
        }
        return errorParser.getResult() + TRANSLATION_RESULT;
    }

}
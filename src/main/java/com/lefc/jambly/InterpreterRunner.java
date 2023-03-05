package com.lefc.jambly;

import com.lefc.jambly.domain.Bracket;

import java.io.ByteArrayInputStream;
import java.util.Iterator;

import static java.util.Collections.sort;

public class InterpreterRunner {
    public static String TRANSLATION_RESULT = "";
    private Scanner scanner;
    private parser parser;

    public String run(String file) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(file.getBytes()));
        this.scanner = scanner;
        parser parser = new parser(scanner);
        this.parser = parser;
        //TODO use the instance
        CUP$parser$actions cup$parser$actions = new CUP$parser$actions(parser);

        String result = null;
        try {
            parser.parse();
            Bracket bracket = new Bracket(scanner);
            if(!bracket.isValid()){
                PrintText printText = bracket.buildError();
                parser.error.add(printText);
            }

            sort(parser.error);
            removeErrorsOutsideTheSourceProgram();

            result = buildResult(this.parser.cont_errori, new ErrorFormatter());
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

    private void removeErrorsOutsideTheSourceProgram() {
        int posLastSemicolon = scanner.getPosLastSemicolon();
        Iterator<PrintText> printTextIterator = parser.error.iterator();
        while (printTextIterator.hasNext()) {
            if (printTextIterator.next().getPos() >= posLastSemicolon) {
                printTextIterator.remove();
                return;
            }
        }
    }

    private String buildResult(int errorCounter, ErrorFormatter errorFormatter) {
        String result = errorFormatter.print(this.parser.error, errorCounter);

        if (errorCounter == 0) {
            return TRANSLATION_RESULT + "\nCompilazione avvenuta correttamente! Non si sono verificati errori sintattici!";
        }
        if (!CUP$parser$actions.FlagSyn && errorCounter <= 5) {
            return result + TRANSLATION_RESULT;
        }
        return result;
    }

}
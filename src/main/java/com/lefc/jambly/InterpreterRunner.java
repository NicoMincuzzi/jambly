package com.lefc.jambly;

import com.lefc.jambly.repository.SemanticErrorRepository;

import java.io.*;
import java.util.Iterator;

import static java.util.Collections.sort;

public class InterpreterRunner {

    public static final String ERROR_FILE = "FileErr.txt";
    public static String TRANSLATION_RESULT = "";
    private Scanner scanner;
    private parser parser;

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
            new ErrorParser(new SemanticErrorRepository()).print(this.parser.error, this.parser.cont_errori);
            result = buildResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            TRANSLATION_RESULT = "";
            File f = new File(ERROR_FILE);
            f.delete();
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
            int i = (scanner.get_close_par() != 0) ? scanner.get_close_par() : scanner.get_open_par();
            printText.setPos(i);
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

    private String buildResult() throws IOException {
        if (!new File(ERROR_FILE).exists()) {
            return TRANSLATION_RESULT + ("\nCompilazione avvenuta correttamente!" + " Non si sono verificati errori sintattici!");
        }
        if (CUP$parser$actions.FlagSyn || Support.getnumErr() > 5) {
            return readFile(ERROR_FILE);
        }
        return readFile(ERROR_FILE) + TRANSLATION_RESULT;
    }

    private String readFile(String path) throws IOException {
        BufferedReader b = new BufferedReader(new FileReader(path));
        StringBuilder result = new StringBuilder();
        while (true) {
            String line = b.readLine();
            if (line == null)
                break;
            result.append(line).append("\n");
        }
        return result.toString();
    }
}
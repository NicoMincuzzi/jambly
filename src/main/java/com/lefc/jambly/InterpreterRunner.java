package com.lefc.jambly;

import java.io.*;
import java.util.Collections;

public class InterpreterRunner {

    public static final String ERROR_FILE = "FileErr.txt";
    public static final String TRANSLATION_FILE = "FileTrad.txt";

    public String run(String file) {
        InputStream targetStream = new ByteArrayInputStream(file.getBytes());
        parser parser = new parser(new Scanner(targetStream));
        new CUP$parser$actions(parser);

        String result = null;
        try {
            parser.parse();
            parser.calcola_par(); //per un errore sulle parentesi
            Collections.sort(parser.error);
            parser.remove();   //per errori al di fuori del source program
            parser.print_error(); //stampa della lista di errori
            result = buildResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            File f = new File(TRANSLATION_FILE);
            f.delete();
            f = new File(ERROR_FILE);
            f.delete();
        }
        return result;
    }

    private String buildResult() throws IOException {
        if (!CUP$parser$actions.FlagSyn && Support.getnumErr() < 5) {
            if (new File(ERROR_FILE).exists()) {
                return readFileTwo(ERROR_FILE) + readFileTwo(TRANSLATION_FILE);
            }
            return readFileTwo(TRANSLATION_FILE) + ("\nCompilazione avvenuta correttamente!" + " Non si sono verificati errori sintattici!");
        }
        return readFileTwo(ERROR_FILE);
    }

    private String readFileTwo(String path) throws IOException {
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

package com.lefc.jambly;

import java.io.*;

public class InterpreterRunner {

    public String run(String file) {
        InputStream targetStream = new ByteArrayInputStream(file.getBytes());
        parser parser = new parser(new Scanner(targetStream));
        CUP$parser$actions cup$parser$actions = new CUP$parser$actions(parser);

        String result = null;
        try {
            parser.parse();
            parser.calcola_par(); //per un errore sulle parentesi
            parser.ordina_list(); //ordina la lista degli errori
            parser.remove();   //per errori al di fuori del source program
            parser.print_error(); //stampa della lista di errori
            result = buildResult();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            File f = new File("FileTrad.txt");
            f.delete();
            f = new File("FileErr.txt");
            f.delete();
        }
        return result;
    }

    private String buildResult() throws IOException {
        String result;
        if (!CUP$parser$actions.FlagSyn && Support.getnumErr() < 5) {
            if (new File("FileErr.txt").exists()) {
                String errorFile = readFileTwo("FileErr.txt");
                String translationFile = readFileTwo("FileTrad.txt");
                result = errorFile + translationFile;
            } else {
                String translationFile = readFileTwo("FileTrad.txt");
                String okTranslationMsg = "\nCompilazione avvenuta correttamente!" + " Non si sono verificati errori sintattici!";
                result = translationFile + okTranslationMsg;
            }
        } else {
            //AreaTxt2.setForeground(Color.RED);
            result = readFileTwo("FileErr.txt");
        }
        return result;
    }

    private String readFileTwo(String path) throws IOException {
        FileReader f = new FileReader(path);
        BufferedReader b = new BufferedReader(f);
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

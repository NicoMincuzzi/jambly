package com.lefc.jambly;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class InterpreterRunnerTest {

    @Test
    void case6Test() {
        String file = "public class Caso6{\n" +
                "\n" +
                "  public static void main(){\n" +
                "\n" +
                "     int raggio = 5;\n" +
                "     int diametro;\n" +
                "     int raggioQuad;\n" +
                "     final double pigreco = 3.14;\n" +
                "     double circonferenza;\n" +
                "     double area; \n" +
                "\n" +
                "     diametro = 2*raggio;\n" +
                "     raggioQuad = raggio * raggio;\n" +
                "\n" +
                "     if(raggio > 0){\n" +
                "       \n" +
                "       circonferenza = pigreco*diametro;\n" +
                "       \n" +
                "       area = pigreco * raggioQuad;\n" +
                "       \n" +
                "\n" +
                "     }\n" +
                "\n" +
                "\n" +
                " }\n" +
                "}\n" +
                ";\n";
        InterpreterRunner interpreter = new InterpreterRunner();
        String result = interpreter.run(file);
        String expected = "ERRORI TROVATI: 2\n\nWARNING: tipi differenti! Si provvederà ad effettuare un cast per la risoluzione del problema!\nOUTPUT ERROR:\ncirconferenza = pigreco*diametro;\nRiga n.17\n\nWARNING: tipi differenti! Si provvederà ad effettuare un cast per la risoluzione del problema!\nOUTPUT ERROR:\narea = pigreco * raggioQuad;\nRiga n.19\n\n.data\n\nraggio .WORD 5d\ndiametro .WORD ?\nraggioQuad .WORD ?\npigreco EQU 3.14\ncirconferenza .DOUBLE ?\narea .DOUBLE ?\n\n.code\n\nMOVE raggio, $s0\nMOVE diametro, $s1\nMOVE raggioQuad, $s2\nMOVE.D pigreco, $f0\nMOVE.D circonferenza, $f2\nMOVE.D area, $f4\n\n\n\n\n\n\nMUL $s1, 2, $s0\nMUL $s2, $s0, $s0\nSLTI $t0, $s0, 0\nBEQ $t0, $zero, ESCI1\nMTC1 $s1, $f6\nMUL.D $f2, $f0, $f6\nMTC1 $s2, $f8\nMUL.D $f4, $f0, $f8\nESCI1:\n\n\nend\n";
        assertThat(result, is(expected));
    }
}
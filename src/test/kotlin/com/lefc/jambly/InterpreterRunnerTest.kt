package com.lefc.jambly

import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test

internal class InterpreterRunnerTest {

    @Test
    fun case6Test() {
        val file = """public class Caso6{

  public static void main(){

     int raggio = 5;
     int diametro;
     int raggioQuad;
     final double pigreco = 3.14;
     double circonferenza;
     double area; 

     diametro = 2*raggio;
     raggioQuad = raggio * raggio;

     if(raggio > 0){
       
       circonferenza = pigreco*diametro;
       
       area = pigreco * raggioQuad;
       

     }


 }
}
;
"""
        val interpreter = InterpreterRunner()
        val result = interpreter.run(file)
        val expected =
            "ERRORI TROVATI: 2\n\nWARNING: tipi differenti! Si provvederà ad effettuare un cast per la risoluzione del problema!\nOUTPUT ERROR:\ncirconferenza = pigreco*diametro;\nRiga n.17\n\nWARNING: tipi differenti! Si provvederà ad effettuare un cast per la risoluzione del problema!\nOUTPUT ERROR:\narea = pigreco * raggioQuad;\nRiga n.19\n\n.data\n\nraggio .WORD 5d\ndiametro .WORD ?\nraggioQuad .WORD ?\npigreco EQU 3.14\ncirconferenza .DOUBLE ?\narea .DOUBLE ?\n\n.code\n\nMOVE raggio, \$s0\nMOVE diametro, \$s1\nMOVE raggioQuad, \$s2\nMOVE.D pigreco, \$f0\nMOVE.D circonferenza, \$f2\nMOVE.D area, \$f4\n\n\n\n\n\n\nMUL \$s1, 2, \$s0\nMUL \$s2, \$s0, \$s0\nSLTI \$t0, \$s0, 0\nBEQ \$t0, \$zero, ESCI1\nMTC1 \$s1, \$f6\nMUL.D \$f2, \$f0, \$f6\nMTC1 \$s2, \$f8\nMUL.D \$f4, \$f0, \$f8\nESCI1:\n\n\nend\n"
        MatcherAssert.assertThat(result, Matchers.`is`(expected))
    }
}
package com.lefc.jambly

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import java.io.IOException
import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.Files
import java.nio.file.Paths

class AcceptanceTest {

    @Test
    fun case1Test() {
        val file = readLineByLineJava8("./src/main/resources/assembly_files/Caso1.txt")
        val interpreter = InterpreterRunner()
        val result = interpreter.run(file)
        val expected =
            "ERRORI TROVATI: 3\n\nError: Modificatori uguali o dello stesso tipo!\nOUTPUT ERROR:\npublic private static void main(){\nRiga n.3\n\nSOSTITUZIONE PUNTO E VIRGOLA CON VIRGOLA!!!\nOUTPUT ERROR:\nint max = 0,\nRiga n.5\n\nERROR: la variabile non e' stata dichiarata in nessun scope!\nOUTPUT ERROR:\ni = 0;\nRiga n.8\n\n.data\n\nmax .WORD 0d\ndim EQU 7\nV .WORD 22, 5, 7, 11, 2, 33, 9\n\n.code\n\nMOVE max, \$s0\nMOVE dim, \$s1\nMOVE V, \$s2\n\n\nADDI \$s3, \$zero, 0\nCICLO2:  SLTI \$t0, \$s3, 7\nBEQ \$t0, \$zero, ESCI2\nMULT \$t2, \$s3, 4\nADD \$t2, \$t2, \$s2\nLW \$t1, 0(\$t2)\nSLT \$t3, \$s0, \$t1\nBEQ \$t3, \$zero, ELSE1\nBEQ \$s0, \$t1, ELSE1\nADD \$s0, \$zero, \$t1\n ESCI1\nELSE1: ADD \$s0, \$zero, \$s0\nESCI1:\n\nADDI \$s3, \$s3, 1\nCICLO2\nESCI2:\n\n\nend\n"
        assertThat(result, Matchers.`is`(expected))
    }

    @Test
    fun case2Test() {
        val file = readLineByLineJava8("./src/main/resources/assembly_files/Caso2.txt")
        val interpreter = InterpreterRunner()
        val result = interpreter.run(file)
        val expected =
            "ERRORI TROVATI: 3\n\nPUNTO E VIRGOLA MANCANTE!!!\nOUTPUT ERROR:\nint lavoro = 0\nRiga n.5\n\nERROR: assegnazione di tipi differenti! Verrà effettuata un'approssimazione per difetto!\nOUTPUT ERROR:\nint i = 3.8;\nRiga n.6\n\nERRORE NELLE PARENTESI DEGLI ARRAY!!!\nOUTPUT ERROR:\nint V() = new int[] { 22, 5, 7, 11, 2, 33, 9};\nRiga n.8\n\n.data\n\nlavoro .WORD 0d\nj .WORD 1d\ni .WORD d\ndim EQU 7\nV .WORD 22, 5, 7, 11, 2, 33, 9\ni .WORD d\n\n.code\n\nMOVE lavoro, \$s0\nMOVE i, \$s1\nMOVE j, \$s2\nMOVE dim, \$s3\nMOVE V, \$s4\n\n\n\n\nADDI \$s1, \$zero, 0\nCICLO3:  SLTI \$t0, \$s1, 7\nBEQ \$t0, \$zero, ESCI3\n\tADDI \$s2, \$s1, 1\nCICLO2:  SLTI \$t1, \$s2, 7\nBEQ \$t1, \$zero, ESCI2\n\tMULT \$t3, \$s1, 4\nADD \$t3, \$t3, \$s4\nLW \$t2, 0(\$t3)\nMULT \$t5, \$s2, 4\nADD \$t5, \$t5, \$s4\nLW \$t4, 0(\$t5)\nSLT \$t6, \$t2, \$t4\nBEQ \$t6, \$zero, ESCI1\nADD \$s0, \$zero, \$t2\nSW \$t4 0(\$t2)\nSW \$s0 0(\$t4)\nESCI1:\n\n\tADDI \$s2, \$s2, 1\nCICLO2\nESCI2:\n\n\tADDI \$s1, \$s1, 1\nCICLO3\nESCI3:\n\n\nend\n"
        assertThat(result, Matchers.`is`(expected))
    }

    @Test
    fun case3Test() {
        val file = readLineByLineJava8("./src/main/resources/assembly_files/Caso3.txt")
        val interpreter = InterpreterRunner()
        val result = interpreter.run(file)
        val expected =
            "ERRORI TROVATI: 4\n\nPUNTO E VIRGOLA MANCANTE!!!\nOUTPUT ERROR:\nint x = 6\nRiga n.5\n\nSOSTITUZIONE PUNTO E VIRGOLA CON VIRGOLA!!!\nOUTPUT ERROR:\nint i = 0,\nRiga n.7\n\nERROR: IL COSTRUTTO WHILE ACCETTA ESCLUSIVAMENTE IL TIPO BOOLEAN!\nOUTPUT ERROR:\nwhile ( i + x ){\nRiga n.10\n\nFATAL ERROR: NON E' POSSIBILE EFFETTUARE LA DIVISIONE PER 0!\nOUTPUT ERROR:\nj = x / i;\nRiga n.13\n"
        assertThat(result, Matchers.`is`(expected))
    }

    @Test
    fun case4Test() {
        val file = readLineByLineJava8("./src/main/resources/assembly_files/Caso4.txt")
        val interpreter = InterpreterRunner()
        val result = interpreter.run(file)
        val expected =
            "ERRORI TROVATI: 4\n\nERRORE LESSICALE/SINTATTICO \nOUTPUT ERROR:\ninto i;\nRiga n.6\n\nSOSTITUZIONE PUNTO E VIRGOLA CON VIRGOLA!!!\nOUTPUT ERROR:\nint V[] = new int[] { 22, 5, 7, 11, 2, 33, 9},\nRiga n.7\n\nERROR: tipi degli operandi errati per un operatore binario!\nOUTPUT ERROR:\nwhile( i < \"ciao\"){\nRiga n.10\n\nOUTPUT ERROR:\nParentesi in difetto\nRiga n.24\n\n.data\n\nmax .WORD 0d\ni .WORD ?\ndim EQU 7\nV .WORD 22, 5, 7, 11, 2, 33, 9\n\n.code\n\nMOVE max, \$s0\nMOVE i, \$s1\nMOVE dim, \$s2\nMOVE V, \$s3\n\n\n\nADDI \$s1, \$zero, 0\nCICLO2:  \nMULT \$t1, \$s1, 4\nADD \$t1, \$t1, \$s3\nLW \$t0, 0(\$t1)\nSLT \$t2, \$s0, \$t0\nBEQ \$t2, \$zero, ELSE1\nBEQ \$s0, \$t0, ELSE1\nADD \$s0, \$zero, \$t0\n ESCI1\nELSE1: ADD \$s0, \$zero, \$s0\nESCI1:\n\nADDI \$s1, \$s1, 1\nCICLO2\nESCI2:\n"
        assertThat(result, Matchers.`is`(expected))
    }

    @Test
    fun case5Test() {
        val file = readLineByLineJava8("./src/main/resources/assembly_files/Caso5.txt")
        val interpreter = InterpreterRunner()
        val result = interpreter.run(file)
        val expected =
            "ERRORI TROVATI: 4\n\nERRORE LESSICALE/SINTATTICO \nOUTPUT ERROR:\nfor(i = 0, i < 7, i++){\nRiga n.11\n\nERROR: LA DIMENSIONE DEL VETTORE DEVE ESSERE UN NUMERO INTERO!\nOUTPUT ERROR:\nlavoro = V[x];\nRiga n.14\n\nOUTPUT ERROR:\nParentesi in difetto\nRiga n.19\n\nFATAL ERROR: IMPOSSIBILE COMPLETARE L'ESECUZIONE, POICHE' E' STATA RAGGIUNTA LA FINE DEL FILE PREMATURAMENTE!OUTPUT ERROR:\n\nRiga n.22\n"
        assertThat(result, Matchers.`is`(expected))
    }

    @Test
    fun case6Test() {
        val file = readLineByLineJava8("./src/main/resources/assembly_files/Caso6.txt")
        val interpreter = InterpreterRunner()
        val result = interpreter.run(file)
        val expected =
            "ERRORI TROVATI: 2\n\nWARNING: tipi differenti! Si provvederà ad effettuare un cast per la risoluzione del problema!\nOUTPUT ERROR:\ncirconferenza = pigreco*diametro;\nRiga n.17\n\nWARNING: tipi differenti! Si provvederà ad effettuare un cast per la risoluzione del problema!\nOUTPUT ERROR:\narea = pigreco * raggioQuad;\nRiga n.19\n\n.data\n\nraggio .WORD 5d\ndiametro .WORD ?\nraggioQuad .WORD ?\npigreco EQU 3.14\ncirconferenza .DOUBLE ?\narea .DOUBLE ?\n\n.code\n\nMOVE raggio, \$s0\nMOVE diametro, \$s1\nMOVE raggioQuad, \$s2\nMOVE.D pigreco, \$f0\nMOVE.D circonferenza, \$f2\nMOVE.D area, \$f4\n\n\n\n\n\n\nMUL \$s1, 2, \$s0\nMUL \$s2, \$s0, \$s0\nSLTI \$t0, \$s0, 0\nBEQ \$t0, \$zero, ESCI1\nMTC1 \$s1, \$f6\nMUL.D \$f2, \$f0, \$f6\nMTC1 \$s2, \$f8\nMUL.D \$f4, \$f0, \$f8\nESCI1:\n\n\nend\n"
        assertThat(result, Matchers.`is`(expected))
    }

    @Test
    fun case7Test() {
        val file = readLineByLineJava8("./src/main/resources/assembly_files/Caso7.txt")
        val interpreter = InterpreterRunner()
        val result = interpreter.run(file)
        val expected =
            "ERRORI TROVATI: 0\n\n.data\n\nlavoro .WORD 0d\ni .WORD ?\nj .WORD 1d\ndim EQU 7\nV .WORD 22, 5, 7, 11, 2, 33, 9\n\n.code\n\nMOVE lavoro, \$s0\nMOVE i, \$s1\nMOVE j, \$s2\nMOVE dim, \$s3\nMOVE V, \$s4\n\n\n\n\nADDI \$s1, \$zero, 0\nCICLO3:  SLTI \$t0, \$s1, 7\nBEQ \$t0, \$zero, ESCI3\n\tADDI \$s2, \$s1, 1\nCICLO2:  SLTI \$t1, \$s2, 7\nBEQ \$t1, \$zero, ESCI2\n\tMULT \$t3, \$s1, 4\nADD \$t3, \$t3, \$s4\nLW \$t2, 0(\$t3)\nMULT \$t5, \$s2, 4\nADD \$t5, \$t5, \$s4\nLW \$t4, 0(\$t5)\nSLT \$t6, \$t2, \$t4\nBEQ \$t6, \$zero, ESCI1\nADD \$s0, \$zero, \$t2\nSW \$t4 0(\$t2)\nSW \$s0 0(\$t4)\nESCI1:\n\n\tADDI \$s2, \$s2, 1\nCICLO2\nESCI2:\n\n\tADDI \$s1, \$s1, 1\nCICLO3\nESCI3:\n\n\nend\n"
        assertThat(result, Matchers.`is`(expected))
    }

    private fun readLineByLineJava8(filePath: String): String {
        val contentBuilder = StringBuilder()
        try {
            Files.lines(Paths.get(filePath), UTF_8).use { stream ->
                stream.forEach { s: String ->
                    contentBuilder.append(s).append("\n")
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return contentBuilder.toString()
    }
}
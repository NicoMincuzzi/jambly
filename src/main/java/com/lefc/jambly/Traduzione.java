package com.lefc.jambly;

import java.util.ListIterator;

public class Traduzione {

    private static boolean flagL = false;
    private static String RegFP;
    private static String APP;
    private static String TRAD = "";

    public static String getRegFP() {
        return RegFP;
    }

    public static String createReg(String tipo, String var) {

        Record rec = SymbolTable.getCurrRec(var);
        String Rg;
        String TR;

        if (tipo.equals("DOUBLE")) {
            Rg = "$f" + CUP$parser$actions.countRegFP;
            TR = "MOVE.D" + " " + var + "," + " " + Rg;
            CUP$parser$actions.countRegFP += 2;
        } else {
            Rg = "$s" + CUP$parser$actions.countReg;
            TR = "MOVE" + " " + var + "," + " " + Rg;
            CUP$parser$actions.countReg++;
        }

        rec.setRegister(Rg);
        return TR;

    }

    //METODO PER LA TRADUZIONE DELLE DICHIARAZIONI DELLE VARIABILI E DEGLI ARRAY
    public static String tradDecl(String tipo, String varad) {
        Record rec;
        String TIPO = "";
        String VAR;
        String VAL;
        int z = 0;

        switch (tipo) {
            case "INTEGER":
                TIPO = ".WORD";
                break;
            case "DOUBLE":
                TIPO = ".DOUBLE";
                break;
            default:
                break;
        }

        if (!varad.contains("[]")) { //NEL "THEN" SONO GESTITE LE VARIABILI
            /*IL SEGUENTE "IF-ELSE" VERIFICA LA PRESENZA O MENO DEL SIMBOLO DI ASSEGNAZIONE*/
            if (varad.contains("=")) {
                VAR = varad.substring(0, varad.indexOf("=") - 1);
            } else {
                VAR = varad;
            }

            rec = SymbolTable.getCurrRec(VAR);
            VAL = rec.getValue().toString();
            /*IL SEGUENTE "IF-ELSE" VERIFICA LA PRESENZA O MENO DEL VALORE DELLA VARIABILE*/
            if (VAL.equals("null")) {
                TRAD = VAR + " " + TIPO + " " + "?";
            } else {
                TRAD = VAR + " " + TIPO + " " + VAL + "d";
            }

        } else { //NEL "ELSE" SONO GESTITI GLI ARRAY
            if (varad.contains("new")) {

                VAR = varad.substring(0, varad.indexOf("[]"));
                Record rec3 = SymbolTable.getCurrRec(VAR);

                if (rec3.getList().isEmpty()) {
                    TRAD = "dim" + " " + "EQU" + " " + rec3.getList().size() + "\n" + VAR + " " + TIPO + " " + "?";
                } else {
                    TRAD = "dim" + " " + "EQU" + " " + rec3.getList().size() + "\n" + VAR + " " + TIPO;

                    ListIterator it = rec3.getList().listIterator(rec3.getList().size()); //per ottenere la lista al contrario
                    rec3.setValue(rec3.getList().size());
                    /*IL SEGUENTE CICLO "WHILE" PERMETTE DI OTTENERE TUTTI I VALORI DELL'ARRAY*/
                    while (it.hasPrevious()) {
                        if (z == 0) {
                            TRAD = TRAD + " " + it.previous().toString(); //per il primo elemento dell'array
                        } else {
                            TRAD = TRAD + "," + " " + it.previous().toString(); //per tutti gli altri elementi dell'array
                        }
                        z++;
                    }
                }

            } else {

                VAR = varad.substring(0, varad.indexOf("[]"));
                Record rec3 = SymbolTable.getCurrRec(VAR);
                VAL = rec3.getValue().toString();
                /*IL SEGUENTE "IF-ELSE" VERIFICA LA PRESENZA O MENO DEI VALORI DELL'ARRAY*/
                if (rec3.getList().isEmpty()) {
                    TRAD = "dim" + " " + "EQU" + " " + VAL + "\n" + VAR + " " + TIPO + " " + "?";
                } else {
                    TRAD = "dim" + " " + "EQU" + " " + VAL + "\n" + VAR + " " + TIPO;

                    ListIterator it = rec3.getList().listIterator(rec3.getList().size());

                    /*IL SEGUENTE CICLO "WHILE" PERMETTE DI OTTENERE TUTTI I VALORI DELL'ARRAY*/
                    while (it.hasPrevious()) {
                        if (z == 0) {
                            TRAD = TRAD + " " + it.previous().toString();
                        } else {
                            TRAD = TRAD + "," + " " + it.previous().toString();
                        }
                        z++;
                    }
                }
            }
        }

        return TRAD;
    }

    /*METODO PER LA TRADUZIONE DI ESPRESSIONI IN CUI ENTRAMBI I MEMBRI HANNO COME TIPO: INTEGER*/
    public static String tradEspr(String multgen, String ungen, String multesto, String untesto, int I, String T) {


        /*I SEGUENTI "IF-ELSE" ANNIDATI PERMETTONO DI TRATTARE TUTTE LE COMBINAZIONI POSSIBILI TRA VARIABILI, ARRAY E NUMERI*/
        switch (multgen) {
            case "VARIABILE": {
                Record rec = SymbolTable.retrieveVariableInsideScope(multesto);

                if (ungen.equals("NUMERO")) {
                    if (I == 3) {
                        TRAD = OpEspr(I, T) + "I" + " " + "RIS" + ", " + rec.getRegister() + ", " + untesto;
                    } else if (I == 4) {
                        TRAD = OpEspr(3, T) + "I" + " " + "RIS" + ", " + rec.getRegister() + ", " + "-" + untesto;
                    } else {
                        TRAD = OpEspr(I, T) + " " + "RIS" + ", " + rec.getRegister() + ", " + untesto;
                    }
                } else if (ungen.equals("ARRAY_ACCESS")) {
                    if (untesto.startsWith("$t")) {
                        TRAD = OpEspr(I, T) + " " + "RIS" + ", " + rec.getRegister() + ", " + untesto;
                    } else {
                        int l = untesto.lastIndexOf(" $t");
                        String UT = untesto.substring(l + 1, l + 4);
                        TRAD = untesto + "\n" + OpEspr(I, T) + " " + "RIS" + ", " + rec.getRegister() + ", " + UT;
                    }
                } else {
                    Record rec2 = SymbolTable.getCurrRec(untesto);
                    TRAD = OpEspr(I, T) + " " + "RIS" + ", " + rec.getRegister() + ", " + rec2.getRegister();
                }

                break;
            }
            case "NUMERO": {
                Record rec = SymbolTable.getCurrRec(untesto);
                if (I == 3) {
                    if (ungen.equals("VARIABILE")) {
                        TRAD = OpEspr(I, T) + "I" + " " + "RIS" + ", " + multesto + ", " + rec.getRegister();
                    } else if (ungen.equals("ARRAY_ACCESS")) {
                        if (untesto.startsWith("$t")) {
                            TRAD = OpEspr(I, T) + " " + "RIS" + ", " + multesto + ", " + untesto;
                        } else {
                            int l = untesto.lastIndexOf(" $t");
                            String UT = untesto.substring(l + 1, l + 4);
                            TRAD = untesto + "\n" + OpEspr(I, T) + "I" + " " + "RIS" + ", " + multesto + ", " + UT;
                        }
                    } else {
                        TRAD = OpEspr(I, T) + "I" + " " + "RIS" + ", " + multesto + ", " + untesto;
                    }
                } else if (I == 4) {
                    if (ungen.equals("VARIABILE")) {
                        TRAD = OpEspr(3, T) + "I" + " " + "RIS" + ", " + multesto + ", " + "-" + rec.getRegister();
                    } else if (ungen.equals("ARRAY_ACCESS")) {
                        if (untesto.startsWith("$t")) {
                            TRAD = OpEspr(3, T) + "I" + " " + "RIS" + ", " + "-" + multesto + ", " + untesto;
                        } else {
                            int l = untesto.lastIndexOf(" $t");
                            String UT = untesto.substring(l + 1, l + 4);
                            TRAD = untesto + "\n" + OpEspr(3, T) + "I" + " " + "RIS" + ", " + "-" + multesto + ", " + UT;
                        }
                    } else {
                        TRAD = OpEspr(3, T) + "I" + " " + "RIS" + ", " + multesto + ", " + "-" + untesto;
                    }
                } else {
                    if (ungen.equals("VARIABILE")) {
                        TRAD = OpEspr(I, T) + " " + "RIS" + ", " + multesto + ", " + rec.getRegister();
                    } else if (ungen.equals("ARRAY_ACCESS")) {
                        if (untesto.startsWith("$t")) {
                            TRAD = OpEspr(I, T) + " " + "RIS" + ", " + multesto + ", " + untesto;
                        } else {
                            int l = untesto.lastIndexOf(" $t");
                            String UT = untesto.substring(l + 1, l + 4);
                            TRAD = untesto + "\n" + OpEspr(I, T) + " " + "RIS" + ", " + multesto + ", " + UT;
                        }
                    } else {
                        TRAD = OpEspr(I, T) + "I" + " " + "RIS" + ", " + multesto + ", " + rec.getRegister();
                    }
                }
                break;
            }
            case "ARRAY_ACCESS":
                String UT;
                if (multesto.startsWith("$t")) {
                    UT = multesto;
                    multesto = "";
                } else {
                    int l = multesto.lastIndexOf(" $t");
                    UT = multesto.substring(l + 1, l + 4);
                }
                if (ungen.equals("VARIABILE")) {
                    Record rec = SymbolTable.getCurrRec(untesto);
                    TRAD = multesto + "\n" + OpEspr(I, T) + " " + "RIS" + ", " + UT + ", " + rec.getRegister();
                } else if (ungen.equals("NUMERO")) {
                    if (I == 3) {
                        TRAD = multesto + "\n" + OpEspr(I, T) + "I" + " " + "RIS" + ", " + UT + ", " + untesto;
                    } else if (I == 4) {
                        TRAD = multesto + "\n" + OpEspr(3, T) + "I" + " " + "RIS" + ", " + UT + ", " + "-" + untesto;
                    } else {
                        TRAD = multesto + "\n" + OpEspr(I, T) + " " + "RIS" + ", " + UT + ", " + untesto;
                    }
                } else {
                    if (untesto.startsWith("$t")) {
                        TRAD = OpEspr(I, T) + " " + "RIS" + ", " + UT + ", " + untesto;
                    } else {
                        int m = untesto.lastIndexOf(" $t");
                        String US = untesto.substring(m + 1, m + 4);
                        TRAD = multesto + "\n" + untesto + "\n" + OpEspr(I, T) + " " + "RIS" + ", " + UT + ", " + US;
                    }
                }
                break;
        }
        return TRAD;
    }

    /*METODO PER LA TRADUZIONE DI ESPRESSIONI IN CUI ENTRAMBI I MEMBRI HANNO COME TIPO: DOUBLE (OVERLOADING)*/
    public static String tradEspr(String multgen, String ungen, String multesto, String untesto, String T, int I) {
        String TRAD2;
        /*I SEGUENTI "IF-ELSE" ANNIDATI PERMETTONO DI TRATTARE TUTTE LE COMBINAZIONI POSSIBILI TRA VARIABILI, ARRAY E NUMERI*/
        switch (multgen) {
            case "VARIABILE": {
                Record rec = SymbolTable.getCurrRec(multesto);
                if (ungen.equals("NUMERO")) {
                    if (I == 3) {
                        TRAD = "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + untesto + "\n" +
                                OpEspr(I, T) + " " + "RIS" + ", " + rec.getRegister() + ", " + "$f" + CUP$parser$actions.countRegFP;
                    } else if (I == 4) {
                        TRAD = "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + untesto + "\n" +
                                OpEspr(I, T) + " " + "RIS" + ", " + rec.getRegister() + ", " + "$f" + CUP$parser$actions.countRegFP;
                    } else {
                        TRAD = "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + untesto + "\n" +
                                OpEspr(I, T) + " " + "RIS" + ", " + rec.getRegister() + ", " + "$f" + CUP$parser$actions.countRegFP;
                    }
                    CUP$parser$actions.countRegFP += 2;
                } else if (ungen.equals("ARRAY_ACCESS")) {
                    if (untesto.startsWith("$f")) {
                        TRAD = OpEspr(I, T) + " " + "RIS" + ", " + rec.getRegister() + ", " + untesto;
                    } else {
                        int l = untesto.lastIndexOf(" $f");
                        String UT = untesto.substring(l + 1, l + 4);
                        TRAD = untesto + "\n" + OpEspr(I, T) + " " + "RIS" + ", " + rec.getRegister() + ", " + UT;
                    }
                } else {
                    Record rec2 = SymbolTable.getCurrRec(untesto);
                    TRAD = OpEspr(I, T) + " " + "RIS" + ", " + rec.getRegister() + ", " + rec2.getRegister();
                }
                break;
            }
            case "NUMERO": {
                Record rec = SymbolTable.getCurrRec(untesto);
                TRAD = "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + multesto + "\n";
                if (I == 3) {
                    if (ungen.equals("VARIABILE")) {

                        TRAD = TRAD.concat(OpEspr(I, T) + " " + "RIS" + ", " + "$f" + CUP$parser$actions.countRegFP + ", " +
                                rec.getRegister());

                    } else if (ungen.equals("ARRAY_ACCESS")) {

                        if (untesto.startsWith("$f")) {
                            TRAD = TRAD + "\n" + OpEspr(I, T) + " " + "RIS" + ", " + "$f" + CUP$parser$actions.countRegFP + ", " + untesto;
                        } else {
                            int l = untesto.lastIndexOf(" $f");
                            String UT = untesto.substring(l + 1, l + 4);
                            TRAD = TRAD + "\n" + untesto + "\n" + OpEspr(I, T) + " " + "RIS" + ", " + "$f" + CUP$parser$actions.countRegFP + ", " + UT;
                        }
                    } else {
                        int value = CUP$parser$actions.countRegFP + 2;
                        TRAD2 = "L.D." + "$f" + value + "const" + untesto + "\n";
                        TRAD = TRAD.concat(TRAD2);
                        TRAD = TRAD.concat(OpEspr(I, T) + " " + "RIS" + ", " + "$f" + CUP$parser$actions.countRegFP + ", " + "$f" + value);

                    }
                } else if (I == 4) {
                    if (ungen.equals("VARIABILE")) {

                        TRAD = TRAD.concat(OpEspr(I, T) + " " + "RIS" + ", " + "$f" + CUP$parser$actions.countRegFP + ", " +
                                rec.getRegister());

                    } else if (ungen.equals("ARRAY_ACCESS")) {

                        if (untesto.startsWith("$f")) {
                            TRAD = TRAD + "\n" + OpEspr(I, T) + " " + "RIS" + ", " + "$f" + CUP$parser$actions.countRegFP + ", " + untesto;
                        } else {
                            int l = untesto.lastIndexOf(" $f");
                            String UT = untesto.substring(l + 1, l + 4);
                            TRAD = TRAD + "\n" + untesto + "\n" + OpEspr(I, T) + " " + "RIS" + ", " + "$f" + CUP$parser$actions.countRegFP + ", " + UT;
                        }
                    } else {

                        int value = CUP$parser$actions.countRegFP + 2;
                        TRAD2 = "L.D." + "$f" + value + ", " + "const" + untesto + "\n";
                        TRAD = TRAD.concat(TRAD2);
                        TRAD = TRAD.concat(OpEspr(I, T) + " " + "RIS" + ", " + "$f" + CUP$parser$actions.countRegFP + ", " + "$f" + value);

                    }
                } else {
                    if (ungen.equals("VARIABILE")) {

                        TRAD = TRAD.concat(OpEspr(I, T) + " " + "RIS" + ", " + "$f" + CUP$parser$actions.countRegFP + ", " +
                                rec.getRegister());

                    } else if (ungen.equals("ARRAY_ACCESS")) {

                        if (untesto.startsWith("$f")) {
                            TRAD = TRAD + "\n" + OpEspr(I, T) + " " + "RIS" + ", " + "$f" + CUP$parser$actions.countRegFP + ", " + untesto;
                        } else {
                            int l = untesto.lastIndexOf(" $f");
                            String UT = untesto.substring(l + 1, l + 4);
                            TRAD = TRAD + "\n" + untesto + "\n" + OpEspr(I, T) + " " + "RIS" + ", " + "$f" + CUP$parser$actions.countRegFP + ", " + UT;
                        }
                    } else {
                        int value = CUP$parser$actions.countRegFP + 2;
                        TRAD2 = "L.D." + "$f" + value + ", " + "const" + untesto + "\n";
                        TRAD = TRAD.concat(TRAD2);
                        TRAD = TRAD.concat(OpEspr(I, T) + " " + "RIS" + ", " + "$f" + CUP$parser$actions.countRegFP + ", " + "$f" + value);
                    }

                }
                CUP$parser$actions.countRegFP += 4;
                break;
            }
            case "ARRAY_ACCESS":

                String UT;
                if (multesto.startsWith("$f")) {
                    UT = multesto;
                    multesto = "";
                } else {
                    int l = multesto.lastIndexOf(" $f");
                    UT = multesto.substring(l + 1, l + 4);
                }

                if (ungen.equals("VARIABILE")) {
                    Record rec = SymbolTable.getCurrRec(untesto);
                    TRAD = multesto + "\n" + OpEspr(I, T) + " " + "RIS" + ", " + UT + ", " + rec.getRegister();
                } else if (ungen.equals("NUMERO")) {
                    if (I == 3) {
                        TRAD = multesto + "\n" + "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + untesto + "\n" +
                                OpEspr(I, T) + " " + "RIS" + ", " + UT + ", " + "$f" + CUP$parser$actions.countRegFP;
                    } else if (I == 4) {
                        TRAD = multesto + "\n" + "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + untesto + "\n" +
                                OpEspr(I, T) + " " + "RIS" + ", " + UT + ", " + "$f" + CUP$parser$actions.countRegFP;
                    } else {
                        TRAD = multesto + "\n" + "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + untesto + "\n" +
                                OpEspr(I, T) + " " + "RIS" + ", " + UT + ", " + "$f" + CUP$parser$actions.countRegFP;
                    }

                    CUP$parser$actions.countRegFP += 2;
                } else {

                    if (untesto.startsWith("$f")) {
                        TRAD = OpEspr(I, T) + " " + "RIS" + ", " + UT + ", " + untesto;
                    } else {
                        int m = untesto.lastIndexOf(" $f");
                        String US = untesto.substring(m + 1, m + 4);
                        TRAD = multesto + "\n" + untesto + "\n" + OpEspr(I, T) + " " + "RIS" + ", " + UT + ", " + US;
                    }
                }

                break;
        }
        return TRAD;
    }


    public static void castFP(String tipo1, String gen1, String testo1, String gen2, String testo2) {

        if (tipo1.equals("INTEGER")) {
            flagL = true;
            funct(gen1, testo1);
        } else {
            flagL = false;
            funct(gen2, testo2);
        }
    }

    public static String funct(String gen, String testo) {
        switch (gen) {
            case "NUMERO":

                APP = "ADDI" + " " + "$t" + CUP$parser$actions.countRTemp + ", " + "$zero" + ", " + testo + "\n" +
                        "MTC1" + " " + "$t" + CUP$parser$actions.countRTemp + ", " + "$f" + CUP$parser$actions.countRegFP + "\n" +
                        "CVT.D.W" + " " + "$f" + "CONT" + ", " + "$f" + CUP$parser$actions.countRegFP + "\n";
                CUP$parser$actions.countRegFP += 2;
                APP = APP.replace("CONT", Integer.toString(CUP$parser$actions.countRegFP));
                CUP$parser$actions.countRTemp++;

                break;
            case "ARRAY_ACCESS":

                if (testo.startsWith("$t")) {
                    APP = "MTC1" + " " + testo + ", " + "$f" + CUP$parser$actions.countRegFP + "\n";
                } else {
                    int l = testo.lastIndexOf(" $t");
                    String UT = testo.substring(l + 1, l + 4);
                    APP = "MTC1" + " " + UT + ", " + "$f" + CUP$parser$actions.countRegFP + "\n";
                }

                break;
            case "VARIABILE":

                Record rec = SymbolTable.getCurrRec(testo);
                APP = "MTC1" + " " + rec.getRegister() + ", " + "$f" + CUP$parser$actions.countRegFP + "\n";

                break;
            default:
                APP = "";
         /*while(testo.lastIndexOf("$s") != -1){
            String RFP,GPR;
            RFP = "$f"+CUP$parser$actions.countRegFP;
            GPR = testo.substring(testo.lastIndexOf("$s"), testo.length());
            APP = APP.concat("MTC1"+" "+GPR+", "+RFP+"\n");
            testo = testo.replaceAll(GPR, RFP);
         }*/
                APP = APP + testo; //VA TESTATA!!!!!!!!


                break;
        }

        RegFP = "$f" + CUP$parser$actions.countRegFP;
        CUP$parser$actions.countRegFP += 2;

        return APP;
    }


    /*METODO PER LA TRADUZIONE DI ESPRESSIONI IN CUI I MEMBRI HANNO COME TIPO: INTEGER-DOUBLE O VICEVERSA (OVERLOADING)*/
    public static String tradEspr(int I, String multgen, String ungen, String multesto, String untesto, String T) {

        String TRAD2 = "";
        /*IL SEGUENTE COSTRUTTO "IF-ELSE" VERIFICA, IN BASE AL BOOLEANO "flagL", SE IL PRIMO O IL SECONDO MEMBRO È UN INTERO O UN DOUBLE*/
        if (flagL) {
            /*I SEGUENTI "IF-ELSE" ANNIDATI PERMETTONO DI TRATTARE TUTTE LE COMBINAZIONI POSSIBILI TRA VARIABILI, ARRAY E NUMERI*/
            if (multgen.equals("VARIABILE")) {

                if (ungen.equals("NUMERO")) {
                    if (I == 3) {
                        TRAD = "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + untesto + "\n" + APP + OpEspr(I, T) + " " + "RIS" +
                                ", " + RegFP + ", " + "$f" + CUP$parser$actions.countRegFP;
                    } else if (I == 4) {
                        TRAD = "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + untesto + "\n" + APP + OpEspr(I, T) + " " + "RIS" +
                                ", " + RegFP + ", " + "$f" + CUP$parser$actions.countRegFP;
                    } else {
                        TRAD = "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + untesto + "\n" + APP + OpEspr(I, T) + " " + "RIS" +
                                ", " + RegFP + ", " + "$f" + CUP$parser$actions.countRegFP;
                    }

                    CUP$parser$actions.countRegFP += 2;

                } else if (ungen.equals("ARRAY_ACCESS")) {

                    if (untesto.startsWith("$f")) {
                        TRAD = APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + untesto;
                    } else {
                        int l = untesto.lastIndexOf(" $f");
                        String UT = untesto.substring(l + 1, l + 4);
                        TRAD = untesto + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + UT;
                    }

                } else {
                    Record rec2 = SymbolTable.getCurrRec(untesto);
                    TRAD = APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + rec2.getRegister();
                }

            } else if (multgen.equals("NUMERO")) {

                Record rec = SymbolTable.getCurrRec(untesto);

                if (I == 3) {

                    if (ungen.equals("VARIABILE")) {

                        TRAD = APP.concat(OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + rec.getRegister());

                    } else if (ungen.equals("ARRAY_ACCESS")) {
                        if (untesto.startsWith("$f")) {
                            TRAD = TRAD + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + untesto;
                        } else {
                            int l = untesto.lastIndexOf(" $f");
                            String UT = untesto.substring(l + 1, l + 4);
                            TRAD = TRAD + "\n" + untesto + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + UT;
                        }
                    } else {
                        int value = CUP$parser$actions.countRegFP + 2;
                        TRAD2 = "L.D." + "$f" + value + "const" + untesto + "\n";
                        TRAD = TRAD2 + "\n" + APP + "\n" + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + "$f" + value;

                    }

                } else if (I == 4) {

                    if (ungen.equals("VARIABILE")) {

                        TRAD = APP.concat(OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + rec.getRegister());

                    } else if (ungen.equals("ARRAY_ACCESS")) {

                        if (untesto.startsWith("$f")) {
                            TRAD = TRAD + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + untesto;
                        } else {
                            int l = untesto.lastIndexOf(" $f");
                            String UT = untesto.substring(l + 1, l + 4);
                            TRAD = TRAD + "\n" + untesto + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + UT;
                        }

                    } else {
                        int value = CUP$parser$actions.countRegFP + 2;
                        TRAD2 = "L.D." + "$f" + value + "const" + untesto + "\n";
                        TRAD = TRAD2 + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + "$f" + value;
                    }

                } else {

                    if (ungen.equals("VARIABILE")) {

                        TRAD = APP.concat(OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + rec.getRegister());

                    } else if (ungen.equals("ARRAY_ACCESS")) {

                        if (untesto.startsWith("$f")) {
                            TRAD = TRAD + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + untesto;
                        } else {
                            int l = untesto.lastIndexOf(" $f");
                            String UT = untesto.substring(l + 1, l + 4);
                            TRAD = TRAD + "\n" + untesto + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + UT;
                        }

                    } else {
                        int value = CUP$parser$actions.countRegFP + 2;
                        TRAD2 = "L.D." + "$f" + value + "const" + untesto + "\n";
                        TRAD = TRAD2 + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + "$f" + value;
                    }

                }
                CUP$parser$actions.countRegFP += 4;
            } else if (multgen.equals("ARRAY_ACCESS")) {

                if (multesto.startsWith("$t")) {
                    multesto = "";
                }

                if (ungen.equals("VARIABILE")) {

                    Record rec = SymbolTable.getCurrRec(untesto);
                    TRAD = multesto + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + rec.getRegister();

                } else if (ungen.equals("NUMERO")) {

                    if (I == 3) {
                        TRAD = multesto + "\n" + "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + untesto + "\n" +
                                APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + "$f" + CUP$parser$actions.countRegFP;
                    } else if (I == 4) {
                        TRAD = multesto + "\n" + "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + untesto + "\n" +
                                APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + "$f" + CUP$parser$actions.countRegFP;
                    } else {
                        TRAD = multesto + "\n" + "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + untesto + "\n" +
                                APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + "$f" + CUP$parser$actions.countRegFP;
                    }

                    CUP$parser$actions.countRegFP += 2;
                } else {

                    if (untesto.startsWith("$f")) {
                        TRAD = multesto + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + untesto; //va corretto e modificata il metodo funct()
                    } else {
                        int m = untesto.lastIndexOf(" $f");
                        String US = untesto.substring(m + 1, m + 4);
                        TRAD = multesto + "\n" + untesto + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + US;
                    }
                }

            }

            return TRAD;

        } else {
            /*I SEGUENTI "IF-ELSE" ANNIDATI PERMETTONO DI TRATTARE TUTTE LE COMBINAZIONI POSSIBILI TRA VARIABILI, ARRAY E NUMERI*/
            switch (multgen) {
                case "VARIABILE":
                    Record rec = SymbolTable.retrieveVariableInsideScope(multesto);

                    if (ungen.equals("NUMERO")) {
                        if (I == 3) {
                            TRAD = APP + OpEspr(I, T) + " " + "RIS" + ", " + rec.getRegister() + ", " + RegFP;
                        } else if (I == 4) {
                            TRAD = APP + OpEspr(I, T) + " " + "RIS" + ", " + rec.getRegister() + ", " + RegFP;
                        } else {
                            TRAD = APP + OpEspr(I, T) + " " + "RIS" + ", " + rec.getRegister() + ", " + RegFP;
                        }

                        CUP$parser$actions.countRegFP += 2;
                    } else if (ungen.equals("ARRAY_ACCESS")) {
                        if (untesto.startsWith("$t")) {
                            TRAD = APP + OpEspr(I, T) + " " + "RIS" + ", " + rec.getRegister() + ", " + RegFP;
                        } else {
                            TRAD = untesto + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + rec.getRegister() + ", " + RegFP;
                        }
                    } else {

                        TRAD = APP + OpEspr(I, T) + " " + "RIS" + ", " + rec.getRegister() + ", " + RegFP;

                    }
                    break;
                case "NUMERO":

                    TRAD = "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + multesto + "\n";

                    if (I == 3) {

                        if (ungen.equals("VARIABILE")) {
                            TRAD = TRAD + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " +
                                    "$f" + CUP$parser$actions.countRegFP + ", " + RegFP;
                        } else if (ungen.equals("ARRAY_ACCESS")) {

                            if (untesto.startsWith("$t")) {
                                TRAD = TRAD + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + "$f" + CUP$parser$actions.countRegFP + ", " + RegFP;
                            } else {
                                TRAD = TRAD + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + "$f" + CUP$parser$actions.countRegFP + ", " + RegFP;
                            }

                        } else {
                            TRAD = TRAD + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + "$f" + CUP$parser$actions.countRegFP + ", " + RegFP;
                        }

                    } else if (I == 4) {

                        if (ungen.equals("VARIABILE")) {
                            TRAD = TRAD + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " +
                                    "$f" + CUP$parser$actions.countRegFP + ", " + RegFP;
                        } else if (ungen.equals("ARRAY_ACCESS")) {

                            if (untesto.startsWith("$t")) {
                                TRAD = TRAD + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + "$f" + CUP$parser$actions.countRegFP + ", " + RegFP;
                            } else {
                                TRAD = TRAD + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + "$f" + CUP$parser$actions.countRegFP + ", " + RegFP;
                            }

                        } else {
                            TRAD = TRAD + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " +
                                    "$f" + CUP$parser$actions.countRegFP + ", " + RegFP;
                        }

                    } else {

                        if (ungen.equals("VARIABILE")) {
                            TRAD = TRAD + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " +
                                    "$f" + CUP$parser$actions.countRegFP + ", " + RegFP;
                        } else if (ungen.equals("ARRAY_ACCESS")) {

                            if (untesto.startsWith("$t")) {
                                TRAD = TRAD + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + "$f" + CUP$parser$actions.countRegFP + ", " + RegFP;
                            } else {
                                TRAD = TRAD + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + "$f" + CUP$parser$actions.countRegFP + ", " + RegFP;
                            }

                        } else {
                            TRAD = TRAD + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " +
                                    "$f" + CUP$parser$actions.countRegFP + ", " + RegFP;
                        }

                    }

                    CUP$parser$actions.countRegFP += 4;
                    break;
                case "ARRAY_ACCESS":

                    String UT;
                    if (multesto.startsWith("$f")) {
                        UT = multesto;
                        multesto = "";
                    } else {
                        int l = multesto.lastIndexOf(" $f");
                        UT = multesto.substring(l + 1, l + 4);
                    }

                    if (ungen.equals("VARIABILE")) {

                        TRAD = multesto + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + UT + ", " + RegFP;

                    } else if (ungen.equals("NUMERO")) {
                        if (I == 3) {
                            TRAD = APP + OpEspr(I, T) + " " + "RIS" + ", " + UT + ", " + RegFP;
                        } else if (I == 4) {
                            TRAD = APP + OpEspr(I, T) + " " + "RIS" + ", " + UT + ", " + RegFP;
                        } else {
                            TRAD = APP + OpEspr(I, T) + " " + "RIS" + ", " + UT + ", " + RegFP;
                        }

                        CUP$parser$actions.countRegFP += 2;
                    } else {
                        if (untesto.startsWith("$t")) {
                            TRAD = multesto + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + "$f" + UT + ", " + RegFP;
                        } else {
                            TRAD = multesto + "\n" + untesto + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + UT + ", " + RegFP;
                        }
                    }

                    break;
            }

            return TRAD;
        }

    }


    /*METODO PER LA SELEZIONE DEL TIPO DI OPERAZIONE*/
    public static String OpEspr(int I, String T) {
        if (I == 1) {
            if (T.equals("DOUBLE")) {
                return "MUL.D";
            } else {
                return "MUL";
            }
        } else if (I == 2) {
            if (T.equals("DOUBLE")) {
                return "DIV.D";
            } else {
                return "DIV";
            }
        } else if (I == 3) {
            if (T.equals("DOUBLE")) {
                return "ADD.D";
            } else {
                return "ADD";
            }
        } else {
            if (T.equals("DOUBLE")) {
                return "SUB.D";
            } else {
                return "SUB";
            }
        }

    }

    /*METODO PER LA TRADUZIONE DI ESPRESSIONI BINARIE CON ENTRAMBI GLI ELEMENTI DI TIPO INTEGER*/
    public static String tradRel(String G_L, String G_R, String T_L, String T_R, String RTemp, int I, String Tp_L, String Tp_R) {
        String REG;
        Record rec;
        switch (G_L) {
            case "VARIABILE":
                rec = SymbolTable.retrieveVariableInsideScope(T_L);

                TRAD = selGenBinR(G_R, G_L, "", T_R, rec.getRegister(), RTemp, I, Tp_R);

                break;
            case "ARRAY_ACCESS":
                REG = getRegArr(T_L, Tp_L);
                if (T_L.startsWith("$")) {
                    T_L = "";
                } else {
                    T_L = T_L + "\n";
                }
                TRAD = selGenBinR(G_R, G_L, T_L, T_R, REG, RTemp, I, Tp_R);

                break;
            case "NUMERO":

                TRAD = selGenBinR(G_R, G_L, T_L, T_R, T_L, RTemp, I, Tp_R);

                break;
            default:
                System.out.println("ERROR:genere non supportato!");
                System.exit(0);
        }

        return TRAD;
    }

    /*METODO CHE PERMETTE DI SELEZIONARE IL GENERE DEL SECONDO MEMBRO (QUELLO DI DESTRA)*/
    public static String selGenBinR(String G_R, String G_L, String T_L, String T_R, String Reg_1, String R_T, int I, String Tp_R) {

        String REG;
        Record rec;

        switch (G_R) {
            case "VARIABILE":
                rec = SymbolTable.getCurrRec(T_R);
                T_R = "";

                TRAD = tradBInt(T_L, T_R, R_T, Reg_1, rec.getRegister(), I, G_L, G_R);

                break;
            case "ARRAY_ACCESS":
                REG = getRegArr(T_R, Tp_R);
                if (T_R.startsWith("$")) {
                    T_R = "";
                } else {
                    T_R = T_R + "\n";
                }
                TRAD = tradBInt(T_L, T_R, R_T, Reg_1, REG, I, G_L, G_R);

                break;
            case "NUMERO":

                TRAD = tradRelZero(T_L, "", R_T, Reg_1, T_R, I, G_L, G_R);

                break;
            default:
                System.out.println("ERROR:genere non supportato!");
                System.exit(0);
        }

        return TRAD;
    }

    /*METODO CHE EFFETTUA LA TRADUZIONE VERA E PROPRIA DELL'ESPR BINARIA, NEL CASO IN CUI ENTRAMBI I MEMBRI SONO INTEGER*/
    public static String tradBInt(String T_L, String T_R, String R_T, String Reg_1, String Reg_2, int I, String Gen_1, String Gen_2) {

        if (Gen_1.equals("NUMERO") || Gen_2.equals("NUMERO")) {

            if (I == 3 || I == 4) {
                TRAD = T_L + T_R + "SLTI" + " " + R_T + ", " + Reg_1 + ", " + Reg_2 + "\n" + "BEQ" + " " + R_T + ", " + "$zero, " + "OFFSET" +
                        "\n" + "BEQ" + " " + Reg_1 + ", " + Reg_2 + ", " + "OFFSET";
            } else {
                TRAD = T_L + T_R + "SLTI" + " " + R_T + ", " + Reg_1 + ", " + Reg_2 + "\n" + "BEQ" + " " + R_T + ", " + "$zero, " + "OFFSET";
            }

        } else {

            if (I == 3 || I == 4) {
                TRAD = T_L + T_R + "SLT" + " " + R_T + ", " + Reg_1 + ", " + Reg_2 + "\n" + "BEQ" + " " + R_T + ", " + "$zero, " + "OFFSET" +
                        "\n" + "BEQ" + " " + Reg_1 + ", " + Reg_2 + ", " + "OFFSET";
            } else {
                TRAD = T_L + T_R + "SLT" + " " + R_T + ", " + Reg_1 + ", " + Reg_2 + "\n" + "BEQ" + " " + R_T + ", " + "$zero, " + "OFFSET";
            }

        }

        return TRAD;
    }

    /*METODO CHE PERMETTE LA TRADUZIONE DI RELAZIONI BINARIE, NELLA QUALI IL SECONDO MEMBRO È PARI A 0 O UN NUMERO*/
    public static String tradRelZero(String T_L, String T_R, String R_T, String Reg_1, String Reg_2, int I, String Gen_1, String Gen_2) {

        if (I == 1 && T_R.equals("0")) {
            TRAD = T_L + "BLTZ" + ", " + Reg_1 + ", " + "OFFSET";
        } else if (I == 2 && T_R.equals("0")) {
            TRAD = T_L + "BGTZ" + ", " + Reg_1 + ", " + "OFFSET";
        } else if (I == 3 && T_R.equals("0")) {
            TRAD = T_L + "BLEZ" + ", " + Reg_1 + ", " + "OFFSET";
        } else if (I == 4 && T_R.equals("0")) {
            TRAD = T_L + "BGEZ" + ", " + Reg_1 + ", " + "OFFSET";
        } else {
            TRAD = tradBInt(T_L, T_R, R_T, Reg_1, Reg_2, I, Gen_1, Gen_2);
        }

        return TRAD;
    }


    /*METODO PER LA TRADUZIONE DI ESPRESSIONI BINARIE CON ENTRAMBI GLI ELEMENTI DI TIPO DOUBLE*/
    public static String tradRel(String G_L, String G_R, String T_L, String T_R, int I, String Tp_L) {

        String REG;

        switch (G_L) {
            case "VARIABILE":

                Record rec = SymbolTable.getCurrRec(T_L);  //ottengo il record corrispondente alla variabile

                TRAD = selGenR("", T_L, G_R, T_R, rec.getRegister(), Tp_L, I, false);

                break;
            case "ARRAY_ACCESS":

                REG = getRegArr(T_L, Tp_L); //estraggo il registro corrispondente all'array

                if (T_L.startsWith("$")) {
                    T_L = "";
                } else {
                    T_L = T_L + "\n";
                }

                TRAD = selGenR("", T_L, G_R, T_R, REG, Tp_L, I, true);

                break;
            case "NUMERO":

                String NUM = funct(G_L, T_L);  //assegno al numero un registro

                TRAD = selGenR("", NUM, G_R, T_R, RegFP, Tp_L, I, true);

                break;
            default:

                System.out.println("ERROR:genere non supportato!");
                System.exit(0);
        }

        return TRAD;
    }

    /*METODO CHE PERMETTE LA SELEZIONE DEL TERMINE DESTRO DI TIPO DOUBLE*/
    public static String selGenR(String T_3, String T_L, String G_R, String T_R, String REG_1, String Tp_L, int I, boolean FLAG) {

        String REG = "";
        String testo_1;
        String testo_2 = "";
        String testo_3 = "";

        if (FLAG) {
            testo_1 = T_L;
        } else {
            testo_1 = "";
        }

        switch (G_R) {
            case "VARIABILE":
                Record rec = SymbolTable.getCurrRec(T_R);
                testo_3 = T_3;

                TRAD = selLogOp(I, testo_1, testo_2, testo_3, REG_1, rec.getRegister());

                break;
            case "ARRAY_ACCESS":
                REG = getRegArr(T_R, Tp_L);

                if (T_R.startsWith("$")) {
                    testo_2 = "";
                } else {
                    testo_2 = T_R + "\n";
                }

                testo_3 = T_3;

                TRAD = selLogOp(I, testo_1, testo_2, testo_3, REG_1, REG);

                break;
            case "NUMERO":
                testo_2 = "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + T_R + "\n";
                REG = "$f" + CUP$parser$actions.countRegFP;

                TRAD = selLogOp(I, testo_1, testo_2, testo_3, REG_1, REG);

                CUP$parser$actions.countRegFP += 2;
                break;
            default:

                System.out.println("ERROR:genere non supportato!");
                System.exit(0);

        }

        return TRAD;
    }


    /*METODO CHE PERMETTE DI SELEZIONARE L'OPERAZIONE BINARIA DESIDERATA*/
    public static String selLogOp(int I, String testo1, String testo2, String testo3, String Reg_1, String Reg_2) {
        if (I == 1) {
            TRAD = createTrad(testo1, testo2, testo3, "LT", Reg_1, Reg_2);
        } else if (I == 2) {
            TRAD = createTrad(testo1, testo2, testo3, "GT", Reg_1, Reg_2);
        } else if (I == 3) {
            TRAD = createTrad(testo1, testo2, testo3, "LE", Reg_1, Reg_2);
        } else {
            TRAD = createTrad(testo1, testo2, testo3, "GE", Reg_1, Reg_2);
        }
        return TRAD;
    }

    /*METODO CHE PERMETTE IL RECUPERO DEL REGISTRO CHE IDENTIFICA L'ARRAY*/
    public static String getRegArr(String testo, String tipo) {
        int i;
        String RegArr;

        if (tipo.equals("INTEGER")) {
            if (testo.startsWith("$t")) {
                RegArr = testo;
            } else {
                i = testo.lastIndexOf(" $t");
                RegArr = testo.substring(i + 1, i + 4);
            }
        } else {
            if (testo.startsWith("$f")) {
                RegArr = testo;
            } else {
                i = testo.lastIndexOf(" $f");
                RegArr = testo.substring(i + 1, i + 4);
            }
        }
        return RegArr;
    }

    /*METODO CHE PERMETTE LA TRADUZIONE DELL'ESPRESSIONE BINARIA*/
    public static String createTrad(String testo1, String testo2, String testo3, String Log_Op, String Reg_1, String Reg_2) {
        TRAD = testo1 + testo2 + testo3 + "C." + Log_Op + ".D " + Reg_1 + ", " + Reg_2;
        return TRAD;
    }

    /*METODO PER LA TRADUZIONE DI ESPRESSIONI BINARIE CON ELEMENTI DI TIPO INTEGER/DOUBLE E VICEVERSA*/
    public static String tradRel(String gen_L, String gen_R, String testo_L, String testo_R, String tipo_L, String tipo_R, int I) {
        String REG;
        if (tipo_L.equals("INTEGER") && tipo_R.equals("DOUBLE")) {
            switch (gen_L) {
                case "VARIABILE":
                    String tradVar = funct(gen_L, testo_L);

                    TRAD = selGenR(tradVar, testo_L, gen_R, testo_R, RegFP, tipo_L, I, false);

                    break;
                case "ARRAY_ACCESS":
                    String tradArr = funct(gen_L, testo_L);

                    if (testo_L.startsWith("$")) {
                        testo_L = "";
                    } else {
                        testo_L = testo_L + "\n";
                    }

                    TRAD = selGenR(tradArr, testo_L, gen_R, testo_R, RegFP, tipo_R, I, true);

                    break;
                case "NUMERO":
                    String NUM = funct(gen_L, testo_L);

                    TRAD = selGenR(NUM, "", gen_R, testo_R, RegFP, tipo_L, I, false);

                    break;
                default:
                    System.out.println("ERROR:genere non supportato!");
                    System.exit(0);
            }
        } else {
            switch (gen_L) {
                case "VARIABILE":
                    Record rec = SymbolTable.getCurrRec(testo_L);

                    TRAD = selGen("", testo_L, gen_R, testo_R, rec.getRegister(), tipo_L, I, false);

                    break;
                case "ARRAY_ACCESS":
                    REG = getRegArr(testo_L, tipo_L);

                    if (testo_L.startsWith("$")) {
                        testo_L = "";
                    } else {
                        testo_L = testo_L + "\n";
                    }

                    TRAD = selGen("", testo_L, gen_R, testo_R, REG, tipo_L, I, true);

                    break;
                case "NUMERO":
                    String NUM = "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + testo_L + "\n";
                    REG = "$f" + CUP$parser$actions.countRegFP;
                    CUP$parser$actions.countRegFP += 2;

                    TRAD = selGen(NUM, "", gen_R, testo_R, REG, tipo_L, I, false);

                    break;
                default:
                    System.out.println("ERROR:genere non supportato!");
                    System.exit(0);
            }
        }

        return TRAD;
    }

    /*METODO CHE PERMETTE LA SELEZIONE DEL TERMINE DESTRO DI TIPO INTEGER*/
    public static String selGen(String T_3, String T_L, String G_R, String T_R, String REG_1, String Tp_L, int I, boolean FLAG) {
        //String REG = "";
        String testo_1;
        String testo_2 = "";
        String testo_3 = "";

        if (FLAG) {
            testo_1 = T_L;
        } else {
            testo_1 = "";
        }

        switch (G_R) {
            case "VARIABILE":
                String tradVar = funct(G_R, T_R);
                testo_2 = tradVar;
                testo_3 = T_3;

                TRAD = selLogOp(I, testo_1, testo_2, testo_3, REG_1, RegFP);

                break;
            case "ARRAY_ACCESS":
                String tradArr = funct(G_R, T_R);

                if (T_L.startsWith("$")) {
                    testo_2 = "\n" + tradArr;
                } else {
                    testo_2 = T_R + "\n" + tradArr;
                }

                testo_3 = T_3;

                TRAD = selLogOp(I, testo_1, testo_2, testo_3, REG_1, RegFP);

                break;
            case "NUMERO":
                String NUM = funct(G_R, T_R);
                testo_2 = NUM;
                TRAD = selLogOp(I, testo_1, testo_2, testo_3, REG_1, RegFP);

                break;
            default:
                System.out.println("ERROR:genere non supportato!");
                System.exit(0);
        }

        return TRAD;
    }


    public static String tradEQNEQ(String G_L, String G_R, String T_L, String T_R, String Tp_L, String Tp_R, int I) {

        String REG;
        Record rec;

        switch (G_L) {
            case "VARIABILE":
                rec = SymbolTable.getCurrRec(T_L);

                TRAD = selGBinREQNEQ(G_R, "", T_R, Tp_L, Tp_R, rec.getRegister(), I, "");

                break;
            case "ARRAY_ACCESS":
                REG = getRegArr(T_L, Tp_L);

                if (T_L.startsWith("$")) {
                    T_L = "";
                } else {
                    T_L = T_L + "\n";
                }

                TRAD = selGBinREQNEQ(G_R, T_L, T_R, Tp_L, Tp_R, REG, I, "");

                break;
            case "NUMERO":

                if (Tp_L.equals("INTEGER")) {

                    TRAD = selGBinREQNEQ(G_R, "", T_R, Tp_L, Tp_R, T_L, I, "");
                } else {
                    String NUM = "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + T_L + "\n";
                    REG = "$f" + CUP$parser$actions.countRegFP;
                    CUP$parser$actions.countRegFP += 2;

                    TRAD = selGBinREQNEQ(G_R, NUM, T_R, Tp_L, Tp_R, REG, I, "");
                }

                break;
            default:
                System.out.println("ERROR:genere non supportato!");
                System.exit(0);
        }

        return TRAD;
    }


    public static String selGBinREQNEQ(String G_R, String T_L, String T_R, String Tp_L, String Tp_R, String Reg_1, int I, String T_3) {

        String REG;
        Record rec;

        switch (G_R) {
            case "VARIABILE":
                rec = SymbolTable.getCurrRec(T_R);
                T_R = "";

                TRAD = tradBIntEQNEQ(T_3, T_L, T_R, Tp_L, Tp_R, Reg_1, rec.getRegister(), I);

                break;
            case "ARRAY_ACCESS":
                REG = getRegArr(T_R, Tp_R);

                if (T_R.startsWith("$")) {
                    T_R = "";
                } else {
                    T_R = T_R + "\n";
                }

                TRAD = tradBIntEQNEQ(T_3, T_L, T_R, Tp_L, Tp_R, Reg_1, REG, I);

                break;
            case "NUMERO":

                if (Tp_R.equals("INTEGER")) {

                    TRAD = tradBIntEQNEQ(T_3, T_L, "", Tp_L, Tp_R, Reg_1, T_R, I);
                } else {
                    String NUM = "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + T_R + "\n";
                    REG = "$f" + CUP$parser$actions.countRegFP;
                    CUP$parser$actions.countRegFP += 2;

                    TRAD = tradBIntEQNEQ(T_3, T_L, NUM, Tp_L, Tp_R, Reg_1, REG, I);

                }
                break;
            default:
                System.out.println("ERROR:genere non supportato!");
                System.exit(0);
        }

        return TRAD;
    }


    public static String tradBIntEQNEQ(String T_3, String T_L, String T_R, String Tp_L, String Tp_R, String Reg_1, String Reg_2, int I) {

        if (Tp_L.equals("INTEGER") && Tp_R.equals("INTEGER")) {

            if (I == 1) {
                TRAD = T_L + T_R + "BNE" + " " + Reg_1 + ", " + Reg_2 + ", " + "OFFSET";
            } else {
                TRAD = T_L + T_R + "BQE" + " " + Reg_1 + ", " + Reg_2 + ", " + "OFFSET";
            }

        } else {

            if (I == 1) {
                TRAD = T_L + T_R + T_3 + "C.NE.D" + " " + Reg_1 + ", " + Reg_2;
            } else {
                TRAD = T_L + T_R + T_3 + "C.EQ.D" + " " + Reg_1 + ", " + Reg_2;
            }
        }

        return TRAD;
    }


    public static String tradEQNEQFPI(String G_L, String G_R, String T_L, String T_R, String Tp_L, String Tp_R, int I) {
        String REG;
        Record rec;
        if (Tp_L.equals("INTEGER") && Tp_R.equals("DOUBLE")) {
            switch (G_L) {
                case "VARIABILE":
                    String tradVar = funct(G_L, T_L);
                    TRAD = selGBinREQNEQ(G_R, tradVar, T_R, Tp_L, Tp_R, RegFP, I, "");
                    break;
                case "ARRAY_ACCESS":
                    String tradArr = funct(G_L, T_L);
                    if (T_L.startsWith("$")) {
                        T_L = "";
                    } else {
                        T_L = T_L + "\n";
                    }
                    TRAD = selGBinREQNEQ(G_R, T_L, T_R, Tp_L, Tp_R, RegFP, I, tradArr);
                    break;
                case "NUMERO":
                    String NUM = "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + T_L + "\n";
                    REG = "$f" + CUP$parser$actions.countRegFP;
                    CUP$parser$actions.countRegFP += 2;
                    TRAD = selGBinREQNEQ(G_R, NUM, T_R, Tp_L, Tp_R, REG, I, "");
                    break;
                default:
                    System.out.println("ERROR:genere non supportato!");
                    System.exit(0);
            }
        } else {
            switch (G_L) {
                case "VARIABILE":
                    rec = SymbolTable.getCurrRec(T_L);
                    TRAD = selGBinREQNEQ2(G_R, "", T_R, Tp_L, Tp_R, rec.getRegister(), I, "");
                    break;
                case "ARRAY_ACCESS":
                    REG = getRegArr(T_L, Tp_L);
                    if (T_L.startsWith("$")) {
                        T_L = "";
                    } else {
                        T_L = T_L + "\n";
                    }
                    TRAD = selGBinREQNEQ2(G_R, T_L, T_R, Tp_L, Tp_R, REG, I, "");
                    break;
                case "NUMERO":
                    String NUM = "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + T_L + "\n";
                    REG = "$f" + CUP$parser$actions.countRegFP;
                    CUP$parser$actions.countRegFP += 2;
                    TRAD = selGBinREQNEQ2(G_R, NUM, T_R, Tp_L, Tp_R, REG, I, "");
                    break;
                default:
                    System.out.println("ERROR:genere non supportato!");
                    System.exit(0);
            }
        }
        return TRAD;
    }

    public static String selGBinREQNEQ2(String G_R, String T_L, String T_R, String Tp_L, String Tp_R, String Reg_1, int I, String T_3) {
        switch (G_R) {
            case "VARIABILE":
                String tradVar = funct(G_R, T_R);
                T_R = "";
                TRAD = tradBIntEQNEQ(tradVar, T_L, T_R, Tp_L, Tp_R, Reg_1, RegFP, I);
                break;
            case "ARRAY_ACCESS":
                String tradArr = funct(G_R, T_R);
                if (T_R.startsWith("$")) {
                    T_R = "";
                } else {
                    T_R = T_R + "\n";
                }
                TRAD = tradBIntEQNEQ(tradArr, T_L, T_R, Tp_L, Tp_R, Reg_1, RegFP, I);
                break;
            case "NUMERO":
                String tradNum = funct(G_R, T_R);
                TRAD = tradBIntEQNEQ(tradNum, T_L, "", Tp_L, Tp_R, Reg_1, RegFP, I);
                break;
            default:
                System.out.println("ERROR:genere non supportato!");
                System.exit(0);
        }
        return TRAD;
    }
}

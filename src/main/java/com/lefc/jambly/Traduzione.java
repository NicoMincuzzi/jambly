package com.lefc.jambly;

import java.util.ListIterator;

import static com.lefc.jambly.SymbolTable.getCurrRec;
import static com.lefc.jambly.SymbolTable.retrieveVariableInsideScope;

public class Traduzione {

    private static boolean flagL = false;
    private static String RegFP;
    private static String APP;
    private static String assemblyOutput = "";

    public static String getRegFP() {
        return RegFP;
    }

    public static String createReg(String type, String var) {
        Record record = getCurrRec(var);
        String register;

        if (type.equals("DOUBLE")) {
            register = "$f" + CUP$parser$actions.countRegFP;
            CUP$parser$actions.countRegFP += 2;
            record.setAssemblyRegister(register);
            return "MOVE.D " + var + ", " + register;
        }
        register = "$s" + CUP$parser$actions.countReg;
        CUP$parser$actions.countReg++;
        record.setAssemblyRegister(register);
        return "MOVE " + var + ", " + register;
    }

    //METODO PER LA TRADUZIONE DELLE DICHIARAZIONI DELLE VARIABILI E DEGLI ARRAY
    public static String tradDecl(String type, String statement) {
        String TIPO = "";
        String variable;
        int z = 0;

        switch (type) {
            case "INTEGER":
                TIPO = ".WORD";
                break;
            case "DOUBLE":
                TIPO = ".DOUBLE";
                break;
            default:
                break;
        }

        if (!statement.contains("[]")) {
            variable = statement.contains("=") ? statement.substring(0, statement.indexOf("=") - 1) : statement;

            Record record = getCurrRec(variable);

            assemblyOutput = (record.getValue().toString().equals("null")) ? variable + " " + TIPO + " ?" : variable + " " + TIPO + " " + record.getValue().toString() + "d";
            return assemblyOutput;
        }
        if (statement.contains("new")) {
            variable = statement.substring(0, statement.indexOf("[]"));
            Record record = getCurrRec(variable);

            assemblyOutput = "dim EQU " + record.getList().size() + "\n" + variable + " " + TIPO;
            if (record.getList().isEmpty()) {
                assemblyOutput += " ?";
                return assemblyOutput;
            }
            ListIterator<String> it = record.getList().listIterator(record.getList().size()); //per ottenere la lista al contrario
            record.setValue(record.getList().size());
            while (it.hasPrevious()) {
                assemblyOutput += (z == 0) ? " " + it.previous() : ", " + it.previous();
                z++;
            }
            return assemblyOutput;
        }
        variable = statement.substring(0, statement.indexOf("[]"));
        Record record = getCurrRec(variable);
        assemblyOutput = "dim EQU " + record.getValue().toString() + "\n" + variable + " " + TIPO;
        if (record.getList().isEmpty()) {
            assemblyOutput += " ?";
            return assemblyOutput;
        }

        ListIterator<String> it = record.getList().listIterator(record.getList().size());
        while (it.hasPrevious()) {
            assemblyOutput = (z == 0) ? assemblyOutput + " " + it.previous() : assemblyOutput + ", " + it.previous();
            z++;
        }
        return assemblyOutput;
    }

    /*METODO PER LA TRADUZIONE DI ESPRESSIONI IN CUI ENTRAMBI I MEMBRI HANNO COME TIPO: INTEGER*/
    public static String tradEspr(String multgen, String ungen, String multesto, String untesto, int I, String T) {
        /*I SEGUENTI "IF-ELSE" ANNIDATI PERMETTONO DI TRATTARE TUTTE LE COMBINAZIONI POSSIBILI TRA VARIABILI, ARRAY E NUMERI*/
        switch (multgen) {
            case "VARIABILE": {
                Record rec = retrieveVariableInsideScope(multesto);
                if (ungen.equals("NUMERO")) {
                    if (I == 3) {
                        assemblyOutput = OpEspr(I, T) + "I RIS, " + rec.getRegister() + ", " + untesto;
                    } else if (I == 4) {
                        assemblyOutput = OpEspr(3, T) + "I RIS, " + rec.getRegister() + ", -" + untesto;
                    } else {
                        assemblyOutput = OpEspr(I, T) + " RIS, " + rec.getRegister() + ", " + untesto;
                    }
                } else if (ungen.equals("ARRAY_ACCESS")) {
                    if (untesto.startsWith("$t")) {
                        assemblyOutput = OpEspr(I, T) + " RIS, " + rec.getRegister() + ", " + untesto;
                    } else {
                        int l = untesto.lastIndexOf(" $t");
                        String UT = untesto.substring(l + 1, l + 4);
                        assemblyOutput = untesto + "\n" + OpEspr(I, T) + " RIS, " + rec.getRegister() + ", " + UT;
                    }
                } else {
                    Record rec2 = getCurrRec(untesto);
                    assemblyOutput = OpEspr(I, T) + " RIS, " + rec.getRegister() + ", " + rec2.getRegister();
                }
                break;
            }
            case "NUMERO": {
                Record rec = getCurrRec(untesto);
                if (I == 3) {
                    if (ungen.equals("VARIABILE")) {
                        assemblyOutput = OpEspr(I, T) + "I RIS, " + multesto + ", " + rec.getRegister();
                    } else if (ungen.equals("ARRAY_ACCESS")) {
                        if (untesto.startsWith("$t")) {
                            assemblyOutput = OpEspr(I, T) + " RIS, " + multesto + ", " + untesto;
                        } else {
                            int l = untesto.lastIndexOf(" $t");
                            String UT = untesto.substring(l + 1, l + 4);
                            assemblyOutput = untesto + "\n" + OpEspr(I, T) + "I RIS, " + multesto + ", " + UT;
                        }
                    } else {
                        assemblyOutput = OpEspr(I, T) + "I RIS, " + multesto + ", " + untesto;
                    }
                } else if (I == 4) {
                    if (ungen.equals("VARIABILE")) {
                        assemblyOutput = OpEspr(3, T) + "I RIS, " + multesto + ", " + "-" + rec.getRegister();
                    } else if (ungen.equals("ARRAY_ACCESS")) {
                        if (untesto.startsWith("$t")) {
                            assemblyOutput = OpEspr(3, T) + "I RIS, " + "-" + multesto + ", " + untesto;
                        } else {
                            int l = untesto.lastIndexOf(" $t");
                            String UT = untesto.substring(l + 1, l + 4);
                            assemblyOutput = untesto + "\n" + OpEspr(3, T) + "I RIS, -" + multesto + ", " + UT;
                        }
                    } else {
                        assemblyOutput = OpEspr(3, T) + "I RIS, " + multesto + ", -" + untesto;
                    }
                } else {
                    if (ungen.equals("VARIABILE")) {
                        assemblyOutput = OpEspr(I, T) + " " + "RIS" + ", " + multesto + ", " + rec.getRegister();
                    } else if (ungen.equals("ARRAY_ACCESS")) {
                        if (untesto.startsWith("$t")) {
                            assemblyOutput = OpEspr(I, T) + " " + "RIS" + ", " + multesto + ", " + untesto;
                        } else {
                            int l = untesto.lastIndexOf(" $t");
                            String UT = untesto.substring(l + 1, l + 4);
                            assemblyOutput = untesto + "\n" + OpEspr(I, T) + " " + "RIS" + ", " + multesto + ", " + UT;
                        }
                    } else {
                        assemblyOutput = OpEspr(I, T) + "I" + " " + "RIS" + ", " + multesto + ", " + rec.getRegister();
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
                    Record rec = getCurrRec(untesto);
                    assemblyOutput = multesto + "\n" + OpEspr(I, T) + " " + "RIS" + ", " + UT + ", " + rec.getRegister();
                } else if (ungen.equals("NUMERO")) {
                    if (I == 3) {
                        assemblyOutput = multesto + "\n" + OpEspr(I, T) + "I" + " " + "RIS" + ", " + UT + ", " + untesto;
                    } else if (I == 4) {
                        assemblyOutput = multesto + "\n" + OpEspr(3, T) + "I" + " " + "RIS" + ", " + UT + ", " + "-" + untesto;
                    } else {
                        assemblyOutput = multesto + "\n" + OpEspr(I, T) + " " + "RIS" + ", " + UT + ", " + untesto;
                    }
                } else {
                    if (untesto.startsWith("$t")) {
                        assemblyOutput = OpEspr(I, T) + " " + "RIS" + ", " + UT + ", " + untesto;
                    } else {
                        int m = untesto.lastIndexOf(" $t");
                        String US = untesto.substring(m + 1, m + 4);
                        assemblyOutput = multesto + "\n" + untesto + "\n" + OpEspr(I, T) + " " + "RIS" + ", " + UT + ", " + US;
                    }
                }
                break;
        }
        return assemblyOutput;
    }

    /*METODO PER LA TRADUZIONE DI ESPRESSIONI IN CUI ENTRAMBI I MEMBRI HANNO COME TIPO: DOUBLE (OVERLOADING)*/
    public static String tradEspr(String multgen, String ungen, String multesto, String untesto, String T, int I) {
        /*I SEGUENTI "IF-ELSE" ANNIDATI PERMETTONO DI TRATTARE TUTTE LE COMBINAZIONI POSSIBILI TRA VARIABILI, ARRAY E NUMERI*/
        switch (multgen) {
            case "VARIABILE": {
                if (ungen.equals("NUMERO")) {
                    if (I == 3) {
                        assemblyOutput = "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + untesto + "\n" +
                                OpEspr(I, T) + " " + "RIS" + ", " + getCurrRec(multesto).getRegister() + ", " + "$f" + CUP$parser$actions.countRegFP;
                    } else if (I == 4) {
                        assemblyOutput = "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + untesto + "\n" +
                                OpEspr(I, T) + " " + "RIS" + ", " + getCurrRec(multesto).getRegister() + ", " + "$f" + CUP$parser$actions.countRegFP;
                    } else {
                        assemblyOutput = "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + untesto + "\n" +
                                OpEspr(I, T) + " " + "RIS" + ", " + getCurrRec(multesto).getRegister() + ", " + "$f" + CUP$parser$actions.countRegFP;
                    }
                    CUP$parser$actions.countRegFP += 2;
                } else if (ungen.equals("ARRAY_ACCESS")) {
                    if (untesto.startsWith("$f")) {
                        assemblyOutput = OpEspr(I, T) + " " + "RIS" + ", " + getCurrRec(multesto).getRegister() + ", " + untesto;
                    } else {
                        int l = untesto.lastIndexOf(" $f");
                        String UT = untesto.substring(l + 1, l + 4);
                        assemblyOutput = untesto + "\n" + OpEspr(I, T) + " " + "RIS" + ", " + getCurrRec(multesto).getRegister() + ", " + UT;
                    }
                } else {
                    assemblyOutput = OpEspr(I, T) + " " + "RIS" + ", " + getCurrRec(multesto).getRegister() + ", " + getCurrRec(untesto).getRegister();
                }
                return assemblyOutput;
            }
            case "NUMERO": {
                String TRAD2;
                assemblyOutput = "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + multesto + "\n";
                if (I == 3) {
                    if (ungen.equals("VARIABILE")) {
                        assemblyOutput = assemblyOutput.concat(OpEspr(I, T) + " " + "RIS" + ", " + "$f" + CUP$parser$actions.countRegFP + ", " +
                                getCurrRec(untesto).getRegister());
                    } else if (ungen.equals("ARRAY_ACCESS")) {
                        if (untesto.startsWith("$f")) {
                            assemblyOutput = assemblyOutput + "\n" + OpEspr(I, T) + " " + "RIS" + ", " + "$f" + CUP$parser$actions.countRegFP + ", " + untesto;
                        } else {
                            int l = untesto.lastIndexOf(" $f");
                            String UT = untesto.substring(l + 1, l + 4);
                            assemblyOutput = assemblyOutput + "\n" + untesto + "\n" + OpEspr(I, T) + " " + "RIS" + ", " + "$f" + CUP$parser$actions.countRegFP + ", " + UT;
                        }
                    } else {
                        int value = CUP$parser$actions.countRegFP + 2;
                        TRAD2 = "L.D." + "$f" + value + "const" + untesto + "\n";
                        assemblyOutput = assemblyOutput.concat(TRAD2);
                        assemblyOutput = assemblyOutput.concat(OpEspr(I, T) + " " + "RIS" + ", " + "$f" + CUP$parser$actions.countRegFP + ", " + "$f" + value);
                    }
                } else {
                    String TRAD21;
                    if (ungen.equals("VARIABILE")) {
                        assemblyOutput = assemblyOutput.concat(OpEspr(I, T) + " " + "RIS" + ", " + "$f" + CUP$parser$actions.countRegFP + ", " +
                                getCurrRec(untesto).getRegister());
                    } else if (ungen.equals("ARRAY_ACCESS")) {
                        if (untesto.startsWith("$f")) {
                            assemblyOutput = assemblyOutput + "\n" + OpEspr(I, T) + " " + "RIS" + ", " + "$f" + CUP$parser$actions.countRegFP + ", " + untesto;
                        } else {
                            int l = untesto.lastIndexOf(" $f");
                            String UT = untesto.substring(l + 1, l + 4);
                            assemblyOutput = assemblyOutput + "\n" + untesto + "\n" + OpEspr(I, T) + " " + "RIS" + ", " + "$f" + CUP$parser$actions.countRegFP + ", " + UT;
                        }
                    } else {
                        int value = CUP$parser$actions.countRegFP + 2;
                        TRAD21 = "L.D." + "$f" + value + ", " + "const" + untesto + "\n";
                        assemblyOutput = assemblyOutput.concat(TRAD21);
                        assemblyOutput = assemblyOutput.concat(OpEspr(I, T) + " " + "RIS" + ", " + "$f" + CUP$parser$actions.countRegFP + ", " + "$f" + value);
                    }
                }
                CUP$parser$actions.countRegFP += 4;
                return assemblyOutput;
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
                    Record rec = getCurrRec(untesto);
                    assemblyOutput = multesto + "\n" + OpEspr(I, T) + " " + "RIS" + ", " + UT + ", " + rec.getRegister();
                } else if (ungen.equals("NUMERO")) {
                    if (I == 3) {
                        assemblyOutput = multesto + "\n" + "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + untesto + "\n" +
                                OpEspr(I, T) + " " + "RIS" + ", " + UT + ", " + "$f" + CUP$parser$actions.countRegFP;
                    } else if (I == 4) {
                        assemblyOutput = multesto + "\n" + "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + untesto + "\n" +
                                OpEspr(I, T) + " " + "RIS" + ", " + UT + ", " + "$f" + CUP$parser$actions.countRegFP;
                    } else {
                        assemblyOutput = multesto + "\n" + "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + untesto + "\n" +
                                OpEspr(I, T) + " " + "RIS" + ", " + UT + ", " + "$f" + CUP$parser$actions.countRegFP;
                    }
                    CUP$parser$actions.countRegFP += 2;
                } else {
                    if (untesto.startsWith("$f")) {
                        assemblyOutput = OpEspr(I, T) + " " + "RIS" + ", " + UT + ", " + untesto;
                    } else {
                        int m = untesto.lastIndexOf(" $f");
                        String US = untesto.substring(m + 1, m + 4);
                        assemblyOutput = multesto + "\n" + untesto + "\n" + OpEspr(I, T) + " " + "RIS" + ", " + UT + ", " + US;
                    }
                }
                return assemblyOutput;
        }
        return assemblyOutput;
    }


    public static void castFP(String tipo1, String gen1, String testo1, String gen2, String testo2) {
        if (tipo1.equals("INTEGER")) {
            flagL = true;
            funct(gen1, testo1);
            return;
        }
        flagL = false;
        funct(gen2, testo2);
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
                Record rec = getCurrRec(testo);
                APP = "MTC1" + " " + rec.getRegister() + ", " + "$f" + CUP$parser$actions.countRegFP + "\n";
                break;
            default:
                APP = "";
                APP = APP + testo;
                break;
        }
        RegFP = "$f" + CUP$parser$actions.countRegFP;
        CUP$parser$actions.countRegFP += 2;
        return APP;
    }

    /*METODO PER LA TRADUZIONE DI ESPRESSIONI IN CUI I MEMBRI HANNO COME TIPO: INTEGER-DOUBLE O VICEVERSA (OVERLOADING)*/
    public static String tradEspr(int I, String multgen, String ungen, String multesto, String untesto, String T) {
        /*IL SEGUENTE COSTRUTTO "IF-ELSE" VERIFICA, IN BASE AL BOOLEANO "flagL", SE IL PRIMO O IL SECONDO MEMBRO È UN INTERO O UN DOUBLE*/
        if (flagL) {
            /*I SEGUENTI "IF-ELSE" ANNIDATI PERMETTONO DI TRATTARE TUTTE LE COMBINAZIONI POSSIBILI TRA VARIABILI, ARRAY E NUMERI*/
            switch (multgen) {
                case "VARIABILE":
                    if (ungen.equals("NUMERO")) {
                        assemblyOutput = "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + untesto + "\n" + APP + OpEspr(I, T) + " " + "RIS" +
                                ", " + RegFP + ", " + "$f" + CUP$parser$actions.countRegFP;
                        CUP$parser$actions.countRegFP += 2;
                        return assemblyOutput;
                    } else if (ungen.equals("ARRAY_ACCESS")) {
                        if (untesto.startsWith("$f")) {
                            assemblyOutput = APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + untesto;
                        }
                        return untesto + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " +
                                untesto.substring(untesto.lastIndexOf(" $f") + 1, untesto.lastIndexOf(" $f") + 4);
                    }
                    return APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + getCurrRec(untesto).getRegister();
                case "NUMERO":
                    if (I == 3) {
                        if (ungen.equals("VARIABILE")) {
                            assemblyOutput = APP.concat(OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + getCurrRec(untesto).getRegister());
                        } else if (ungen.equals("ARRAY_ACCESS")) {
                            if (untesto.startsWith("$f")) {
                                assemblyOutput = assemblyOutput + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + untesto;
                            } else {
                                assemblyOutput = assemblyOutput + "\n" + untesto + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " +
                                        untesto.substring(untesto.lastIndexOf(" $f") + 1, untesto.lastIndexOf(" $f") + 4);
                            }
                        } else {
                            int value = CUP$parser$actions.countRegFP + 2;
                            assemblyOutput = ("L.D." + "$f" + value + "const" + untesto + "\n") + "\n" + APP + "\n" + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + "$f" + value;
                        }
                    } else if (I == 4) {
                        if (ungen.equals("VARIABILE")) {
                            assemblyOutput = APP.concat(OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + getCurrRec(untesto).getRegister());
                        } else if (ungen.equals("ARRAY_ACCESS")) {
                            if (untesto.startsWith("$f")) {
                                assemblyOutput = assemblyOutput + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + untesto;
                            } else {
                                assemblyOutput = assemblyOutput + "\n" + untesto + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " +
                                        untesto.substring(untesto.lastIndexOf(" $f") + 1, untesto.lastIndexOf(" $f") + 4);
                            }
                        } else {
                            int value = CUP$parser$actions.countRegFP + 2;
                            assemblyOutput = ("L.D." + "$f" + value + "const" + untesto + "\n") +
                                    "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + "$f" + value;
                        }
                    } else {
                        if (ungen.equals("VARIABILE")) {
                            assemblyOutput = APP.concat(OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + getCurrRec(untesto).getRegister());
                        } else if (ungen.equals("ARRAY_ACCESS")) {
                            if (untesto.startsWith("$f")) {
                                assemblyOutput = assemblyOutput + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + untesto;
                            } else {
                                int l = untesto.lastIndexOf(" $f");
                                String UT = untesto.substring(l + 1, l + 4);
                                assemblyOutput = assemblyOutput + "\n" + untesto + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + UT;
                            }
                        } else {
                            int value = CUP$parser$actions.countRegFP + 2;
                            String translation = "L.D." + "$f" + value + "const" + untesto + "\n";
                            assemblyOutput = translation + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + "$f" + value;
                        }
                    }
                    CUP$parser$actions.countRegFP += 4;
                    return assemblyOutput;
                case "ARRAY_ACCESS":
                    if (multesto.startsWith("$t"))
                        multesto = "";
                    if (ungen.equals("VARIABILE")) {
                        return multesto + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + getCurrRec(untesto).getRegister();
                    } else if (ungen.equals("NUMERO")) {
                        assemblyOutput = multesto + "\n" + "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + untesto + "\n" +
                                APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + "$f" + CUP$parser$actions.countRegFP;
                        CUP$parser$actions.countRegFP += 2;
                        return assemblyOutput;
                    } else {
                        if (untesto.startsWith("$f")) {
                            return multesto + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " + untesto;
                        }
                        return multesto + "\n" + untesto + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + RegFP + ", " +
                                untesto.substring(untesto.lastIndexOf(" $f") + 1, untesto.lastIndexOf(" $f") + 4);
                    }
            }
        }
        /*I SEGUENTI "IF-ELSE" ANNIDATI PERMETTONO DI TRATTARE TUTTE LE COMBINAZIONI POSSIBILI TRA VARIABILI, ARRAY E NUMERI*/
        switch (multgen) {
            case "VARIABILE":
                Record record = retrieveVariableInsideScope(multesto);
                if (ungen.equals("NUMERO")) {
                    CUP$parser$actions.countRegFP += 2;
                    return APP + OpEspr(I, T) + " " + "RIS" + ", " + record.getRegister() + ", " + RegFP;
                } else if (ungen.equals("ARRAY_ACCESS")) {
                    if (untesto.startsWith("$t")) {
                        return APP + OpEspr(I, T) + " " + "RIS" + ", " + record.getRegister() + ", " + RegFP;
                    }
                    return untesto + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + record.getRegister() + ", " + RegFP;
                }
                return APP + OpEspr(I, T) + " " + "RIS" + ", " + record.getRegister() + ", " + RegFP;
            case "NUMERO":
                assemblyOutput = "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + multesto + "\n";
                assemblyOutput = assemblyOutput + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + "$f" + CUP$parser$actions.countRegFP + ", " + RegFP;
                CUP$parser$actions.countRegFP += 4;
                return assemblyOutput;
            case "ARRAY_ACCESS":
                String UT;
                if (multesto.startsWith("$f")) {
                    UT = multesto;
                    multesto = "";
                } else {
                    UT = multesto.substring(multesto.lastIndexOf(" $f") + 1, multesto.lastIndexOf(" $f") + 4);
                }

                if (ungen.equals("VARIABILE")) {
                    return multesto + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + UT + ", " + RegFP;
                } else if (ungen.equals("NUMERO")) {
                    CUP$parser$actions.countRegFP += 2;
                    return APP + OpEspr(I, T) + " " + "RIS" + ", " + UT + ", " + RegFP;
                }
                if (untesto.startsWith("$t")) {
                    assemblyOutput = multesto + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + "$f" + UT + ", " + RegFP;
                    return assemblyOutput;
                }
                return multesto + "\n" + untesto + "\n" + APP + OpEspr(I, T) + " " + "RIS" + ", " + UT + ", " + RegFP;
        }
        return assemblyOutput;
    }

    /*METODO PER LA SELEZIONE DEL TIPO DI OPERAZIONE*/
    public static String OpEspr(int I, String T) {
        if (I == 1) {
            if (T.equals("DOUBLE")) {
                return "MUL.D";
            }
            return "MUL";
        } else if (I == 2) {
            if (T.equals("DOUBLE")) {
                return "DIV.D";
            }
            return "DIV";
        } else if (I == 3) {
            if (T.equals("DOUBLE")) {
                return "ADD.D";
            }
            return "ADD";
        }
        if (T.equals("DOUBLE")) {
            return "SUB.D";
        }
        return "SUB";
    }

    /*METODO PER LA TRADUZIONE DI ESPRESSIONI BINARIE CON ENTRAMBI GLI ELEMENTI DI TIPO INTEGER*/
    public static String tradRel(String G_L, String G_R, String T_L, String T_R, String RTemp, int I, String Tp_L, String Tp_R) {
        switch (G_L) {
            case "VARIABILE":
                Record record = retrieveVariableInsideScope(T_L);
                assemblyOutput = selGenBinR(G_R, G_L, "", T_R, record.getRegister(), RTemp, I, Tp_R);
                break;
            case "ARRAY_ACCESS":
                String registry = retrieveArrayRegister(T_L, Tp_L);
                assemblyOutput = selGenBinR(G_R, G_L, (T_L.startsWith("$")) ? "" : T_L + "\n", T_R, registry, RTemp, I, Tp_R);
                break;
            case "NUMERO":
                assemblyOutput = selGenBinR(G_R, G_L, T_L, T_R, T_L, RTemp, I, Tp_R);
                break;
        }
        return assemblyOutput;
    }

    /*METODO CHE PERMETTE DI SELEZIONARE IL GENERE DEL SECONDO MEMBRO (QUELLO DI DESTRA)*/
    public static String selGenBinR(String G_R, String G_L, String T_L, String T_R, String Reg_1, String R_T, int I, String Tp_R) {
        switch (G_R) {
            case "VARIABILE":
                Record currentRecord = getCurrRec(T_R);
                T_R = "";
                assemblyOutput = tradBInt(T_L, T_R, R_T, Reg_1, currentRecord.getRegister(), I, G_L, G_R);
                break;
            case "ARRAY_ACCESS":
                String registry = retrieveArrayRegister(T_R, Tp_R);
                if (T_R.startsWith("$")) {
                    T_R = "";
                } else {
                    T_R = T_R + "\n";
                }
                assemblyOutput = tradBInt(T_L, T_R, R_T, Reg_1, registry, I, G_L, G_R);
                break;
            case "NUMERO":
                assemblyOutput = tradRelZero(T_L, "", R_T, Reg_1, T_R, I, G_L, G_R);
                break;
        }
        return assemblyOutput;
    }

    /*METODO CHE PERMETTE LA TRADUZIONE DI RELAZIONI BINARIE, NELLA QUALI IL SECONDO MEMBRO È PARI A 0 O UN NUMERO*/
    public static String tradRelZero(String T_L, String T_R, String R_T, String Reg_1, String Reg_2, int I, String Gen_1, String Gen_2) {

        if (I == 1 && T_R.equals("0")) {
            assemblyOutput = T_L + "BLTZ" + ", " + Reg_1 + ", " + "OFFSET";
        } else if (I == 2 && T_R.equals("0")) {
            assemblyOutput = T_L + "BGTZ" + ", " + Reg_1 + ", " + "OFFSET";
        } else if (I == 3 && T_R.equals("0")) {
            assemblyOutput = T_L + "BLEZ" + ", " + Reg_1 + ", " + "OFFSET";
        } else if (I == 4 && T_R.equals("0")) {
            assemblyOutput = T_L + "BGEZ" + ", " + Reg_1 + ", " + "OFFSET";
        } else {
            assemblyOutput = tradBInt(T_L, T_R, R_T, Reg_1, Reg_2, I, Gen_1, Gen_2);
        }
        return assemblyOutput;
    }

    /*METODO CHE EFFETTUA LA TRADUZIONE VERA E PROPRIA DELL'ESPR BINARIA, NEL CASO IN CUI ENTRAMBI I MEMBRI SONO INTEGER*/
    private static String tradBInt(String T_L, String T_R, String R_T, String Reg_1, String Reg_2, int I, String Gen_1, String Gen_2) {
        if (Gen_1.equals("NUMERO") || Gen_2.equals("NUMERO")) {
            String t = T_L + T_R + "SLTI " + R_T + ", " + Reg_1 + ", " + Reg_2 + "\nBEQ " + R_T + ", $zero, OFFSET";
            if (I == 3 || I == 4) {
                return t + "\nBEQ " + Reg_1 + ", " + Reg_2 + ", OFFSET";
            }
            return t;
        }

        String s = T_L + T_R + "SLT " + R_T + ", " + Reg_1 + ", " + Reg_2 + "\nBEQ " + R_T + ", $zero, OFFSET";
        if (I == 3 || I == 4) {
            return s + "\nBEQ " + Reg_1 + ", " + Reg_2 + ", OFFSET";
        }
        return s;
    }

    /*METODO PER LA TRADUZIONE DI ESPRESSIONI BINARIE CON ENTRAMBI GLI ELEMENTI DI TIPO DOUBLE*/
    public static String tradRel(String G_L, String G_R, String T_L, String T_R, int I, String Tp_L) {
        switch (G_L) {
            case "VARIABILE":
                Record currentRecord = getCurrRec(T_L);
                assemblyOutput = selGenR("", T_L, G_R, T_R, currentRecord.getRegister(), Tp_L, I, false);
                break;
            case "ARRAY_ACCESS":
                String REG;
                REG = retrieveArrayRegister(T_L, Tp_L);
                if (T_L.startsWith("$")) {
                    T_L = "";
                } else {
                    T_L = T_L + "\n";
                }
                assemblyOutput = selGenR("", T_L, G_R, T_R, REG, Tp_L, I, true);
                break;
            case "NUMERO":
                String NUM = funct(G_L, T_L);  //assegno al numero un registro
                assemblyOutput = selGenR("", NUM, G_R, T_R, RegFP, Tp_L, I, true);
                break;
        }
        return assemblyOutput;
    }

    /*METODO CHE PERMETTE LA SELEZIONE DEL TERMINE DESTRO DI TIPO DOUBLE*/
    private static String selGenR(String T_3, String T_L, String G_R, String T_R, String REG_1, String Tp_L, int I, boolean FLAG) {
        String REG;
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
                assemblyOutput = selectBinaryOperation(I, testo_1, testo_2, T_3, REG_1, getCurrRec(T_R).getRegister());
                break;
            case "ARRAY_ACCESS":
                REG = retrieveArrayRegister(T_R, Tp_L);
                if (T_R.startsWith("$")) {
                    testo_2 = "";
                } else {
                    testo_2 = T_R + "\n";
                }
                assemblyOutput = selectBinaryOperation(I, testo_1, testo_2, T_3, REG_1, REG);
                break;
            case "NUMERO":
                assemblyOutput = selectBinaryOperation(I, testo_1, "L.D.$f" + CUP$parser$actions.countRegFP + ", const" + T_R + "\n", testo_3, REG_1, "$f" + CUP$parser$actions.countRegFP);
                CUP$parser$actions.countRegFP += 2;
                break;
        }
        return assemblyOutput;
    }

    private static String selectBinaryOperation(int I, String testo1, String testo2, String testo3, String Reg_1, String Reg_2) {
        if (I == 1) {
            assemblyOutput = testo1 + testo2 + testo3 + "C.LT.D " + Reg_1 + ", " + Reg_2;
        } else if (I == 2) {
            assemblyOutput = testo1 + testo2 + testo3 + "C.GT.D " + Reg_1 + ", " + Reg_2;
        } else if (I == 3) {
            assemblyOutput = testo1 + testo2 + testo3 + "C.LE.D " + Reg_1 + ", " + Reg_2;
        } else {
            assemblyOutput = testo1 + testo2 + testo3 + "C.GE.D " + Reg_1 + ", " + Reg_2;
        }
        return assemblyOutput;
    }

    private static String retrieveArrayRegister(String testo, String tipo) {
        if (tipo.equals("INTEGER")) {
            return (testo.startsWith("$t")) ? testo : testo.substring(testo.lastIndexOf(" $t") + 1, testo.lastIndexOf(" $t") + 4);
        }
        return (testo.startsWith("$f")) ? testo : testo.substring(testo.lastIndexOf(" $f") + 1, testo.lastIndexOf(" $f") + 4);
    }

    /*METODO PER LA TRADUZIONE DI ESPRESSIONI BINARIE CON ELEMENTI DI TIPO INTEGER/DOUBLE E VICEVERSA*/
    public static String tradRel(String gen_L, String gen_R, String testo_L, String testo_R, String tipo_L, String tipo_R, int I) {
        String REG;
        if (tipo_L.equals("INTEGER") && tipo_R.equals("DOUBLE")) {
            switch (gen_L) {
                case "VARIABILE":
                    String tradVar = funct(gen_L, testo_L);
                    assemblyOutput = selGenR(tradVar, testo_L, gen_R, testo_R, RegFP, tipo_L, I, false);
                    break;
                case "ARRAY_ACCESS":
                    String tradArr = funct(gen_L, testo_L);
                    if (testo_L.startsWith("$")) {
                        testo_L = "";
                    } else {
                        testo_L = testo_L + "\n";
                    }
                    assemblyOutput = selGenR(tradArr, testo_L, gen_R, testo_R, RegFP, tipo_R, I, true);
                    break;
                case "NUMERO":
                    String NUM = funct(gen_L, testo_L);
                    assemblyOutput = selGenR(NUM, "", gen_R, testo_R, RegFP, tipo_L, I, false);
                    break;
            }
            return assemblyOutput;
        }
        switch (gen_L) {
            case "VARIABILE":
                assemblyOutput = selGen("", testo_L, gen_R, testo_R, getCurrRec(testo_L).getRegister(), I, false);
                break;
            case "ARRAY_ACCESS":
                REG = retrieveArrayRegister(testo_L, tipo_L);
                if (testo_L.startsWith("$")) {
                    testo_L = "";
                } else {
                    testo_L = testo_L + "\n";
                }
                assemblyOutput = selGen("", testo_L, gen_R, testo_R, REG, I, true);
                break;
            case "NUMERO":
                String NUM = "L.D.$f" + CUP$parser$actions.countRegFP + ", const" + testo_L + "\n";
                REG = "$f" + CUP$parser$actions.countRegFP;
                CUP$parser$actions.countRegFP += 2;
                assemblyOutput = selGen(NUM, "", gen_R, testo_R, REG, I, false);
                break;
        }
        return assemblyOutput;
    }

    /*METODO CHE PERMETTE LA SELEZIONE DEL TERMINE DESTRO DI TIPO INTEGER*/
    public static String selGen(String T_3, String T_L, String G_R, String T_R, String REG_1, int I, boolean FLAG) {
        String testo_1;
        String testo_3 = "";

        if (FLAG) {
            testo_1 = T_L;
        } else {
            testo_1 = "";
        }

        switch (G_R) {
            case "VARIABILE":
                assemblyOutput = selectBinaryOperation(I, testo_1, funct(G_R, T_R), T_3, REG_1, RegFP);
                break;
            case "ARRAY_ACCESS":
                String tradArr = funct(G_R, T_R);
                assemblyOutput = selectBinaryOperation(I, testo_1, (T_L.startsWith("$")) ? "\n" + tradArr : T_R + "\n" + tradArr, T_3, REG_1, RegFP);
                break;
            case "NUMERO":
                assemblyOutput = selectBinaryOperation(I, testo_1, funct(G_R, T_R), testo_3, REG_1, RegFP);
                break;
        }
        return assemblyOutput;
    }

    public static String tradEQNEQ(String G_L, String G_R, String T_L, String T_R, String Tp_L, String Tp_R, int I) {
        switch (G_L) {
            case "VARIABILE":
                assemblyOutput = selGBinREQNEQ(G_R, "", T_R, Tp_L, Tp_R, getCurrRec(T_L).getRegister(), I, "");
                break;
            case "ARRAY_ACCESS":
                assemblyOutput = selGBinREQNEQ(G_R, (T_L.startsWith("$")) ? "" : T_L + "\n", T_R, Tp_L, Tp_R, retrieveArrayRegister(T_L, Tp_L), I, "");
                break;
            case "NUMERO":
                if (Tp_L.equals("INTEGER")) {
                    assemblyOutput = selGBinREQNEQ(G_R, "", T_R, Tp_L, Tp_R, T_L, I, "");
                } else {
                    String NUM = "L.D." + "$f" + CUP$parser$actions.countRegFP + ", " + "const" + T_L + "\n";
                    CUP$parser$actions.countRegFP += 2;
                    assemblyOutput = selGBinREQNEQ(G_R, NUM, T_R, Tp_L, Tp_R, "$f" + CUP$parser$actions.countRegFP, I, "");
                }
                break;
        }
        return assemblyOutput;
    }

    private static String tradBIntEQNEQ(String T_3, String T_L, String T_R, String Tp_L, String Tp_R, String Reg_1, String Reg_2, int I) {
        if (Tp_L.equals("INTEGER") && Tp_R.equals("INTEGER")) {
            assemblyOutput = (I == 1) ? T_L + T_R + "BNE " : T_L + T_R + "BQE ";
            assemblyOutput += Reg_1 + ", " + Reg_2 + ", OFFSET";
        } else {
            assemblyOutput = (I == 1) ? T_L + T_R + T_3 + "C.NE.D " : T_L + T_R + T_3 + "C.EQ.D ";
            assemblyOutput += Reg_1 + ", " + Reg_2;
        }
        return assemblyOutput;
    }

    public static String tradEQNEQFPI(String G_L, String G_R, String T_L, String T_R, String Tp_L, String Tp_R, int I) {
        Record rec;
        if (Tp_L.equals("INTEGER") && Tp_R.equals("DOUBLE")) {
            switch (G_L) {
                case "VARIABILE":
                    String tradVar = funct(G_L, T_L);
                    assemblyOutput = selGBinREQNEQ(G_R, tradVar, T_R, Tp_L, Tp_R, RegFP, I, "");
                    break;
                case "ARRAY_ACCESS":
                    String tradArr = funct(G_L, T_L);
                    if (T_L.startsWith("$")) {
                        T_L = "";
                    } else {
                        T_L = T_L + "\n";
                    }
                    assemblyOutput = selGBinREQNEQ(G_R, T_L, T_R, Tp_L, Tp_R, RegFP, I, tradArr);
                    break;
                case "NUMERO":
                    String NUM = "L.D.$f" + CUP$parser$actions.countRegFP + ", const" + T_L + "\n";
                    CUP$parser$actions.countRegFP += 2;
                    assemblyOutput = selGBinREQNEQ(G_R, NUM, T_R, Tp_L, Tp_R, "$f" + CUP$parser$actions.countRegFP, I, "");
                    break;
            }
            return assemblyOutput;
        }
        switch (G_L) {
            case "VARIABILE":
                rec = getCurrRec(T_L);
                assemblyOutput = selGBinREQNEQ2(G_R, "", T_R, Tp_L, Tp_R, rec.getRegister(), I);
                break;
            case "ARRAY_ACCESS":
                assemblyOutput = selGBinREQNEQ2(G_R, (T_L.startsWith("$")) ? "" : T_L + "\n", T_R, Tp_L, Tp_R, retrieveArrayRegister(T_L, Tp_L), I);
                break;
            case "NUMERO":
                String NUM = "L.D.$f" + CUP$parser$actions.countRegFP + ", const" + T_L + "\n";
                CUP$parser$actions.countRegFP += 2;
                assemblyOutput = selGBinREQNEQ2(G_R, NUM, T_R, Tp_L, Tp_R, "$f" + CUP$parser$actions.countRegFP, I);
                break;
        }
        return assemblyOutput;
    }

    private static String selGBinREQNEQ(String G_R, String T_L, String T_R, String Tp_L, String Tp_R, String Reg_1, int I, String T_3) {
        switch (G_R) {
            case "VARIABILE":
                assemblyOutput = tradBIntEQNEQ(T_3, T_L, "", Tp_L, Tp_R, Reg_1, getCurrRec(T_R).getRegister(), I);
                break;
            case "ARRAY_ACCESS":
                assemblyOutput = tradBIntEQNEQ(T_3, T_L, (T_R.startsWith("$")) ? "" : T_R + "\n", Tp_L, Tp_R, Reg_1, retrieveArrayRegister(T_R, Tp_R), I);
                break;
            case "NUMERO":
                if (Tp_R.equals("INTEGER")) {
                    assemblyOutput = tradBIntEQNEQ(T_3, T_L, "", Tp_L, Tp_R, Reg_1, T_R, I);
                } else {
                    String NUM = "L.D.$f" + CUP$parser$actions.countRegFP + ", const" + T_R + "\n";
                    CUP$parser$actions.countRegFP += 2;
                    assemblyOutput = tradBIntEQNEQ(T_3, T_L, NUM, Tp_L, Tp_R, Reg_1, "$f" + CUP$parser$actions.countRegFP, I);
                }
                break;
        }
        return assemblyOutput;
    }

    private static String selGBinREQNEQ2(String G_R, String T_L, String T_R, String Tp_L, String Tp_R, String Reg_1, int I) {
        switch (G_R) {
            case "VARIABILE":
            case "NUMERO":
                String tradVar = funct(G_R, T_R);
                assemblyOutput = tradBIntEQNEQ(tradVar, T_L, "", Tp_L, Tp_R, Reg_1, RegFP, I);
                break;
            case "ARRAY_ACCESS":
                String tradArr = funct(G_R, T_R);
                T_R = (T_R.startsWith("$")) ? "" : T_R + "\n";
                assemblyOutput = tradBIntEQNEQ(tradArr, T_L, T_R, Tp_L, Tp_R, Reg_1, RegFP, I);
                break;
        }
        return assemblyOutput;
    }
}

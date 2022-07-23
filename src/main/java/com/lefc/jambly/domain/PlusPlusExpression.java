package com.lefc.jambly.domain;

import com.lefc.jambly.Record;
import com.lefc.jambly.SymbolTable;
import com.lefc.jambly.model.Obj;
import com.lefc.jambly.parser;

public class PlusPlusExpression implements Expression {
    public String calculate(Obj postespr, parser parser) {
        if (!postespr.tipo.equals("INTEGER")) {
            parser.report_error("ERROR: IL TIPO DI TALE VARIABILE DEVE ESSERE NECESSARIAMENTE UN INTERO!\n", postespr);
        }
        Record record = SymbolTable.getCurrRec(postespr.testo);
        return "ADDI " + record.getRegister() + ", " + record.getRegister() + ", 1";
    }
}
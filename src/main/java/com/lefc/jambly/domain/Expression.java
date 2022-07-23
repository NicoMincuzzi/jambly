package com.lefc.jambly.domain;

import com.lefc.jambly.model.Obj;
import com.lefc.jambly.parser;

interface Expression {
    String calculate(Obj postespr, parser parser);
}

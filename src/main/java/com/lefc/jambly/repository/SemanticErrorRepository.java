package com.lefc.jambly.repository;

import java.io.FileWriter;
import java.io.IOException;

public class SemanticErrorRepository {

    public void writeFileErr(String msg) throws IOException {
        FileWriter file = new FileWriter("FileErr.txt", true);
        file.write(msg);
        file.flush();
    }
}

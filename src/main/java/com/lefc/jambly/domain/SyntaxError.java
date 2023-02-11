package com.lefc.jambly.domain;

import com.lefc.jambly.PrintText;
import com.lefc.jambly.Scanner;

public class SyntaxError implements Error{
    private boolean flag = false;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void foo(String message, Object info, Scanner scan, PrintText output){
        /*cont_errori++;
        try {
            output = scan.text();
            //il null serve per la syntax_error e per la fatal_error
            if (info == null) {
                if (!fatal) {
                    output.setMessage(message);
                    error.add(output);
                    return;
                }
                //caso in cui viene superata la fine del file
                fatal = false;
                output.setMessage(message);

                if (output.getPos() >= scan.getPos_vir()) {
                    output.setString("");
                }
                error.add(output);
                return;
            }
            output.setMessage(message);
            error.add(output);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}

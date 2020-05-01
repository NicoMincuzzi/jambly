package com.lefc.jambly;

import javax.swing.*;
import java.awt.*;

public class Main_Window extends JFrame {
    JPanel panel1;
    JPanel panel11;
    JPanel panel12;
    JButton button1;
    JButton button2;
    JPanel cuscinetto1;
    JPanel cuscinetto2;
    JPanel cuscinetto3;
    JPanel cuscinetto4;

    public Main_Window() {
        super("Jambly");
        initComponent();
    }

    private void initComponent() {
        panel1 = new JPanel(new GridLayout(2, 2));
        panel11 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel12 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        button1 = new JButton("Tool JFlex/Cup");
        button2 = new JButton("Compilatore");
        cuscinetto1 = new JPanel();
        cuscinetto2 = new JPanel();
        cuscinetto3 = new JPanel();
        cuscinetto4 = new JPanel();

        Container cont = this.getContentPane();
        cont.setLayout(new BorderLayout());

        cuscinetto1.setPreferredSize(new Dimension(200, 110));
        cuscinetto2.setPreferredSize(new Dimension(110, 200));
        cuscinetto3.setPreferredSize(new Dimension(200, 110));
        cuscinetto4.setPreferredSize(new Dimension(110, 200));

        cont.add(panel1, BorderLayout.CENTER);
        cont.add(cuscinetto1, BorderLayout.NORTH);
        cont.add(cuscinetto3, BorderLayout.SOUTH);
        cont.add(cuscinetto2, BorderLayout.EAST);
        cont.add(cuscinetto4, BorderLayout.WEST);

        panel1.add(panel11);
        panel1.add(panel12);

        panel11.add(button1);
        panel12.add(button2);
        //button2.setEnabled(false);

        //if(new File("/home/nicola/Nicola/Progetti-Esercitazioni/Java/Compilatore/src/compilatore/ExprLex.java").exists() && 
        //new File("/home/nicola/Nicola/Progetti-Esercitazioni/Java/Compilatore/src/compilatore/parser.java").exists() &&
        //new File("/home/nicola/Nicola/Progetti-Esercitazioni/Java/Compilatore/src/compilatore/sym.java").exists()){
        button2.setEnabled(true);
        //}

        button1.setPreferredSize(new Dimension(150, 40));
        button2.setPreferredSize(new Dimension(150, 40));

        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button2ActionPerformed(evt);
            }
        });

        this.setSize(400, 400);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize(); //restituisce un riferimento al oggetto java.awt.Dimension. Contiene le proprietà 
        //che conterranno la dimensione dello schermo in pixel.
        double x = (dim.getWidth() - this.getWidth()) / 2;
        double y = (dim.getHeight() - this.getHeight()) / 2;
        setLocation((int) x, (int) y);
        setResizable(false);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {
        Support_Tool ST = new Support_Tool();
    }

    private void button2ActionPerformed(java.awt.event.ActionEvent evt) {
        Compilatore compiler = new Compilatore();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main_Window().setVisible(true);
            }
        });
    }
}

package com.lefc.jambly;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SupportTool extends JFrame {

    private JTextField textField1;
    private JTextField textField2;

    private String path1;
    private String path2;

    public SupportTool() {
        super("Run Scanner/Parsing");
    }

    public void runLexerParserWindow() {
        JPanel cuscinetto1 = new JPanel();
        JPanel cuscinetto2 = new JPanel();
        JPanel cuscinetto3 = new JPanel(new BorderLayout());
        JPanel cuscinetto4 = new JPanel();
        JPanel panel1 = new JPanel(new GridLayout(2, 1));
        JPanel panel2 = new JPanel(new BorderLayout());
        JPanel panel3 = new JPanel(new BorderLayout());
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel header2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel panel4 = new JPanel(new GridLayout(2, 1));
        JPanel panel5 = new JPanel(new GridLayout(2, 1));
        JPanel panel6 = new JPanel(new BorderLayout());
        JPanel panel7 = new JPanel(new BorderLayout());
        JPanel panel8 = new JPanel(new BorderLayout());
        JPanel panel9 = new JPanel(new BorderLayout());
        JPanel panTextField1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panTextField2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panButton1 = new JPanel(new GridLayout(1, 2));
        JPanel panButton2 = new JPanel(new GridLayout(1, 2));
        JPanel unaryButton1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel unaryButton2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel unaryButton3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel unaryButton4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel unaryButton5 = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JLabel scannerLabel = new JLabel("SCANNER");
        JLabel parserLabel = new JLabel("PARSER");
        textField1 = new JTextField();
        textField2 = new JTextField();
        JButton scannerBrowseButton = new JButton("Browse...");
        JButton parserBrowseButton = new JButton("Browse...");
        JButton scanButton = new JButton("SCAN");
        JButton parseButton = new JButton("PARSE");
        JButton confirmedButton = new JButton("Ok");

        Container cont = this.getContentPane();
        cont.setLayout(new BorderLayout());

        cuscinetto1.setPreferredSize(new Dimension(600, 30));
        cuscinetto2.setPreferredSize(new Dimension(30, 600));
        cuscinetto3.setPreferredSize(new Dimension(600, 30));
        cuscinetto4.setPreferredSize(new Dimension(30, 600));
        header.setPreferredSize(new Dimension(540, 20));
        header2.setPreferredSize(new Dimension(540, 20));

        cont.add(panel1, BorderLayout.CENTER);
        cont.add(cuscinetto1, BorderLayout.NORTH);
        cont.add(cuscinetto3, BorderLayout.SOUTH);
        cont.add(cuscinetto2, BorderLayout.EAST);
        cont.add(cuscinetto4, BorderLayout.WEST);

        panel1.add(panel2);
        panel1.add(panel3);

        panel2.add(header, BorderLayout.NORTH);
        header.add(scannerLabel);

        panel3.add(header2, BorderLayout.NORTH);
        header2.add(parserLabel);

        panel2.add(panel4, BorderLayout.CENTER);
        panel4.add(panel6);
        panel4.add(panel7);

        panel6.add(panTextField1, BorderLayout.CENTER);
        panTextField1.add(textField1);
        textField1.setPreferredSize(new Dimension(350, 30));

        panel6.add(panButton1, BorderLayout.EAST);
        panButton1.setPreferredSize(new Dimension(150, 125));
        panButton1.add(unaryButton1);
        unaryButton1.add(scannerBrowseButton);
        scannerBrowseButton.setPreferredSize(new Dimension(110, 30));

        unaryButton3.setPreferredSize(new Dimension(540, 40));
        panel7.add(unaryButton3, BorderLayout.NORTH);
        unaryButton3.add(scanButton);
        scanButton.setPreferredSize(new Dimension(110, 20));

        panel7.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.SOUTH);

        panel3.add(panel5, BorderLayout.CENTER);
        panel5.add(panel8);
        panel5.add(panel9);

        panel8.add(panTextField2, BorderLayout.CENTER);
        panTextField2.add(textField2);
        textField2.setPreferredSize(new Dimension(350, 30));

        panel8.add(panButton2, BorderLayout.EAST);
        panButton2.setPreferredSize(new Dimension(150, 125));
        panButton2.add(unaryButton2);
        unaryButton2.add(parserBrowseButton);
        parserBrowseButton.setPreferredSize(new Dimension(110, 30));

        unaryButton4.setPreferredSize(new Dimension(540, 40));
        panel9.add(unaryButton4, BorderLayout.NORTH);
        unaryButton4.add(parseButton);
        parseButton.setPreferredSize(new Dimension(110, 20));

        confirmedButton.setPreferredSize(new Dimension(80, 30));
        panel9.add(unaryButton5, BorderLayout.SOUTH);
        unaryButton5.add(confirmedButton);

        scannerBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });


        parserBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button2ActionPerformed(evt);
            }
        });

        scanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button3ActionPerformed(evt);
            }
        });

        parseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    button4ActionPerformed(evt);
                } catch (IOException ex) {
                    Logger.getLogger(SupportTool.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        confirmedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button5ActionPerformed(evt);
            }
        });

        this.setSize(600, 600);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize(); //restituisce un riferimento al oggetto java.awt.Dimension. Contiene le proprietà 
        //che conterranno la dimensione dello schermo in pixel.
        double x = (dim.getWidth() - this.getWidth()) / 2;
        double y = (dim.getHeight() - this.getHeight()) / 2;
        setLocation((int) x, (int) y);
        setResizable(false);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);

    }

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser fchooser = new JFileChooser("/home/nicola/Nicola/Progetti-Esercitazioni/Java/Compilatore");
        fchooser.showOpenDialog(null);
        File f = fchooser.getSelectedFile();
        path1 = f.getAbsolutePath();
        textField1.setText(path1);
    }

    private void button2ActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser fchooser = new JFileChooser("/home/nicola/Nicola/Progetti-Esercitazioni/Java/Compilatore");
        fchooser.showOpenDialog(null);
        File f = fchooser.getSelectedFile();
        path2 = f.getAbsolutePath();
        textField2.setText(path2);
    }

    private void button3ActionPerformed(java.awt.event.ActionEvent evt) {
        File f = new File(path1);
        jflex.Main.generate(f);
    }

    private void button4ActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
        path2 = path2.substring(0, path2.lastIndexOf("/") + 1);
        System.out.println("The current selected path:  " + path2);
        Process pr = Runtime.getRuntime().exec("java java_cup.Main " + path2 + "provGramm.cup");
        try {
            pr.waitFor();
        } catch (Exception e) {
            System.out.println("Cannot create the parser class. " + e);
        }

        pr = Runtime.getRuntime().exec("mv sym.java parser.java " + path2);
        try {
            pr.waitFor();
        } catch (Exception e) {
            System.out.println("Cannot move files in project package. " + e);
        }
    }

    private void button5ActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
    }
}

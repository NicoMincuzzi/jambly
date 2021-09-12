package com.lefc.jambly;

import javax.swing.*;
import java.awt.*;

import static java.awt.BorderLayout.*;
import static java.awt.Toolkit.getDefaultToolkit;

public class JamblyApp extends JFrame {

    public JamblyApp() {
        super("Jambly");
        setSize(400, 400);
        setResizable(false);
        runMainWindow();
    }

    private void runMainWindow() {
        JPanel panel1 = new JPanel(new GridLayout(2, 2));
        JPanel panel11 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel panel12 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton button1 = new JButton("Tool JFlex/Cup");
        JButton button2 = new JButton("Interpreter");
        JPanel cuscinetto1 = new JPanel();
        JPanel cuscinetto2 = new JPanel();
        JPanel cuscinetto3 = new JPanel();
        JPanel cuscinetto4 = new JPanel();


        cuscinetto1.setPreferredSize(new Dimension(200, 110));
        cuscinetto2.setPreferredSize(new Dimension(110, 200));
        cuscinetto3.setPreferredSize(new Dimension(200, 110));
        cuscinetto4.setPreferredSize(new Dimension(110, 200));

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel1, CENTER);
        getContentPane().add(cuscinetto1, NORTH);
        getContentPane().add(cuscinetto3, SOUTH);
        getContentPane().add(cuscinetto2, EAST);
        getContentPane().add(cuscinetto4, WEST);

        panel1.add(panel11);
        panel1.add(panel12);

        panel11.add(button1);
        panel12.add(button2);

        button2.setEnabled(true);

        button1.setPreferredSize(new Dimension(150, 40));
        button2.setPreferredSize(new Dimension(150, 40));

        button1.addActionListener(this::button1ActionPerformed);
        button2.addActionListener(this::interpreterButtonActionPerformed);

        setLocation(
                (int) ((getDefaultToolkit().getScreenSize().getWidth() - getWidth()) / 2),
                (int) ((getDefaultToolkit().getScreenSize().getHeight() - getHeight()) / 2)
        );

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {
        SupportTool supportTool = new SupportTool();
        supportTool.runLexerParserWindow();
    }

    private void interpreterButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Interpreter compiler = new Interpreter();
        compiler.runMainWindow();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new JamblyApp().setVisible(true));
    }

}

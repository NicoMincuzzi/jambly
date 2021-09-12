package com.lefc.jambly;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.US_ASCII;

public class Interpreter extends JFrame {
    private static String Path;

    private JButton browseButton;
    private JButton runButton;
    private JTextField textField;
    private JTextArea AreaTxt1;
    private JTextArea AreaTxt2;

    public Interpreter() {
        super("Java-Assembly Interpreter");
    }

    public void runMainWindow() {
        JPanel pan1 = new JPanel(new BorderLayout());
        JPanel pan2 = new JPanel(new BorderLayout());
        JPanel pan21 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel pan22 = new JPanel(new GridLayout(1, 2));
        JPanel pan22L = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel pan22R = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel pan3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel pan10 = new JPanel(new GridLayout(1, 1));
        JPanel pan101 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel pan11 = new JPanel(new GridLayout(1, 1));
        JPanel pan12 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        browseButton = new JButton("Browse...");
        runButton = new JButton("Run Interpreter");
        textField = new JTextField();
        JPanel pan31 = new JPanel(new BorderLayout());
        JPanel pan311 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel pan312 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        AreaTxt1 = new JTextArea();
        JScrollPane scrollPan1 = new JScrollPane(AreaTxt1);
        AreaTxt2 = new JTextArea();
        JScrollPane scrollPan2 = new JScrollPane(AreaTxt2);
        JLabel sourceCodeLabel = new JLabel("  Source Code:");
        JLabel outputLabel = new JLabel("Output:");
        JPanel cuscinetto = new JPanel(new FlowLayout());

        Container cont = this.getContentPane();
        cont.setLayout(new BorderLayout());

        cont.add(pan1, BorderLayout.NORTH);
        cont.add(pan2, BorderLayout.CENTER);
        cont.add(pan3, BorderLayout.SOUTH);

        browseButton.setPreferredSize(new Dimension(110, 30));
        textField.setPreferredSize(new Dimension(650, 30));

        runButton.setEnabled(false);

        pan1.add(pan11, BorderLayout.EAST);
        pan1.setPreferredSize(new Dimension(900, 40));

        pan1.add(pan10);
        pan10.add(pan101);
        pan101.add(textField);

        pan11.add(pan12);
        pan12.setPreferredSize(new Dimension(150, 80));
        pan12.add(browseButton);

        browseButton.setPreferredSize(new Dimension(110, 30));

        browseButton.addActionListener(evt -> {
            try {
                browseButtonActionPerformed(evt);
            } catch (IOException ex) {
                Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        runButton.addActionListener(evt -> {
            try {
                runInterpreterButtonActionPerformed(evt);
            } catch (IOException ex) {
                Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        pan2.setPreferredSize(new Dimension(900, 110));
        pan2.add(pan21, BorderLayout.NORTH);
        pan21.add(runButton);
        runButton.setPreferredSize(new Dimension(150, 30));

        pan2.add(cuscinetto, BorderLayout.CENTER);
        cuscinetto.setPreferredSize(new Dimension(150, 10));
        pan2.add(pan22, BorderLayout.SOUTH);
        pan22.add(pan22L);
        pan22.add(pan22R);
        pan22L.add(sourceCodeLabel);
        pan22R.add(outputLabel);

        pan3.setPreferredSize(new Dimension(900, 450));
        pan3.add(pan31);

        pan31.add(pan311, BorderLayout.CENTER);
        pan311.setPreferredSize(new Dimension(440, 435));

        scrollPan1.setPreferredSize(new Dimension(430, 425));
        scrollPan1.setBounds(3, 3, 300, 200);
        pan311.add(scrollPan1);

        AreaTxt1.setText("");
        AreaTxt1.setLineWrap(true); //limita il testo al riquadro
        AreaTxt1.setWrapStyleWord(true);//formatta correttamente il testo
        AreaTxt1.setEditable(false);

        pan31.add(pan312, BorderLayout.EAST);
        pan312.setPreferredSize(new Dimension(440, 435));

        scrollPan2.setPreferredSize(new Dimension(430, 425));
        scrollPan2.setBounds(3, 3, 300, 200);
        pan312.add(scrollPan2);

        AreaTxt2.setText("");
        AreaTxt2.setLineWrap(true); //limita il testo al riquadro
        AreaTxt2.setWrapStyleWord(true);//formatta correttamente il testo
        AreaTxt2.setEditable(false);

        Font font = new Font("Verdana", Font.BOLD, 12);
        AreaTxt1.setFont(font);
        AreaTxt2.setFont(font);

        this.setSize(900, 600);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        double x = (dimension.getWidth() - this.getWidth()) / 2;
        double y = (dimension.getHeight() - this.getHeight()) / 2;
        setLocation((int) x, (int) y);
        setResizable(false);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
        JFileChooser jFileChooser = new JFileChooser(Paths.get("./src/main/resources/assembly_files/").toAbsolutePath().toString());
        jFileChooser.showOpenDialog(null);

        File f = jFileChooser.getSelectedFile();
        Path = f.getAbsolutePath();
        textField.setText(Path);
        AreaTxt1.setText("");

        readFile(Path);
        runButton.setEnabled(true);
    }

    private void runInterpreterButtonActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
        AreaTxt2.setText("");
        String file = readFile(Path, US_ASCII);

        String result = new InterpreterRunner().run(file);
        AreaTxt2.append(result);

        File f = new File("FileTrad.txt");
        f.delete();
        f = new File("FileErr.txt");
        f.delete();

        browseButton.setEnabled(false);
        runButton.setEnabled(false);
    }

    private String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    private void readFile(String path) throws IOException {
        FileReader f = new FileReader(path);
        BufferedReader b = new BufferedReader(f);

        while (true) {
            String s = b.readLine();
            if (s == null)
                break;
            AreaTxt1.append(s + "\n");
        }
    }


}

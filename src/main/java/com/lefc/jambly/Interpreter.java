package com.lefc.jambly;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.awt.Font.BOLD;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.file.Paths.get;

public class Interpreter extends JFrame {
    private static String Path;
    private JButton browseButton;
    private JButton runButton;
    private JTextField textField;
    private JTextArea SourceBox;
    private JTextArea OutputBox;

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
        SourceBox = new JTextArea();
        JScrollPane scrollPan1 = new JScrollPane(SourceBox);
        OutputBox = new JTextArea();
        JScrollPane scrollPan2 = new JScrollPane(OutputBox);
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

        SourceBox.setText("");
        SourceBox.setLineWrap(true); //limita il testo al riquadro
        SourceBox.setWrapStyleWord(true);//formatta correttamente il testo
        SourceBox.setEditable(false);

        pan31.add(pan312, BorderLayout.EAST);
        pan312.setPreferredSize(new Dimension(440, 435));

        scrollPan2.setPreferredSize(new Dimension(430, 425));
        scrollPan2.setBounds(3, 3, 300, 200);
        pan312.add(scrollPan2);

        OutputBox.setText("");
        OutputBox.setLineWrap(true); //limita il testo al riquadro
        OutputBox.setWrapStyleWord(true);//formatta correttamente il testo
        OutputBox.setEditable(false);

        Font font = new Font("Verdana", BOLD, 12);
        SourceBox.setFont(font);
        OutputBox.setFont(font);

        this.setSize(900, 600);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        double x = (dimension.getWidth() - this.getWidth()) / 2;
        double y = (dimension.getHeight() - this.getHeight()) / 2;
        setLocation((int) x, (int) y);
        setResizable(false);

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
        JFileChooser jFileChooser = new JFileChooser(get("./src/main/resources/assembly_files/").toAbsolutePath().toString());
        jFileChooser.showOpenDialog(null);

        Path = jFileChooser.getSelectedFile().getAbsolutePath();
        textField.setText(Path);
        SourceBox.setText("");

        BufferedReader bufferedReader = new BufferedReader(new FileReader(Path));
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null)
                break;
            SourceBox.append(line + "\n");
        }
        runButton.setEnabled(true);
    }

    private void runInterpreterButtonActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
        OutputBox.setText("");
        String file = new String(Files.readAllBytes(get(Path)), US_ASCII);

        String result = new InterpreterRunner().run(file);
        OutputBox.append(result);

        browseButton.setEnabled(false);
        runButton.setEnabled(false);
    }
}

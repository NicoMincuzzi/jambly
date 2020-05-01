package com.lefc.jambly;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Compilatore extends JFrame {
    private static String Path;

    private JPanel pan1;
    private JPanel pan2;
    private JPanel pan21;
    private JPanel pan22;
    private JPanel pan22L;
    private JPanel pan22R;
    private JPanel pan3;
    private JPanel pan10;
    private JPanel pan101;
    private JPanel pan11;
    private JPanel pan12;
    private JButton button1;
    private JButton button2;
    private JTextField textField;
    private JPanel pan31;
    private JPanel pan311;
    private JPanel pan312;
    private JTextArea AreaTxt1;
    private JScrollPane ScrollPan1;
    private JTextArea AreaTxt2;
    private JScrollPane ScrollPan2;
    private JLabel label1;
    private JLabel label2;
    private JPanel cuscinetto;

    public static String getPath() {
        return Path;
    }

    public Compilatore() {
        super("Java-Assembly Interpreter");
        initComponent();
    }

    private void initComponent() {
        pan1 = new JPanel(new BorderLayout());
        pan2 = new JPanel(new BorderLayout());
        pan21 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pan22 = new JPanel(new GridLayout(1, 2));
        pan22L = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pan22R = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pan3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pan10 = new JPanel(new GridLayout(1, 1));
        pan101 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pan11 = new JPanel(new GridLayout(1, 1));
        pan12 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        button1 = new JButton("Browse...");
        button2 = new JButton("Analizza/Traduci");
        textField = new JTextField();
        pan31 = new JPanel(new BorderLayout());
        pan311 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pan312 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        AreaTxt1 = new JTextArea();
        ScrollPan1 = new JScrollPane(AreaTxt1);
        AreaTxt2 = new JTextArea();
        ScrollPan2 = new JScrollPane(AreaTxt2);
        label1 = new JLabel("  Codice sorgente:");
        label2 = new JLabel("Errori/Codice Target:");
        cuscinetto = new JPanel(new FlowLayout());

        Container cont = this.getContentPane();
        cont.setLayout(new BorderLayout());

        cont.add(pan1, BorderLayout.NORTH);
        cont.add(pan2, BorderLayout.CENTER);
        cont.add(pan3, BorderLayout.SOUTH);

        button1.setPreferredSize(new Dimension(110, 30));
        textField.setPreferredSize(new Dimension(650, 30));

        button2.setEnabled(false);

        pan1.add(pan11, BorderLayout.EAST);
        pan1.setPreferredSize(new Dimension(900, 40));

        pan1.add(pan10);
        pan10.add(pan101);
        pan101.add(textField);

        pan11.add(pan12);
        pan12.setPreferredSize(new Dimension(150, 80));
        pan12.add(button1);

        button1.setPreferredSize(new Dimension(110, 30));

        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    but1ActionPerformed(evt);
                } catch (IOException ex) {
                    Logger.getLogger(Compilatore.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    but2ActionPerformed(evt);
                } catch (IOException ex) {
                    Logger.getLogger(Compilatore.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        pan2.setPreferredSize(new Dimension(900, 110));
        pan2.add(pan21, BorderLayout.NORTH);
        pan21.add(button2);
        button2.setPreferredSize(new Dimension(150, 30));

        pan2.add(cuscinetto, BorderLayout.CENTER);
        cuscinetto.setPreferredSize(new Dimension(150, 10));
        pan2.add(pan22, BorderLayout.SOUTH);
        pan22.add(pan22L);
        pan22.add(pan22R);
        pan22L.add(label1);
        pan22R.add(label2);

        pan3.setPreferredSize(new Dimension(900, 450));
        pan3.add(pan31);

        pan31.add(pan311, BorderLayout.CENTER);
        pan311.setPreferredSize(new Dimension(440, 435));

        ScrollPan1.setPreferredSize(new Dimension(430, 425));
        ScrollPan1.setBounds(3, 3, 300, 200);
        pan311.add(ScrollPan1);

        AreaTxt1.setText("");
        AreaTxt1.setLineWrap(true); //limita il testo al riquadro
        AreaTxt1.setWrapStyleWord(true);//formatta correttamente il testo
        AreaTxt1.setEditable(false);

        pan31.add(pan312, BorderLayout.EAST);
        pan312.setPreferredSize(new Dimension(440, 435));

        ScrollPan2.setPreferredSize(new Dimension(430, 425));
        ScrollPan2.setBounds(3, 3, 300, 200);
        pan312.add(ScrollPan2);

        AreaTxt2.setText("");
        AreaTxt2.setLineWrap(true); //limita il testo al riquadro
        AreaTxt2.setWrapStyleWord(true);//formatta correttamente il testo
        AreaTxt2.setEditable(false);

        Font font = new Font("Verdana", Font.BOLD, 12);
        AreaTxt1.setFont(font);
        AreaTxt2.setFont(font);

        this.setSize(900, 600);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize(); //restituisce un riferimento al oggetto java.awt.Dimension. Contiene le proprietà 
        //che conterranno la dimensione dello schermo in pixel.
        double x = (dim.getWidth() - this.getWidth()) / 2;
        double y = (dim.getHeight() - this.getHeight()) / 2;
        setLocation((int) x, (int) y);
        setResizable(false);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    private void readFile(String PATH, int N) throws IOException {
        FileReader f = new FileReader(PATH);
        BufferedReader b = new BufferedReader(f);

        String s;
        while (true) {
            s = b.readLine();
            if (s == null)
                break;
            if (N == 1) {
                AreaTxt1.append(s + "\n");
            } else {
                AreaTxt2.append(s + "\n");
            }
        }
    }

    private void but1ActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
        JFileChooser fchooser = new JFileChooser("/home/nicola/Nicola/Progetti-Esercitazioni/Java/Compilatore/CasiTest");
        fchooser.showOpenDialog(null);

        File f = fchooser.getSelectedFile();
        Path = f.getAbsolutePath();
        textField.setText(Path);
        AreaTxt1.setText("");

        readFile(Path, 1);
        button2.setEnabled(true);
    }

    private void but2ActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
        AreaTxt2.setText("");
        runCompil();

        if (!CUP$parser$actions.FlagSyn && Support.getnumErr() < 5) {
            if (new File("FileErr.txt").exists()) {
                readFile("FileErr.txt", 2);
                readFile("FileTrad.txt", 2);
            } else {
                readFile("FileTrad.txt", 2);
                AreaTxt2.append("\nCompilazione avvenuta correttamente!" +
                        " Non si sono verificati errori sintattici!");
            }
        } else {
            AreaTxt2.setForeground(Color.RED);
            readFile("FileErr.txt", 2);
        }

        File f = new File("FileTrad.txt");
        f.delete();
        f = new File("FileErr.txt");
        f.delete();

        button1.setEnabled(false);
        button2.setEnabled(false);
    }

    private void runCompil() throws FileNotFoundException {
        FileReader fr = new FileReader(Path);

        Scanner scanner = new Scanner(fr);
        parser p = new parser(scanner);
        try {
            p.parse();
            p.calcola_par(); //per un errore sulle parentesi
            p.ordina_list(); //ordina la lista degli errori
            p.remove();   //per errori al di fuori del source program
            p.print_error(); //stampa della lista di errori
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

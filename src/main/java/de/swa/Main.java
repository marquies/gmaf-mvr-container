package de.swa;


import de.swa.Import.controller.ButtonListener;
import de.swa.Import.controller.Controller;
import de.swa.gc.GraphCode;
import de.swa.gc.GraphCodeGenerator;
import de.swa.gc.GraphCodeIO;
import de.swa.gmaf.GMAF;
import de.swa.mmfg.MMFG;
import de.swa.mmfg.Node;
import de.swa.mmfg.builder.FeatureVectorBuilder;
import de.swa.mmfg.builder.XMLEncodeDecode;
import de.swa.ui.Configuration;
import de.swa.ui.MMFGCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Hello, World!");

        Main m = new Main();
    //    m = new Main();
        m.init(m);
        m.processCollection();


    }

    public void init(Main m) {
    /*    //1. Create the frame.
        JFrame frame = new JFrame("FrameDemo");

        //2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //3. Create components and put them in the frame.
        //...create emptyLabel...
        JLabel helloWorld = new JLabel("ContainerDemo");
        JButton close = new JButton("Close");
        JButton performProcessing = new JButton("Process Collection");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });
        performProcessing.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
				try {
					processCollection();
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}
        });
        frame.getContentPane().add(helloWorld, BorderLayout.CENTER);
        frame.getContentPane().add(close, BorderLayout.SOUTH);
        frame.getContentPane().add(performProcessing, BorderLayout.NORTH);

        //4. Size the frame.
        frame.pack();

        //5. Show it.
        frame.setVisible(true);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
 */
        System.out.println("Main m.init()");
        System.out.println("StartUIMP gestartet");

        // Fenster für die startUIImp
        JFrame startUIImp = new JFrame();
        startUIImp.setLayout(new BorderLayout());

        // Textfeld und Statusfeld zur Ausgabe von Mitteilungen
        JTextArea jtaTextfeld = new JTextArea();
        JTextArea jtaStatusfeld = new JTextArea();

        // Buttons für die Menüpunkte - in einem JPanel übereinander anlegen
        JButton jbImpMvr = new JButton("Eine MVR-Datei zum Einlesen auswählen");
//		JButton jbImpPD = new JButton("Eine zum Video gehörige CSV-Datei auswählen");
        JButton jbImpPD = new JButton("Start myGmafPlugin");

 //       Controller controller = new Controller(m);
        Controller controller = new Controller(this);

        JPanel jPanel = new JPanel(new GridLayout(2,1)); // bei zwei aktivierten Buttons GridLayout(2,1)); setzen
        jPanel.add(jbImpMvr);
        jPanel.add(jbImpPD);


        // Zuweisen der Buttons und der Textfelder zur startUIImp
        // Buttons
        startUIImp.add(BorderLayout.NORTH,jPanel);

        // Textfelder
        startUIImp.add(BorderLayout.CENTER,jtaTextfeld);
        startUIImp.add(BorderLayout.SOUTH,jtaStatusfeld);

        // Einstellungen für die startUIImp
        startUIImp.setSize(450,300);
        startUIImp.setLocationRelativeTo(null);
        startUIImp.setVisible(true);
        startUIImp.setTitle("Klaus Maier: Plugin Import");

        // Starteintrag des Textfeldes
        jtaTextfeld.setText("Die Anwendung ist gestartet und bereit. \n"
                + "Wählen Sie als erstes eine MVR-Datei aus. \nDiese MVR-Datei muss genau ein Video im MP4-Format \nund mindestens eine Datei im CSV-Format enthalten.");

		/*
		jtaStatusfeld.setText("Es wurde noch keine Video-Datei eingespielt. \n"
				+ "Es wurde noch keine CSV-Datei eingespielt.");
		*/

        // Controller erzeugen
        Controller gmafImport = new Controller(this);

        /*
         *	Anmelden der Buttons bei dem Buttonlistener.
         *	Dieser befindet sich in plugin_import.controller.ButtonListener.
         *	Es werden ein Text, das Textfeld, das Statusfeld und die referenzierte Controller-Objekt gmafImport übergeben.
         */
        jbImpMvr.addMouseListener(new ButtonListener("jbImpMvr", jtaTextfeld, jtaStatusfeld, gmafImport, m));
        jbImpPD.addMouseListener(new ButtonListener("jbImpPD", jtaTextfeld, jtaStatusfeld, gmafImport, m));



    }

    public void processCollection() throws IOException {
        System.out.println("Main m.processCollection()");

        Runtime rt = Runtime.getRuntime();

        MMFGCollection collection = MMFGCollection.getInstance();
        collection.init();

        System.out.println("Das sind meine MMFGs");
        long prevTotal = 0;
        long prevFree = rt.freeMemory();
        GMAF gmaf = new GMAF();

        System.out.println("processCollection()_1");
        Vector<MMFG> mmfgs = collection.getCollection();
        for (int i = 0; i < mmfgs.size(); i++) {
            MMFG m = mmfgs.get(i);
            System.out.println(m.getGeneralMetadata().getFileName());
            File f = m.getGeneralMetadata().getFileReference();
            byte[] bytes = new byte[0];
            MMFG fv = gmaf.processAsset(bytes,
                    f.getName(),
                    "system",
                    Configuration.getInstance().getMaxRecursions(),
                    Configuration.getInstance().getMaxNodes(),
                    f.getName(),
                    f
            );
            System.out.println("processCollection()_2");
            // Write MMFG to disk
            String xml = FeatureVectorBuilder.flatten(fv, new XMLEncodeDecode());
            RandomAccessFile raf = new RandomAccessFile(Configuration.getInstance().getMMFGRepo() + File.separatorChar + f.getName() + ".mmfg", "rw");
            raf.setLength(0);
            raf.writeBytes(xml);
            raf.close();

            // Write GC to disk
            GraphCode gc = GraphCodeGenerator.generate(fv);
            GraphCodeIO.write(gc, new File(Configuration.getInstance().getGraphCodeRepository() + File.separatorChar + f.getName() + ".gc"));
            MMFGCollection.getInstance().replaceMMFGInCollection(fv, f);



            System.out.println(fv.getGeneralMetadata().getFileName());
            System.out.println("processCollection()_3");


            // Print free system memory
            long total = rt.totalMemory();
            long free = rt.freeMemory();
            if (total != prevTotal || free != prevFree) {
                System.out.println(
                        String.format("Total: %s, Free: %s, Diff: %s",
                                total,
                                free,
                                prevFree - free));
                prevTotal = total;
                prevFree = free;
            }
            System.gc();
            System.runFinalization();

            System.out.println("GraphCode exported to " + Configuration.getInstance().getGraphCodeRepository());
        }
    }
}

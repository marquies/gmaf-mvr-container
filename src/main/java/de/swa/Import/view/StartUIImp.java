package de.swa.Import.view;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import de.swa.Import.controller.ButtonListener;
import de.swa.Import.controller.Controller;
import de.swa.Main;
import de.swa.MyGmafPlugin;


/**
 * In dieser Klasse werden die Elemente der StartUIImp erstellt, 
 * 
 * Die StartUIImp besteht aus einem oberen und unteren Teil.
 * Im oberen Teil befinden sich zwei Buttons für die Menüpunkte
 * und im unteren Teil ein Textfenster zur Ausgabe von Informationen.
 * Es werden Menüeinträge und Elemente der Toolbar erstellt,
 * und diese bei dem Listener ButtonListener angemeldet.
 * 
 * @author Klaus Maier
 * 
 */

public class StartUIImp extends JFrame {
	
	
	/* default serial version ID */
/*	private static final long serialVersionUID = 1L;


	
	/* Controller für die Steuerung */
	//private GMAFImport gmafImport;
	
	
/*	public StartUIImp (String title) {

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

		Controller controller = new Controller(this);

		jbImpPD.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main m = new Main();
				try {
					m.processCollection();
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}

				// Oder alternativ dein Controller
				try {
					controller.startPlugin();
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}

			}
		});

		
		JPanel jPanel = new JPanel(new GridLayout(2,1)); // bei zwei aktivierten Buttons GridLayout(2,1)); setzen
		jPanel.add(jbImpMvr);
		jPanel.add(jbImpPD);
//		wird vorerst nicht eingeblendet


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
		startUIImp.setTitle(title);
		
		// Starteintrag des Textfeldes
		jtaTextfeld.setText("Die Anwendung ist gestartet und bereit. \n"
				+ "Wählen Sie als erstes eine MVR-Datei aus. \nDiese MVR-Datei muss genau ein Video im MP4-Format \nund mindestens eine Datei im CSV-Format enthalten.");
		

		jtaStatusfeld.setText("Es wurde noch keine Video-Datei eingespielt. \n"
				+ "Es wurde noch keine CSV-Datei eingespielt.");

		
		// Controller erzeugen
		Controller gmafImport = new Controller(this);
		
		/*
		*	Anmelden der Buttons bei dem Buttonlistener.
		*	Dieser befindet sich in plugin_import.controller.ButtonListener.
		*	Es werden ein Text, das Textfeld, das Statusfeld und die referenzierte Controller-Objekt gmafImport übergeben.
		*/

	/*	jbImpMvr.addMouseListener(new ButtonListener("jbImpMvr", jtaTextfeld, jtaStatusfeld, gmafImport, this));
		jbImpPD.addMouseListener(new ButtonListener("jbImpPD", jtaTextfeld, jtaStatusfeld, gmafImport, this));
		

		}

	;
	*/
}

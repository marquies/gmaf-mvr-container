package de.swa.Import.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import javax.swing.JTextArea;

import de.swa.Import.view.StartUIImp;

import de.swa.Main;
import de.swa.MyGmafPlugin;




/**
 * In dieser Klasse werden die Klicks auf Menü-Buttons der StartUIImp
 * den entsprechenden Methoden zugeordnet.
 * Die Methoden werden durch das FileInterface ausgeführt.
 * 
 *	@author Klaus Maier
 * 
 */

public class ButtonListener implements MouseListener{

	
	/* Identifikator des angeklickten Buttons in der StartUIImp */
	private String strName;
	
	/* Referenz auf das Textfeld der StartUIImp */
	private JTextArea jtaTF;
	
	/* Referenz auf das Statusfeld der StartUIImp */
	private JTextArea jtaSF;
	
	/* Referenz auf den gmafImport */
	private Controller gmafImport;
	
	/* Referenz auf die startUIImp */
//	private StartUIImp startUIImp;
	private Main m;

	/**
	 * Der Konstruktor wird aufgerufen vom Konstruktor der Klasse import.view.StartUIImp.
	 */

//	public ButtonListener(String caption, JTextArea jtaTextfeld, JTextArea jtaStatusfeld, Controller inGmafImport, StartUIImp inStartUIImp) {
	public ButtonListener(String caption, JTextArea jtaTextfeld, JTextArea jtaStatusfeld, Controller inGmafImport, Main inM) {
		strName = caption;
		jtaTF = jtaTextfeld;
		jtaSF = jtaStatusfeld;
		gmafImport = inGmafImport;
	//	startUIImp = inStartUIImp;
		m = inM;

	}
	
	/* 
	 * überschreiben der geerbten Methoden von MouseListener
	 * Je nach angeklicktem Button wird die zuständige
	 * Methode des Controllers aufgerufen und der Inhalt des Textfeldes geändert.
	 */
	
	@Override
	public void mousePressed(MouseEvent e)  { 
		switch(this.strName) {
		case "jbImpMvr":
			jtaTF.setText("Bitte wählen Sie eine MVR-Datei aus.");
            try {
                gmafImport.gmafImport(jtaTF, jtaSF);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            break;
	//	Statusfeld wird vorerst nicht benutzt
		case "jbImpPD":
	//		jtaSF.setText("Statusfeld UI");
	//		jtaTF.setText("MyGmafPlugin wurde gestartet");
	/*		System.out.println("Start MyGmafPlugin");
            try {
				gmafImport.startPlugin();
			} catch (IOException ex) {
                throw new RuntimeException(ex);
            }
	*/
			jtaTF.setText("");
			try {
                System.out.println("Buttonlistener m.processCollection()");
		//		m.processCollection();
				gmafImport.startPlugin();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
		
	}
	
	// Diese weiteren Methoden werden nicht benötigt.
	@Override
	public void mouseClicked(MouseEvent e) {}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	
	@Override
	public void mouseExited(MouseEvent e) {}
	
	@Override
	public void mouseReleased(MouseEvent e) {}
}
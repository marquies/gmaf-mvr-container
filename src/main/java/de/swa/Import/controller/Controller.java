package de.swa.Import.controller;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.opencsv.CSVReader;

import de.swa.Import.view.StartUIImp;
import de.swa.Main;
import de.swa.MyGmafPlugin;
import de.swa.gc.GraphCode;
import de.swa.gc.GraphCodeGenerator;
import de.swa.gc.GraphCodeIO;
import de.swa.gmaf.GMAF;
import de.swa.gmaf.PluginChain;
import de.swa.mmfg.MMFG;
import de.swa.mmfg.builder.FeatureVectorBuilder;
import de.swa.mmfg.builder.XMLEncodeDecode;
import de.swa.ui.Configuration;
import de.swa.ui.MMFGCollection;

/** 
 * Die Klasse Import steuert die Anwendung.
 * Alle relevanten Methoden zur Steuerung der Anwendung 
 * werden über den Import ausgeführt oder aufgerufen.
 * 
 * @author Klaus Maier
 */ 

public class Controller {

	/* GUI der Anwendung */
//	private StartUIImp startUIImp;
	private Main m;

	/* mvrfile, deren Inhalte importiert werden sollen */
	private File mvrFile;

	/* csvfile, deren Inhalte importiert werden sollen */
	private File csvFile;

	/* Name der mvrfile ohne Endung - für Benennung des Importordners */
	private String mvrFileName;

	/* Referenz auf das Textfeld der StartUIImp */
	private JTextArea jtaTF;
	
	/* Referenz auf das Statusfeld der StartUIImp 
	   Wird vorerst nicht implementiert
	private JTextArea jtaSF;
	*/

	/* damit merkt sich die Anwendung den zuletzt gewählten Import-Pfad */
	private String pathLast;

	/* Ergebnis, ob eine MVR-Datei ausgewählt wurde */
	private Boolean boolMvr;

	/* Ergebnis, wie viele Mp4-Dateien in der gewählten MVR-Datei enthalten sind */
	private Integer intMp4;

	/* Ergebnis, wie viele CSV-Dateien in der gewählten MVR-Datei enthalten sind */
	private Integer intCsv;

	/* Prüfung auf PD-Datentyp Puls */
	private String csvPulse;

	/* Prüfung auf PD-Datentyp Blutdruck */
	private String csvRR;

	/* Prüfung auf PD-Datentyp Emotion */
	private String csvEmotion;

	/* Variable für das Erfüllen der CSV-Vorgaben */
	private Boolean csvFormat;

	private Vector<MMFG> mmfgs;

	private String[] line;


	/* Variable zum Auslesen des MVR-Archivs */
	private Integer EOF;

	/**
	 * Der Konstruktor wird aufgerufen vom Konstruktor der Klasse plugin_import.view.startUIImp.
	 */
//	public Controller(StartUIImp inStartUIImp) {
	public Controller(Main inM) {

		m = inM;
		pathLast = "empty";
	}

	public void gmafImport(JTextArea jtaTextfeld, JTextArea jtaStatusfeld) throws IOException {
		jtaTF = jtaTextfeld;
		/* vorerst nicht implementiert
		 * jtaSF = jtaStatusfeld;
		 */

		EOF = -1;

		// MVR-Datei aussuchen
		fileChooserMvr();


		/* Wenn keine Datei ausgewählt wurde, sage das. */

		if (mvrFile == null) {

			jtaTF.setText("Es wurde keine Datei ausgewählt.");
		} else {

			// Es wurde eine Datei ausgewählt
			jtaTF.setText("Es wurde die Datei " + mvrFile.getName() + " ausgewählt.");
			pathLast = mvrFile.getPath();


			/* Wenn keine Datei ausgewählt wurde, sage das. */

			if (mvrFile == null) {
				jtaTF.setText("Es wurde keine Datei ausgewählt.");
			} else {


				// Es wurde eine Datei ausgewählt
				jtaTF.setText("Es wurde die Datei " + mvrFile.getName() + " ausgewählt.");
				System.out.println("Es wurde die Datei " + mvrFile.getName() + " ausgewählt.");
				pathLast = mvrFile.getPath();

				// prüfe ob MVR-Datei

				if (checkMvr(mvrFile) == false) {
					jtaTF.setText("Die ausgewählte Datei ist keine MVR-Datei. \n"
							+ "Bitte wählen Sie eine MVR-Datei aus.");
					System.out.println("Die ausgewählte Datei ist keine MVR-Datei. Bitte wählen Sie eine MVR-Datei aus.");

				} else {

					// prüfe, wie viele MP4-Dateien in der MVR-Datei enthalten sind
					try {
						checkContent(mvrFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println("Es sind " + intMp4 + " Videodateien vorhanden.");
					System.out.println("Es sind " + intCsv + " CSV-Dateien vorhanden.");

					if (intMp4 != 1) {
						jtaTF.setText("Die ausgewählte MVR-Datei enthält keine oder mehr als eine MP4-Datei. \n"
								+ "Bitte wählen Sie eine MVR-Datei mit genau einer enthaltenen MP4-Datei aus.");
						System.out.println("Die ausgewählte MVR-Datei enthält keine oder mehr als eine MP4-Datei. \n"
								+ "Bitte wählen Sie eine MVR-Datei mit genau einer enthaltenen MP4-Datei aus.");
					} else {

						// prüfe, ob in MVR eine CSV-Datei enthalten ist
						if (intCsv == 0) {
							jtaTF.setText("Die ausgewählte MVR-Datei enthält keine CSV-Datei. \n"
									+ "Bitte wählen Sie eine MVR-Datei mit enthaltener CSV-Datei aus.");
							System.out.println("Die ausgewählte MVR-Datei enthält keine CSV-Datei. \n"
									+ "Bitte wählen Sie eine MVR-Datei mit enthaltener CSV-Datei aus.");
						} else {

							// Alle Voraussetzungen sind erfüllt. Das MVR-Archiv wird ausgepackt und in collection gespeichert
							// gleichzeitg werden die beinhalteten Dateien in Extra-Ordner gespeichert
								try {
									storeUnzipped(mvrFile);
								} catch (IOException e) {
									e.printStackTrace();
								}

								// MMFGs erstellen
								//PluginChain myPluginChain = new PluginChain(plugins);
								//startPlugin ();

								}
						}
					}
				}
			}
		}

		/* Mit dieser Methode wird eine MVR-Datei ausgewählt */
		public void fileChooserMvr () {

			/* Dateiauswahl-Dialog wird geöffnet*/
			JFileChooser chooserMvr = new JFileChooser(pathLast);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("MVR-Dateien", "mvr");
			chooserMvr.setFileFilter(filter);
			mvrFile = null;
			chooserMvr.showOpenDialog(null);

			mvrFile = chooserMvr.getSelectedFile();

		}

		/* Diese Methode prüft, ob eine MVR-File vorliegt */
		public boolean checkMvr (File mvrFile){
			/* String des zu prüfenden Dateinamens */
			String fileNameProof;

			/* String der gewünschten Datei-Endung */
			String format;

			Boolean boolMvr;

			fileNameProof = mvrFile.getName();
			format = ".mvr";

			/* entsprechen die letzten vier Zeichen des Dateinamens dem String ".mvr"? */
			boolMvr = false;
			if (format.equals(fileNameProof.substring(fileNameProof.length() - 4, fileNameProof.length()))) {
				boolMvr = true;
			}
			return boolMvr;
		}

		/* Diese Methode prüft, ob und wie viele MP4-Dateien und wie viele CSV-Dateien in der MVR-Datei enthalten sind. */
		public void checkContent (File inMvr) throws IOException {
			/* String des zu prüfenden Dateinamens */
			String fileContentProof;

			/* erster String der gewünschten Datei-Endung */
			String formatMp4;

			/* zweiter String der gewünschten Datei-Endung */
			String formatCsv;

			// Wert-Zuweisungen
			formatMp4 = ".mp4";
			formatCsv = ".csv";
			// csvFormat = false;
			intMp4 = 0;
			intCsv = 0;

			try (ZipFile zf = new ZipFile(inMvr.getPath())) {
				for (Enumeration<? extends ZipEntry> e = zf.entries();
					 e.hasMoreElements(); ) {
					ZipEntry entry = e.nextElement();
					fileContentProof = entry.getName();
					// Wie viele MP4-Dateien sind in der MVR-Datei enthalten?
					if (formatMp4.equals(fileContentProof.substring(fileContentProof.length() - 4, fileContentProof.length()))) {
						intMp4++;
					}

					// Wie viele CSV-Dateien sind in der MVR-Datei enthalten?
					if (formatCsv.equals(fileContentProof.substring(fileContentProof.length() - 4, fileContentProof.length()))) {
						intCsv++;
					} // if

					// Ist in den CSV-Dateien in Zeile 1 eine Ziffer (Spalte Timestamp), in Zeile 2 ein String (PD-Typ) und in Zeile 3 eine Ziffer (Spalte PD-Typ)?
					// Test Zeile1
					// Test Zeile2
					// Test Zeile3
					// Auswertung csvFormat


				} // for
			} // try
		}

		/* Diese Methode speichert das MVR-Archiv in entpackter Form in vorbereitetem Ordner */
		public void storeUnzipped (File inMvr) throws IOException {

			// den Unterordner für das entpackte MVR-Archiv erstellen
			mvrFileName = mvrFile.getName().substring(0, mvrFile.getName().length() - 4);
			File myRootFile = new File(System.getProperty("user.dir"));
			String targetPathDir = myRootFile.getParent() + "\\gmaf-mvr-container\\plugin_import_entpackte_Dateien\\" + mvrFileName; // Pfad und Name des Ordners
			File dir = new File(targetPathDir);
			if (!dir.mkdir()) {
				dir.delete();
				dir.mkdir();
			}

			// Inhalte des MVR-Archivs entpacken und im neuen Ordner speichern
			try (ZipFile zf = new ZipFile(inMvr.getPath())) {

				for (Enumeration<? extends ZipEntry> e = zf.entries();
					 e.hasMoreElements(); ) {
					ZipEntry target = (ZipEntry) e.nextElement();
					String targetPathFile = targetPathDir + "\\" + mvrFileName + "_" + target.getName();
					File file = new File(targetPathFile);
					file.createNewFile();
					BufferedInputStream bis = new BufferedInputStream(zf.getInputStream(target));
					File newDir = new File(file.getParent());
					newDir.mkdirs();
					BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
					for (int c; (c = bis.read()) != EOF; )
						bos.write((byte) c);
					bos.close();

				}  //for
				// Nur mvr in collection speichern
				// String strPathImport = myRootFile.getParent() + "\\gmav-mvr\\collection\\";
				String strPathImport = "C:\\Users\\Maier\\IdeaProjects\\gmaf-mvr\\collection\\";
				Path pfad = Paths.get(strPathImport + inMvr.getName());
				try {
					Files.copy(Paths.get(inMvr.getAbsolutePath()), pfad, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.out.println("Die Datei " + inMvr.getName() + " wurde in der collection gespeichert");
				jtaTF.setText("Die Datei " + inMvr.getName() + " wurde in der collection gespeichert");

			}  // try
		}  // storeUnzipped


		public void mergeMMFG () { //exception wieder löschen

			/* Dateiauswahl-Dialog wird geöffnet*/
			JFileChooser chooserCsv = new JFileChooser(pathLast);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV-Dateien", "csv");
			chooserCsv.setFileFilter(filter);
			csvFile = null;
			chooserCsv.showOpenDialog(null);
			csvFile = chooserCsv.getSelectedFile();
			if (csvFile != null) {
				try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
					System.out.println("csvFile: " + csvFile.getName());
					for (int i = 1; i < 4; i++) {
						line = reader.readNext();
						System.out.println(line[0]);
						// ﻿ irgendwie ersetzen mit (leer)
					}
					for (int i = 4; i < 6; i++) {
						line = reader.readNext();
						System.out.println(line[0]);
						System.out.println(line[1]);
						System.out.println(line[2]);
						System.out.println(line[3]);
						System.out.println(line[4]);
						System.out.println(line[5]);
						System.out.println(line[6]);
					}


				} catch (IOException e) {  // try
					e.printStackTrace();
				}
			}
		}

		public void startPlugin () throws IOException {
				System.out.println("Hello, World! (Controller)");
				Runtime rt = Runtime.getRuntime();


				MMFGCollection collection = MMFGCollection.getInstance();
				collection.init();

				System.out.println("Das sind meine MMFGs (Controller)");
				long prevTotal = 0;
				long prevFree = rt.freeMemory();

				GMAF gmaf = new GMAF();

				Vector<MMFG> mmfgs = collection.getCollection();
				mmfgs = collection.getCollection();
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


					// Print free system memory
					long total = rt.totalMemory();
					long free = rt.freeMemory();
					if (total != prevTotal || free != prevFree) {
						System.out.println(
								String.format("Total: %s, Free: %s, Diff: %s (Controller)",
										total,
										free,
										prevFree - free));
						prevTotal = total;
						prevFree = free;
					}
					System.gc();
					System.runFinalization();

					System.out.println("GraphCode exported to (Controller) " + Configuration.getInstance().getGraphCodeRepository());
				}
		//		PluginChain myPluginChain = new PluginChain(MMFG);
		//		PluginChain myPluginChain = new PluginChain.getPlugins(); // Hier muss ein GmafPlugin hinein
		}

	}


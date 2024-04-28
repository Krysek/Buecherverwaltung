/*
 * Autor: Christian Frei
 * Datum: 09.06.2004
 */
 
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.SwingUtilities;

/*
 * GUI ist das Hauptfenster
 */
public class GUI extends JFrame implements ActionListener, ChangeListener
{
	private Info dlgInfo;
	
	private JMenuBar mbMenuBar;
		private JMenu mDatei;
			private JMenuItem miBeenden;
		private JMenu mBearbeiten;
			private JCheckBoxMenuItem miBuecher;
			private JCheckBoxMenuItem miAutoren;
			private JCheckBoxMenuItem miVerlaege;
			private JCheckBoxMenuItem miGenres;
		private JMenu mAktion;
			private JMenuItem miNeu;
			private JMenuItem miSpeichern;
			private JMenuItem miLoeschen;
			private JMenuItem miSuchen;
	private JMenu mHilfe;
		private JMenuItem miInfo;
			

	private JTabbedPane tabRegister;
		private TabBuecher objTabBuecher;
		private TabAutor objTabAutor;
		private TabVerlaege objTabVerlag;
		private TabGenres objTabGenres;
		
	private Buecher objBuecher;
	private Autoren objAutoren;
	private Genres objGenres;
	private Verlaege objVerlaege;
	
	GUI()
	{
		super("Bücherverwaltung");
		
		DB.setConnection();

		objBuecher = new Buecher();
		objBuecher.seekInDB(new Buch());
		objAutoren = new Autoren();
		objAutoren.seekInDB(new Autor());
		objGenres = new Genres();
		objGenres.seekInDB(new Genre());
		objVerlaege = new Verlaege();
		objVerlaege.seekInDB(new Verlag());
		
		// Verlinken
		objBuecher.setAutorenLink(objAutoren);
		objBuecher.setVerlaegeLink(objVerlaege);
		objBuecher.setGenresLink(objGenres);
		
		tabRegister = new JTabbedPane();
		this.setSize(573,480);
		this.setLocation(50,50);
		this.setResizable(false);
		setMenu();
		
		objTabBuecher = new TabBuecher(this, objBuecher, objAutoren,
											objVerlaege, objGenres);
		tabRegister.addTab("Bücher", objTabBuecher.getTabPanel());
		objTabAutor = new TabAutor(this, objAutoren);
		tabRegister.addTab("Autoren", objTabAutor.getTabPanel());
		objTabVerlag = new TabVerlaege(this, objVerlaege);
		tabRegister.addTab("Verläge", objTabVerlag.getTabPanel());
		objTabGenres = new TabGenres(this, objGenres);
		tabRegister.addTab("Genres", objTabGenres.getTabPanel());

		tabRegister.addChangeListener(this);
		this.getContentPane().add(tabRegister);

		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				closeWnd();
			}
		});
		try
		{
			// Windows Look And Feel einstellen
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
		}
		catch(Exception e)
		{
			System.out.println("Error LOF");
		}
		this.setVisible(true);
	}
	
	public void stateChanged(ChangeEvent event) 
	{
		// Veränderung des Register
		if(event.getSource() == tabRegister)
		{
			int index = tabRegister.getSelectedIndex();
			
			if(index == 0)
			{
				objTabBuecher.reloadComboBoxen();
				miBuecher.setSelected(true);
				miAutoren.setSelected(false);
				miVerlaege.setSelected(false);
				miGenres.setSelected(false);
			}
			else if(index == 1)
			{
				miBuecher.setSelected(false);
				miAutoren.setSelected(true);
				miVerlaege.setSelected(false);
				miGenres.setSelected(false);
			}
			else if(index == 2)
			{
				miBuecher.setSelected(false);
				miAutoren.setSelected(false);
				miVerlaege.setSelected(true);
				miGenres.setSelected(false);
			}
			else if(index == 3)
			{
				miBuecher.setSelected(false);
				miAutoren.setSelected(false);
				miVerlaege.setSelected(false);
				miGenres.setSelected(true);
			}
		}
	}
	public void actionPerformed(ActionEvent event)
	{
		// Wenn das im Fenstermenü "Beenden" selektiert wurde
		if(event.getSource() ==  miBeenden)
		{
			closeWnd();
		}
		// Wenn das im Fenstermenü "Bücher" selektiert wurde
		else if (event.getSource() ==  miBuecher)
		{
			miBuecher.setSelected(true);
			miAutoren.setSelected(false);
			miVerlaege.setSelected(false);
			miGenres.setSelected(false);
			tabRegister.setSelectedIndex(0);
		}
		// Wenn das im Fenstermenü "Autoren" selektiert wurde
		else if (event.getSource() ==  miAutoren)
		{
			miBuecher.setSelected(false);
			miAutoren.setSelected(true);
			miVerlaege.setSelected(false);
			miGenres.setSelected(false);
			tabRegister.setSelectedIndex(1);
		}
		// Wenn das im Fenstermenü "Verlaege" selektiert wurde
		else if (event.getSource() ==  miVerlaege)
		{
			miBuecher.setSelected(false);
			miAutoren.setSelected(false);
			miVerlaege.setSelected(true);
			miGenres.setSelected(false);
			tabRegister.setSelectedIndex(2);
		}
		// Wenn das im Fenstermenü "Genres" selektiert wurde
		else if (event.getSource() ==  miGenres)
		{
			miBuecher.setSelected(false);
			miAutoren.setSelected(false);
			miVerlaege.setSelected(false);
			miGenres.setSelected(true);
			tabRegister.setSelectedIndex(3);
		}
		// Wenn das im Fenstermenü "Neu" selektiert wurde
		else if (event.getSource() ==  miNeu)
		{
			if(miBuecher.isSelected() 		== true)
				objTabBuecher.neu();
			else if(miAutoren.isSelected() 	== true)
				objTabAutor.neu();
			else if(miVerlaege.isSelected() == true)
				objTabVerlag.neu();
			else if(miGenres.isSelected() 	== true)
				objTabGenres.neu();
		}
		// Wenn das im Fenstermenü "Speicheren" selektiert wurde
		else if (event.getSource() ==  miSpeichern)
		{
			if(miBuecher.isSelected() 		== true)
				objTabBuecher.speichern();
			else if(miAutoren.isSelected() 	== true)
				objTabAutor.speichern();
			else if(miVerlaege.isSelected() == true)
				objTabVerlag.speichern();
			else if(miGenres.isSelected() 	== true)
				objTabGenres.speichern();
		}
		// Wenn das im Fenstermenü "Löschen" selektiert wurde
		else if (event.getSource() ==  miLoeschen)
		{
			if(miBuecher.isSelected() 		== true)
				objTabBuecher.loesche();
			else if(miAutoren.isSelected() 	== true)
				objTabAutor.loesche();
			else if(miVerlaege.isSelected() == true)
				objTabVerlag.loesche();
			else if(miGenres.isSelected() 	== true)
				objTabGenres.loesche();
		}
		// Wenn das im Fenstermenü "Suchen" selektiert wurde
		else if (event.getSource() ==  miSuchen)
		{
			if(miBuecher.isSelected() 		== true)
				objTabBuecher.aktuallisiereListe();
			else if(miAutoren.isSelected() 	== true)
				objTabAutor.aktuallisiereListe();
			else if(miVerlaege.isSelected() == true)
				objTabVerlag.aktuallisiereListe();
			else if(miGenres.isSelected() 	== true)
				objTabGenres.aktuallisiereListe();
		}		
		else if (event.getSource() ==  miInfo)
		{
			dlgInfo = new Info(this);
		}
		
	}
	
	public void closeWnd()
	/*
	 * GUI ist das Hauptfenster
	 */
	{
		// Anfrage, ob das Programm wirklich geschlossen werden soll
		if (JOptionPane.showConfirmDialog(this,"Wollen Sie wirklich Bücherverwaltung beenden?",
				"Beenden",
				JOptionPane.YES_NO_OPTION) == 0)
		{
			// Schliessen der DB-Verbindungen
			DB.setClose();
			// Schliessen Anwendung
			System.exit(0);
		}
	}
	
	private void setMenu()
	/*
	 * Fenstermenü initialisieren
	 */
	{
		mbMenuBar = new JMenuBar();
		this.setJMenuBar(mbMenuBar);
		
			mDatei = new JMenu("Datei");
			mDatei.setMnemonic('D');
			mbMenuBar.add(mDatei);
				miBeenden = new JMenuItem("Beenden");
				setCtrlAccelerator(miBeenden, 'E');
				miBeenden.addActionListener(this);
				mDatei.add(miBeenden);
			
			mBearbeiten = new JMenu("Bearbeiten");
			mBearbeiten.setMnemonic('B');
			mbMenuBar.add(mBearbeiten);
				miBuecher = new JCheckBoxMenuItem("Bücher");
				setCtrlAccelerator(miBuecher, 'B');
				miBuecher.addActionListener(this);
				mBearbeiten.add(miBuecher);
				miBuecher.setSelected(true);
				
				miAutoren = new JCheckBoxMenuItem("Autoren");
				setCtrlAccelerator(miAutoren, 'A');
				miAutoren.addActionListener(this);
				mBearbeiten.add(miAutoren);
				
				miVerlaege = new JCheckBoxMenuItem("Verläge");
				setCtrlAccelerator(miVerlaege, 'V');
				miVerlaege.addActionListener(this);
				mBearbeiten.add(miVerlaege);
						
				miGenres = new JCheckBoxMenuItem("Genres");
				setCtrlAccelerator(miGenres, 'G');
				miGenres.addActionListener(this);
				mBearbeiten.add(miGenres);

		mAktion = new JMenu("Aktion");
		mAktion.setMnemonic('A');
		mbMenuBar.add(mAktion);
			miNeu = new JMenuItem("Neu");
			setCtrlAccelerator(miNeu, 'N');
			miNeu.addActionListener(this);
			mAktion.add(miNeu);
			
			miSpeichern = new JMenuItem("Speichern");
			setCtrlAccelerator(miSpeichern, 'S');
			miSpeichern.addActionListener(this);
			mAktion.add(miSpeichern);
			
			miLoeschen = new JMenuItem("Löschen");
			setCtrlAccelerator(miLoeschen, 'L');
			miLoeschen.addActionListener(this);
			mAktion.add(miLoeschen);
			
			mAktion.addSeparator();
			
			miSuchen = new JMenuItem("Suchen");
			setCtrlAccelerator(miSuchen, 'U');
			miSuchen.addActionListener(this);
			mAktion.add(miSuchen);

		mHilfe = new JMenu("Hilfe");
		mHilfe.setMnemonic('H');
		mbMenuBar.add(mHilfe);
			miInfo = new JMenuItem("Info");
			setCtrlAccelerator(miInfo, 'I');
			miInfo.addActionListener(this);
			mHilfe.add(miInfo);
	}
	
	private void setCtrlAccelerator(JMenuItem mi, char acc)
	/*
	 * Shortcut mit einem Menüpunkt verknüpfen
	 * 1. Parameter: Menüpunkt, welche ein Shortcut erhalten soll
	 * 2. Parameter: Das Zeichen für das Shortcut genutzt werden soll
	 */
	{
		KeyStroke ks = KeyStroke.getKeyStroke(acc, Event.CTRL_MASK);
		mi.setAccelerator(ks);
	}
	
	public void setVisible(boolean b)
	/*
	 * setVisible wurde überladen, damit das Fenster nicht ausgeblendet werden kann.
	 */
	{
		super.setVisible(true);
	}
}

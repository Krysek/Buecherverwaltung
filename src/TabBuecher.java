/*
 * Autor: Christian Frei
 * Datum: 09.06.2004
 */
 
import javax.swing.*;
import javax.swing.event.*;

import java.sql.*;
import java.awt.*;

public class TabBuecher extends TabPanel implements ChangeListener
{	
	private Buecher objBuecher;
	private Autoren objAutoren;
	private Verlaege objVerlaege;
	private Genres objGenres;
	

	private JTextField txtSucheISBN, txtSucheTitel, txtSucheAutorName;
	private JTextField txtSucheAutorVorname, txtSucheVerlag, txtSucheGenre;
	private JLabel lblSucheISBN, lblSucheTitel, lblSucheAutorName;
	private JLabel  lblSucheAutorVorname, lblSucheVerlag, lblSucheGenre;
	

	private JTextField txtDetailsISBN, txtDetailsTitel;
	private JSpinner spnDetailsAnzahl;
	//Labels für die Detailsanzeige
	private JLabel lblDetailsISBN, lblDetailsTitel, lblDetailsAutor;
	private JLabel lblDetailsVerlag, lblDetailsGenre, lblDetailsAnzahl;
	//ComboBoxen für die Detailsanzeige
	private JComboBox cbDetailsAutor, cbDetailsVerlag, cbDetailsGenre;
	
	
	TabBuecher(GUI wnd, Buecher objBuecher,
				Autoren objAutoren, Verlaege objVerlaege,
				Genres objGenres)
	{
		super(wnd, "Bücher");

		this.objBuecher	= objBuecher;
		this.objAutoren	= objAutoren;
		this.objVerlaege= objVerlaege;
		this.objGenres	= objGenres;
	
		setPnlSuche();
		setPnlDetails();
		
		aktuallisiereListe();
	}	

	//public void ActionPerformed
	public void stateChanged(ChangeEvent event) 
	{
		if(event.getSource() == spnDetailsAnzahl)
		{
			int zahl = Integer.parseInt(spnDetailsAnzahl.getValue().toString());

			if(zahl < 0)
			{
				if(Integer.parseInt(spnDetailsAnzahl.getValue().toString()) < 0)
					spnDetailsAnzahl.setValue(spnDetailsAnzahl.getNextValue());
			}
		}
	}
	
	public void aktuallisiereDetails()
	{
		Buch tmpBuch;
			
		tmpBuch = (Buch) this.getListe().getSelectedValue();
		txtDetailsISBN.setText(tmpBuch.getISBN());
		txtDetailsTitel.setText(tmpBuch.getTitel());
			
		tmpBuch = (Buch) this.getListe().getSelectedValue();
		
		cbDetailsAutor.setSelectedItem(tmpBuch.getAutor());
		cbDetailsVerlag.setSelectedItem(tmpBuch.getVerlag());
		cbDetailsGenre.setSelectedItem(tmpBuch.getGenre());	
		
		spnDetailsAnzahl.setValue(new Integer(tmpBuch.getAnzahl()));
	}
	
	public void aktuallisiereListe()
	{
		int i;
		
		objBuecher.seekInDB(new Buch(txtSucheISBN.getText(),
							txtSucheTitel.getText(),
							new Autor(0, txtSucheAutorName.getText(), txtSucheAutorVorname.getText()),
							new Verlag(0, txtSucheVerlag.getText()), 
							new Genre(0, txtSucheGenre.getText())));
								
		this.getListe().removeAll();
		this.getListe().setListData(objBuecher.getItems());
		
		// Ersten Eintrag selektieren, wenn min. ein Eintrag enthalten ist
		if(objBuecher.getItemCount() > 0)
			this.getListe().setSelectedIndex(0);
	}
	
	public void neu()
	{
		int index = 0;
		ResultSet objRSErgebnis;
		
		try
		{
			objRSErgebnis = DB.getQuery("SELECT ID_Buch " +
											"FROM Buecher " +
											"WHERE ID_Buch = 0");
			// Fehlermeldung, wenn es einen Datensatz
			// mit der ID == 0 existiert
			if(objRSErgebnis.next())
				messageErrorNeuerEintragExistiert();
			else
			{
				objRSErgebnis = DB.getQuery("SELECT ISBN " +
												"FROM Buecher " +
												"WHERE ISBN = '0000000000'");
				// Fehlermeldung, wenn der die ISBN == '0000000000'
				if(objRSErgebnis.next())
					JOptionPane.showConfirmDialog(getGUI(),"Es existiert bereits " +
														"einen neues Buch mit " +
														"der ISBN '0000000000'.",
														"Fehler",-1);
				else
				{
					// neuen Datensatz erstellen
					objBuecher.createItemInDB();
					aktuallisiereListe();
				}
			}
			// Eintrag mit der ID == 0 selektieren
			while(((Buch)this.getListe().getSelectedValue()).getID_Buch() != 0)
				this.getListe().setSelectedIndex(index++);
		}
		catch(Exception e)
		{
		}
	}
	
	public void speichern()
	{
		int index;
		int ID;
		ResultSet objRSErgebnis;
		String ISBN;
		int test;

		// Ist etwas in der Liste ausgewählte
		if(getListe().isSelectionEmpty() == false)
		{
			ID = ((Buch)this.getListe().getSelectedValue()).getID_Buch();
			ISBN = ((Buch)this.getListe().getSelectedValue()).getISBN();
			
			try
			{
				if(txtDetailsISBN.getText().equals("0000000000"))
					JOptionPane.showMessageDialog(getGUI(),
													"ISBN '0000000000' ist ungültig.",
													"Fehler",
													JOptionPane.WARNING_MESSAGE);
													
				else if(txtDetailsISBN.getText().length() != 10)
					// Falls die ISBN zu kurz ist
					JOptionPane.showMessageDialog(getGUI(),"ISBN hat ein falsches Format.","Fehler",JOptionPane.WARNING_MESSAGE);
				else
				{
					objRSErgebnis = DB.getQuery("SELECT ID_Buch, ISBN " +
																	"FROM Buecher " +
																	"WHERE ISBN like '" + txtDetailsISBN.getText() + "'");
	
					// Prüfen, ob die ISBN bereits existiert.
					if(objRSErgebnis.next() && objRSErgebnis.getInt(1) != ID)
						JOptionPane.showMessageDialog(getGUI(),
													"ISBN existiert bereits.",
													"Fehler",
													JOptionPane.WARNING_MESSAGE);
					
					else if (optionUeberschreiben() == 0)
					{
						Buch tmpBuch = new Buch(ID,
												txtDetailsISBN.getText(),
												txtDetailsTitel.getText(),
												(Autor) cbDetailsAutor.getSelectedItem(),
												(Verlag) cbDetailsVerlag.getSelectedItem(),
												(Genre) cbDetailsGenre.getSelectedItem(),
												Integer.parseInt(spnDetailsAnzahl.getValue().toString()));
								
						ID = objBuecher.saveItemInDB(tmpBuch);
						aktuallisiereListe();
							
						//Das eben geänderte Buch selektieren
						index = 0;
						while(((Buch)this.getListe().getSelectedValue()).getID_Buch() != ID)
							this.getListe().setSelectedIndex(index++);
					}
				}
			}
			catch(Exception e)
			{
			}
		}
	}
	
	public void loesche()
	{
		// Ist etwas in der Liste ausgewählte
		if(getListe().isSelectionEmpty() == false)
		{
			// Löscht das Buch
			if (optionLoeschen() == 0)
			{
				objBuecher.removeItemInDB((Buch)this.getListe().getSelectedValue());
				aktuallisiereListe();
			}
		}
	}

	// Den Inhalt des Panel Details initialisieren
	public void setPnlDetails()
	{
		lblDetailsISBN = new JLabel("ISBN:");
		lblDetailsISBN.setSize(50, 20);
		lblDetailsISBN.setLocation(10,20);
		this.getPnlDetails().add(lblDetailsISBN);
				
		lblDetailsTitel = new JLabel("Titel:");
		lblDetailsTitel.setSize(50, 20);
		lblDetailsTitel.setLocation(10,50);
		this.getPnlDetails().add(lblDetailsTitel);
				
		lblDetailsAutor = new JLabel("Autor:");
		lblDetailsAutor.setSize(50, 20);
		lblDetailsAutor.setLocation(10,80);
		this.getPnlDetails().add(lblDetailsAutor);
				
		lblDetailsVerlag = new JLabel("Verlag:");
		lblDetailsVerlag.setSize(50, 20);
		lblDetailsVerlag.setLocation(10,110);
		this.getPnlDetails().add(lblDetailsVerlag);
				
		lblDetailsGenre = new JLabel("Genre:");
		lblDetailsGenre.setSize(50, 20);
		lblDetailsGenre.setLocation(10,140);
		this.getPnlDetails().add(lblDetailsGenre);
				
		lblDetailsAnzahl = new JLabel("Anzahl:");
		lblDetailsAnzahl.setSize(50, 20);
		lblDetailsAnzahl.setLocation(10,170);
		this.getPnlDetails().add(lblDetailsAnzahl);
		
		txtDetailsISBN = getTextfield(65,20);
		txtDetailsISBN.setDocument(new JTextFieldLimit(10, true));
		this.getPnlDetails().add(txtDetailsISBN);
		
		
		txtDetailsTitel = getTextfield(65,50);
		txtDetailsTitel.setDocument(new JTextFieldLimit(50));
		txtDetailsTitel.addActionListener(this);
		this.getPnlDetails().add(txtDetailsTitel);
		
		cbDetailsAutor = new JComboBox(objAutoren.getItems());
		cbDetailsAutor.setSize(195, 20);
		cbDetailsAutor.setLocation(65,80);
		cbDetailsAutor.setBackground(Color.white);
		this.getPnlDetails().add(cbDetailsAutor);
		
		cbDetailsVerlag = new JComboBox(objVerlaege.getItems());
		cbDetailsVerlag.setSize(195, 20);
		cbDetailsVerlag.setLocation(65,110);
		cbDetailsVerlag.setBackground(Color.white);
		this.getPnlDetails().add(cbDetailsVerlag);
								
		cbDetailsGenre = new JComboBox(objGenres.getItems());
		cbDetailsGenre.setSize(195, 20);
		cbDetailsGenre.setLocation(65,140);
		cbDetailsGenre.setBackground(Color.white);
		this.getPnlDetails().add(cbDetailsGenre);
		
		
		SpinnerNumberModel snm = new SpinnerNumberModel(0,0,0,1);
		snm.setMaximum(null);
		spnDetailsAnzahl = new JSpinner(snm);
		spnDetailsAnzahl.setSize(195, 20);
		spnDetailsAnzahl.setLocation(65,170);
		JSpinner.NumberEditor objNE = (JSpinner.NumberEditor)spnDetailsAnzahl.getComponent(2);
			((JFormattedTextField)objNE.getComponent(0)).setFont(this.getSchrift());	
		spnDetailsAnzahl.setBorder(null);		
		spnDetailsAnzahl.addChangeListener(this);
		this.getPnlDetails().add(spnDetailsAnzahl);
	}

	// Die Comboboxen neu initialisieren (aktuallisieren der ComboBoxen)
	public void reloadComboBoxen()
	{
		objAutoren.seekInDB(new Autor());
		this.getPnlDetails().remove(cbDetailsAutor);
		cbDetailsAutor = new JComboBox(objAutoren.getItems());
		cbDetailsAutor.setSize(195, 20);
		cbDetailsAutor.setLocation(65,80);
		cbDetailsAutor.setBackground(Color.white);			
		this.getPnlDetails().add(cbDetailsAutor);

		objVerlaege.seekInDB(new Verlag());
		this.getPnlDetails().remove(cbDetailsVerlag);
		cbDetailsVerlag = new JComboBox(objVerlaege.getItems());
		cbDetailsVerlag.setSize(195, 20);
		cbDetailsVerlag.setLocation(65,110);
		cbDetailsVerlag.setBackground(Color.white);
		this.getPnlDetails().add(cbDetailsVerlag);

		objGenres.seekInDB(new Genre());
		this.getPnlDetails().remove(cbDetailsGenre);
		cbDetailsGenre = new JComboBox(objGenres.getItems());
		cbDetailsGenre.setSize(195, 20);
		cbDetailsGenre.setLocation(65,140);
		cbDetailsGenre.setBackground(Color.white);
		this.getPnlDetails().add(cbDetailsGenre);
		
		aktuallisiereListe();
	}

	// Den Inhalt des Panel Suchen initialisieren
	public void setPnlSuche()
	{
		txtSucheISBN = getTextfield(65,20);
		txtSucheISBN.addActionListener(this);
		txtSucheISBN.setActionCommand("Suchen");
		txtSucheISBN.setDocument(new JTextFieldLimit(10, true));
		this.getPnlSuchen().add(txtSucheISBN);
		
		txtSucheTitel = getTextfield(340,20);
		txtSucheTitel.setDocument(new JTextFieldLimit(50));
		this.getPnlSuchen().add(txtSucheTitel);
		
		txtSucheAutorName = getTextfield(65,50);
		txtSucheAutorName.setDocument(new JTextFieldLimit(20));
		this.getPnlSuchen().add(txtSucheAutorName);
		
		txtSucheAutorVorname = getTextfield(340,50);
		txtSucheAutorVorname.setDocument(new JTextFieldLimit(20));
		this.getPnlSuchen().add(txtSucheAutorVorname);
		
		txtSucheVerlag = getTextfield(65,80);
		txtSucheVerlag.setDocument(new JTextFieldLimit(20));
		this.getPnlSuchen().add(txtSucheVerlag);
		
		txtSucheGenre = getTextfield(340,80);
		txtSucheGenre.setDocument(new JTextFieldLimit(20));
		this.getPnlSuchen().add(txtSucheGenre);
		
		lblSucheISBN = new JLabel("ISBN:");
		lblSucheISBN.setSize(50, 20);
		lblSucheISBN.setLocation(10,20);
		this.getPnlSuchen().add(lblSucheISBN);
				
		lblSucheTitel = new JLabel("Titel:");
		lblSucheTitel.setSize(50, 20);
		lblSucheTitel.setLocation(277,20);
		this.getPnlSuchen().add(lblSucheTitel);
				
		lblSucheAutorName = new JLabel("Name:");
		lblSucheAutorName.setSize(50, 20);
		lblSucheAutorName.setLocation(10,50);
		this.getPnlSuchen().add(lblSucheAutorName);
				
		lblSucheAutorVorname = new JLabel("Vorname:");
		lblSucheAutorVorname.setSize(60, 20);
		lblSucheAutorVorname.setLocation(277,50);
		this.getPnlSuchen().add(lblSucheAutorVorname);
				
		lblSucheVerlag = new JLabel("Verlag:");
		lblSucheVerlag.setSize(50, 20);
		lblSucheVerlag.setLocation(10,80);
		this.getPnlSuchen().add(lblSucheVerlag);
				
		lblSucheGenre = new JLabel("Genre:");
		lblSucheGenre.setSize(50, 20);
		lblSucheGenre.setLocation(277,80);
		this.getPnlSuchen().add(lblSucheGenre);
	}
}

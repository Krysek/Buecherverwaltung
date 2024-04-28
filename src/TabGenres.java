/*
 * Autor: Christian Frei
 * Datum: 09.06.2004
 */
 
import javax.swing.*;
import java.sql.*;

public class TabGenres extends TabPanel
{	
	Genres objGenres;
	
	JLabel lblDetailsName;
	JTextField txtDetailsName;
	
	JLabel lblSucheName;
	JTextField txtSucheName;
	
	TabGenres(GUI wnd, Genres objGenres)
	{
		super(wnd, "Genres");
		this.objGenres = objGenres;
		
		setPnlDetails();
		setPnlSuche();
		
		aktuallisiereListe();
	}

	// Den Inhalt des Panel Details initialisieren
	public void setPnlDetails()
	{
		lblDetailsName = new JLabel("Name:");
		lblDetailsName.setSize(50, 20);
		lblDetailsName.setLocation(10,20);
		this.getPnlDetails().add(lblDetailsName);
		
		txtDetailsName = getTextfield(65,20);
		txtDetailsName.setDocument(new JTextFieldLimit(20));
		this.getPnlDetails().add(txtDetailsName);
	}

	// Den Inhalt des Panel Suchen initialisieren
	public void setPnlSuche()
	{

		lblSucheName = new JLabel("Name:");
		lblSucheName.setSize(50, 20);
		lblSucheName.setLocation(10,20);
		this.getPnlSuchen().add(lblSucheName);
		
		txtSucheName = getTextfield(65,20);
		txtSucheName.setDocument(new JTextFieldLimit(20));
		this.getPnlSuchen().add(txtSucheName);
	}

	public void aktuallisiereDetails()
	{
		Genre tmpGenre;
		
		tmpGenre = (Genre) this.getListe().getSelectedValue();
		txtDetailsName.setText(tmpGenre.getName());
			
		tmpGenre = (Genre) this.getListe().getSelectedValue();		
	}
	
	public void aktuallisiereListe()
	{
		objGenres.seekInDB(new Genre(0, txtSucheName.getText()));
								
		this.getListe().removeAll();
		this.getListe().setListData(objGenres.getItems());
		if(objGenres.getItemCount() > 0)
			this.getListe().setSelectedIndex(0);
	}
	
	public void neu()
	{	
		int index = 0;
		ResultSet objRSErgebnis;
		
		try
		{
			objRSErgebnis = DB.getQuery("SELECT ID_Genre " +
													"FROM Genres " + 
													"WHERE ID_Genre = 0");

			if(objRSErgebnis.next())
				// Fehlermeldung wenn es einen Datensatz
				// mit der ID == 0 existiert
				messageErrorNeuerEintragExistiert();
			else
			{
				// neuen Datensatz erstellen
				objGenres.createItemInDB();					
				aktuallisiereListe();
			}
			// Eintrag mit der ID == 0 selektieren
			while(((Genre)this.getListe().getSelectedValue()).getID_Genre() != 0)
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
		
		// Ist etwas in der Liste ausgewählte
		if(getListe().isSelectionEmpty() == false)
		{
			ID = ((Genre)this.getListe().getSelectedValue()).getID_Genre();
			// ID == 1 darf nicht gelöscht werden (Fehlermeldung)
			if(ID == 1)
				messageErrorUnbekanntAendern();
			else
			{	
				try
				{
					objRSErgebnis = DB.getQuery("SELECT ID_Genre " +
															"FROM Genres " +
															"WHERE ID_Genre = " + ID);
					if(objRSErgebnis.next() && objRSErgebnis.getInt(1) != ID)
						// Der Eintrag bereits existiert.
						messageErrorEintagExistiert();
					else if (optionUeberschreiben() == 0)
					{
						Genre tmpGenre = new Genre(ID, txtDetailsName.getText());
						
						// Eintrag speichern
						ID = objGenres.saveItemInDB(tmpGenre);
						aktuallisiereListe();
							
						//Das eben geänderte Buch selektieren
						index = 0;
						this.getListe().setSelectedIndex(0);
						while(((Genre)this.getListe().getSelectedValue()).getID_Genre() != ID)
							this.getListe().setSelectedIndex(index++);
					}
					
				}
				catch(Exception e)
				{
				}
			}
		}
	}
	
	public void loesche()
	{
		int ID;
		ResultSet objRSErgebnis;

		// Ist etwas in der Liste ausgewählte
		if(getListe().isSelectionEmpty() == false)
		{
			ID =((Genre)this.getListe().getSelectedValue()).getID_Genre();
			
			// ID == 1 darf nicht gelöscht werden (Fehlermeldung)
			if(ID == 1)
				messageErrorUnbekanntAendern();
			// Buch löschen bestättigen
			else if (optionLoeschen() == 0)
			{
				try
				{
					// Prüfen, ob der Eintag benötigt wird
					objRSErgebnis = DB.getQuery("SELECT ID_Genre " +
															"FROM Buecher " +
															"WHERE ID_Genre = " + ID);
					
					// Ja: Fehlermeldung
					if(objRSErgebnis.next())
						messageErrorAbhaengigkeit();
					// Nein: Löschen
					else
					{
						objGenres.removeItemInDB((Genre)this.getListe().getSelectedValue());
						aktuallisiereListe();
					}
				}
				catch(Exception e)
				{
				}
			}
		}
	}
	
}
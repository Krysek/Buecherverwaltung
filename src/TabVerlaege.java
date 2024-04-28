/*
 * Autor: Christian Frei
 * Datum: 09.06.2004
 */
 
import java.sql.*;
import javax.swing.*;

public class TabVerlaege extends TabPanel
{	
	
	JLabel lblSucheName, lblDetailsName;
	JTextField txtSucheName, txtDetailsName;
	Verlaege objVerlaege;
	
	TabVerlaege(GUI wnd, Verlaege objVerlaege)
	{
		super(wnd, "Verlag");

		this.objVerlaege = objVerlaege;
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
		Verlag tmpVerlag;
		
		tmpVerlag = (Verlag) this.getListe().getSelectedValue();
		txtDetailsName.setText(tmpVerlag.getName());
			
		tmpVerlag = (Verlag) this.getListe().getSelectedValue();		
	}
	
	public void aktuallisiereListe()
	{
		objVerlaege.seekInDB(new Verlag(0, txtSucheName.getText()));
								
		this.getListe().removeAll();
		this.getListe().setListData(objVerlaege.getItems());
		
		if(objVerlaege.getItemCount() > 0)
			this.getListe().setSelectedIndex(0);
	}
	
	public void neu()
	{
		int index = 0;
		
		try
		{
			ResultSet objRSErgebnis = DB.getQuery("SELECT ID_Verlag "+
													"FROM Verlaege " +
													"WHERE ID_Verlag = 0");

			if(objRSErgebnis.next())
				// Fehlermeldung wenn es einen Datensatz
				// mit der ID == 0 existiert
				messageErrorNeuerEintragExistiert();
			else
			{
				// neuen Datensatz erstellen
				objVerlaege.createItemInDB();					
				aktuallisiereListe();
			}
			// Datensatz mit der ID == 0 selektieren
			while(((Verlag)this.getListe().getSelectedValue()).getID_Verlag() != 0)
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
			ID = ((Verlag)this.getListe().getSelectedValue()).getID_Verlag();
			// ID == 1 darf nicht gelöscht werden (Fehlermeldung)
			if(ID == 1)
				messageErrorUnbekanntAendern();
			else
			{	
				try
				{
					objRSErgebnis = DB.getQuery("SELECT ID_Verlag " +
															"FROM Verlaege " +
															"WHERE ID_Verlag = " + ID);
					if(objRSErgebnis.next() && objRSErgebnis.getInt(1) != ID)
						// Der Eintrag bereits existiert.
						messageErrorEintagExistiert();
					else if (optionUeberschreiben() == 0)
					{
						Verlag tmpVerlag = new Verlag(ID, txtDetailsName.getText());
						
						// Eintrag speichern
						ID = objVerlaege.saveItemInDB(tmpVerlag);
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
		// zu löschende ID zu weisen
		int ID;
		ResultSet objRSErgebnis;

		// Ist etwas in der Liste ausgewählte
		if(getListe().isSelectionEmpty() == false)
		{
			ID =((Verlag)this.getListe().getSelectedValue()).getID_Verlag();
			// ID == 1 darf nicht gelöscht werden (Fehlermeldung)
			if(ID == 1)
				messageErrorUnbekanntAendern();
			// Buch löschen bestättigen
			else if (optionLoeschen() == 0)
			{
				try
				{
					// Prüfen, ob der Eintag benötigt wird
					objRSErgebnis = DB.getQuery("SELECT ID_Verlag " +
															"FROM Buecher " +
															"WHERE ID_Verlag = " + ID);
					
					// Ja: Fehlermeldung
					if(objRSErgebnis.next())
					messageErrorAbhaengigkeit();
					// Nein: Löschen
					else
					{
						objVerlaege.removeItemInDB((Verlag)this.getListe().getSelectedValue());
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
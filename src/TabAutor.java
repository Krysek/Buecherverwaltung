/*
 * Autor: Christian Frei
 * Datum: 09.06.2004
 */
 
import java.sql.*;
import javax.swing.*;

public class TabAutor extends TabPanel
{	
	Autoren objAutoren;
	
	JLabel lblSucheName, lblSucheVorname, lblDetailsName,  lblDetailsVorname;
	JTextField txtSucheName,txtSucheVorname, txtDetailsName, txtDetailsVorname;
	
	TabAutor(GUI wnd, Autoren objAutoren)
	{
		super(wnd, "Autor");
		
		this.objAutoren = objAutoren;
		
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
				
		lblDetailsVorname = new JLabel("Vorname:");
		lblDetailsVorname.setSize(55, 20);
		lblDetailsVorname.setLocation(10,50);
		this.getPnlDetails().add(lblDetailsVorname);
		
		txtDetailsVorname = getTextfield(65,50);
		txtDetailsVorname.setDocument(new JTextFieldLimit(20));
		this.getPnlDetails().add(txtDetailsVorname);
	}
	
	// Den Inhalt des Panel Suchen initialisieren
	public void setPnlSuche()
	{

		lblSucheName = new JLabel("Name:");
		lblSucheName.setSize(50, 20);
		lblSucheName.setLocation(10,20);
		this.getPnlSuchen().add(lblSucheName);
		
		lblSucheVorname = new JLabel("Vorname:");
		lblSucheVorname.setSize(55, 20);
		lblSucheVorname.setLocation(277,20);
		this.getPnlSuchen().add(lblSucheVorname);
		
		txtSucheName = getTextfield(65,20);
		txtSucheName.setDocument(new JTextFieldLimit(20));
		this.getPnlSuchen().add(txtSucheName);
		
		txtSucheVorname = getTextfield(340,20);
		txtSucheVorname.setDocument(new JTextFieldLimit(20));
		this.getPnlSuchen().add(txtSucheVorname);
	}

	public void aktuallisiereDetails()
	{
		Autor tmpAutor;
		
		tmpAutor = (Autor) this.getListe().getSelectedValue();
		txtDetailsName.setText(tmpAutor.getName());
		txtDetailsVorname.setText(tmpAutor.getVorname());
			
		tmpAutor = (Autor) this.getListe().getSelectedValue();		
	}
	
	public void aktuallisiereListe()
	{
		int i;
		objAutoren.seekInDB(new Autor(0, txtSucheName.getText(), txtSucheVorname.getText()));
								
		this.getListe().removeAll();
		this.getListe().setListData(objAutoren.getItems());
		
		if(objAutoren.getItemCount() > 0)
			this.getListe().setSelectedIndex(0);
	}
	
	public void neu()
	{
		int index = 0;
		ResultSet objRSErgebnis;
		
		try
		{
			objRSErgebnis = DB.getQuery("SELECT ID_Autor "+
													"FROM Autoren " +
													"WHERE ID_Autor = 0");

			if(objRSErgebnis.next())
				// Fehlermeldung wenn es einen Datensatz
				// mit der ID == 0 existiert
				messageErrorNeuerEintragExistiert();
			else
			{
				// neuen Datensatz erstellen
				objAutoren.createItemInDB();					
				aktuallisiereListe();
			}
			// Datensatz mit der ID == 0 selektieren
			while(((Autor)this.getListe().getSelectedValue()).getID_Autor() != 0)
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
			ID = ((Autor)this.getListe().getSelectedValue()).getID_Autor();
			if(ID == 1)
				messageErrorUnbekanntAendern();
			else
			{	
				try
				{
					objRSErgebnis = DB.getQuery("SELECT ID_Autor " +
															"FROM Autoren " +
															"WHERE ID_Autor = " + ID);
					if(objRSErgebnis.next() && objRSErgebnis.getInt(1) != ID)
						// Der Eintrag bereits existiert.
						messageErrorEintagExistiert();
					else if (optionUeberschreiben() == 0)
					{
						Autor tmpAutor = new Autor(ID, txtDetailsName.getText(),
													txtDetailsVorname.getText());
						
						// Eintrag speichern
						ID = objAutoren.saveItemInDB(tmpAutor);
						aktuallisiereListe();
							
						//Das eben geänderte Buch selektieren
						index = 0;
						this.getListe().setSelectedIndex(0);
						while(((Autor)this.getListe().getSelectedValue()).getID_Autor() != ID)
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

		if(getListe().isSelectionEmpty() == false)
		{
			ID =((Autor)this.getListe().getSelectedValue()).getID_Autor();
			
			// ID == 1 darf nicht gelöscht werden (Fehlermeldung)
			if(ID == 1)
				messageErrorUnbekanntAendern();
			// Buch löschen bestättigen
			else if (optionLoeschen() == 0)
			{
				try
				{
					// Prüfen, ob der Eintag benötigt wird
					objRSErgebnis = DB.getQuery("SELECT ID_Autor " +
															"FROM Buecher " +
															"WHERE ID_Autor = " + ID);
					
					// Ja: Fehlermeldung
					if(objRSErgebnis.next())
						messageErrorAbhaengigkeit();
					// Nein: Löschen
					else
					{
						objAutoren.removeItemInDB((Autor)this.getListe().getSelectedValue());
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
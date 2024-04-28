/*
 * Autor: Christian Frei
 * Datum: 09.06.2004
 */
 
import java.util.*;
import java.sql.*;

/*
 * Autoren verwaltet eine Collection von Autor.
 * Zudem ist sie eine Schnittstelle zur Datenbanktabelle Autoren.
 */
public class Autoren
{
	private Vector objAutoren;
	int nextID;
	
	Autoren()
	{
		objAutoren = new Vector();
	}
	
	private void addItem(Autor objAutor)
	// Autor zur Collection hinzufügen
	{
		objAutoren.addElement(objAutor);
	}
	
	private void removeItem(Autor objAutor)
	// Autor aus Collection entfernen
	{
		objAutoren.remove(objAutor);
	}
	
	private void removeAllItems()
	// Alle Autor-Objekte löschen
	{
		objAutoren.removeAllElements();
	}
	
	public Autor getItemAt(int Index)
	// Autor von Index zurückgeben
	{
		return (Autor) objAutoren.elementAt(Index);
	}
	
	public Vector getItems()
	// Komplette Collection zurückgeben
	{
		return objAutoren;
	}
	
	public int getItemCount()
	// Rückgabe der Anzahl der Objekten
	{
		return objAutoren.size();
	}
	
	public void seekInDB(Autor objAutor)
	{
		int indexAutor, indexVerlag, indexGenre;
		int ID_Autor, ID_Verlag, ID_Genre;
		String strQuery;
	
		removeAllItems();
	
		strQuery = "SELECT ID_Autor, Name, Vorname "+
							"FROM Autoren "+
							"WHERE Name = Name ";
						
		if (!objAutor.getName().equals(""))
			strQuery += "AND Name like '" + objAutor.getName() + "' ";
		if (!objAutor.getVorname().equals(""))
			strQuery += "AND Vorname like '" + objAutor.getVorname() + "' ";
	
	
		strQuery += "ORDER BY Name ASC";
	
		strQuery = strQuery.replace('*','%');
		strQuery = strQuery.replace('?','_');
	
		try
		{
			ResultSet objRSErgebnis = DB.getQuery(strQuery);
		
			while(objRSErgebnis.next())
			{
				indexAutor = 0;
				indexVerlag = 0;
				indexGenre = 0;
			
				addItem(new Autor(objRSErgebnis.getInt(1),
										objRSErgebnis.getString(2),
										objRSErgebnis.getString(3)));
			}
			objRSErgebnis = DB.getQuery("SELECT max(ID_Autor) FROM Autoren");
			objRSErgebnis.next();
			nextID = objRSErgebnis.getInt(1) +1;
		}
		catch(Exception e)
		{
		}				
	}

	public void createItemInDB()
	/*
	 * createItemInDB() fügt einen leeren Datensatz in Bücher mit der 
	 * ID_Verlag = 0 und dem Namen 'Neuer Genre'
	 */
	{
		String strSQL;
		int index;
		
		strSQL = "INSERT INTO Autoren " + 
					"(ID_Autor, Name, Vorname) " +
					"VALUES (0 , 'Neuer Name', 'Neuer Vorname')";

		DB.setUpdate(strSQL);
	}
	
	public int saveItemInDB(Autor objAutor)
	/*
	 * Speicher des Autors
	 * Parameter: Daten des Autors
	 * Rückgabe: ID
	 */
	{
		String strSQL;
		int indexID;
		
		// Wenn die ID == 0 ist wird eine neue ID zugeweisen
		if (objAutor.getID_Autor() == 0)
		{
			// neue ID zuweisen
			objAutor.setID_Autor(nextID);
			// SQL-Statment erstellen
			strSQL = "UPDATE Autoren " +
						"SET ID_Autor = '" + nextID + "', " +
							"Name = '" + objAutor.getName() + "', " +
							"Vorname = '" + objAutor.getVorname() + "' " +
				 		"WHERE ID_Autor = 0";
		}
		else
		{
			// SQL-Statment erstellen
			strSQL = "UPDATE Autoren " +
					"SET Name = '" + objAutor.getName() + "', " +
					"Vorname = '" + objAutor.getVorname() + "' " +
				 "WHERE ID_Autor = " + objAutor.getID_Autor();
		}

		// Update der DB
		DB.setUpdate(strSQL);
		
		// ID zurückgeben
		return objAutor.getID_Autor();
	}
	
	public void removeItemInDB(Autor objAutor)
	
	{
		String strSQL;

		// SQL-Statment erstellen
		strSQL = "DELETE FROM Autoren " +
				 "WHERE ID_Autor= " + objAutor.getID_Autor();
		// Löschen des Eintrags		
		DB.setUpdate(strSQL);
		// Löschen das Objekts
		removeItem(objAutor);
	}
}

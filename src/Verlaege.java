/*
 * Autor: Christian Frei
 * Datum: 09.06.2004
 */
 
import java.util.*;
import java.sql.*;

/*
 * Verlaege verwaltet eine Collection von Verlag.
 * Zudem ist sie eine Schnittstelle zur Datenbanktabelle Verlaege.
 */
public class Verlaege
{
	private Vector objVerlaege;
	int nextID;
	
	Verlaege()
	{
		objVerlaege = new Vector();
	}
	
	private void addItem(Verlag objVerlag)
	// Verlag zur Collection hinzufügen
	{
		objVerlaege.addElement(objVerlag);
	}
	
	private void removeItem(Verlag objVerlag)
	// Verlag aus Collection entfernen
	{
		objVerlaege.remove(objVerlag);
	}
	
	private void removeAllItems()
	// Alle Verlag-Objekte löschen
	{
		objVerlaege.removeAllElements();
	}
	
	public Verlag getItemAt(int index)
	// Verlag aus Collection entfernen
	{
		return (Verlag) objVerlaege.elementAt(index);
	}
	
	public Vector getItems()
	// Verlag aus Collection entfernen
	{
		return objVerlaege;
	}
	
	public int getItemCount()
	// Rückgabe der Anzahl der Objekten
	{
		return objVerlaege.size();
	}

	
	public void seekInDB(Verlag objVerlag)
		{
			int indexAutor, indexVerlag, indexGenre;
			int ID_Autor, ID_Verlag, ID_Genre;
			String strQuery;
		
			removeAllItems();
		
			strQuery = "SELECT ID_Verlag, Name "+
								"FROM Verlaege "+
								"WHERE Name = Name ";
							
			if (!objVerlag.getName().equals(""))
				strQuery = strQuery + "AND Name like '" + objVerlag.getName() + "' ";
		
		
			strQuery = strQuery + "ORDER BY Name ASC";
		
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
				
					addItem(new Verlag(objRSErgebnis.getInt(1),
											objRSErgebnis.getString(2)));
				}
				objRSErgebnis = DB.getQuery("SELECT max(ID_Verlag) FROM Verlaege");
				objRSErgebnis.next();
				nextID = objRSErgebnis.getInt(1) +1;
			}
			catch(Exception e)
			{
			}				
		}
		

	public void createItemInDB()
	/*
	 * createItemInDB() fügt einen leeren Datensatz in Verlaege mit der 
	 * ID_Verlag = 0 und dem Namen 'Neuer Verlag'
	 */
	{
		
		String strSQL;
		strSQL = "INSERT INTO Verlaege " + 
					"(ID_Verlag, Name) " +
					"VALUES (0 , 'Neuer Verlag')";

		DB.setUpdate(strSQL);
	}
	
	public int saveItemInDB(Verlag objVerlag)
	/*
	 * Speicher des Verlags
	 * Parameter: Daten des Verlags
	 * Rückgabe: ID
	 */
	{
		String strSQL;
		int index;
		
		// Wenn die ID == 0 ist wird eine neue ID zugeweisen
		if (objVerlag.getID_Verlag() == 0)
		{
			// neue ID zuweisen
			objVerlag.setID_Verlag(nextID);
			// SQL-Statment erstellen
			strSQL = "UPDATE Verlaege " +
					"SET ID_Verlag = '" + objVerlag.getID_Verlag() + "', " +
					"Name = '" + objVerlag.getName() + "' " +
				 "WHERE ID_Verlag = 0";
		}
		else
		{
			// SQL-Statment erstellen
			strSQL = "UPDATE Verlaege " +
					"SET Name = '" + objVerlag.getName() + "' " +
				 "WHERE ID_Verlag = " + objVerlag.getID_Verlag();
		}
		
		// Update der DB
		DB.setUpdate(strSQL);

		// ID zurückgeben
		return objVerlag.getID_Verlag();
	}
	
	public void removeItemInDB(Verlag objVerlag)
	
	{
		String strSQL;

		// SQL-Statment erstellen
		strSQL = "DELETE FROM Verlaege " +
				 "WHERE ID_Verlag= " + objVerlag.getID_Verlag();

		// Löschen des Eintrags		
		DB.setUpdate(strSQL);
		// Löschen das Objekts
		removeItem(objVerlag);
	}
}

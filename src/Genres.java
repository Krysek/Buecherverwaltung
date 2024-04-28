/*
 * Autor: Christian Frei
 * Datum: 09.06.2004
 */
 
import java.util.*;
import java.sql.*;

/*
 * Genres verwaltet eine Collection von Genre.
 * Zudem ist sie eine Schnittstelle zur Datenbanktabelle Genres.
 */
public class Genres
{

	private Vector objGenres;
	private int nextID;
	Genres()
	{
		objGenres = new Vector();
	}
	
	private void addItem(Genre objGenre)
	// Gerne zur Collection hinzufügen
	{
		objGenres.addElement(objGenre);
	}
	
	private void removeItem(Genre objGenre)
	// Gerne aus Collection entfernen
	{
		objGenres.remove(objGenre);
	}

	private void removeAllItems()
	// Alle Gerne-Objekte löschen
	{
		objGenres.removeAllElements();
	}
	
	public Genre getItemAt(int Index)
	// Gerne aus Collection entfernen
	{
		return (Genre) objGenres.elementAt(Index);
	}
	
	public Vector getItems()
	// Gerne aus Collection entfernen
	{
		return objGenres;
	}
	
	public int getItemCount()
	// Rückgabe der Anzahl der Objekten
	{
		return objGenres.size();
	}
	
	public void seekInDB(Genre objGenre)
		{
			int indexAutor, indexVerlag, indexGenre;
			int ID_Autor, ID_Verlag, ID_Genre;
			String strQuery;
		
			removeAllItems();
		
			strQuery = "SELECT ID_Genre, Name "+
								"FROM Genres "+
								"WHERE Name = Name ";
							
			if (!objGenre.getName().equals(""))
				strQuery = strQuery + "AND Name like '" + objGenre.getName() + "' ";
		
		
			strQuery = strQuery + "ORDER BY Name ASC";
		
			strQuery = strQuery.replace('*','%');
			strQuery = strQuery.replace('?','_');
		
			try
			{
				ResultSet objRSErgebnis = DB.getQuery(strQuery);

				while(objRSErgebnis.next())
					addItem(new Genre(objRSErgebnis.getInt(1),
											objRSErgebnis.getString(2)));
				
				objRSErgebnis = DB.getQuery("SELECT max(ID_Genre) FROM Genres");
				objRSErgebnis.next();
				nextID = objRSErgebnis.getInt(1) +1;
			}
			catch(Exception e)
			{
			}				
		}
		

	public void createItemInDB()
	/*
	 * createItemInDB() fügt einen leeren Datensatz in Genres mit der 
	 * ID_Genre = 0 und dem Namen 'Neuer Genre'
	 */
	{
		String strSQL;
		strSQL = "INSERT INTO Genres " + 
					"(ID_Genre, Name) " +
					"VALUES (0 , 'Neues Genre')";

		DB.setUpdate(strSQL);
	}
	
	public int saveItemInDB(Genre objGenre)
	/*
	 * Speicher des Genres
	 * Parameter: Daten des Genres
	 * Rückgabe: ID
	 */
	{
		String strSQL;
		int index;

		// Wenn die ID == 0 ist wird eine neue ID zugeweisen
      	if (objGenre.getID_Genre() == 0)
      	{
			// neue ID zuweisen
			objGenre.setID_Genre(nextID);
			// SQL-Statment erstellen
			strSQL = "UPDATE Genres " +
					"SET ID_Genre = '" + nextID + "', " +
					"Name = '" + objGenre.getName() + "' " +
				 "WHERE ID_Genre = 0";
      	}
      	else
      	{
			// SQL-Statment erstellen
			strSQL = "UPDATE Genres " +
					"SET Name = '" + objGenre.getName() + "' " +
				 "WHERE ID_Genre = " + objGenre.getID_Genre();
      	}

		// Update der DB
		DB.setUpdate(strSQL);

		// ID zurückgeben
		return objGenre.getID_Genre();
	}
	
	public void removeItemInDB(Genre objGenre)
	
	{
		String strSQL;

		// SQL-Statment erstellen
		strSQL = "DELETE FROM Genres " +
				 "WHERE ID_Genre = " + objGenre.getID_Genre();

		// Löschen des Eintrags		
		DB.setUpdate(strSQL);
		// Löschen das Objekts
		removeItem(objGenre);
	}
}

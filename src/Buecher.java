/*
 * Autor: Christian Frei
 * Datum: 09.06.2004
 */
 
import java.util.*;
import java.sql.*;

/*
 * Buecher verwaltet eine Collection von Buch.
 * Zudem ist sie eine Schnittstelle zur Datenbanktabelle Buecher.
 */
public class Buecher
{

	private Vector objBuecher;
	private Autoren objAutoren;
	private Verlaege objVerlaege;
	private Genres objGenres;
	int nextID;
	
	Buecher()
	{
		objBuecher = new Vector();
	}
	
	public void setAutorenLink(Autoren objAutoren)
	{
		this.objAutoren = objAutoren;
	}
	
	public void setVerlaegeLink(Verlaege objVerlaege)
	{
		this.objVerlaege = objVerlaege;
	}
	
	public void setGenresLink(Genres objGenres)
	{
		this.objGenres = objGenres;
	}
	
	private void addItem(Buch objBuch)
	// Verlag zur Collection hinzufügen
	{
		objBuecher.addElement(objBuch);
	}
	
	private void removeItem(Buch objBuch)
	// Buch aus Collection entfernen
	{
		objBuecher.remove(objBuch);
	}
	
	public void removeAllItems()
	// Alle Buch-Objekte löschen
	{
		objBuecher.removeAllElements();
	}
	
	public Buch getItemAt(int Index)
	// Buch aus Collection entfernen
	{
		return (Buch) objBuecher.elementAt(Index);
	}
	
	public Vector getItems()
	// Buch aus Collection entfernen
	{
		return objBuecher;
	}
	
	public int getItemCount()
	// Rückgabe der Anzahl der Objekten
	{
		return objBuecher.size();
	}
	
	
	public void seekInDB(Buch objBuch)
	{
		int indexAutor, indexVerlag, indexGenre;
		int ID_Autor, ID_Verlag, ID_Genre;
		String strQuery;
		
		removeAllItems();
		
		strQuery = "SELECT Buecher.ID_Buch, Buecher.ISBN, "+
								"Buecher.Titel, Buecher.Anzahl, " + 
								"Autoren.ID_Autor, Verlaege.ID_Verlag, " +
								"Genres.ID_Genre " +
							"FROM Buecher, Autoren, Genres, Verlaege "+
							"WHERE Buecher.ID_Autor = Autoren.ID_Autor AND " +
							"Buecher.ID_Verlag = Verlaege.ID_Verlag AND " +
							"Buecher.ID_Genre = Genres.ID_Genre ";
							
		if (!objBuch.getISBN().equals(""))
			strQuery += "AND Buecher.ISBN like '" + objBuch.getISBN() + "' ";
		if (!objBuch.getTitel().equals(""))
			strQuery += "AND Buecher.Titel like '" + objBuch.getTitel() + "' ";
		if (!objBuch.getAutor().getName().equals(""))
			strQuery += "AND Autoren.Name like '" + objBuch.getAutor().getName() + "' ";
		if (!objBuch.getAutor().getVorname().equals(""))
			strQuery += "AND Autoren.Vorname like '" + objBuch.getAutor().getVorname() + "' ";
		if (!objBuch.getVerlag().getName().equals(""))
			strQuery += "AND Verlaege.Name like '" + objBuch.getVerlag().getName() + "' ";
		if (!objBuch.getGenre().getName().equals(""))
			strQuery += "AND Genres.Name like '" + objBuch.getGenre().getName() + "' ";
		
		strQuery = strQuery + "ORDER BY Buecher.Titel ASC";
		
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
				ID_Autor = objRSErgebnis.getInt(5);
				ID_Verlag = objRSErgebnis.getInt(6);
				ID_Genre = objRSErgebnis.getInt(7);

				int id = objRSErgebnis.getInt(1);
				
				// Durchläuft die Schleife bis der Autor zum Buch gefunden worde
				while(ID_Autor != objAutoren.getItemAt(indexAutor).getID_Autor())
					indexAutor++;
				// Durchläuft die Schleife bis der Verlag zum Buch gefunden worde
				while(ID_Verlag != objVerlaege.getItemAt(indexVerlag).getID_Verlag())
					indexVerlag++;
				// Durchläuft die Schleife bis das Genre zum Buch gefunden worde
				while(ID_Genre != objGenres.getItemAt(indexGenre).getID_Genre())
					indexGenre++;
				addItem(new Buch(id,
										objRSErgebnis.getString(2),
										objRSErgebnis.getString(3),
										objAutoren.getItemAt(indexAutor),
										objVerlaege.getItemAt(indexVerlag),
										objGenres.getItemAt(indexGenre),
										objRSErgebnis.getInt(4)));
			}
			objRSErgebnis = DB.getQuery("SELECT max(ID_Buch) FROM Buecher");
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
	 * ID_Buch = 0, Anzahl = 0 und alle anderen erhalten 'Unbekannt'
	 */
	{
		String strSQL;

		strSQL = "INSERT INTO Buecher " + 
			"(ID_Buch, Titel, ISBN, ID_Autor, ID_Verlag, ID_Genre) " +
			"VALUES (0, 'Unbekannt', " + 
			"'0000000000', " + 1 + ", " +
			1 + ", " + 1 + ");";
		
		DB.setUpdate(strSQL);
		
	}
	
	public void removeItemInDB(Buch objBuch)
	{
		String strSQL;

		// SQL-Statment erstellen
		strSQL = "DELETE FROM Buecher " +
				 "WHERE ID_Buch = " + objBuch.getID_Buch();

		// Löschen des Eintrags		
		DB.setUpdate(strSQL);
		// Löschen das Objekts
		removeItem(objBuch);
	}
	
	public int saveItemInDB(Buch objBuch)
	/*
	 * Speicher des Buchs
	 * Parameter: Daten des Buchs
	 * Rückgabe: ID
	 */
	{
		String strSQL;
		int indexID_Buch;

		// Wenn die ID == 0 ist wird eine neue ID zugeweisen
      	if (objBuch.getID_Buch() == 0)
      	{
			// neue ID zuweisen
			objBuch.setID_Buch(nextID);
			// SQL-Statment erstellen
			strSQL = "UPDATE Buecher " +
					"SET ID_BUCH = " + objBuch.getID_Buch() + ", " +
					"Titel = '" + objBuch.getTitel() + "', " +
					"ISBN = '" + objBuch.getISBN() + "', " +
					"ID_Autor = " + objBuch.getAutor().getID_Autor() + ", " +
					"ID_Verlag = " + objBuch.getVerlag().getID_Verlag() + ", " +
					"ID_Genre = " + objBuch.getGenre().getID_Genre() + ", " +
					"Anzahl = " + objBuch.getAnzahl() + " " +
				 "WHERE ID_Buch = 0";
      	}
      	else
      	{
			// SQL-Statment erstellen
			strSQL = "UPDATE Buecher " +
					"SET Titel = '" + objBuch.getTitel() + "', " +
					"ISBN = '" + objBuch.getISBN() + "', " +
					"ID_Autor = " + objBuch.getAutor().getID_Autor() + ", " +
					"ID_Verlag = " + objBuch.getVerlag().getID_Verlag() + ", " +
					"ID_Genre = " + objBuch.getGenre().getID_Genre() + ", " +
					"Anzahl = " + objBuch.getAnzahl() + " " +
				 "WHERE ID_Buch = " + objBuch.getID_Buch();
      	}

		// Update der DB
		DB.setUpdate(strSQL);

		// ID zurückgeben
		return objBuch.getID_Buch();

	}
}

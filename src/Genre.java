/*
 * Autor: Christian Frei
 * Datum: 09.06.2004
 */

/*
 * Genre enthält alle Infos zu einem Genre
 */
public class Genre
{
	private int ID_Genre;
	private String Name;
	
	Genre()
	{
		this.ID_Genre = 0;
		this.Name = "";		
	}
	
	Genre(int ID_Genre, String Name)
	{
		this.ID_Genre = ID_Genre;
		this.Name = Name;
	}
	
	// set-Methoden
	public void setID_Genre(int ID_Genre)
	{
		this.ID_Genre = ID_Genre;
	}
	
	public void setName(String Name)
	{
		this.Name = Name;
	}
	
	// set-Methoden
	public int getID_Genre()
	{
		return this.ID_Genre;
	}
	
	public String getName()
	{
		return this.Name;
	}
	
	public String toString()
	{
		return this.Name;
	}
}

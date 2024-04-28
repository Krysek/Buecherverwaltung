/*
 * Autor: Christian Frei
 * Datum: 09.06.2004
 */

/*
 * Autor enthält alle Infos zu einem Autor
 */
public class Autor
{
	private int ID_Autor;
	private String Name;
	private String Vorname;
	
	Autor()
	{
		this.ID_Autor = 0;
		this.Name = "";
		this.Vorname = "";
	}
	
	Autor(int ID_Autor, String Name, String Vorname)
	{
		this.ID_Autor = ID_Autor;
		this.Name = Name;
		this.Vorname = Vorname;
	}
	
	// set-Methoden
	public void setID_Autor(int ID_Autor)
	{
		this.ID_Autor = ID_Autor;
	}
	
	public void setName(String Name)
	{
		this.Name = Name;
	}
	
	public void setVorname(String Vorname)
	{
		this.Vorname = Vorname;
	}

	// get-Methoden
	public int getID_Autor()
	{
		return this.ID_Autor;
	}
	
	public String getName()
	{
		return this.Name;
	}
	
	public String getVorname()
	{
		return this.Vorname;
	}
	
	public String toString()
	{
		return this.Name + ", " + this.Vorname;
	}
	
}

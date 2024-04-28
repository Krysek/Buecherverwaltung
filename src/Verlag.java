/*
 * Autor: Christian Frei
 * Datum: 09.06.2004
 */

/*
 * Verlag enthält alle Infos zu einem Verlag
 */
public class Verlag
{
	private int ID_Verlag;
	private String Name;
	
	Verlag()
	{
		this.ID_Verlag = 0;
		this.Name = "";		
		
	}
	
	Verlag(int ID_Verlag, String Name)
	{
		this.ID_Verlag = ID_Verlag;
		this.Name = Name;		
	}
	
	// set-Methoden
	public void setID_Verlag(int ID_Verlag)
	{
		this.ID_Verlag = ID_Verlag;
	}
	
	public void setName(String Name)
	{
		this.Name = Name;
	}
	
	// get-Methoden
	public int getID_Verlag()
	{
		return this.ID_Verlag;
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

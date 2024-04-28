/*
 * Autor: Christian Frei
 * Datum: 09.06.2004
 */

/*
 * Buch enthält alle Infos zu einem Buch
 */
public class Buch
{
	private int ID_Buch;
	private String ISBN;
	private String Titel;
	private Autor objAutor;
	private Verlag objVerlag;
	private Genre objGenre;
	private int Anzahl;
	
	// Konstruktor
	public Buch()
	{
		this.ID_Buch = 0;
		this.ISBN = "";
		this.Titel = "";
		this.objAutor = new Autor();
		this.objVerlag = new Verlag();
		this.objGenre = new Genre();
		this.Anzahl = 0;
	}
	
	public Buch(String ISBN, String Titel,
				Autor objAutor, Verlag objVerlag,
				Genre objGenre,	int Anzahl)
	{
		this.ISBN = ISBN;
		this.Titel = Titel;
		this.objAutor = objAutor;
		this.objVerlag = objVerlag;
		this.objGenre = objGenre;
		this.Anzahl = Anzahl;		
	}
	
	public Buch(String ISBN, String Titel,
				Autor objAutor,
				Verlag objVerlag, Genre objGenre)
	{
		this.ISBN = ISBN;
		this.Titel = Titel;
		this.objAutor = objAutor;
		this.objVerlag = objVerlag;
		this.objGenre = objGenre;
	}
	
	public Buch(int ID_Buch , String ISBN,
				String Titel, Autor objAutor,
				Verlag objVerlag, Genre objGenre,
				int Anzahl)
	{
		this.ID_Buch = ID_Buch;
		this.ISBN = ISBN;
		this.Titel = Titel;
		this.objAutor = objAutor;
		this.objVerlag = objVerlag;
		this.objGenre = objGenre;
		this.Anzahl = Anzahl;
	}
	
	
	// Set-Methoden
	public void setID_Buch(int ID_Buch)
	{
		this.ID_Buch = ID_Buch;
	}
	
	public void setISBN(String ISBN)
	{
		this.ISBN = ISBN;
	}
	
	public void setTitel(String Titel)
	{
		this.Titel = Titel;
	}
	
	public void setAutor(Autor objAutor)
	{
		this.objAutor = objAutor;
	}
	
	public void setVerlag(Verlag objVerlag)
	{
		this.objVerlag = objVerlag;
	}
	
	public void setGenre(Genre objGenre)
	{
		this.objGenre = (objGenre);
	}
	
	public void setAnzahl(int Anzahl)
	{
		this.Anzahl = Anzahl;
	}
	
	
	// get-Methoden
	public int getID_Buch()
	{
		return this.ID_Buch;
	}
	
	public String getISBN()
	{
		return this.ISBN;
	}
	
	public String getTitel()
	{
		return this.Titel;
	}
	
	public Autor getAutor()
	{
		return this.objAutor;
	}
	
	public Verlag getVerlag()
	{
		return this.objVerlag;
	}
	
	public Genre getGenre()
	{
		return this.objGenre;
	}
	
	public int getAnzahl()
	{
		return this.Anzahl;
	}
	
	public String toString()
	{
		return this.Titel;
	}
	
	
}

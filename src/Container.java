/*public class Autoren
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
	{
		objAutoren.removeAllElements();
	}
	
	public Autor getItemAt(int Index)
	// Autor aus Collection entfernen
	{
		return (Autor) objAutoren.elementAt(Index);
	}
	
	public Vector getItems()
	// Verlag aus Collection entfernen
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
				strQuery = strQuery + "AND Name like '" + objAutor.getName() + "' ";
			if (!objAutor.getVorname().equals(""))
				strQuery = strQuery + "AND Vorname like '" + objAutor.getVorname() + "' ";
		
		
			strQuery = strQuery + "ORDER BY Name ASC";
		
			strQuery = strQuery.replace('*','%');
			strQuery = strQuery.replace('?','_');
		
			System.out.println(strQuery);
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
	{
		String strSQL;
		strSQL = "INSERT INTO Autoren " + 
					"(ID_Verlag, Name, Vorname) " +
					"VALUES (0 , 'Neuer Name', 'Neuer Vorname')";

		System.out.println(strSQL);
		DB.setUpdate(strSQL);
	}
	
	public int saveItemInDB(Autor objAutor)
	{
		String strSQL;
		int indexID;
      
		if (objAutor.getID_Autor() == 0)
		{
			objAutor.setID_Autor(nextID);
			strSQL = "UPDATE Autoren " +
					"SET ID_Autor = '" + nextID + "', " +
					"Name = '" + objAutor.getName() + "', " +
					"Vorname = '" + objAutor.getVorname() + "' " +
				 "WHERE ID_Autor = 0";
		}
		else
		{
			strSQL = "UPDATE Autoren " +
					"SET Name = '" + objAutor.getName() + "', " +
					"Vorname = '" + objAutor.getVorname() + "' " +
				 "WHERE ID_Autor = " + objAutor.getID_Autor();
		}

		DB.setUpdate(strSQL);
		
		indexID = 0;
		while (objAutor.getID_Autor() != getItemAt(indexID).getID_Autor())
			indexID++;
		
		objAutoren.setElementAt(objAutor, indexID); 
		
		return objAutor.getID_Autor();
	}
	
	public void removeItemInDB(Autor objAutor)
	
	{
		String strSQL;
		
		strSQL = "DELETE FROM Verlaege " +
				 "WHERE ID_Verlag= " + objAutor.getID_Autor();
		System.out.println("krass");			
		DB.setUpdate(strSQL);
		System.out.println("krass");
		removeItem(objAutor);
	}
}*/
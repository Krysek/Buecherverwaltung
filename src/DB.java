/*
 * Autor: Christian Frei
 * Datum: 09.06.2004
 */
 
import java.sql.*;

/*
 * DB stellt Methoden zur DB-Verbindung zuverfügung
 */
 
public class DB
{
	static Connection con;
   
	public static void setConnection()
	/*
	 * Aufbau einer Verbindung zu DB
	 */
	{
	   try
	   {
		  //Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		  //con = DriverManager.getConnection("jdbc:odbc:Buchverwaltung");
               
                  Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                  //con = DriverManager.getConnection("jdbc:ucanaccess://E://Users//Chris//iCloudDrive//Dokumente//NetBeansProjects//Buecherverwaltung//DB//Buchverwaltung.accdb");
                  con = DriverManager.getConnection("jdbc:ucanaccess://.//DB//Buchverwaltung.accdb");
                  //String databaseURL = "jdbc:ucanaccess://E://Users//Chris//iCloudDrive//Dokumente//NetBeansProjects//Buecherverwaltung//DB//Buchverwaltung.accdb";
                  //con = DriverManager.getConnection(databaseURL);
		  //con = DriverManager.getConnection("jdbc:h2:file:~/Buchverwaltung.mdb");
	   }
	   catch(Exception e)
	   {
		  System.out.println("Fehler beim Aufbau der Connection von Buchverwaltung: " + e);
	   }
	}
	
	public static void setClose()
	/*
	 * Schließen der DB-Verbindung
	 */
	{
	   try
	   {
		  con.close();
	   }
	   catch(Exception e)
	   {
		  System.out.println("Fehler beim Schliessen der Connection von DB: " + e);
	   }
	}
   
	
	public static ResultSet getQuery(String strQuery)
	/*
	 * SQL-Statement an die DB schicken (mit Rückgabe (Select))
	 * Parameter: SQL-Select-Statement
	 * Rückgabewert: Ergebnis des SQL-Select-Statements
	 */
	{
	   try
	   {
			Statement Befehl = con.createStatement();
			ResultSet objResult = Befehl.executeQuery(strQuery);
			return objResult;
	   }
	   catch(Exception e)
	   {
			System.out.println("Fehler bei der Query: " + e);
			return null;
	   }
	}
   
	public static void setUpdate(String strQuery)
	/*
	 * Anfrage an DB SQL-Statement (ohne Rückgabe (Update, Delete usw.))
	 */
	{
	   int intResult;
         
	   try
	   {
		  Statement Befehl = con.createStatement();
		  intResult = Befehl.executeUpdate(strQuery);
	   }
	   catch(Exception e)
	   {
		  System.out.println("Fehler bei Upadate: " + e);
	   }
	}
}

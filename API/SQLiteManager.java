package API;

import java.sql.*;
import java.util.Scanner;

public class SQLiteManager
{

    private Connection connection;
    private String table;

    public SQLiteManager()
    {
        this.table = "UserDatas";
    }

    public void connect()
    {
        connection = null;
        try
        {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:usermanager.sqlite");
        }
        catch ( Exception e )
        {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    public void createUserTable()
    {
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        try
        {
            Statement statement = connection.createStatement();
            String qry = "CREATE TABLE IF NOT EXISTS " + table + " (username VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            statement.executeUpdate(qry);
            statement.close();
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

	
    public static void benutzerLoeschen(String userName) 
{
    try 
    {  
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection(url, username, password);

        PreparedStatement st = connection.prepareStatement("DELETE FROM Table WHERE name = " + userName + ";");
        st.executeUpdate(); 
        connection.close();
    } 
    catch(Exception e) 
    {
        System.out.println(e);
    }
}    
    
    public boolean userExists(String userName)
    {
        String qry = "SELECT * FROM " + table + " WHERE username = '" + userName + "'";
        try {
            ResultSet rs = connection.createStatement().executeQuery(qry);
            if(rs != null) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean resetTable() {
        String qry = "drop table " + table;
        try
        {
            PreparedStatement ps = connection.prepareStatement(qry);
            ps.executeUpdate();
            ps.close();
            createUserTable();
            return true;
        }
        catch (SQLException e)
        {

        }
        return false;
    }

    

    public String getPassword(String UserName)
    {
        String qry = "SELECT * FROM " + table + " WHERE username = '" + UserName + "'";
        try
        {
            ResultSet rs = connection.createStatement().executeQuery(qry);
            while (rs.next())
            {
                return rs.getString("password");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    static void pruefePassWort(String passWortVergabe)
    {
                Scanner eingabe = new Scanner(System.in);//Scanner f??r Eingabe der zu-??berpr??fenden Passw??rter
		System.out.println("Gib Passwort ein!!");
		String passwortPruefung = eingabe.next() ;//Speichern des eingebenen Passwortes
		int i =1;//Z??hlvariable f??r Versuche
		
		while(passWortVergabe.equals(passwortPruefung)==false && i<3)
        		{
				System.out.println("Passwort falsch. Neuer Versuch");
				passwortPruefung = eingabe.next();
				i++;
        		}	
		if (passWortVergabe.equals(passwortPruefung)==true)
        		{
			System.out.println("Zugang gew??hrt");
        		}
		else 
        		{
		System.out.println("Passwort falsch. Zugang verweigert. Wenden Sie sich an den System-Administrator");
        		}
    }

    public static void haupt(String[] args) 
    {
		Scanner passwortEingabe = new Scanner(System.in);//Scanner zur Nutzereingabe
		System.out.println("Vergebe ein Passwort");//Auffordderung zur Passwortvergabe
		String passWortVergabe = passwortEingabe.next();//Speichern des eingebenen Passwortes
		pruefePassWort(passWortVergabe);//Methodenaufruf und ??bergabe des Passwortes
    }
    
    
    

    public boolean registerUser(String userName, String password)
    {
        String qry = "INSERT INTO " + table + "(username, password) VALUES(?, ?)";
        try
        {
            PreparedStatement ps = connection.prepareStatement(qry);
            ps.setString(1, userName);
            ps.setString(2, password);
            ps.executeUpdate();
            ps.close();
            return true;
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
            return false;
        }
    }

    public String getTable()
    {
        return table;
    }

    public Connection getConnection()
    {
        return connection;
    }

}

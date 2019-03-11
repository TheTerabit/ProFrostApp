import java.sql.*;
import javax.swing.*;
public class sqliteConnection {
	Connection conn=null;
	
	public static Connection dbConnector(){
		String path=System.getProperty("user.dir");
		String path3=System.getProperty("java.class.path");

		//JOptionPane.showMessageDialog(null, path+":::"+path3);
		try{
			Class.forName("org.sqlite.JDBC");
			Connection conn=DriverManager.getConnection("jdbc:sqlite:"+path+"\\Resources\\DataBase.db");
			return conn;
		}catch(Exception e){
			//JOptionPane.showMessageDialog(null, "B³¹d po³¹czenia z baz¹ danych"+ e+" "+path);
			String path2=System.getProperty("java.class.path");
			path2 = path2.replace("/ProFrostAppMac.jar","");
			try{
				Class.forName("org.sqlite.JDBC");
				Connection conn=DriverManager.getConnection("jdbc:sqlite:"+path2+"/Resources/DataBase.db");
				return conn;
				}catch(Exception e1){
				JOptionPane.showMessageDialog(null, "B³¹d po³¹czenia z baz¹ danych"+ e+":::"+e1+" "+path2);
				
				return null;
			}
			//return null;
		}
		
		
	}
}

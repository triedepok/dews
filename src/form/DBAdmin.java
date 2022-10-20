/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DBAdmin.java
 *
 * Created on March 19, 2011
 */

package form;
import com.mysql.jdbc.Driver;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;


/**
 *
 * @author trie
 */
public final class DBAdmin {
    private Connection koneksi;
    String folderuser = null;
    
    public DBAdmin(String folder){
        folderuser = folder;
        try{
            koneksi = this.makeConnection();
        }
        catch(SQLException s)
        {
            System.err.println("Ada kesalahan dalam koneksi. Silakan ulangi kembali. \n");
        }
    }
    
    public Connection makeConnection() throws SQLException {
        
        String  _database,_username,_password,_hostname;
        String kelihatan = "trie";
                    
        Properties p1 = new Properties();
        try {
             FileInputStream in = new FileInputStream(folderuser+".config/.gdm-db");
             p1.load(in);
             in.close();
             _database = p1.getProperty("database");
             _username = p1.getProperty("username");
             _password = p1.getProperty("password");
             _hostname = p1.getProperty("hostname");
        }catch (IOException iOException) {
             _database = "";
             _username = "";
             _password = "";
             _hostname = "";
        }
        
        String _hostName = "jdbc:mysql://" + _hostname + ":3306/" + _database;
        if (koneksi == null) {
          
            Driver driver = new Driver();
            // buat koneksi
             koneksi = DriverManager.getConnection(_hostName, _username, _password);
            
         }
         return koneksi;
     }

     public Statement getStatement() throws SQLException{
        return koneksi.createStatement();
     }

     public ResultSet getRS(String query) throws SQLException{
        return getStatement().executeQuery(query);
     }

     public void executeRS(String query) throws SQLException{
        getStatement().execute(query);
     }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package feedparser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author salamk
 */
public class DBConnection {

    String url = "jdbc:derby://localhost:1527/parsfr";
    String driver="org.apache.derby.jdbc.ClientDriver";
    String user = "pars";
    String pwd = "trinitron";

    public DBConnection(){
        
    }

    public Connection getConnection(){

        Connection con = null;
        
        try{
            Class.forName(driver);
        }catch(ClassNotFoundException nfe){
            System.out.println(nfe.getMessage());
        }

        try{

            con = DriverManager.getConnection(url, user, pwd);

        }catch(SQLException sqle){

        }catch(Exception e){
            
        }
        
        return con;
    }


    public void closeConnection(Connection con){
        
        try
        {
            con.close();
        }
        catch(Exception e)
        {
            
        }
    }
    

}

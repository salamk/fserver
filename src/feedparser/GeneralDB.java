/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feedparser;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author salamk
 */
public class GeneralDB {

    private DBConnection dbc;

    public GeneralDB() {
        dbc = new DBConnection();
    }

    public ArrayList searchRecord(String query) {

        ArrayList al = new ArrayList();

        Connection con = dbc.getConnection();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();

            //System.out.println(rs.getFetchSize());
            while (rs.next()) {
                Vector v = new Vector();
                for (int i = 0; i <= colCount - 1; i++) {
                    v.add(rs.getString(i + 1));
                }
                al.add(v);
            }

            dbc.closeConnection(con);

        } catch (SQLException e) {
            //System.out.println(e.getMessage());
            dbc.closeConnection(con);

        } catch (Exception ae) {
            //System.out.println(ae.getMessage());
            dbc.closeConnection(con);
        }

        return al;

    }

    public ArrayList getSingleRowData(String query) {
        ArrayList al = new ArrayList();
        Connection con = dbc.getConnection();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 0; i <= colCount - 1; i++) {
                    al.add(rs.getString(i + 1));
                }
            }

            dbc.closeConnection(con);

        } catch (SQLException e) {
            //System.out.println(e.getMessage());
            dbc.closeConnection(con);

        } catch (Exception ae) {
            //System.out.println(ae.getMessage());
            dbc.closeConnection(con);
        }


        return al;
    }

    public String execute(String query) {

        String success = "";
        Connection con = dbc.getConnection();

        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            success = "OK";
            dbc.closeConnection(con);
        } catch (SQLException e) {
            success = e.getMessage();
            dbc.closeConnection(con);
        } 
        
        return success;
    }

    public String getSingleColumnData(String query) {
        String result = "";
        Connection con = dbc.getConnection();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                result = rs.getString(1);
            }

            dbc.closeConnection(con);

        } catch (SQLException e) {
            result+=e.getMessage();
            //System.out.println(e.getMessage());
            dbc.closeConnection(con);

        } catch (Exception ae) {
            result+=ae.getMessage();
            //System.out.println(ae.getMessage());
            dbc.closeConnection(con);
        }

        return result;
    }

    public void loguser(String user, String txt, String winName) {

        String query = "INSERT INTO OPLOG("
                + "OP_NAME, OP_DESCRIPTION, OP_USER)VALUES("
                + "'" + winName + "',"
                + "'" + txt + "',"
                + "'" + user + "')";

        this.execute(query);

    }


     public Vector getSingleRow(String query){
        Vector v = null;
        
        Connection con = dbc.getConnection();

        Statement stmt = null;
        ResultSet rs = null;

        try{
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            int n = rs.getMetaData().getColumnCount();

            while(rs.next()){
                v = new Vector();
                for(int i=0; i<=n-1; i++){
                    v.add(rs.getString(i+1));
                }
            }

            con.close();

        } catch (SQLException e) {
            
            //System.out.println(e.getMessage());
            dbc.closeConnection(con);

        } catch (Exception ae) {
            //System.out.println(ae.getMessage());
            dbc.closeConnection(con);
        }
        
        return v;
    }
     
     public String getCurrentUser(){
         return "system";
     }
     
     public String getCurrentTime(){
         String ttime = this.getSingleColumnData("SELECT NOW()");
         return ttime;
     }
     
     public String getCurrentDate(){
         String ttime = this.getSingleColumnData("SELECT CURDATE()");
         return ttime;
     }

}

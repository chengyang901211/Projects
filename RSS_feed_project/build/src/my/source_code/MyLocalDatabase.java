/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.source_code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyLocalDatabase {

    
    private List<List<String>> data_list = new ArrayList<List<String>>();
    
    public MyLocalDatabase(){
        Connection c = null;
        int i = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cpts323","root", "ying1041");
            Statement database = c.createStatement();
            String query = "select * from locations";
            ResultSet rs = database.executeQuery(query);
            rs.next();
            while(rs.next())
            {
               data_list.add( Arrays.asList(rs.getString("City"), rs.getString("Latitude"),rs.getString("Longitude"))); 
               //rs.getString("City");
               //System.out.print(rs.getString("City")+ "  ");
               //System.out.print(rs.getString("Latitude") + "  ");
               //System.out.println(rs.getString("Longitude") + "  ");
               i++;
            }
        } catch ( Exception e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                System.exit(0);
        }    
    }
   
    public void printDataList(){
       for(List<String> i : data_list){
           System.out.println(i);        
       }
    }
    
    public List<String> searchDataList(String title){      
        int j = 0;
       for(List<String> i : data_list){
           //System.out.println(i.get(0).toString());
           if(title.contains(" " + i.get(0).toString() + " ")){
               //System.out.println(j + " Hi .. " +i);
               return i;
           }
           j++;
       } 
       return null;
    }
}

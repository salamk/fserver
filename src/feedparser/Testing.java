/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedparser;

import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author salam
 */
public class Testing {
    public Testing(){
        String query = "select symbol_code from market_symbol";
        ArrayList<Vector> al = new GeneralDB().searchRecord(query);
        String str ="";
        for(Vector v: al){
            System.out.println((String)v.get(0)+","+"12.45");
        }
        
    }
    
    public static void main(String[] args){
        new Testing();
    }
}

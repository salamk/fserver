/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedparser;

import java.math.BigInteger;
import javax.swing.UIManager;

/**
 *
 * @author salam
 */
public class FeedParser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println(new BigInteger("e", 36));
        try{
            UIManager.setLookAndFeel(new com.jgoodies.looks.plastic.Plastic3DLookAndFeel());
        }catch(Exception e){
            ServerConsole.taText.setText(e.getMessage()+"\n");
        }
        
        
        new ServerConsole().setVisible(true);
        
        
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedparser.parsing;
import java.net.*;
import java.io.*;
/**
 * Write a description of class test here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DownloadZipFile
{
    
    public DownloadZipFile(String urlString, String saveTo, String saveFileName)
    {
         try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            File file = new File(saveTo+saveFileName);
            FileOutputStream out = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int count;
            while ((count = in.read(b)) >= 0) {
                out.write(b, 0, count);
            }
            
            out.flush(); 
            out.close(); 
            in.close();
 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
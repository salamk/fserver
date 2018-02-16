/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedparser.parsing;

import feedparser.CommonTasks;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author salam
 */
public class ServerUpdateTime {
    public ServerUpdateTime(){
        File file = new File("lup.php");
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dt = sdf.format(d);
        
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write("<?php echo \"CMU>"+dt+"\"; ?>");
            
            bw.close();
            this.postFileToServer("lup.php", file);
            
        }catch(Exception e){
        
        }
    }
    
        private boolean postFileToServer(String fileNameOnServer, File fileToUpload) {
        //  System.out.println("Begin posting data to server..");
        //System.out.println("Initializing thread...");

        boolean success = false;

        BufferedReader in = null;
        BufferedInputStream fis = null;
        OutputStream os = null;

        try {
            String url = CommonTasks.getUrlUploadAtomFile();
            String configString = CommonTasks.getBasicConfigString();
            configString += "&filename=" + fileNameOnServer;
            url = url + configString;

            HttpURLConnection httpUrlConnection
                    = (HttpURLConnection) new URL(url).openConnection();

            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestMethod("POST");
            os = httpUrlConnection.getOutputStream();

            fis = new BufferedInputStream(new FileInputStream(fileToUpload));
            int totalByte = fis.available();
            for (int i = 0; i < totalByte; i++) {
                os.write(fis.read());
            }

            os.close();
            in = new BufferedReader(
                    new InputStreamReader(
                            httpUrlConnection.getInputStream()));

            String s = null;
            while ((s = in.readLine()) != null) {
                if (s.startsWith("ELN>")) {
                    s = s.replaceAll("ELN>", "");
                    System.out.println(s);
                }

            }
            in.close();
            fis.close();

            success = true;

        } catch (MalformedURLException me) {
            System.out.println(me.getMessage());
        } catch (ProtocolException pe) {
            System.out.println(pe.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } finally {
            try {
                in.close();
                fis.close();
                os.close();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

        return success;
    }

}

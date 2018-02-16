/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedparser.parsing;

import feedparser.CommonTasks;
import feedparser.GeneralDB;
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
import java.util.ArrayList;
import java.util.Vector;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

/**
 *
 * @author salam
 */
public class DisStAtom implements Runnable {

    private String maxDate;
    private String outputFolder;
    private static final String zip_password = "TpbWvajk@Fy07L?o7GRkyQ&I323jxmds32$3#dsb";

    public DisStAtom(int days, String outputFolder) {
        File folder = new File(outputFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String query = "select distinct event_date from sdata order by "
                + "event_date desc";
        ArrayList<Vector> al = new GeneralDB().searchRecord(query);
        ArrayList <String> dateList = new ArrayList<String>();
        
        for(Vector v: al){
            dateList.add((String)v.get(0));
          //  System.out.println("Dates returned: "+(String)v.get(0));
        }
        
        this.maxDate = dateList.get(days);
        this.outputFolder = outputFolder;
        
    }

    public void run() {
            boolean created = makeFile();
            if (created) {
               // System.out.println("File: " + "static" + ".csv.zip created successfully");
            }else{
                System.out.println("Error in uploading...");
            }
    }

    private boolean makeFile() {
        boolean created = false;
        String query = "select price, volume, event_time from sdata where "
                + "event_date > '" + maxDate + "' "
                + "order by event_date desc";
        
        ArrayList<Vector> al = new GeneralDB().searchRecord(query);
        if (al.size() == 0) {
            System.out.println("Zero results returned");
        } else {
            String sep = System.getProperty("file.separator");
            File file = new File(outputFolder + sep + "static" + ".csv");
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                for (Vector v : al) {
                    String price = (String) v.get(0);
                    String volume = (String) v.get(1);
                    String eventTime = (String) v.get(2);

                    String line = price + "," + volume + "," + eventTime;
                    bw.write(line);
                    bw.newLine();
                }
                bw.close();

                String zipFilePath = outputFolder+sep+"static" + ".zip";
                created = true;
                boolean compressed = this.compress(file.getAbsolutePath(),
                        zipFilePath, getPassword());
                if (compressed) {
                    created = true;
                    
//                    File f = new File(zipFilePath);
//                    boolean posted = this.postFileToServer(f.getName(), f);
//                    if(posted){
                       System.out.println("File: "+file.getName()+" OK /");
                    file.delete();
                    created = true;
//                    }
                    
                }

            } catch (IOException e) {
                System.out.println("Error while creating file: " + file.getAbsolutePath());
            }
        }

        return created;
    }


    private boolean compress(String inputFile, String compressedFile, String password) {
        boolean compressed = false;

        try {
            ZipFile zipFile = new ZipFile(compressedFile);
            File inputFileH = new File(inputFile);
            ZipParameters parameters = new ZipParameters();

            // COMP_DEFLATE is for compression
            // COMp_STORE no compression
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            // DEFLATE_LEVEL_ULTRA = maximum compression
            // DEFLATE_LEVEL_MAXIMUM
            // DEFLATE_LEVEL_NORMAL = normal compression
            // DEFLATE_LEVEL_FAST
            // DEFLATE_LEVEL_FASTEST = fastest compression
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);
            // Set encryption method
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
            // Set key strength
            parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
            parameters.setEncryptFiles(true);

            parameters.setPassword(password);
            // file compressed
            zipFile.addFile(inputFileH, parameters);

         //   long uncompressedSize = inputFileH.length();
        //    File outputFileH = new File(compressedFile);
         //   long comrpessedSize = outputFileH.length();

            //System.out.println("Size "+uncompressedSize+" vs "+comrpessedSize);
            //double ratio = (double) comrpessedSize / (double) uncompressedSize;
           // System.out.println("File compressed with compression ratio : " + ratio);
            compressed = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return compressed;
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


    private String getPassword() {
        String password = "";
        CommonTasks ct = new CommonTasks();
        String b64p = ct.encodeBase64(zip_password);
        password = ct.getMd5(b64p);
       // System.out.println("The P-Hash is: " + password);
        return password;
    }

}

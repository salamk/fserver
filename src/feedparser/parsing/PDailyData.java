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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

/**
 *
 * @author salam
 */
public class PDailyData implements Runnable {

    private static final String zip_password = "TpbWvajk@Fy07L?o7GRkyQ&I323jxmds32$3#dsb";
    private static final String csv_outputFolder = "cdata/";
    private static final String zip_outputFolder = "cdata/";

    public PDailyData() {

    }

    public void run() {
        prepareZipFiles();
       // doUploading();
    }

    private void doUploading() {
        File folder = new File(zip_outputFolder);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String fileName = listOfFiles[i].getName();
                if(fileName.endsWith(".zip")){
                    boolean uploaded = this.postFileToServer(fileName, listOfFiles[i]);
                    if(uploaded){
                        System.out.println("Done-Uploading: "+fileName);
                    }else{
                        System.out.println("ERROR IN UPLOADING: "+fileName);
                    }
                }
            } 
        }

    }

    private void prepareZipFiles() {
        Date d = new Date();
        System.out.println("Today date is: " + d.toString());

        //System.out.println("Preparing encryption key..");
        String password = getPassword();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String of = sdf.format(d);
        //System.out.println("The value of OF Var is: " + of);

        //System.out.println("Preparing buffered reader..");
        BufferedWriter bw = null;
        //System.out.println("Buffered reader is set to null");

        System.out.println("===== Getting symbol list..");
        ArrayList<String> symbolList = getSymbolList();
        int size = symbolList.size();
        System.out.println("===== Daemon returned symbol list of size: " + size);

//        System.out.println("Preparing output directories..");
//        String dir = System.getProperty("user.home");
//        String sep = System.getProperty("file.separator");
//        dir = dir + sep + "Parser" + sep + "Output" + sep + of;
//        System.out.println("The dir path is: " + dir);
//
//        System.out.println("Checking if dir path exists..");
        File file = new File(csv_outputFolder);
        File zfile = new File(zip_outputFolder);

        if (!file.exists()) {
            file.mkdirs();
        }

        if (!zfile.exists()) {
            zfile.mkdirs();
        }

        // System.out.println("Initializing iterators...");
        int count = 0;
        // System.out.println("Count variable is set to zero..");

        for (String symbol : symbolList) {
            try {

                //System.out.println("===== "+symbol + ": Preparing file..");
                File f = new File(csv_outputFolder + symbol + ".csv");
                //System.out.println("===== File path is: " + f.getAbsolutePath());
                //System.out.println("===== Preparing buffered reader");
                bw = new BufferedWriter(new FileWriter(f));
                //System.out.println("===== B.Reader is set on FW over " + f.getName());
               // System.out.println("===== Querying database..");

                String query = "select ddate,dopen, dhigh,"
                        + "dlow, dclose, dvolume from daily_quote where "
                        + "dsymbol like '" + symbol + "'"
                        + " order by ddate desc";

                ArrayList<Vector> al = new GeneralDB().searchRecord(query);
              //  System.out.println("===== engine returned with record size " + al.size());

                //System.out.println("===== Preparing..");
                int c = 0;
                for (Vector v : al) {

                    String ddate = (String) v.get(0);
                    String dopen = (String) v.get(1);
                    String dhigh = (String) v.get(2);
                    String dlow = (String) v.get(3);
                    String dclose = (String) v.get(4);
                    String dvolume = (String) v.get(5);

                    String line = ddate + "," + dopen + "," + dhigh + ","
                            + "" + dlow + "," + dclose + "," + dvolume;

                    c++;
                    //   System.out.println("===== ===== Writing line: " + c);
                    bw.write(line);
                    bw.newLine();
                    // System.out.println("===== ===== Writing line: " + c + " finished successfully");
                }

                //System.out.println("===== Closing BW Object ");
                bw.close();
                count++;
                //System.out.println("===== Record operation " + count + " finished successfully");

                //System.out.println("===== Preparing compression operation..");
                File symbolZipFile = new File(zip_outputFolder + symbol + ".zip");
              //  System.out.println("===== Compressing: File Name: " + symbolZipFile.getName());

                //System.out.println("Forwarding to compressor engine...");
                boolean compressed = this.compress(csv_outputFolder + symbol + ".csv",
                        symbolZipFile.getAbsolutePath(), password);

                //System.out.println("===== Engine returned value");
                boolean written = false;

                if (compressed) {
                    written = true;
                        System.out.println("===== File: "+symbolZipFile.getName()+" OK/");
                        f.delete();
//                    boolean uploaded = this.postFileToServer(symbol+".zip", symbolZipFile);
//                    if(uploaded){
//                        System.out.println("===== File: "+symbolZipFile.getName()+" uploaded successfully");
//                    }else{
//                        System.out.println("===== ERROR IN UPLOADING");
//                        System.out.println("===== Program is unable to continue..");
//                        return;
//                    }
                } else {
                    written = false;
                    System.out.println("===== Compressing engine emitted FAILURE signal..");
                    System.out.println("===== Program is unable to continue ...");
                    return;
                    //this.postFileToServer(symbolZipFile.getName(), symbolZipFile);
                }

            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    bw.close();
                } catch (IOException ioe) {
                }
            }

        }

    }

    private String getPassword() {
        String password = "";
        CommonTasks ct = new CommonTasks();
        String b64p = ct.encodeBase64(zip_password);
        password = ct.getMd5(b64p);
        // System.out.println("The P-Hash is: " + password);
        return password;
    }

    private boolean postFileToServer(String fileNameOnServer, File fileToUpload) {
        System.out.println("Begin posting data to server..");
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

    private boolean compress(String inputFile, String compressedFile, String password) {
        boolean compressed = false;

        try {
            ZipFile zipFile = new ZipFile(compressedFile);
            File inputFileH = new File(inputFile);
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);
            // Set encryption method
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
            // Set key strength
            parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
            parameters.setEncryptFiles(true);

            parameters.setPassword(password);
            // file compressed
            zipFile.addFile(inputFileH, parameters);

            long uncompressedSize = inputFileH.length();
            File outputFileH = new File(compressedFile);
            long comrpessedSize = outputFileH.length();

            //System.out.println("Size "+uncompressedSize+" vs "+comrpessedSize);
            double ratio = (double) comrpessedSize / (double) uncompressedSize;
            //System.out.println("File compressed with compression ratio : " + ratio);
            compressed = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return compressed;
    }

    private ArrayList<String> getSymbolList() {

        ArrayList<String> symbolList = new ArrayList<>();
        String query = "select symbol_code from market_symbol";
        ArrayList<Vector> al = new GeneralDB().searchRecord(query);

        for (Vector v : al) {
            symbolList.add((String) v.get(0));
        }

        return symbolList;
    }

}

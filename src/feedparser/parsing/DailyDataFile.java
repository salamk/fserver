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
import java.util.Iterator;
import java.util.Vector;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

/**
 *
 * @author salam
 */
public class DailyDataFile {

    private static final String zip_password = "TpbWvajk@Fy07L?o7GRkyQ&I323jxmds32$3#dsb";

    public DailyDataFile(Date[] d, String outputFolder) {
        for (int i = 0; i <= d.length - 1; i++) {
            makeDailyFile(d[i], outputFolder);
        }
    }

    public DailyDataFile(Date d, String outputFolder) {
        makeDailyFile(d, outputFolder);
    }

    private boolean makeDailyFile(Date d, String outputFolder) {
        boolean compressed = false;
        String date = new SimpleDateFormat("yyyy-MM-dd").format(d);
        String query = "select dsymbol, dopen, dhigh, dlow,"
                + "dclose, dvolume, dchange, dpchange, dmcap, (dhigh-dlow) as hldiff "
                + "from daily_quote where ddate = '" + date + "'";

        ArrayList al = new GeneralDB().searchRecord(query);
        if (al.isEmpty()) {
            System.out.println("DB Engine pumped zero results");
            return false;
        }

        Iterator i = al.iterator();

        File folder = new File(outputFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String sep = System.getProperty("file.separator");
        File file = new File(folder + sep + "D_" + date + ".csv");

        if (file.exists()) {
            System.out.println("File already exists in pool container..");
            return false;
        }

        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter(file));
            while (i.hasNext()) {
                Vector v = (Vector) i.next();
                String symbol = (String) v.get(0);
                String open = (String) v.get(1);
                String high = (String) v.get(2);
                String low = (String) v.get(3);
                String close = (String) v.get(4);
                String volume = (String) v.get(5);
                String change = (String) v.get(6);
                String pchange = (String) v.get(7);
                String mcap = (String) v.get(8);
                String hldiff = (String) v.get(9);

                String sector = getSymbolSector(symbol);
                String category = getSymbolCategory(symbol);

                if (sector == null || category == null
                        || sector.length() < 3 || category.length() < 3) {

                } else {

                    String line = symbol + "," + open + "," + high + "," + low + "," + close + ","
                            + "" + volume + "," + change + "," + pchange + "," + mcap + "," + hldiff + ","
                            + "" + sector + "," + category;
                    bw.write(line);
                    bw.newLine();
                }
            }

            bw.close();

            String zipFilePath = outputFolder + sep + "D_" + date + ".zip";
            compressed = this.compress(file.getAbsolutePath(),
                    zipFilePath, getPassword());
            if (compressed) {
                System.out.println(file.getAbsolutePath() + "...Compressed: OK");
                file.delete();
            }

        } catch (IOException ioe) {

        } finally {
            try {
                bw.close();
            } catch (IOException e) {
            }
        }

        return compressed;

    }
    
    
    

    private String getSymbolSector(String scode) {
        String sector = "";
        String query = "select symbol_sector from market_symbol where "
                + "symbol_code like '" + scode + "'";
        sector = new GeneralDB().getSingleColumnData(query);

        return sector;
    }

    private String getSymbolCategory(String scode) {
        String cat = "";

        String query = "select symbol_category from market_symbol where "
                + "symbol_code like '" + scode + "'";
        cat = new GeneralDB().getSingleColumnData(query);
        return cat;
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

    public static void main(String[] args) {
        String dt = "2016-05-20";
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dt);
        } catch (Exception e) {
        }
        DailyDataFile d = new DailyDataFile(date, "DFiles");
    }

}

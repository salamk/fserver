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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

/**
 *
 * @author salam
 */
public class CatScript implements Runnable{

    private static final String zip_password = "TpbWvajk@Fy07L?o7GRkyQ&I323jxmds32$3#dsb";

    public CatScript() {

    }
    
    public void run(){
        createCatScript();
    }

    private void createCatScript() {

//        tfCatScript.setText("Categorization - Please wait...");
//        tfCatScript.update(tfCatScript.getGraphics());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date dt = new Date();
        File outputfile = new File("cat.csv");
        BufferedWriter bw = null;

        try {

            bw = new BufferedWriter(new FileWriter(outputfile));
            String query = "select symbol_code, symbol_name, symbol_sector, symbol_category,"
                    + "symbol_short_name from market_symbol order by symbol_sector";
            GeneralDB gdb = new GeneralDB();
            ArrayList<Vector> al = gdb.searchRecord(query);
            for (Vector v : al) {
                String scode = (String) v.get(0);
                String sname = (String) v.get(1);
                String ssector = (String) v.get(2);
                String scategory = (String) v.get(3);
                String sshort = (String) v.get(4);

                String line = scode + "," + sname + "," + ssector + "," + scategory + "," + sshort;
                bw.write(line);
                bw.newLine();
            }

//            tfCatScript.setText("TL - Please wait...");
//            tfCatScript.update(tfCatScript.getGraphics());
            String q2 = "select max(ddate) from daily_quote";
            String maxDate = gdb.getSingleColumnData(q2);

            String q3 = "select dsymbol, dmcap from daily_quote "
                    + "where ddate = '" + maxDate + "' "
                    + "order by dmcap desc "
                    + " FETCH FIRST 30 ROWS ONLY";

            //System.out.println(q3);
            ArrayList<Vector> al3 = gdb.searchRecord(q3);

            for (Vector v : al3) {
                String symbol = (String) v.get(0);
                String q4 = "select symbol_name, symbol_short_name from "
                        + "market_symbol where symbol_code like '" + symbol + "'";
                //System.out.println(q4);
                Vector vec = gdb.getSingleRow(q4);

                if (vec == null) {

                } else {

                    String scode = symbol;
                    String sname = (String) vec.get(0);
                    String ssector = "Last Day";
                    String scategory = "Top-30";
                    String sshort = (String) vec.get(1);
                    String line = scode + "," + sname + "," + ssector + "," + scategory + "," + sshort;
                    bw.write(line);
                    bw.newLine();
                }
            }

//            tfCatScript.setText("T10 - Please wait...");
//            tfCatScript.update(tfCatScript.getGraphics());
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            Date mdate = null;
            try {
                mdate = sdf2.parse(maxDate);
            } catch (ParseException pe) {
                System.out.println(pe.getMessage());
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(mdate);
            cal.add(Calendar.DAY_OF_MONTH, -10);
            String ndate = sdf2.format(cal.getTime());

            String q4 = "select dsymbol, SUM(dmcap)as cap from daily_quote "
                    + "where ddate > '" + ndate + "' "
                    + "and dsymbol not like '%-%' "
                    + "group by dsymbol "
                    + "order by cap desc "
                    + "FETCH FIRST 30 ROWS ONLY";
            //System.out.println(q4);
            ArrayList<Vector> al4 = gdb.searchRecord(q4);
            for (Vector v : al4) {
                String symbol = (String) v.get(0);
                String q5 = "select symbol_name, symbol_short_name from "
                        + "market_symbol where symbol_code like '" + symbol + "'";
                Vector vec = gdb.getSingleRow(q5);

                if (vec == null) {

                } else {

                    String scode = symbol;
                    String sname = (String) vec.get(0);
                    String ssector = "Last 10-days";
                    String scategory = "Top-30";
                    String sshort = (String) vec.get(1);
                    String line = scode + "," + sname + "," + ssector + "," + scategory + "," + sshort;

                    bw.write(line);
                    bw.newLine();
                }
            }

//            tfCatScript.setText("T30 - Please wait...");
//            tfCatScript.update(tfCatScript.getGraphics());
            cal.setTime(mdate);
            cal.add(Calendar.DAY_OF_MONTH, -30);
            ndate = sdf2.format(cal.getTime());

            String qv = "select dsymbol, SUM(dmcap)as cap from daily_quote "
                    + "where ddate > '" + ndate + "' "
                    + "and dsymbol not like '%-%' "
                    + "group by dsymbol "
                    + "order by cap desc "
                    + "FETCH FIRST 30 ROWS ONLY";
            //System.out.println(qv);

            ArrayList<Vector> qvl = gdb.searchRecord(qv);
            for (Vector v : qvl) {
                String symbol = (String) v.get(0);
                String q = "select symbol_name, symbol_short_name from "
                        + "market_symbol where symbol_code like '" + symbol + "'";
                Vector vec = gdb.getSingleRow(q);

                if (vec == null) {
                } else {

                    String scode = symbol;
                    String sname = (String) vec.get(0);
                    String ssector = "Last 30-days";
                    String scategory = "Top-30";
                    String sshort = (String) vec.get(1);
                    String line = scode + "," + sname + "," + ssector + "," + scategory + "," + sshort;
                    bw.write(line);
                    bw.newLine();
                }
            }

            bw.close();

//            tfCatScript.setText("Compressing - Please wait...");
//            tfCatScript.update(tfCatScript.getGraphics());
            boolean written = false;
            File compressedCatFile = new File("cat.csv.zip");
            boolean compressed = this.compress(outputfile.getAbsolutePath(),
                    compressedCatFile.getAbsolutePath(), getPassword());

            if (compressed) {

                String fileNameOnServer = "cat.csv.zip";
                this.postFileToServer(fileNameOnServer, compressedCatFile);

                written = true;
//                tfCatScript.setText("OK");
//                tfCatScript.update(tfCatScript.getGraphics());
            } else {
                written = false;
//                tfCatScript.setText("ERROR");
//                tfCatScript.update(tfCatScript.getGraphics());

            }

        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } finally {
            try {
                bw.close();
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }

        }
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedparser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

/**
 *
 * @author salam
 */
public class CommonTasks {

    private static final String manager_name = "salamk";
    private static final String manager_password = "axd44ghdklT32TTF643KLPI89fea";
    private static final String manager_pin = "ThisWorldIsDesertedNow";
    private static final String url_create_user = "http://www.coolmarch.net/cmcterm.php?configstr=";
    private static final String url_new_request_register = "http://www.coolmarch.net/cmctermcfu.php?configstr=";
    private static final String url_upload_atom_file = "http://www.coolmarch.net/cmuzf.php?configstr=";
    private static final String url_logger_script = "http://www.coolmarch.net/cmctermlogger.php?configstr=";
    private static final String zip_password = "TpbWvajk@Fy07L?o7GRkyQ&I323jxmds32$3#dsb";
    private static final String sep = ":HXY:";
    private static final String csv_outputFolder = "cdata/";
    private static final String zip_outputFolder = "cdata/";
    private static final String root_path = "";

    public CommonTasks() {
    }

    public static String getManagerName() {
        return manager_name;
    }

    public static String getBasicConfigString() {

        String configString = "";
        String managerName = CommonTasks.getManagerName();
        String managerPassword = CommonTasks.getManagerPassword();
        String managerPin = CommonTasks.getManagerPin();

        Calendar cal = new GregorianCalendar();
        Date time = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeString = sdf.format(time);

        CommonTasks ct = new CommonTasks();
        String managerNameMd5 = ct.getMd5(managerName);
        String managerPasswordMd5 = ct.getMd5(managerPassword);
        String managerPinMd5 = ct.getMd5(managerPin);

        String str = managerNameMd5 + sep + managerPasswordMd5 + sep + ""
                + "" + managerPinMd5 + sep + timeString;

        String encoded = ct.encodeBase64(str);

        configString = ct.getBase64UrlSafe(encoded);

        return configString;

    }

    public static String getCsv_outputFolder() {
        return csv_outputFolder;
    }

    public static String getZip_outputFolder() {
        return zip_outputFolder;
    }

    public static String getUrlUploadAtomFile() {
        return url_upload_atom_file;
    }

    public static String getManagerPassword() {
        return manager_password;
    }

    public static String getManagerPin() {
        return manager_pin;
    }

    public static String getUrlCreateUser() {
        return url_create_user;
    }

    public static String getUrlNewRequestRegister() {
        return url_new_request_register;
    }

    public static String getUrlLoggerScript() {
        return url_logger_script;
    }

    public String getMd5(String string) {
        String str = "";
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(string.getBytes());
            //Get the hash's bytes 
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            str = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return str;
    }

    public String decodeBase64(String str) {
        byte[] decodedValue = Base64.getDecoder().decode(str);
        String originalString = "";
        for (int i = 0; i < decodedValue.length; i++) {
            originalString += (char) decodedValue[i];
        }
        return originalString;
    }

    public String encodeBase64(String str) {
        String encoded = Base64.getEncoder().encodeToString(str.getBytes());
        return encoded;
    }

    public String getBase64UrlSafe(String encodedString) {
        encodedString = encodedString.replaceAll("\\+", "-");
        encodedString = encodedString.replaceAll("/", "_");
        encodedString = encodedString.replaceAll("=", ".");
        return encodedString;
    }

    public String getZipPassword() {
        String password = "";
        CommonTasks ct = new CommonTasks();
        String b64p = ct.encodeBase64(zip_password);
        password = ct.getMd5(b64p);
        // System.out.println("The P-Hash is: " + password);
        return password;
    }

    public boolean postAtomFileToServer(String fileNameOnServer, File fileToUpload) {
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

    public boolean compress(String inputFile, String compressedFile, String password) {
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
    
    
        public boolean compress(String inputFile, String compressedFile) {
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

            parameters.setPassword(this.getZipPassword());
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


    public ArrayList<String> getSymbolList() {

        ArrayList<String> symbolList = new ArrayList<>();
        String query = "select symbol_code from market_symbol";
        ArrayList<Vector> al = new GeneralDB().searchRecord(query);

        for (Vector v : al) {
            symbolList.add((String) v.get(0));
        }

        return symbolList;
    }

}

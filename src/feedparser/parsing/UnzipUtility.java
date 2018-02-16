/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedparser.parsing;

/**
 *
 * @author sania
 */
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This utility extracts files and directories of a standard zip file to a
 * destination directory.
 *
 * @author www.codejava.net
 *
 */
public class UnzipUtility {

    /**
     * Size of the buffer to read/write data
     */
    private static final int BUFFER_SIZE = 4096;

    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified
     * by destDirectory (will be created if does not exists)
     *
     * @param zipFilePath
     * @param destDirectory
     * @throws IOException
     */
    public UnzipUtility() {
    }

    public void unzip(String zipFilePath, String destDirectory) throws IOException {
        // //System.out.println("Unzip Utility called...");
        // //System.out.println("Creating directories...");
//        File destDir = new File(destDirectory);
//        if (!destDir.exists()) {
//            destDir.mkdir();
//        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        extractFile(zipIn, destDirectory);
//        ZipEntry entry = zipIn.getNextEntry();
//        //System.out.println("Zip entry is being read");
//        // iterates over entries in the zip file
//        while (entry != null) {
//            String filePath = destDirectory + File.separator + entry.getName();
//            if (!entry.isDirectory()) {
//                // if the entry is a file, extracts it
//                //System.out.println("DBG-1.....");
//                extractFile(zipIn, filePath);
//            } else {
//                // if the entry is a directory, make the directory
//                //System.out.println("DBG-2.....");
//                File dir = new File(filePath);
//                dir.mkdir();
//            }
//            
//            //System.out.println("DBG-3.....");
//            zipIn.closeEntry();
//            //System.out.println("DBG-4.....");
//            entry = zipIn.getNextEntry();
//            //System.out.println("DBG-5.....");
//        }
//        
//        //System.out.println("DBG-6.....");
//        zipIn.close();
//        //System.out.println("DBG-7..ok.....");
    }

    /**
     * Extracts a zip entry (file entry)
     *
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private void extractFile(ZipInputStream zipIn, String filePath)
            throws IOException {

        //System.out.println("Unzip Utility called...");
        BufferedOutputStream bos
                = new BufferedOutputStream(new FileOutputStream(filePath));
        //System.out.println("Buffered Reader is set on output of " + filePath);
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        //System.out.println("Start reading zip entries...");
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }

        //System.out.println("read all entries successfully...");

        bos.close();
    }

    public void unZipIt(String zipFile, String outputFolder) {

        byte[] buffer = new byte[1024];

        try {

            //create output directory is not exists
            File folder = new File("cache/dailydata/fdg");
            if (!folder.exists()) {
                folder.mkdir();
            }

            //get the zip file content
            ZipInputStream zis
                    = new ZipInputStream(new FileInputStream(zipFile));
            //System.out.println(zis.toString());
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {

                String fileName = ze.getName();
                //System.out.println("The file name is :" + fileName);
                File newFile = new File(outputFolder + File.separator + fileName);

                //System.out.println("file unzip : " + newFile.getAbsoluteFile());

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

            //System.out.println("Done");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void gunzipIt(String zipfile, String zipout) {

        byte[] buffer = new byte[1024];

        try {

            GZIPInputStream gzis
                    = new GZIPInputStream(new FileInputStream(zipfile));

            FileOutputStream out
                    = new FileOutputStream(zipout);

            int len;
            while ((len = gzis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }

            gzis.close();
            out.close();

            //System.out.println("Done");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) { //some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

}

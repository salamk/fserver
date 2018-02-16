/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import net.lingala.zip4j.io.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

/**
 *
 * @author salam
 */
public class ZipContainer {

    ArrayList filesToAdd = new ArrayList();

    public boolean CreateZipWithOutputStream(String sAbsolutePath,
            String outputFilePath, String extension, String password) {

        boolean zipOk = false;

        ZipOutputStream outputStream = null;
        InputStream inputStream = null;

        try {

            ArrayList arrLocal = exploredFolder(sAbsolutePath, extension);

            outputStream = new ZipOutputStream(new FileOutputStream(new File(outputFilePath)));

            ZipParameters parameters = new ZipParameters();

            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

            if (password.compareToIgnoreCase("NO") == 0) {
                ;
            } else {

                parameters.setEncryptFiles(true);

                parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);

                parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);

                parameters.setPassword(password);
            }

            for (int i = 0; i < arrLocal.size(); i++) {
                File file = (File) arrLocal.get(i);

                outputStream.putNextEntry(file, parameters);

                if (file.isDirectory()) {
                    outputStream.closeEntry();
                    continue;
                }

                inputStream = new FileInputStream(file);
                byte[] readBuff = new byte[4096];
                int readLen = -1;

                while ((readLen = inputStream.read(readBuff)) != -1) {
                    outputStream.write(readBuff, 0, readLen);
                }

                outputStream.closeEntry();

                inputStream.close();
            }

            outputStream.finish();
            zipOk = true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return zipOk;
    }

    public ArrayList exploredFolder(String sAbsolutePath, String extension) {
        File[] sfiles;
        File fsSelectedPath = new File(sAbsolutePath);
        sfiles = fsSelectedPath.listFiles();
        if (sfiles == null) {
            return null;
        }
        for (int j = 0; j < sfiles.length; j++) {
            File f = sfiles[j];
            if (f.isDirectory() == true) {
                ;
            } else {
                String name = f.getName();
                if (name.endsWith(extension)) {
                    filesToAdd.add(f);
                }

            }
        }
        return filesToAdd;
    }

    public static void main(String[] args) {
        //new CreateZipWithOutputStream().CreateZipWithOutputStream("c:\\ZipTest");
    }
}

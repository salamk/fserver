/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedparser;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.FilenameUtils;

import java.io.File;

public class Zipper
{ 
    private String password;
    private static final String EXTENSION = "zip";

    public Zipper(String password)
    {
        this.password = password;
    }

    public void pack(String filePath) throws ZipException
    {
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);
        zipParameters.setEncryptFiles(true);
        zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
        zipParameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
        zipParameters.setPassword(password);
        String baseFileName = FilenameUtils.getBaseName(filePath);
        String destinationZipFilePath = baseFileName + "." + EXTENSION;
        ZipFile zipFile = new ZipFile(destinationZipFilePath);
        zipFile.addFile(new File(filePath), zipParameters);
    }

    public void unpack(String sourceZipFilePath, String extractedZipFilePath) throws ZipException
    {
        ZipFile zipFile = new ZipFile(sourceZipFilePath + "." + EXTENSION);

        if (zipFile.isEncrypted())
        {
            zipFile.setPassword(password);
        }
        
        zipFile.extractAll(extractedZipFilePath);
    }
}
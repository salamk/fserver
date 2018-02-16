/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedparser.parsing;

import feedparser.CommonTasks;
import feedparser.GeneralDB;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author salam
 */
public class CMSymbolData {

    public CMSymbolData() {
        CommonTasks ct = new CommonTasks();
        ArrayList<String> symbolList = ct.getSymbolList();
        int size = symbolList.size();
        if (size == 0) {
            System.out.println("Daemon returned list of size zero..");
            System.out.println("Program aborted with error status");
            return;
        }

        File file = new File(CommonTasks.getCsv_outputFolder());
        File zfile = new File(CommonTasks.getZip_outputFolder());

        if (!file.exists()) {
            file.mkdirs();
        }

        if (!zfile.exists()) {
            zfile.mkdirs();
        }

        int count = 0;
        BufferedWriter bw = null;
        for (String symbol : symbolList) {
            try {

                File f = new File(CommonTasks.getCsv_outputFolder() + symbol + ".csv");
                bw = new BufferedWriter(new FileWriter(f));
                String query = "select ddate,dopen, dhigh,"
                        + "dlow, dclose, dvolume from daily_quote where "
                        + "dsymbol like '" + symbol + "'"
                        + " order by ddate desc";

                ArrayList<Vector> al = new GeneralDB().searchRecord(query);
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
                    bw.write(line);
                    bw.newLine();
                }

                bw.close();
                count++;
                File symbolZipFile = new File(CommonTasks.getZip_outputFolder() + symbol + ".zip");
                boolean compressed = ct.compress(CommonTasks.getCsv_outputFolder() + symbol + ".csv",
                        symbolZipFile.getAbsolutePath(), ct.getZipPassword());

                if (compressed) {
                    System.out.println("===== File: " + symbolZipFile.getName() + " OK/");
                    f.delete();
                } else {
                    System.out.println("===== Compressing engine emitted FAILURE signal..");
                    System.out.println("Error while compressing file: "+f.getAbsolutePath());
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
}

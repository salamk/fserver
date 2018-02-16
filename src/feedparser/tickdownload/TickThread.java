/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedparser.tickdownload;

import feedparser.CommonTasks;
import feedparser.GeneralDB;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author salam
 */
public class TickThread implements Runnable {

    private static final String zip_password = "TpbWvajk@Fy07L?o7GRkyQ&I323jxmds32$3#dsb";

    public TickThread() {

    }

    public void run() {

        while (true) {
            System.out.println("Thread awaked, working...");
            HtmlOutput ho = new HtmlOutput();
            ArrayList<String> al = ho.getParsedLines();
            boolean ok = doProcess(al);
            if (ok) {
                boolean zipOk = makeZip();
                if (zipOk) {
                    boolean posted = uploadFile();
                }
            }

            ok = this.makeStaticFile(ho.getExchangeStatus());
            if (ok) {
                
                boolean zipOk = this.makeStaticZip();
                if(zipOk){
                    boolean posted = this.uploadStatic();
                }

            }

            try {
                System.out.println("Getting into sleeping state");
                Thread.sleep(300000);
            } catch (InterruptedException ie) {
                System.out.println(ie.getMessage());
            }
        }
    }

    private boolean uploadFile() {
        CommonTasks ct = new CommonTasks();
        boolean posted = ct.postAtomFileToServer("current.zip", new File("current.zip"));
        return posted;
    }

    private boolean makeZip() {
        String file = "current.csv";
        String zip = "current.zip";

        File zf = new File(zip);
        zf.delete();

        CommonTasks ct = new CommonTasks();
        String pwd = this.getPassword();
        boolean ok = ct.compress(file, zip, pwd);
        if (ok) {
            System.out.println("file compressed successfully");
        } else {
            System.out.println("file compressed failed");
        }

        return ok;
    }

    private boolean makeStaticFile(String text) {
        boolean ok = false;
        BufferedWriter bw = null;
        try {
            File file = new File("exchange.csv");
            bw = new BufferedWriter(new FileWriter(file));
            String[] toke = text.split("\n");
            for (int i = 0; i <= toke.length - 1; i++) {
                bw.write(toke[i]);
                bw.newLine();
            }

            bw.close();
            ok = true;

        } catch (IOException e) {

        }

        return ok;
    }

    private boolean makeStaticZip() {
        String file = "exchange.csv";
        String zip = "exchange.zip";

        File zf = new File(zip);
        zf.delete();

        CommonTasks ct = new CommonTasks();
        String pwd = this.getPassword();
        boolean ok = ct.compress(file, zip, pwd);
        if (ok) {
            System.out.println("file compressed successfully");
        } else {
            System.out.println("file compressed failed");
        }

        return ok;
    }

    private boolean uploadStatic() {
        CommonTasks ct = new CommonTasks();
        boolean posted = ct.postAtomFileToServer("exchange.zip", new File("exchange.zip"));
        return posted;
    }

    private boolean doProcess(ArrayList<String> al) {
        boolean ok = false;
        BufferedWriter bw = null;
        DecimalFormat df = new DecimalFormat("#.##");

        ArrayList<DailyData> ddlist = new ArrayList<>();
        System.out.println("The total parsed lines returned by system is: " + al.size());
        for (String line : al) {
            //System.out.println(line);
            String[] toke = line.split(",");
            DailyData dd = new DailyData();
            dd.setQuoteDate("");
            dd.setSymbol(toke[0]);
            dd.setOpen(Double.parseDouble(toke[1]));
            dd.setHigh(Double.parseDouble((toke[2])));
            dd.setLow(Double.parseDouble(toke[3]));
            dd.setClose(Double.parseDouble(toke[4]));
            dd.setVolume(Double.parseDouble(toke[5]));
            dd.setChange(Double.parseDouble(toke[6]));

            double close = dd.getClose();
            double change = dd.getChange();
            double pchange = (change / close) * 100;
            dd.setPchange(pchange);
            double volume = dd.getVolume();
            double mcap = volume * close;
            dd.setMcap(mcap);
            double high = dd.getHigh();
            double low = dd.getLow();
            double hldiff = high - low;
            dd.setHlDiff(hldiff);

            ddlist.add(dd);
        }

        System.out.println("The substantial list is containing " + ddlist.size() + " #records");

        try {
            File file = new File("current.csv");
            bw = new BufferedWriter(new FileWriter(file));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String dt = sdf.format(date);

            for (DailyData dd : ddlist) {
                String symbol = dd.getSymbol();
                double open = dd.getOpen();
                double high = dd.getHigh();
                double low = dd.getLow();
                double close = dd.getClose();
                double volume = dd.getVolume();
                double change = dd.getChange();
                double pchange = dd.getPchange();
                double mcap = dd.getMcap();
                double hldiff = dd.getHlDiff();

                String sector = getSymbolSector(symbol);
                String category = getSymbolCategory(symbol);

                String line = dt + "," + symbol + "," + df.format(open) + "," + df.format(high) + "," + df.format(low) + ","
                        + "" + close + "," + volume + "," + df.format(change) + "," + df.format(pchange) + ","
                        + "" + df.format(mcap) + "," + df.format(hldiff) + "," + sector + "," + category;

                bw.write(line);
                bw.newLine();
            }

            bw.close();
            ok = true;

        } catch (IOException e) {

            System.out.println(e.getMessage());
            ok = false;

        } finally {
            try {
                bw.close();
            } catch (Exception e) {
            }
        }

        return ok;

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

    private String getPassword() {
        String password = "";
        CommonTasks ct = new CommonTasks();
        String b64p = ct.encodeBase64(zip_password);
        password = ct.getMd5(b64p);
        // System.out.println("The P-Hash is: " + password);
        return password;
    }

}

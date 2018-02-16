/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedparser.parsing;

import feedparser.GeneralDB;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author sania
 */
public class DailyQuoteList extends ArrayList<DailyQuote> {

    
    public DailyQuoteList(String filePath) {
        File file = new File(filePath);
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMMyyyy");
            
            while ((line = br.readLine()) != null) {
                String[] toke = line.split("\\|");
                String dt = toke[0];
                String symbol = toke[1];
                String sectorCode = toke[2];
                String symbolName = toke[3];
                String op = toke[4];
                String hi = toke[5];
                String lo = toke[6];
                String cl = toke[7];
                String vo = toke[8];
                String la = toke[9];

                try {
                    
                    Date d = sdf.parse(dt);
                    double open = Double.parseDouble(op);
                    double high = Double.parseDouble(hi);
                    double low = Double.parseDouble(lo);
                    double close = Double.parseDouble(cl);
                    double volume = Double.parseDouble(vo);
                    double lastDayClose = Double.parseDouble(la);
                    double marketCap = volume * close;
                    double diff = close - lastDayClose;
                    double change = close - open;
                    double pch = (change / close) * 100;
                    double hlDiff = high - low;

                    DailyQuote dq = new DailyQuote();
                    dq.setClose(close);
                    dq.setDate(d);
                    dq.setHigh(high);
                    dq.setLastDayClose(lastDayClose);
                    dq.setLow(low);
                    dq.setOpen(open);
                    dq.setSectorCode(sectorCode);
                    dq.setSymbol(symbol);
                    dq.setSymbolName(symbolName);
                    dq.setVolume(volume);
                    dq.setMarketCap(marketCap);
                    dq.setDiff(diff);
                    dq.setChange(change);
                    dq.setPercentChange(pch);
                    dq.setHlDiff(hlDiff);
                    add(dq);

                } catch (ParseException pe) {
                    //System.out.println(pe.getMessage());
                }

            }
        } catch (FileNotFoundException fe) {
            //System.out.println(fe.getMessage());
        } catch (IOException ioe) {
            //System.out.println(ioe.getMessage());
        }
    }

    public DailyQuoteList(Date date) {
        //File file = new File(filePath);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dt = sdf.format(date);
        String query = "select dsymbol, dname, dopen, dhigh, dlow, dclose, dvolume,"
                + "dchange, dpchange, dmcap from daily_quote where ddate like '" + dt + "'";
        ArrayList al = new GeneralDB().searchRecord(query);
        Iterator i = al.iterator();
        //double la = 0;
        while (i.hasNext()) {
            Vector v = (Vector) i.next();
            String symbol = (String) v.get(0);
            String sectorCode = "";
            String symbolName = (String) v.get(1);
            String op = (String) v.get(1);
            String hi = (String) v.get(2);
            String lo = (String) v.get(3);
            String cl = (String) v.get(4);
            String vo = (String) v.get(5);
            String la = cl;

            try {
                Date d = sdf.parse(dt);
                double open = Double.parseDouble(op);
                double high = Double.parseDouble(hi);
                double low = Double.parseDouble(lo);
                double close = Double.parseDouble(cl);
                double volume = Double.parseDouble(vo);
                double lastDayClose = Double.parseDouble(la);
                double marketCap = volume * close;
                double diff = close - lastDayClose;
                double change = close - open;
                double pch = (change / close) * 100;
                double hlDiff = high - low;

                DailyQuote dq = new DailyQuote();
                dq.setClose(close);
                dq.setDate(d);
                dq.setHigh(high);
                dq.setLastDayClose(lastDayClose);
                dq.setLow(low);
                dq.setOpen(open);
                dq.setSectorCode(sectorCode);
                dq.setSymbol(symbol);
                dq.setSymbolName(symbolName);
                dq.setVolume(volume);
                dq.setMarketCap(marketCap);
                dq.setDiff(diff);
                dq.setChange(change);
                dq.setPercentChange(pch);
                dq.setHlDiff(hlDiff);
                add(dq);
            } catch (Exception e) {

            }
        }
    }

    public DailyQuoteList(String filePath, String noClose) {
        File file = new File(filePath);
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMMyyyy");
            while ((line = br.readLine()) != null) {
                String[] toke = line.split("\\|");
                String dt = toke[0];
                String symbol = toke[1];
                String sectorCode = toke[2];
                String symbolName = toke[3];
                String op = toke[4];
                String hi = toke[5];
                String lo = toke[6];
                String cl = toke[7];
                String vo = toke[8];
                String la = "0.0";

                try {
                    Date d = sdf.parse(dt);
                    double open = Double.parseDouble(op);
                    double high = Double.parseDouble(hi);
                    double low = Double.parseDouble(lo);
                    double close = Double.parseDouble(cl);
                    double volume = Double.parseDouble(vo);
                    double lastDayClose = Double.parseDouble(la);
                    double marketCap = volume * close;
                    double diff = close - lastDayClose;
                    double change = close - open;
                    double pch = (change / close) * 100;
                    double hlDiff = high - low;

                    DailyQuote dq = new DailyQuote();
                    dq.setClose(close);
                    dq.setDate(d);
                    dq.setHigh(high);
                    dq.setLastDayClose(lastDayClose);
                    dq.setLow(low);
                    dq.setOpen(open);
                    dq.setSectorCode(sectorCode);
                    dq.setSymbol(symbol);
                    dq.setSymbolName(symbolName);
                    dq.setVolume(volume);
                    dq.setMarketCap(marketCap);
                    dq.setDiff(diff);
                    dq.setChange(change);
                    dq.setPercentChange(pch);
                    dq.setHlDiff(hlDiff);
                    add(dq);

                } catch (ParseException pe) {
                    //System.out.println(pe.getMessage());
                }

            }
        } catch (FileNotFoundException fe) {
            //System.out.println(fe.getMessage());
        } catch (IOException ioe) {
            //System.out.println(ioe.getMessage());
        }
    }

}

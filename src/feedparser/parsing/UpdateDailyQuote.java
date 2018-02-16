/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedparser.parsing;

import feedparser.GeneralDB;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author salam
 */
public class UpdateDailyQuote implements Runnable {

    private JTextField tfStatus;

    public UpdateDailyQuote(JTextField t1) {
        this.tfStatus = t1;
    }

    public void run() {
        checkUpdateFiles();
    }

    private void checkUpdateFiles() {
        System.out.println("Querying status and Checking for updates, Please wait");
        String query = "select max(ddate) from daily_quote";
        String ddate = new GeneralDB().getSingleColumnData(query);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startdate = null;
        try {
            startdate = sdf.parse(ddate);

        } catch (ParseException pe) {
            System.out.println("No data returned: Please check connection");
            System.out.println(pe.getMessage());
        }
        Date enddate = new Date();

        List<Date> dates = new ArrayList<Date>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startdate);
        calendar.add(Calendar.DATE, 1);
        while (calendar.getTime().before(enddate)) {
            Date result = calendar.getTime();
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                ;
            } else {
                dates.add(result);
            }
            calendar.add(Calendar.DATE, 1);

        }

        if (dates.isEmpty()) {
            System.out.println("The daily data is uptodate");
            tfStatus.setText("OK");
            return;
        } else {

            System.out.println("See below for missing dates");

            Iterator i = dates.iterator();
            while (i.hasNext()) {
                Date d = (Date) i.next();
                String dt = sdf.format(d);
                System.out.println(">>>>> " + dt);
            }

            System.out.println("Preparing to download files...\n");
            for (int c = 0; c <= dates.size() - 1; c++) {
                Date d = (Date) dates.get(c);
                String dt = sdf.format(d);
                dt = dt.replaceAll("-", "");
                System.out.println("Downloading \t>>>>>>>" + dt + "\n");
                String url = "http://psx.com.pk/scripts/communicator.php?f=" + dt + ".lis.Z&l=Hd";
                System.out.println(url);
                DownloadZipFile dzf = new DownloadZipFile(url,
                        "cache/dailydata/", dt + ".zip");
                UnzipUtility uz = new UnzipUtility();

                try {

                    String zipath = "cache/dailydata/" + dt + ".zip";
                    uz.gunzipIt(zipath, "cache/dailydata/" + dt + ".lis");
                    DailyQuoteList dql = new DailyQuoteList("cache/dailydata/" + dt + ".lis", "");
                    updateQuote(dql);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

        }

    }

    private void updateQuote(DailyQuoteList dql) {
        DecimalFormat td = new DecimalFormat("#.##");
        String file_path = "cache/dailydata/tempdata.pfs";
        try {
            PrintWriter writer = new PrintWriter(file_path, "UTF-8");
            Iterator i = dql.iterator();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            while (i.hasNext()) {

                DailyQuote dq = (DailyQuote) i.next();
                String ddate = df.format(dq.getDate());
                String symbol = dq.getSymbol();
                String sname = dq.getSymbolName();
                String open = td.format(dq.getOpen());
                String high = td.format(dq.getHigh());
                String low = td.format(dq.getLow());
                String close = td.format(dq.getClose());
                String ch = td.format(dq.getChange());
                String pch = td.format(dq.getPercentChange());
                String vol = td.format(dq.getVolume());
                String mcap = td.format(dq.getMarketCap());
                String uid = ddate + "-" + symbol;

                String line = ddate + "," + symbol + "," + sname + ","
                        + "" + open + "," + high + "," + low + ""
                        + "," + close + "," + vol + "," + ch + ","
                        + "" + pch + "," + mcap + "," + uid;

                writer.println(line);
            }

            writer.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.out.println("Some error is thrown, Please check input/output of DB-Engine");
        }

        GeneralDB gdb = new GeneralDB();

        String query = "CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE_LOBS_FROM_EXTFILE(null, "
                + "'DAILY_QUOTE', "
                + "'" + "cache/dailydata/tempdata.pfs' , "
                + "',' , '\"' ," + "'UTF-8', 0)";

        //System.out.println(query);
        String msg = gdb.execute(query);

        if (msg.compareToIgnoreCase("ok") == 0) {
            System.out.println("1xdaily record updated\n");
            tfStatus.setText("OK");
        }
    }

}

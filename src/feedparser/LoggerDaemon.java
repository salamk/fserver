/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedparser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author salam
 */
public class LoggerDaemon implements Runnable {

    public LoggerDaemon() {
    }

    @Override
    public void run() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date();
        String ttime = sdf.format(d);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        while (true) {
            String configString = CommonTasks.getBasicConfigString();
            configString = configString + "&ttime=" + ttime;
            ArrayList<String> lines = this.postUrl(CommonTasks.getUrlLoggerScript(), configString, "ELN>");
            if (lines.size() <= 0) {
                String st = CommonText.monosizeLeft(ttime, 25);
                System.out.println(st + ":Server state idle, ready to accept connections..");
            } else {
                for (String line : lines) {
                    line = line.substring(4);
                    String[] toke = line.split(",");
                    String t = toke[0];
                    String m = toke[1];

                    t = CommonText.monosize(t, 25);
                    System.out.println(t + m);
                }
            }

            try {
                Thread.sleep(50000);
                cal.add(Calendar.MILLISECOND, 50000);
                ttime = sdf.format(cal.getTime());

            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    private ArrayList<String> postUrl(String baseUrl, 
            String pvalue, String lineIdentifier) {

        String url = baseUrl;
        url = url + pvalue;
        //System.out.println(url);
        ArrayList lines = new ArrayList<String>();

        try {
            Document doc = Jsoup.connect(url).get();
            Element body = doc.body();
            HtmlToPlainText h = new HtmlToPlainText();
            String plain = h.getPlainText(body);
            String[] toke = plain.split("\n");
            for (int i = 0; i <= toke.length - 1; i++) {
                String line = toke[i];
                if (line.startsWith(lineIdentifier)) {
                    lines.add(line);
                    System.out.println(line);
                }
            }
        } catch (IOException ioe) {

        }

        return lines;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedparser.tickdownload;

import feedparser.GeneralDB;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author salam
 */
public class HtmlOutput {

    private HashMap<String, String> map;
    private ArrayList<String> parsedLines;
    private String exchangeStatus;

    public HtmlOutput() {
        parsedLines = new ArrayList<String>();
        try {
            Document doc = Jsoup.connect("http://www.psx.com.pk/phps/mktSummary.php").get();
            //   String title = doc.title();
            // String str = doc.outerHtml();
            updateExchangeStatics(doc);
            Element table = doc.select("table").get(2); //select the first table.
            HtmlToPlainText h = new HtmlToPlainText();
            String plain = h.getPlainText(table);
            //  System.out.println(plain);
            String[] toke = plain.split("\n");

            System.out.println("Total lines found are: " + toke.length);
            for (int i = 40; i <= toke.length - 1; i++) {
                String line = toke[i];
                // System.out.println(line);
                line = line.replaceAll(",", "");
                if (line.length() < 40) {
                } else if (line.startsWith("SYMBOL")) {
                } else {
                    String[] tk = line.split(" ");
                    int length = tk.length;
                    String volume = tk[length - 1];
                    String change = tk[length - 2];
                    String close = tk[length - 3];
                    String low = tk[length - 4];
                    String high = tk[length - 5];
                    String open = tk[length - 6];
                    String ldcp = tk[length - 7];
                    String name = "";
                    for (int j = 0; j <= length - 8; j++) {
                        name += tk[j] + " ";
                    }

                    name = name.trim();

                    name = name.replaceAll("SPOT", "");
                    name = name.replaceAll("XD", "");
                    name = name.replaceAll("XB", "");
                    name = name.replaceAll("XR", "");

                    //String bpart = name.substring(0, name.len);
                    String symbol = getSymbol(name);
                    // System.out.println(name);
                    if (symbol.compareToIgnoreCase("DBPROBLEM") == 0) {
                        System.out.println("System recieved a signal which means there "
                                + "is \nproblem in db connectivity. Please ensure if the"
                                + "db-engine is up and running"
                        );
                        parsedLines = null;
                        return;
                    }

                    if (symbol.compareToIgnoreCase("NORECORD") == 0) {
                        //System.out.println(">>>>>>>>>>>>>>>>NO RECORD>>>>>>>>>>>>>>>>" + name);
                    } else {
//                        
                        String ln = symbol + "," + open + "," + high + "," + low + "," + close + "," + volume + "," + change + "," + ldcp;
                        parsedLines.add(ln);
                        updatePulseValue(symbol,close);

                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    private void updatePulseValue(String parameter, String value) {
        String script = "http://www.coolmarch.net/"
                + "dorequest.php?cstr=UPLVUKLkdsdf&symbolcode=" + parameter + "&symbolvalue=" + value;
        try{
        URL url = new URL(script);
        URLConnection uc = url.openConnection();
        
            BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                System.out.println(inputLine);
            }
            br.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateExchangeStatics(Document doc) {
        ArrayList<String> lineList = new ArrayList<>();
        Element table = doc.select("table").get(0); //select the first table.
        HtmlToPlainText h = new HtmlToPlainText();
        String plain = h.getPlainText(table);
        // System.out.println(plain);
        String[] toke = plain.split("\n");
        for (int i = 0; i <= toke.length - 1; i++) {
            //   System.out.println("...."+toke[i]);
            String line = toke[i];

            if (line.startsWith("Advanced")) {
                lineList.add(line);
            } else if (line.startsWith("Current")) {
                lineList.add(line);
            } else if (line.startsWith("Declined")) {
                lineList.add(line);
            } else if (line.startsWith("Unchanged")) {
                lineList.add(line);
            } else if (line.startsWith("Total")) {
                lineList.add(line);
            } else if (line.startsWith("Change")) {
                lineList.add(line);
            } else if (line.startsWith("Status:")) {
                lineList.add(line);
            }
        }
        String status = "";

        String item = lineList.get(0);
        item = item.trim();
        item = item.replaceAll("Status: ", "");
        item = item.replaceAll("Volume: ", "");
        item = item.replaceAll("Value : ", "");
        item = item.replaceAll("Trades: ", "");
        item = item.replaceAll(",", "");
        item = item.replaceAll(" ", ",");
        status += item + "\n";

        item = lineList.get(1);
        String value = getEndingValue(item);
        status += "" + value + ",";

        item = lineList.get(7);
        value = getEndingValue(item);
        status += "" + value + ",";

        item = lineList.get(8);
        value = getEndingValue(item);
        status += "" + value + "\n";

        item = lineList.get(2);
        value = getEndingValue(item);
        status += "KSE-100," + value + ",";
        item = lineList.get(10);
        value = getEndingValue(item);
        status += "" + value + "\n";

        item = lineList.get(3);
        value = getEndingValue(item);
        status += "AllShare," + value + ",";
        item = lineList.get(11);
        value = getEndingValue(item);
        status += "" + value + "\n";

        item = lineList.get(4);
        value = getEndingValue(item);
        status += "KSE-30," + value + ",";
        item = lineList.get(12);
        value = getEndingValue(item);
        status += "" + value + "\n";

        item = lineList.get(5);
        value = getEndingValue(item);
        status += "KMI-30," + value + ",";
        item = lineList.get(13);
        value = getEndingValue(item);
        status += "" + value + "\n";

        item = lineList.get(6);
        value = getEndingValue(item);
        status += "All(Islamic)," + value + ",";
        item = lineList.get(14);
        value = getEndingValue(item);
        status += "" + value + "\n";

        exchangeStatus = status;

        System.out.println(status);
    }

    public String getExchangeStatus() {
        return exchangeStatus;
    }

    public void setExchangeStatus(String exchangeStatus) {
        this.exchangeStatus = exchangeStatus;
    }

    private String getEndingValue(String str) {
        String[] toke = str.split(" ");
        return toke[1].trim();
    }

    private String getSymbol(String bpart) {
        String symbol = "";
        String query = "select symbol_code from market_symbol where "
                + "symbol_short_name like '" + bpart + "%'";
        ArrayList al = new GeneralDB().searchRecord(query);

        if (al == null) {
            System.out.println(":The database is probably shutdown because \n"
                    + "The search engine that return symbol for name emitted"
                    + "\npanic signal: "
                    + "");
            return "DBPROBLEM";
        }

        if (al.size() > 1) {
            System.out.println("==========Multiple Records Returned=========" + bpart);
        } else if (al.size() <= 0) {
            symbol = "NORECORD";
        } else {

            Vector v = (Vector) al.get(0);
            symbol = (String) v.get(0);
        }
        return symbol;
    }

    public ArrayList<String> getParsedLines() {
        return parsedLines;
    }

    public static void main(String[] args) {
        new HtmlOutput();
    }
}

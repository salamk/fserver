/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedparser.parsing;
import java.util.Date;

/**
 *
 * @author sania
 */
public class DailyQuote {
    private String sectorCode, symbol, symbolName;
    private Date date;
    private double open, high, low, close, volume, lastDayClose, marketCap, diff;
    private int index;
    private double change, percentChange, hlDiff;

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(double percentChange) {
        this.percentChange = percentChange;
    }

    public double getHlDiff() {
        return hlDiff;
    }

    public void setHlDiff(double hlDiff) {
        this.hlDiff = hlDiff;
    }

    
    public double getDiff() {
        return diff;
    }

    public void setDiff(double diff) {
        this.diff = diff;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(double marketCap) {
        this.marketCap = marketCap;
    }

    public DailyQuote() {
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getLastDayClose() {
        return lastDayClose;
    }

    public void setLastDayClose(double lastDayClose) {
        this.lastDayClose = lastDayClose;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSectorCode() {
        return sectorCode;
    }

    public void setSectorCode(String sectorCode) {
        this.sectorCode = sectorCode;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "DailyQuote{" + "symbol=" + symbol + ", date=" + date + ", "
                + "open=" + open + ", high=" + high + ", low=" + low + ", "
                + "close=" + close + ", volume=" + volume + '}';
    }
    
    
}

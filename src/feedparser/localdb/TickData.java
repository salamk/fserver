/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedparser.localdb;

import feedparser.CommonText;

public class TickData {

    private String symbolCode;
    private String symbolPrice;
    private String symbolVolume;
    private String eventTime;
    private String ddate;
    private String uid;

    public TickData() {
    }

    public TickData(String symbolCode, String symbolPrice, String symbolVolume,
            String eventTime, String ddate, String uid) {

        this.symbolCode = symbolCode;
        this.symbolPrice = symbolPrice;
        this.symbolVolume = symbolVolume;
        this.eventTime = eventTime;
        this.ddate = ddate;
        this.uid = uid;
    }

    public String getSymbolCode() {
        return symbolCode;
    }

    public void setSymbolCode(String symbolCode) {
        this.symbolCode = symbolCode;
    }

    public String getSymbolPrice() {
        return symbolPrice;
    }

    public void setSymbolPrice(String symbolPrice) {
        this.symbolPrice = symbolPrice;
    }

    public String getSymbolVolume() {
        return symbolVolume;
    }

    public void setSymbolVolume(String symbolVolume) {
        this.symbolVolume = symbolVolume;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getDdate() {
        return ddate;
    }

    public void setDdate(String ddate) {
        this.ddate = ddate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String toString() {
        String dt = CommonText.monosize(this.getDdate(), 15);
        String scode = CommonText.monosize(this.getSymbolCode(), 15);
        String sprice = CommonText.monosize(this.getSymbolPrice(), 10);
        String svol = CommonText.monosize(this.getSymbolVolume(), 15);
        String etime = CommonText.monosize(this.getEventTime(), 25);

        String str = dt + scode + sprice + svol + etime;
        return str;
    }

}


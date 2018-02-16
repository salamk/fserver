/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedparser.localdb;

import feedparser.CommonText;

class KData {

    private String lineSymbol;
    private String rate;
    private String volume;

    public KData(String lineSymbol, String rate, String volume) {
        this.lineSymbol = lineSymbol;
        this.rate = rate;
        this.volume = volume;
    }

    public String getLineSymbol() {
        return lineSymbol;
    }

    public void setLineSymbol(String lineSymbol) {
        this.lineSymbol = lineSymbol;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String toString() {

        String sy = CommonText.monosize(this.getLineSymbol(), 10);
        String rt = CommonText.monosize(this.getRate(), 5);
        String vo = CommonText.monosize(this.getVolume(), 10);

        String str = sy + rt + vo;

        return str;

    }

}

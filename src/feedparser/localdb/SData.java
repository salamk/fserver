/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedparser.localdb;

import feedparser.CommonText;

class SData {

    private String rate;
    private String volume;

    public SData(String rate, String volume) {

        this.rate = rate;
        this.volume = volume;
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
        String rt = CommonText.monosize(this.getRate(), 25);
        String vo = CommonText.monosize(this.getVolume(), 12);

        String str = rt + vo;

        return str;

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedparser;

/**
 *
 * @author sania
 */
public class CommonText {

    public CommonText() {
    }

    public static String monosize(String str, int length) {
        int strLength = str.length();
        if (strLength >= length) {
            ;
        } else {
            int wcount = length - strLength;
            for (int i = 0; i <= wcount - 1; i++) {
                str = " " + str;
            }
        }

        return str;
    }

    public static String monosizeLeft(String str, int length) {
        int strLength = str.length();
        if (strLength >= length) {
            ;
        } else {
            int wcount = length - strLength;
            for (int i = 0; i <= wcount - 1; i++) {
                str = str + " ";
            }
        }

        return str;
    }

}

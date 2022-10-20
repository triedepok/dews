/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

/**
 *
 * @author ews
 */
public class SerialNumberRequest {

    public static String encode(String sn, String date, String expired, String name, String email, String dep) throws Exception {

        DecimalFormat formatter = new DecimalFormat("00");
        String hasil_sn = Crypt.stringToHex(formatter.format(sn.length()) + sn);
        String hasil_date = Crypt.stringToHex(formatter.format(date.length()) + date);
        String hasil_expired = Crypt.stringToHex(formatter.format(expired.length()) + expired);
        String hasil_name = Crypt.stringToHex(formatter.format(name.length()) + name);
        String hasil_email = Crypt.stringToHex(formatter.format(email.length()) + email);
        String hasil_dep = Crypt.stringToHex(formatter.format(dep.length()) + dep);
        String hasil_all = hasil_sn + hasil_date + hasil_expired + hasil_name + hasil_email + hasil_dep;
        String hasilencode = Crypt.encrypt(hasil_all);

        return hasilencode;
    }

    public static String[] decode(String value) throws UnsupportedEncodingException, Exception {
        String Crypto = Crypt.decrypt(value);
        String valueDecode = Crypt.hexToString(Crypto);
        String[] hasildecode = new String[6];
        int cc = 0;
        for (int i = 0; i < 6;) {
            int aa = Integer.valueOf(valueDecode.substring(cc, cc + 2));
            hasildecode[i] = valueDecode.substring(cc + 2, cc + 2 + aa);
            cc = (cc + 2 + aa);
            i++;
        }
        return hasildecode;
    }
}

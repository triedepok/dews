/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

/**
 *
 * @author trie
 */
public class Main {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                String folderuser = System.getProperty("user.home")+"/";
                Date now = new Date();
                try {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar cal = Calendar.getInstance();
                    Date date = cal.getTime();
                    cal.setTime(date);
                    cal.add(Calendar.DATE, 6);
                    Date kemudian = cal.getTime();
                    
                    String _database = null, _username = null, _password = null, _hostname = null, _id_ws = null, _token = null;
                    String[] o_token = null;

                    String PROP_FILE = ".config/.gdm-db";
                    Properties p1 = new Properties();
                    try {
                        FileInputStream in = new FileInputStream(folderuser + PROP_FILE);
                        p1.load(in);
                        in.close();

                        _database = p1.getProperty("database");
                        _username = p1.getProperty("username");
                        _password = p1.getProperty("password");
                        _hostname = p1.getProperty("hostname");
                        _id_ws = p1.getProperty("id_ws");
                        _token = p1.getProperty("token");

                        if ((!_token.equals("")) || (_token.length() > 60)) {
                            try {
                                o_token = SerialNumberRequest.decode(_token);
                                String[] header = {"MAC \t: ", "Date \t :", "Expired : ", "Name \t: ", "Email \t: ", "Dep \t: "};
                                System.out.println("\n========= Licence ============");
                                for (int i = 2; i < 6;) {
                                    System.out.println(header[i] + o_token[i]);
                                    i++;
                                }
                                System.out.println("==============================");

                                String EMAC = String.valueOf(IpMac.GetAddress("mac"));
                                String O_MAC = String.valueOf(o_token[0]);

                                if (!EMAC.equals(O_MAC)) {
                                    //JOptionPane.showMessageDialog(null, "Aplikasi terinstall secara ilegal\nhubungi administrator kami\n==============================\n                                     triedepok@gmail.com\n\n\n\n", "EWS Server", JOptionPane.ERROR_MESSAGE);
                                    System.err.println("\n================================= \n"+now+"\nAplikasi terinstall secara ilegal \nhubungi administrator kami \n=======triedepok@gmail.com======= \n");
                                    System.exit(1);
                                }

                            } catch (GeneralSecurityException e) {
                                //JOptionPane.showMessageDialog(null, "Lisensi tidak sesuai !!!", "Error", JOptionPane.ERROR_MESSAGE);
                                System.err.println(now+" Lisensi tidak sesuai !!! \n");
                                System.exit(1);
                            }
                        }

                    } catch (IOException iOException) {
                        _database = "";
                        _username = "";
                        _password = "";
                        _hostname = "";
                        _id_ws = "";
                        _token = "";
                    }

                    if ("".equals(_database)) {
                        p1.setProperty("database", "ews_all");
                    }
                    if ("".equals(_username)) {
                        p1.setProperty("username", "ews_all");
                    }
                    if ("".equals(_password)) {
                        p1.setProperty("password", "bppt_2014");
                    }
                    if ("".equals(_hostname)) {
                        p1.setProperty("hostname", "localhost");
                    }
                    if ("".equals(_id_ws)) {
                        p1.setProperty("id_ws", "1");
                    }

                    if ("".equals(_token)) {
                        String row_token = SerialNumberRequest.encode(IpMac.GetAddress("mac").toString(), dateFormat.format(date).toString(), dateFormat.format(kemudian).toString(), "Tri Miyarno", "triedepok@gmail.com", "BPPT");
                        p1.setProperty("token", row_token);
                    } else {
                        boolean cocok = date.after(dateFormat.parse(o_token[1]));
                        if (cocok == true) {
                            String row_token = SerialNumberRequest.encode(o_token[0], dateFormat.format(date).toString(), o_token[2], o_token[3], o_token[4], o_token[5]);
                            p1.setProperty("token", row_token);
                        }
                    }

                    try {
                        FileOutputStream out;
                        out = new FileOutputStream(folderuser + PROP_FILE);
                        p1.store(out, "Data konfigurasi\ntriedepok@gmail.com\nTri Miyarno");
                    } catch (IOException ex) {
                        System.err.println(now+" Gagal melakukan update counter system. \n");
                    }

                    boolean before = dateFormat.parse(o_token[1]).before(dateFormat.parse(o_token[2]));
                    boolean after = date.before(dateFormat.parse(o_token[2]));
                    if (after == false || before == false) {
                        //JOptionPane.showMessageDialog(null, "Batas pemakaian aplikasi sudah berakhir... \nExpired : " + o_token[2] + "\nName    : " + o_token[3] + "\nEmail     : " + o_token[4] + "\nDept      : " + o_token[5], "Lisensi kedaluarsa", JOptionPane.ERROR_MESSAGE);
                        System.err.println(now+" Batas pemakaian aplikasi sudah berakhir...\n");
                        //System.exit(1);

                        Generate generate = new Generate();
                        generate.pack();
                        generate.setVisible(true);
                        generate.run();
                    }

                    String[] cmd = new String[3];
                    cmd[0] = "/bin/bash";
                    cmd[1] = "-c";
                    cmd[2] = "mkdir " + folderuser + "temps;"
                            + "sudo Dta1xxInit start";

                    try {
                        Process p = Runtime.getRuntime().exec(cmd);
                    } catch (IOException e) {
                        System.err.println(now+" Gagal membuat folder temps \n");
                    }

                    //psi_generate PsiStart = new psi_generate(folderuser);
                    ews_generate EwsStart = new ews_generate(folderuser, _id_ws);
                    try {
                        EwsStart.run();
                    } catch (InterruptedException ex) {
                        System.err.println(now+" Gagal generate data EWS \n");
                    }

                } catch (Exception ex) {
                    System.err.println(now+" Perangkat hardware tidak aktif.\n");
                }

            }
        });

    }
}

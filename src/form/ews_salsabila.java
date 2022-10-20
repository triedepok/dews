/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author trie
 */
public class ews_salsabila {

    private DBAdmin db;
    private Connection conn;
    private ResultSet rs;
    private String query;
    String FolderUser;
    File file;
    String network_name, ews_service_name, psi_service_name;
    int ews_pmt_id, psi_pmt_id, network_id, psi_service_number, ews_service_number, ews_data_id;
    int ews_bitrate, video_bitrate, psi_bitrate, psi_audio_id, psi_video_id;
    int status_player, status_muxer, card_port, stream_port, freq, bitrate_max,conten;
    String card_type, stream_ip,command;
    boolean DtaPlay=true;
    boolean DtaStop=false;
    boolean DtaClean=false;
    int jum_section_trdw,jum_section_tcdw,jum_section_tmdw;
    int playcount = 0;
    ProcessBuilder cmd = new ProcessBuilder();
    
    // Variable Array untuk tabel TRDW
    ArrayList<Integer> package_id = new ArrayList<Integer>();
    ArrayList<Integer> package_id_tmdw = new ArrayList<Integer>();
    ArrayList<Integer> location_type_code = new ArrayList<Integer>();
    ArrayList<Integer> location_type_code_tmdw = new ArrayList<Integer>();
    ArrayList<Integer> section_number = new ArrayList<Integer>();
    ArrayList<Integer> section_number_tmdw = new ArrayList<Integer>();
    ArrayList<Integer> last_section_number = new ArrayList<Integer>();
    ArrayList<Integer> last_section_number_tmdw = new ArrayList<Integer>();
    ArrayList<Integer> disaster_code = new ArrayList<Integer>();
    ArrayList<String> location_code = new ArrayList<String>();
    ArrayList<String> char_location_code = new ArrayList<String>();
    ArrayList<String> char_information_message = new ArrayList<String>();
    // Variable Array untuk TCDW
    ArrayList<Integer> package_id_tcdw = new ArrayList<Integer>();
    ArrayList<Integer> authority_tcdw = new ArrayList<Integer>();
    ArrayList<Integer> disaster_code_tcdw = new ArrayList<Integer>();
    ArrayList<String> char_disaster_code_tcdw = new ArrayList<String>();
    ArrayList<String> char_disaster_position_tcdw = new ArrayList<String>();
    ArrayList<String> char_disaster_date_tcdw = new ArrayList<String>();
    ArrayList<String> char_disaster_characteristic = new ArrayList<String>();
    ArrayList<Integer> section_number_tcdw = new ArrayList<Integer>();
    ArrayList<Integer> last_section_number_tcdw = new ArrayList<Integer>();
    // Lokasi yang telah dipilahkan
    ArrayList<Integer> jumlah_lokasi_1_1 = new ArrayList<Integer>();
    ArrayList<Integer> jumlah_lokasi_1_2 = new ArrayList<Integer>();
    ArrayList<Integer> jumlah_lokasi_1_3 = new ArrayList<Integer>();
    ArrayList<Integer> jumlah_lokasi_2_1 = new ArrayList<Integer>();
    ArrayList<Integer> jumlah_lokasi_2_2 = new ArrayList<Integer>();
    ArrayList<Integer> jumlah_lokasi_2_3 = new ArrayList<Integer>();
    ArrayList<Integer> jumlah_lokasi_3_1 = new ArrayList<Integer>();
    ArrayList<Integer> jumlah_lokasi_3_2 = new ArrayList<Integer>();
    ArrayList<Integer> jumlah_lokasi_3_3 = new ArrayList<Integer>();
    ArrayList<Integer> jumlah_lokasi_4_1 = new ArrayList<Integer>();
    ArrayList<Integer> jumlah_lokasi_4_2 = new ArrayList<Integer>();
    ArrayList<Integer> jumlah_lokasi_4_3 = new ArrayList<Integer>();
    ArrayList<Integer> jumlah_lokasi_5_1 = new ArrayList<Integer>();
    ArrayList<Integer> jumlah_lokasi_5_2 = new ArrayList<Integer>();
    ArrayList<Integer> jumlah_lokasi_5_3 = new ArrayList<Integer>();
    ArrayList<Integer> jumlah_lokasi_6_1 = new ArrayList<Integer>();
    ArrayList<Integer> jumlah_lokasi_6_2 = new ArrayList<Integer>();
    ArrayList<Integer> jumlah_lokasi_6_3 = new ArrayList<Integer>();
    

    public ews_salsabila(String folderuser, FileWriter fw) throws IOException {

        // Ambil variable lokasi folder dari main.java
        FolderUser = folderuser;
        // Jalankan Row database konfigurasi hardware
        //RowDataHardware();
        
        try {
            
            String p;
            String namaFile = FolderUser + "temps/ewsconfig.py";
            file = new File(namaFile);
            
            // Row database untuk konfigurasi
            //RowDataHardware();
            fw = new FileWriter(namaFile);
            p = generate_ews();
            fw.write(p);
            // Close file writer
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(psi_generate.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(psi_generate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        command = "chmod +x " + FolderUser + "temps/ewsconfig.py;"
                  + FolderUser + "temps/ewsconfig.py";
        cmd.command("sh", "-c", command);
        cmd.start();

    }
    
    private void RowDataHardware(){
        db = new DBAdmin(FolderUser);

        // ================KONEKSI UTAMA=========================
        try {
            conn = db.makeConnection();

            // Ambil field dari table ews_config
            query = "select * from ews_config";
            rs = db.getRS(query);
            while (rs.next()) {
                //Global
                network_id = rs.getInt("network_id");
                network_name = rs.getString("network_name");
                psi_bitrate = rs.getInt("psi_bitrate");
                //EWS Data
                ews_service_number = rs.getInt("ews_service_number");
                ews_pmt_id = rs.getInt("ews_pmt_id");
                ews_data_id = rs.getInt("ews_data_id");
                ews_service_name = rs.getString("ews_service_name");
                ews_bitrate = rs.getInt("ews_bitrate");
                //Konten
                psi_service_number = rs.getInt("psi_service_number");
                psi_pmt_id = rs.getInt("psi_pmt_id");
                psi_service_name = rs.getString("psi_service_name");
                video_bitrate = rs.getInt("video_bitrate");
                psi_video_id = rs.getInt("psi_video_id");
                psi_audio_id = rs.getInt("psi_audio_id");
                conten = rs.getInt("conten");
                //Status player
                status_player = rs.getInt("status_player");
                status_muxer = rs.getInt("status_muxer");
                //Card Setting
                card_type = rs.getString("card_type");
                card_port = rs.getInt("card_port");
                freq = rs.getInt("freq");
                stream_ip = rs.getString("stream_ip");
                stream_port = rs.getInt("stream_port");

            }
            rs.close();
            conn.close();
            } catch (SQLException ex) {
            Logger.getLogger(ews_generate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void RowDataEws() {
             
        db = new DBAdmin(FolderUser);

        // ================KONEKSI UTAMA=========================
        try {
            conn = db.makeConnection();

            // Ambil field dari table trdw
            package_id.clear();
            location_type_code.clear();
            disaster_code.clear();
            location_code.clear();
            char_location_code.clear();
            section_number.clear();
            last_section_number.clear();
            jum_section_trdw = 0;
            query = "select * from trdw";
            rs = db.getRS(query);
            while (rs.next()) {
                package_id.add(rs.getInt("package_id"));
                location_type_code.add(rs.getInt("location_type_code"));
                disaster_code.add(rs.getInt("disaster_code"));
                location_code.add(rs.getString("location_code"));
                char_location_code.add(rs.getString("char_location_code"));
                section_number.add(rs.getInt("section_number"));
                last_section_number.add(rs.getInt("last_section_number"));
                jum_section_trdw = 1+rs.getInt("last_section_number");
            }
            rs.close();


            // Ambil field dari table tmdw
            package_id_tmdw.clear();
            location_type_code_tmdw.clear();
            char_information_message.clear();
            section_number_tmdw.clear();
            last_section_number_tmdw.clear();
            jum_section_tmdw = 0;
            query = "select * from tmdw";
            rs = db.getRS(query);
            while (rs.next()) {
                package_id_tmdw.add(rs.getInt("package_id"));
                location_type_code_tmdw.add(rs.getInt("location_type_code"));
                char_information_message.add(rs.getString("char_information_message"));
                section_number_tmdw.add(rs.getInt("section_number"));
                last_section_number_tmdw.add(rs.getInt("last_section_number"));
                jum_section_tmdw = 1+rs.getInt("last_section_number");
            }
            rs.close();


            // Ambil field dari table tcdw
            section_number_tcdw.clear();
            last_section_number_tcdw.clear();
            package_id_tcdw.clear();
            authority_tcdw.clear();
            disaster_code_tcdw.clear();
            char_disaster_code_tcdw.clear();
            char_disaster_position_tcdw.clear();
            char_disaster_date_tcdw.clear();
            char_disaster_characteristic.clear();
            section_number_tcdw.clear();
            last_section_number_tcdw.clear();
            jum_section_tcdw = 0;
            query = "select * from tcdw";
            rs = db.getRS(query);
            while (rs.next()) {
                package_id_tcdw.add(rs.getInt("package_id"));
                authority_tcdw.add(rs.getInt("authority"));
                disaster_code_tcdw.add(rs.getInt("disaster_code"));
                section_number_tcdw.add(rs.getInt("section_number"));
                last_section_number_tcdw.add(rs.getInt("last_section_number"));
                char_disaster_characteristic.add(rs.getString("char_disaster_characteristic"));
                char_disaster_position_tcdw.add(rs.getString("char_disaster_position"));
                char_disaster_date_tcdw.add(rs.getString("char_disaster_date"));
                char_disaster_code_tcdw.add(rs.getString("char_disaster_code"));
                jum_section_tcdw = 1+rs.getInt("last_section_number");
            }
            rs.close();
            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(ews_generate.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
    //fungsi mencreate trdw 

    public String generate_trdw(String p, ArrayList<Integer> jumlah) {
        if (jumlah.size() > 0) {
            int lokasi = 0;
            //perulangan untuk location code loop item
            boolean kondisi = false;
            for (int i = 0; i < jumlah.size(); i++) {
                //untuk mencetak header section number
                if (kondisi == false) {
                    lokasi = jumlah.get(i);
                    p = p + "\t trdw_section( \n";
                    p = p + "\t\t disaster_code = " + disaster_code.get(lokasi) + ", \n"
                            + "\t\t package_id = " + package_id.get(lokasi) + ", \n"
                            + "\t\t location_type_code = " + location_type_code.get(lokasi) + ", \n"
                            + "\t\t\t number_of_location_code = [ \n";
                    kondisi = true;
                }
                //untuk mencetak location code loop item sebanyak lokasi
                lokasi = jumlah.get(i);
                p = p + "\t\t\t\t location_code_loop_item( \n"
                        + "\t\t\t\t\t location_code = \"" + location_code.get(lokasi) + "\", \n"
                        + "\t\t\t\t\t ews_post_code_text = \"" + char_location_code.get(lokasi) + "\", \n"
                        + "\t\t\t\t ), \n";

            }
            //untuk mencetak footer trdw
            p = p + "\t\t\t ], \n"
                    + "\t\t version_number = 0, \n"
                    + "\t\t section_number = " + section_number.get(lokasi) + ", \n"
                    + "\t\t last_section_number = " + last_section_number.get(lokasi) + ", \n"
                    + "\t ), \n"
                    + "\n";

        }
        return p;
    }

    public String generate_tcdw(String p, ArrayList<Integer> jumlah) {
        if (jumlah.size() > 0) {
            int lokasi = 0;
            //perulangan untuk location code loop item
            boolean kondisi = false;
            for (int i = 0; i < jumlah.size(); i++) {
                //untuk mencetak header tcdw
                if (kondisi == false) {
                    lokasi = jumlah.get(i);
                    p = p + "\t tcdw_section( \n"
                            + "\t\t\t disaster_code_main_loop =[ \n"
                            + "\t\t\t\t disaster_code_main_item( \n"
                            + "\t\t\t\t\t package_id = " + package_id_tcdw.get(lokasi) + ", \n"
                            + "\t\t\t\t\t authority = " + authority_tcdw.get(lokasi) + ", \n"
                            + "\t\t\t\t\t disaster_code = " + disaster_code_tcdw.get(lokasi) + ", \n"
                            + "\t\t\t\t\t char_disaster_code = \"" + char_disaster_code_tcdw.get(lokasi) + "\", \n"
                            + "\t\t\t\t\t\t disaster_code_text_loop = [ \n";
                    kondisi = true;

                    p = p + "\t\t\t\t\t\t disaster_code_text_item( \n"
                            + "\t\t\t\t\t\t\t char_disaster_text_code = \"" + char_disaster_position_tcdw.get(lokasi) + "\", \n"
                            + "\t\t\t\t\t\t\t ), \n";
                    p = p + "\t\t\t\t\t\t disaster_code_text_item( \n"
                            + "\t\t\t\t\t\t\t char_disaster_text_code = \"" + char_disaster_date_tcdw.get(lokasi) + "\", \n"
                            + "\t\t\t\t\t\t\t ), \n";
                    p = p + "\t\t\t\t\t\t disaster_code_text_item( \n"
                            + "\t\t\t\t\t\t\t char_disaster_text_code = \"" + char_disaster_characteristic.get(lokasi) + "\", \n"
                            + "\t\t\t\t\t\t\t ), \n"
                            + "\t\t\t\t\t\t ] \n"
                            + "\t\t\t\t ), \n";


                }


            }
            //untuk mencetak footer tcdw
            p = p + "\t\t\t ], \n"
                    + "\t\t version_number = 0, \n"
                    + "\t\t section_number = " + section_number_tcdw.get(lokasi) + ", \n"
                    + "\t\t last_section_number = " + last_section_number_tcdw.get(lokasi) + ", \n"
                    + "\t ), \n"
                    + "\n";

        }
        return p;
    }

    public String generate_tmdw(String p, ArrayList<Integer> jumlah) {
        if (jumlah.size() > 0) {
            int lokasi = 0;
            //perulangan untuk location code loop item
            boolean kondisi = false;
            for (int i = 0; i < jumlah.size(); i++) {
                //untuk mencetak header section number
                if (kondisi == false) {
                    lokasi = jumlah.get(i);
                    p = p + "\t tmdw_section( \n";
                    p = p + "\t\t location_type_code = " + location_type_code_tmdw.get(lokasi) + ",\n"
                            + "\t\t package_id = " + package_id_tmdw.get(lokasi) + ", \n";
                    kondisi = true;
                }

                //untuk mencetak message informasi
                lokasi = jumlah.get(i);
                p = p + "\t\t\t tmdw_message_loop = [ \n"
                        + "\t\t\t\t tmdw_message_item( \n"
                        + "\t\t\t\t\t char_information_message = \"" + char_information_message.get(lokasi) + "\", \n"
                        + "\t\t\t\t ), \n";

            }
            //untuk mencetak footer tcdw
            p = p + "\t\t\t ], \n"
                    + "\t\t version_number = 0, \n"
                    + "\t\t section_number = " + section_number_tmdw.get(lokasi) + ", \n"
                    + "\t\t last_section_number = " + last_section_number_tmdw.get(lokasi) + ", \n"
                    + "\t ), \n"
                    + "\n";

        }
        return p;
    }

    public void clarification(ArrayList<Integer> last, ArrayList<Integer> section, ArrayList<Integer> location) {

        jumlah_lokasi_1_1.clear();
        jumlah_lokasi_1_2.clear();
        jumlah_lokasi_1_3.clear();
        jumlah_lokasi_2_1.clear();
        jumlah_lokasi_2_2.clear();
        jumlah_lokasi_2_3.clear();
        jumlah_lokasi_3_1.clear();
        jumlah_lokasi_3_2.clear();
        jumlah_lokasi_3_3.clear();
        jumlah_lokasi_4_1.clear();
        jumlah_lokasi_4_2.clear();
        jumlah_lokasi_4_3.clear();
        jumlah_lokasi_5_1.clear();
        jumlah_lokasi_5_2.clear();
        jumlah_lokasi_5_3.clear();
        jumlah_lokasi_6_1.clear();
        jumlah_lokasi_6_2.clear();
        jumlah_lokasi_6_3.clear();
        int j;
        for (j = 0; j < last.size(); j++) {
            if (section.get(j) == 0) {
                if (location.get(j) == 1) {
                    jumlah_lokasi_1_1.add(j);
                } else if (location.get(j) == 2) {
                    jumlah_lokasi_1_2.add(j);
                } else if (location.get(j) == 3) {
                    jumlah_lokasi_1_3.add(j);
                }
            } else if (section.get(j) == 1) {
                if (location.get(j) == 1) {
                    jumlah_lokasi_2_1.add(j);
                } else if (location.get(j) == 2) {
                    jumlah_lokasi_2_2.add(j);
                } else if (location.get(j) == 3) {
                    jumlah_lokasi_2_3.add(j);
                }
            } else if (section.get(j) == 2) {
                if (location.get(j) == 1) {
                    jumlah_lokasi_3_1.add(j);
                } else if (location.get(j) == 2) {
                    jumlah_lokasi_3_2.add(j);
                } else if (location.get(j) == 3) {
                    jumlah_lokasi_3_3.add(j);
                }
            } else if (section.get(j) == 3) {
                if (location.get(j) == 1) {
                    jumlah_lokasi_4_1.add(j);
                } else if (location.get(j) == 2) {
                    jumlah_lokasi_4_2.add(j);
                } else if (location.get(j) == 3) {
                    jumlah_lokasi_4_3.add(j);
                }
            } else if (section.get(j) == 4) {
                if (location.get(j) == 1) {
                    jumlah_lokasi_5_1.add(j);
                } else if (location.get(j) == 2) {
                    jumlah_lokasi_5_2.add(j);
                } else if (location.get(j) == 3) {
                    jumlah_lokasi_5_3.add(j);
                }
            } else if (section.get(j) == 5) {
                if (location.get(j) == 1) {
                    jumlah_lokasi_6_1.add(j);
                } else if (location.get(j) == 2) {
                    jumlah_lokasi_6_2.add(j);
                } else if (location.get(j) == 3) {
                    jumlah_lokasi_6_3.add(j);
                }
            }
        }
    }

    public void clarification_tcdw(ArrayList<Integer> last, ArrayList<Integer> section) {

        jumlah_lokasi_1_1.clear();
        jumlah_lokasi_2_1.clear();
        jumlah_lokasi_3_1.clear();
        jumlah_lokasi_4_1.clear();
        jumlah_lokasi_5_1.clear();
        jumlah_lokasi_6_1.clear();
        int j;
        for (j = 0; j < last.size(); j++) {
            if (section.get(j) == 0) {
                jumlah_lokasi_1_1.add(j);
            } else if (section.get(j) == 1) {
                jumlah_lokasi_2_1.add(j);
            } else if (section.get(j) == 2) {
                jumlah_lokasi_3_1.add(j);
            } else if (section.get(j) == 3) {
                jumlah_lokasi_4_1.add(j);
            } else if (section.get(j) == 4) {
                jumlah_lokasi_5_1.add(j);
            } else if (section.get(j) == 5) {
                jumlah_lokasi_6_1.add(j);
            }
        }
    }

    private String generate_ews() {

        //parsing data kode pos menurut section dan location_type
        clarification(last_section_number, section_number, location_type_code);
        //membuat text untuk python
        String p;
        p = "#! /usr/bin/env python\n"
                + "import os \n"
                + "from dvbobjects.PSI.PAT import *\n"
                + "from dvbobjects.PSI.NIT import *\n"
                + "from dvbobjects.PSI.SDT import *\n"
                + "from dvbobjects.PSI.PMT import *\n"
                + "from dvbobjects.PSI.TDT import *\n"
                + "from dvbobjects.DVB.Descriptors import *\n"
                + "from dvbobjects.MPEG.Descriptors import *\n"
                + "from dvbobjects.IDEWS.TRDW import * \n"
                + "from dvbobjects.IDEWS.TCDW import * \n"
                + "from dvbobjects.IDEWS.TMDW import * \n"
                + "\n"
                + "\n"
                + "ews_pmt_id =  " + ews_pmt_id + " \n"
                + "ews_service_id = " + ews_service_number + " \n"
                + "ews_data_id =  128 \n"
                + "dirku =\"" + FolderUser + "temps/\"\n"
                + "\n"
                + "\n"
                + "\n"
                + "trdwtable = [ \n";
        //mecreate trdw 6 package id dengan 3 location type

        p = generate_trdw(p, jumlah_lokasi_1_1);
        p = generate_trdw(p, jumlah_lokasi_1_2);
        p = generate_trdw(p, jumlah_lokasi_1_3);
        p = generate_trdw(p, jumlah_lokasi_2_1);
        p = generate_trdw(p, jumlah_lokasi_2_2);
        p = generate_trdw(p, jumlah_lokasi_2_3);
        p = generate_trdw(p, jumlah_lokasi_3_1);
        p = generate_trdw(p, jumlah_lokasi_3_2);
        p = generate_trdw(p, jumlah_lokasi_3_3);
        p = generate_trdw(p, jumlah_lokasi_4_1);
        p = generate_trdw(p, jumlah_lokasi_4_2);
        p = generate_trdw(p, jumlah_lokasi_4_3);
        p = generate_trdw(p, jumlah_lokasi_5_1);
        p = generate_trdw(p, jumlah_lokasi_5_2);
        p = generate_trdw(p, jumlah_lokasi_5_3);
        p = generate_trdw(p, jumlah_lokasi_6_1);
        p = generate_trdw(p, jumlah_lokasi_6_2);
        p = generate_trdw(p, jumlah_lokasi_6_3);

        p = p + "]\n\n\n"
                + "tmdwtable = [ \n";


        clarification(last_section_number_tmdw, section_number_tmdw, location_type_code_tmdw);

        p = generate_tmdw(p, jumlah_lokasi_1_1);
        p = generate_tmdw(p, jumlah_lokasi_1_2);
        p = generate_tmdw(p, jumlah_lokasi_1_3);
        p = generate_tmdw(p, jumlah_lokasi_2_1);
        p = generate_tmdw(p, jumlah_lokasi_2_2);
        p = generate_tmdw(p, jumlah_lokasi_2_3);
        p = generate_tmdw(p, jumlah_lokasi_3_1);
        p = generate_tmdw(p, jumlah_lokasi_3_2);
        p = generate_tmdw(p, jumlah_lokasi_3_3);
        p = generate_tmdw(p, jumlah_lokasi_4_1);
        p = generate_tmdw(p, jumlah_lokasi_4_2);
        p = generate_tmdw(p, jumlah_lokasi_4_3);
        p = generate_tmdw(p, jumlah_lokasi_5_1);
        p = generate_tmdw(p, jumlah_lokasi_5_2);
        p = generate_tmdw(p, jumlah_lokasi_5_3);
        p = generate_tmdw(p, jumlah_lokasi_6_1);
        p = generate_tmdw(p, jumlah_lokasi_6_2);
        p = generate_tmdw(p, jumlah_lokasi_6_3);
        p = p + "]\n\n\n"
                + "tcdwtable = [ \n";

        clarification_tcdw(last_section_number_tcdw, section_number_tcdw);

        p = generate_tcdw(p, jumlah_lokasi_1_1);
        p = generate_tcdw(p, jumlah_lokasi_2_1);
        p = generate_tcdw(p, jumlah_lokasi_3_1);
        p = generate_tcdw(p, jumlah_lokasi_4_1);
        p = generate_tcdw(p, jumlah_lokasi_5_1);
        p = generate_tcdw(p, jumlah_lokasi_6_1);

        p = p + "]\n\n\n";
        p = p + "for index  in range (len(trdwtable)):  \n"
                + "\t out = open(str(dirku) + 'trdwtable'+str(index)+'.sec', \"wb\") \n"
                + "\t out.write(trdwtable[index].pack()) \n"
                + "\t out.close \n"
                + "\t out = open(str(dirku) + 'trdwtable'+str(index)+'.sec', \"wb\") \n"
                + "\t out.close \n"
                + "\t os.system('sec2ts ' + str(ews_data_id) + ' < ' + str(dirku) + 'trdwtable'+str(index)+'.sec > ' + str(dirku) + 'trdw'+str(index+1)+'.ts') \n\n"
                + "for index  in range (len(tcdwtable)): \n"
                + "\t out = open(str(dirku) + 'tcdwtable'+str(index)+'.sec', \"wb\") \n"
                + "\t out.write(tcdwtable[index].pack()) \n"
                + "\t out.close \n"
                + "\t out = open(str(dirku) + 'tcdwtable'+str(index)+'.sec', \"wb\") \n "
                + "\t out.close \n"
                + "\t os.system('sec2ts ' + str(ews_data_id) + ' < ' + str(dirku) + 'tcdwtable'+str(index)+'.sec > ' + str(dirku) + 'tcdw'+str(index+1)+'.ts') \n\n"
                + "for index  in range (len(tmdwtable)): \n"
                + "\t out = open(str(dirku) + 'tmdwtable'+str(index)+'.sec', \"wb\") \n"
                + "\t out.write(tmdwtable[index].pack()) \n"
                + "\t out.close \n"
                + "\t out = open(str(dirku) + 'tmdwtable'+str(index)+'.sec', \"wb\") \n"
                + "\t out.close \n"
                + "\t os.system('sec2ts ' + str(ews_data_id) + ' < ' + str(dirku) + 'tmdwtable'+str(index)+'.sec > ' + str(dirku) + 'tmdw'+str(index+1)+'.ts') \n\n"
                + "os.system('rm ' + str(dirku) + '*.sec')";

        return p;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;

/**
 *
 * @author trie
 */
public class psi_generate {

    private DBAdmin db;
    private Connection conn;
    private ResultSet rs;
    private String query;
    String FolderUser;
    File file;
    
    String NetworkName, EwsServiceName, PsiServiceName;
    int EwsPmtId, PsiPmtId, NetworkId, PsiServiceNumber, EwsServiceNumber, EwsDataId;
    int PsiAudioId, PsiVideoId;
    int status_player, status_muxer, card_port, stream_port;
    String card_type, stream_ip,command;
    ProcessBuilder cmd = new ProcessBuilder();

    public psi_generate(String folderuser, int network_id, int psi_service_number, String psi_service_name, int ews_service_number, String ews_service_name, int psi_pmt_id, int ews_pmt_id, int ews_data_id, int psi_video_id, int psi_audio_id) throws IOException {
        
        FileWriter fw;
        FolderUser = folderuser;
        NetworkId = network_id;
        PsiServiceNumber = psi_service_number;
        PsiServiceName = psi_service_name;
        EwsServiceNumber = ews_service_number;
        EwsServiceName = ews_service_name;
        PsiPmtId = psi_pmt_id;
        EwsPmtId = ews_pmt_id;
        EwsDataId = ews_data_id;
        PsiVideoId = psi_video_id;
        PsiAudioId = psi_audio_id;

        command  = "mkdir " + FolderUser + "temps;"
                   + "sudo Dta1xxInit start";
        cmd.command("sh", "-c", command);
        cmd.start();

        
        try {
            
            String p;
            String namaFile = FolderUser + "temps/psiconfig.py";
            file = new File(namaFile);
            fw   = new FileWriter(namaFile);
            p    = generate_psi();
            fw.write(p);
            fw.close();
        } catch (IOException ex) {
            System.err.println("Gagal membuat file data PSI. \n");
        }
        
        command  = "chmod +x " + FolderUser + "temps/psiconfig.py;"
                   + FolderUser + "temps/psiconfig.py";
        cmd.command("sh", "-c", command);
        cmd.start();

    }

    
    private String generate_psi() {
        //
        String p;
        p = "#! /usr/bin/env python \n"
                + "import os \n"
                + "from dvbobjects.PSI.PAT import * \n"
                + "from dvbobjects.PSI.NIT import * \n"
                + "from dvbobjects.PSI.EIT import * \n"
                + "from dvbobjects.PSI.SDT import * \n"
                + "from dvbobjects.PSI.PMT import * \n"
                + "from dvbobjects.PSI.TDT import * \n"
                + "from dvbobjects.DVB.Descriptors import * \n"
                + "from dvbobjects.MPEG.Descriptors import * \n"
                + "transport_stream_id 		= "+NetworkId+" \n"
                + "original_network_id          = 8552 \n"
                + "prog_service_id 		= "+PsiServiceNumber+" \n"
                + "ews_service_id 		= "+EwsServiceNumber+" \n"
                + "prog_pmt_pid 		= "+PsiPmtId+" \n"
                + "ews_pmt_pid 			= "+EwsPmtId+" \n"
                + "ews_data_pid 		= "+EwsDataId+" \n"
                + "ews_pcr_pid 			= "+EwsDataId+" \n"
                + "stream_type_video 		= 2	#0x1B/27 H.264 0x10/16 Mpeg4 0x02/2 Mpeg2 \n"
                + "stream_type_audio 		= 3	#0x03/3 Audio mpeg2 \n"
                + "pid_pcr 			= "+PsiVideoId+" \n"
                + "pid_video 			= "+PsiVideoId+" \n"
                + "pid_audio 			= "+PsiAudioId+" \n"
                + "dirku 			=\""+FolderUser+"temps/\" \n"
                + " \n"
                + " \n"
                +"nit = network_information_section( \n"
                +"\t network_id = transport_stream_id, \n"
                +"\t network_descriptor_loop = [ \n"
                +"\t\t network_descriptor(network_name = \"BPPT\",), \n"
                +"\t ], \n"
                +"\t transport_stream_loop = [ \n"
                +"\t\t transport_stream_loop_item( \n"
                +"\t\t\t transport_stream_id = transport_stream_id, \n"
                +"\t\t\t original_network_id = 8552, \n"
                +"\t\t\t transport_descriptor_loop = [ \n"
                /*+"\t\t\t\t transport_stream_terrestrial_descriptor( \n"
                +"\t\t\t\t\t center_frequency = 650000000, \n"
                +"\t\t\t\t\t bandwidth = 0, \n"
                +"\t\t\t\t\t constellation = 2, \n"
                +"\t\t\t\t\t hierarchy_information = 0, \n"
                +"\t\t\t\t\t code_rate_HP_stream = 0, \n"
                +"\t\t\t\t\t code_rate_LP_stream = 2, \n"
                +"\t\t\t\t\t guard_interval = 0, \n"
                +"\t\t\t\t\t transmission_mode = 1, \n"
                +"\t\t\t\t\t other_frequency_flag = 0, \n"
                +"\t\t\t\t ), \n"
                */
                +"\t\t\t\t service_list_descriptor( \n"
                +"\t\t\t\t\t dvb_service_descriptor_loop = [ \n"
                +"\t\t\t\t\t\t service_descriptor_loop_item( \n"
                +"\t\t\t\t\t\t\t service_ID = ews_service_id, \n"
                +"\t\t\t\t\t\t\t service_type = 128, \n"
                +"\t\t\t\t\t\t ), \n"
                +"\t\t\t\t\t\t service_descriptor_loop_item( \n"
                +"\t\t\t\t\t\t\t service_ID = prog_service_id, \n"
                +"\t\t\t\t\t\t\t service_type = 1, \n"
                +"\t\t\t\t\t\t ), \n"
                +"\t\t\t\t\t ], \n"
                +"\t\t\t\t ), \n"
                +"\t\t\t\t logical_channel_descriptor( \n"
                +"\t\t\t\t\t lcn_service_descriptor_loop = [ \n"
                +"\t\t\t\t\t\t lcn_service_descriptor_loop_item( \n"
                +"\t\t\t\t\t\t\t service_ID = prog_service_id, \n"
                +"\t\t\t\t\t\t\t visible_service_flag = 1, \n"
		+"\t\t\t\t\t\t\t logical_channel_number = 2, \n"
                +"\t\t\t\t\t\t ), \n"
                +"\t\t\t\t\t ], \n"
                +"\t\t\t\t ), \n"
                +"\t\t\t ], \n"
                +"\t\t ), \n"
                +"\t ], \n"
                +"\t version_number = 1, \n"
                +"\t section_number = 0, \n"
                +"\t last_section_number = 0, \n"
                +" ) \n"
                +"\n"
                +"\n"
                + "pat = program_association_section( \n"
                + "\t transport_stream_id = transport_stream_id, \n"
                + "\t program_loop = [ \n"
                + "\t\t program_loop_item( \n"
                + "\t\t\t program_number = 0, \n"
                + "\t\t\t PID = 16, \n"
                + "\t\t ), \n"
                + "\t\t program_loop_item( \n"
                + "\t\t\t program_number = prog_service_id, \n"
                + "\t\t\t PID = prog_pmt_pid, \n"
                + "\t\t ), \n"
                + "\t\t program_loop_item( \n"
                + "\t\t\t program_number = ews_service_id, \n"
                + "\t\t\t PID = ews_pmt_pid, \n"
                + "\t\t ), \n"
                + "\t ], \n"
                + "\t version_number = 1, \n"
                + "\t section_number = 0, \n"
                + "\t last_section_number = 0,\n"
                + ") \n"
                + " \n"
                + " \n"
                + "sdt = service_description_section( \n"
                + "\t transport_stream_id = transport_stream_id, \n"
                + "\t original_network_id = original_network_id, \n"
                + "\t service_loop = [ \n"
                + "\t\t service_loop_item( \n"
                + "\t\t\t service_ID = prog_service_id, \n"
                + "\t\t\t EIT_schedule_flag = 0, \n"
                + "\t\t\t EIT_present_following_flag = 0, \n"
                + "\t\t\t running_status = 4, \n"
                + "\t\t\t free_CA_mode = 0, \n"
                + "\t\t\t service_descriptor_loop = [ \n"
                + "\t\t\t\t service_descriptor( \n"
                + "\t\t\t\t\t service_type = 1, \n"
                + "\t\t\t\t\t service_provider_name = \"triedepok@gmail.com\", \n"
                + "\t\t\t\t\t service_name = \""+PsiServiceName+"\", \n"
                + "\t\t\t\t ), \n"
                + "\t\t\t ], \n"
                + "\t\t ), \n"
                + "\t\t service_loop_item( \n"
                + "\t\t\t service_ID = ews_service_id, \n"
                + "\t\t\t EIT_schedule_flag = 0, \n"
                + "\t\t\t EIT_present_following_flag = 0, \n"
                + "\t\t\t running_status = 4, \n"
                + "\t\t\t free_CA_mode = 0, \n"
                + "\t\t\t service_descriptor_loop = [ \n"
                + "\t\t\t\t service_descriptor( \n"
                + "\t\t\t\t\t service_type = 128, \n"
                + "\t\t\t\t\t service_provider_name = \"triedepok@gmail.com\", \n"
                + "\t\t\t\t\t service_name = \""+EwsServiceName+"\", \n"
                + "\t\t\t\t ), \n"
                + "\t\t\t ], \n"
                + "\t\t ), \n"
                + "\t ], \n"
                + "\t version_number = 1, \n"
                + "\t section_number = 0, \n"
                + "\t last_section_number = 0, \n"
                + " ) \n"
                + " \n"
                + " \n"
                + "pmt1 = program_map_section( \n"
                + "\t program_number = prog_service_id, \n"
                + "\t PCR_PID = pid_pcr, \n"
                + "\t program_info_descriptor_loop = [], \n"
                + "\t stream_loop = [ \n"
                + "\t\t stream_loop_item( \n"
                + "\t\t\t stream_type = stream_type_video, \n"
                + "\t\t\t elementary_PID = pid_video,	 \n"
                + "\t\t\t element_info_descriptor_loop = [] \n"
                + "\t\t ), \n"
                + "\t\t stream_loop_item( \n"
                + "\t\t\t stream_type = stream_type_audio, \n"
                + "\t\t\t elementary_PID = pid_audio, \n"
                + "\t\t\t element_info_descriptor_loop = [] \n"
                + "\t\t ), \n"
                + "\t ], \n"
                + "\t version_number = 0, \n"
                + "\t section_number = 0, \n"
                + "\t last_section_number = 0, \n"
                + " ) \n"
                + " \n"
                + " \n"
                + "dwpmt = program_map_section( \n"
                + "\t program_number = ews_service_id, \n"
                + "\t PCR_PID = ews_pcr_pid, \n"
                + "\t program_info_descriptor_loop = [], \n"
                + "\t stream_loop = [ \n"
                + "\t\t stream_loop_item( \n"
                + "\t\t\t stream_type = 128, \n"
                + "\t\t\t elementary_PID = ews_data_pid, \n"
                + "\t\t\t element_info_descriptor_loop = [] \n"
                + "\t\t ), \n"
                + "\t ], \n"
                + "\t version_number = 0, \n"
                + "\t section_number = 0, \n"
                + "\t last_section_number = 0, \n"
                + " ) \n"
                + " \n"
                + " \n"
                +"eit = event_information_section( \n"
		+"\t table_id = EIT_ACTUAL_TS_PRESENT_FOLLOWING, \n"
		+"\t service_id = prog_service_id, \n"
		+"\t transport_stream_id = transport_stream_id, \n"
		+"\t original_network_id = original_network_id, \n"
		+"\t event_loop = [ \n"
		+"\t\t event_loop_item( \n"
		+"\t\t\t event_id = 1, \n"
		+"\t\t\t start_year = 115, \n"
		+"\t\t\t start_month = 3, \n"
		+"\t\t\t start_day = 26, \n"
		+"\t\t\t start_hours = 0x00, \n"
		+"\t\t\t start_minutes = 0x00, \n"
		+"\t\t\t start_seconds = 0x00, \n"
		+"\t\t\t duration_hours = 0x23, \n"
		+"\t\t\t duration_minutes = 0x00, \n"
		+"\t\t\t duration_seconds = 0x00, \n"
		+"\t\t\t running_status = 4, \n"
		+"\t\t\t free_CA_mode = 0, \n"
		+"\t\t\t event_descriptor_loop = [ \n"
		+"\t\t\t\t short_event_descriptor ( \n"
		+"\t\t\t\t\t ISO639_language_code = \"ita\", \n"
		+"\t\t\t\t\t event_name = \"TEST EPG - BPPT-3\", \n"
		+"\t\t\t\t\t text = \"Ini adalah program EPG untuk uji coba (triedepok@gmail.com)\", \n"
		+"\t\t\t\t ) \n"
		+"\t\t\t ], \n"
		+"\t\t ), \n"
		+"\t ], \n"
		+"\t version_number = 1, \n"
		+"\t section_number = 0, \n"
                +"\t last_section_number = 0, \n"
		+"\t segment_last_section_number = 0, \n"
		+" ) \n"
		+" \n"
		+" \n"
                + "out = open(str(dirku) + 'nit.sec', \"wb\") \n"
                + "out.write(nit.pack()) \n"
                + "out.close \n"
                + "out = open(str(dirku) + 'nit.sec', \"wb\") \n"
                + "out.close \n"
                + "os.system('sec2ts 16 < ' + str(dirku) + 'nit.sec > ' + str(dirku) + 'nit.ts') \n"
                + " \n"
                + "out = open(str(dirku) + 'pat.sec', \"wb\") \n"
                + "out.write(pat.pack()) \n"
                + "out.close \n"
                + "out = open(str(dirku) + 'pat.sec', \"wb\") \n"
                + "out.close \n"
                + "os.system('sec2ts 0 < ' + str(dirku) + 'pat.sec > ' + str(dirku) + 'pat.ts') \n"
                + " \n"
                + "out = open(str(dirku) + 'sdt.sec', \"wb\") \n"
                + "out.write(sdt.pack()) \n"
                + "out.close \n"
                + "out = open(str(dirku) + 'sdt.sec', \"wb\") \n"
                + "out.close \n"
                + "os.system('sec2ts 17 < ' + str(dirku) + 'sdt.sec > ' + str(dirku) + 'sdt.ts') \n"
                + " \n"
                + "out = open(str(dirku) + 'pmt1.sec', \"wb\") \n"
                + "out.write(pmt1.pack()) \n"
                + "out.close \n"
                + "out = open(str(dirku) + 'pmt1.sec', \"wb\") \n"
                + "out.close \n"
                + "os.system('sec2ts ' + str(prog_pmt_pid) + ' < ' + str(dirku) + 'pmt1.sec > ' + str(dirku) + 'pmt1.ts') \n"
                + " \n"
                + "out = open(str(dirku) + 'dwpmt.sec', \"wb\")  \n"
                + "out.write(dwpmt.pack())  \n"
                + "out.close  \n"
                + "out = open(str(dirku) + 'dwpmt.sec', \"wb\")  \n"
                + "out.close  \n"
                + "os.system('sec2ts ' + str(ews_pmt_pid) + ' < ' + str(dirku) + 'dwpmt.sec > ' + str(dirku) + 'dwpmt.ts') \n"
                + " \n"
                + "os.system('rm ' + str(dirku) + '*.sec') \n"
                + " \n"
                + "out = open(str(dirku) + 'eit.sec', \"wb\") \n"
                + "out.write(eit.pack()) \n"
                + "out.close \n"
                + "out = open(str(dirku) + 'eit.sec', \"wb\") \n"
                + "out.close \n"
                + "os.system('sec2ts 18 < ' + str(dirku) + 'eit.sec > ' + str(dirku) + 'eit.ts') \n"
                + " \n";
        return p;
    }

} //public class psi_generate close

 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import java.io.IOException;

/**
 *
 * @author trie
 */
public class player {
    String folderuser,card_type,folder_video;
    int psi_bitrate,ews_bitrate,null_bitrate,video_bitrate,psi_video_id,psi_audio_id;
    int status_player,status_muxer,card_port,conten;
    String combine, command;
    ProcessBuilder cmd = new ProcessBuilder();

    player(String FolderUser,int conten, String folder_video, int psi_bitrate, int ews_bitrate, int null_bitrate, int video_bitrate, int psi_video_id, int psi_audio_id, int status_player, int status_muxer, String card_type, int card_port, int freq, int jum_section_trdw, int jum_section_tcdw, int jum_section_tmdw) throws InterruptedException, IOException {
        folderuser = FolderUser;
        //Debug
        /*System.out.println("####################################################");
        System.out.println("Proses Player...");
        System.out.println("Lokasi Folder: "+FolderUser);
        System.out.println("PSI Bitrate: "+psi_bitrate);
        System.out.println("EWS Bitrate: "+ews_bitrate);
        System.out.println("EWS Bitrate: "+video_bitrate);
        System.out.println("Session TRDW: "+jum_section_trdw);
        System.out.println("Session TCDW: "+jum_section_tcdw);
        System.out.println("Session TMDW: "+jum_section_tmdw);
        System.out.println("####################################################");
        */
        
        // Buat file fifo
        command    = "mkfifo "+folderuser+"temps/hasil-mux1.ts; mkfifo "+folderuser+"temps/hasil-mux2.ts;mkfifo "+folderuser+"temps/hasil-mux3.ts; "
                    +"mkfifo "+folderuser+"temps/hasil-mux-cc.ts; mkfifo "+folderuser+"temps/hasil-tamp1.ts; mkfifo "+folderuser+"temps/hasil-tamp2.ts; "
                    +"mkfifo "+folderuser+"temps/sample1-filter.ts; mkfifo "+folderuser+"temps/sample1-loop.ts; ";
        cmd.command("sh", "-c", command);
        cmd.start();

        Thread.sleep(100);
        //Melakukan konfigurasi untuk DTA dan DTU Card --- triedepok#gmail.com
        int ews_bit = (ews_bitrate*(jum_section_trdw +jum_section_tcdw + jum_section_tmdw))+null_bitrate;
        int psi_bit;
        int tot_bit;

        String s_trdw="";
        for(int i=1;i<=jum_section_trdw;i++){
            s_trdw = s_trdw+"b:"+ews_bitrate+" "+folderuser+"temps/trdw"+i+".ts ";

        }
        String s_tcdw="";
        for(int i=1;i<=jum_section_tcdw;i++){
            s_tcdw = s_tcdw+"b:"+ews_bitrate+" "+folderuser+"temps/tcdw"+i+".ts ";

        }
        String s_tmdw="";
        for(int i=1;i<=jum_section_tmdw;i++){
            s_tmdw = s_tmdw+"b:"+ews_bitrate+" "+folderuser+"temps/tmdw"+i+".ts ";

        }

        //SI Create table
        String s_psi;
        if(conten==1){
            psi_bit = psi_bitrate*6;
            s_psi    = "tscbrmuxer b:"+psi_bitrate+" "+folderuser+"temps/eit.ts b:"+psi_bitrate+" "+folderuser+"temps/pat.ts b:"+psi_bitrate+" "+folderuser+"temps/nit.ts b:"+psi_bitrate+" "+folderuser+"temps/sdt.ts b:"+psi_bitrate+" "+folderuser+"temps/pmt1.ts b:"+psi_bitrate+" "+folderuser+"temps/dwpmt.ts > "+folderuser+"temps/hasil-mux1.ts & ";
        }else{
            psi_bit = psi_bitrate*5;
            s_psi    = "tscbrmuxer b:"+psi_bitrate+" "+folderuser+"temps/pat.ts b:"+psi_bitrate+" "+folderuser+"temps/sdt.ts b:"+psi_bitrate+" "+folderuser+"temps/dwpmt.ts > "+folderuser+"temps/hasil-mux1.ts & ";
        }
        //EWS create table
        String s_ewsall = "tscbrmuxer "+s_trdw+s_tcdw+s_tmdw+" b:"+null_bitrate+" "+folderuser+"Videos/null.ts > "+folderuser+"temps/hasil-mux2.ts & ";
        //Conten audio, video create
        String s_video  = "tsloop "+folderuser+folder_video+" > "+folderuser+"temps/sample1-loop.ts & "
                         +"tspcrstamp "+folderuser+"temps/sample1-loop.ts "+video_bitrate+" > "+folderuser+"temps/hasil-tamp1.ts & "
                         +"tsfilter "+folderuser+"temps/hasil-tamp1.ts +"+psi_video_id+" +"+psi_audio_id+" > "+folderuser+"temps/sample1-filter.ts & ";

        String s_all;
        if(conten==1){
            //Dengan konten Audio dan Video
            tot_bit = ews_bit+psi_bit+video_bitrate;
            s_all = s_psi+s_ewsall+s_video+"tscbrmuxer b:"+psi_bit+" "+folderuser+"temps/hasil-mux1.ts b:"+ews_bit+" "+folderuser+"temps/hasil-mux2.ts b:"+video_bitrate+" "+folderuser+"temps/sample1-filter.ts > "+folderuser+"temps/hasil-mux3.ts & ";
        }else{
            //Tanpa konten Audio dan Video
            tot_bit = ews_bit+psi_bit+null_bitrate;
            s_all = s_psi+s_ewsall+"tscbrmuxer b:"+psi_bit+" "+folderuser+"temps/hasil-mux1.ts b:"+ews_bit+" "+folderuser+"temps/hasil-mux2.ts b:"+null_bitrate+" "+folderuser+"Videos/null.ts > "+folderuser+"temps/hasil-mux3.ts & ";
        }
        
        //Perbaiki paket TS
        s_all = s_all + "tsfixcc "+folderuser+"temps/hasil-mux3.ts > "+folderuser+"temps/hasil-mux-cc.ts & ";
        s_all = s_all + "tspcrstamp "+folderuser+"temps/hasil-mux-cc.ts "+tot_bit+" > "+folderuser+"temps/hasil-tamp2.ts ; ";


        //Configurasi modulasi RF
        if(card_type.equals("215")){
            System.out.println("Proses Player Modulator DTU-215... \n");
            combine = "DtPlay "+folderuser+"temps/hasil-tamp2.ts -t "+card_type
                +" -mt DVBT"
                +" -r "+tot_bit
                +" -i "+card_port
                +" -mC QPSK"
                +" -mG 1/4"
                +" -mc 1/2"
                +" -l 1"
                +" -mf "+freq+" ;";
            }else if(card_type.equals("115") || card_type.equals("110")){
            System.out.println("Proses Player Modulator DTA-110 dan DTA-115... \n");
            combine = "DtPlay "+folderuser+"temps/hasil-tamp2.ts -t "+card_type
                +" -mt OFDM"
                +" -r "+tot_bit
                +" -i "+card_port
                +" -mC QPSK"
                +" -mG 1/4"
                +" -mc 1/2"
                +" -l 1"
                +" -mf "+freq+" ;";
            }else{
                System.out.println("Proses Player ASI... \n");
                combine = "DtPlay "+folderuser+"temps/hasil-tamp2.ts -t "+card_type
                    +" -r "+tot_bit
                    +" -i "+card_port
                    +" -l 1 ;";
        }
        
            cmd.command("sh", "-c", s_all);
            cmd.start();
            Thread.sleep(100);
            cmd.command("sh", "-c", combine);
            cmd.start();
            //System.out.println(s_all+combine);


        
    }
    
    player(String FolderUser,int ews_bitrate,int null_bitrate,int status_player,int status_muxer,int jum_section_trdw,int jum_section_tcdw,int jum_section_tmdw) throws IOException{
        
        int ews_bit = (ews_bitrate*(jum_section_trdw +jum_section_tcdw + jum_section_tmdw))+ews_bitrate;

        String s_trdw="";
        for(int i=1;i<=jum_section_trdw;i++){
            s_trdw = s_trdw+"b:"+ews_bitrate+" "+folderuser+"temps/trdw"+i+".ts ";

        }
        String s_tcdw="";
        for(int i=1;i<=jum_section_tcdw;i++){
            s_tcdw = s_tcdw+"b:"+ews_bitrate+" "+folderuser+"temps/tcdw"+i+".ts ";

        }
        String s_tmdw="";
        for(int i=1;i<=jum_section_tmdw;i++){
            s_tmdw = s_tmdw+"b:"+ews_bitrate+" "+folderuser+"temps/tmdw"+i+".ts ";

        }
        
        //EWS create table
        String s_ewsall = "tscbrmuxer "+s_trdw+s_tcdw+s_tmdw+"b:"+null_bitrate+" "+folderuser+"Videos/null.ts > "+folderuser+"temps/hasil-mux2.ts & ";
        cmd.command("sh", "-c", s_ewsall);
        cmd.start();
        System.out.println("Update data Ews... \n");
        
    }

    player(String FolderUser, String card_type) throws IOException {
        folderuser = FolderUser;
        String s_stop_play1;
        
              if(card_type.equals("115") || card_type.equals("110")){
                  s_stop_play1 = "killall -TERM DtPlay; killall -TERM tscbrmuxer; killall -TERM tsloop; killall -TERM tsfixcc; killall -TERM tspcrstamp; "
                                        +"sudo Dta1xxInit stop; "
                                        +"sudo Dta1xxInit start; "
                                        +"sudo Dta1xxInit stop; "
                                        +"rm "+folderuser+"temps/*;"
                                        +"killall -TERM sh;";
                  System.out.println("Stop proses Dta 110/115 Player... \n");
              }else if(card_type.equals("215")){
                  s_stop_play1 = "killall -TERM DtPlay; killall -TERM tscbrmuxer; killall -TERM tsloop; killall -TERM tsfixcc; killall -TERM tspcrstamp; "
                                        +"rm "+folderuser+"temps/*; "
                                        +"DtPlay "+folderuser+"Videos/null.ts -t 215 -mt DVBT -r 4752989 -i 1 -mC QPSK -mG 1/4 -mc 1/2 -l 1 -mf 650; "
                                        +"killall -TERM DtPlay;"
                                        +"killall -TERM sh;";
                  System.out.println("Stop proses Dtu 215 Player... \n");
              }else if(card_type.equals("205")){
                  s_stop_play1 = "killall -TERM DtPlay; killall -TERM tscbrmuxer; killall -TERM tsloop; killall -TERM tsfixcc; killall -TERM tspcrstamp; "
                                        +"rm "+folderuser+"temps/*; "
                                        +"DtPlay "+folderuser+"Videos/null.ts -t 205 -r 697696 -i 1 -l 1 ;"
                                        +"killall -TERM DtPlay;"
                                        +"killall -TERM sh;";
                  System.out.println("Stop proses Dtu 205 Player... \n");
              }else{
                  s_stop_play1 = "killall -TERM DtPlay; killall -TERM tscbrmuxer; killall -TERM tsloop; killall -TERM tsfixcc; killall -TERM tspcrstamp; "
                                        +"rm "+folderuser+"temps/*; "
                                        +"DtPlay "+folderuser+"Videos/null.ts -t 105 -r 697696 -i 1 -l 1 ;"
                                        +"killall -TERM DtPlay;"
                                        +"killall -TERM sh;";
                  System.out.println("Stop proses Dta/Dtu Player... \n");
              }
              
              cmd.command("sh", "-c", s_stop_play1);
              cmd.start();
                
    }
    
    
}

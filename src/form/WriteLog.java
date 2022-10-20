/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package form;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ews
 */
public class WriteLog {
    
    public static void WriteData(String data){
        try {
            File file = new File("ews-server.log");
            if (!file.exists()) {
                file.createNewFile();
            }
            
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.append("\r\n");
            bw.close();
            fw.close();
        } catch (IOException e) {
            Logger.getLogger(WriteLog.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public static String ReadData(){
        String hasil="";
        String data;
        try {
            File file = new File("ews-server.log");
            FileReader fr = new FileReader(file.getAbsoluteFile());
            BufferedReader br = new BufferedReader(fr);
            while((data=br.readLine()) != null){
                hasil=hasil+data+" \n";
            }
            br.close();
            fr.close();
        } catch (IOException e) {
            Logger.getLogger(WriteLog.class.getName()).log(Level.SEVERE, null, e);
        }
        return hasil;
    }
    
}

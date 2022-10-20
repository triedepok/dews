/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package form;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 *
 * @author ews
 */
public class IpMac {
    
    public static String GetAddress(String addressType){
       String address = "";
       InetAddress lanIp = null;
        try {

            String ipAddress = null;
            Enumeration<NetworkInterface> net = null;
            net = NetworkInterface.getNetworkInterfaces();
            while(net.hasMoreElements()){
                NetworkInterface element = net.nextElement();
                Enumeration<InetAddress> addresses = element.getInetAddresses();
                while (addresses.hasMoreElements()){
                    InetAddress ip = addresses.nextElement();
                    if (ip instanceof InetAddress){

                        if (ip.isSiteLocalAddress()){

                            ipAddress = ip.getHostAddress();
                            lanIp = InetAddress.getByName(ipAddress);
                        }

                    }

                }
            }

            if(lanIp == null) return null;

            if(addressType.equals("ip")){

                address = lanIp.toString().replaceAll("^/+", "");

            }else if(addressType.equals("mac")){

                address = GetMacAddress(lanIp);

            }else{

                throw new Exception("Specify \"ip\" or \"mac\"");

            }

        } catch (UnknownHostException e) {
        } catch (SocketException e){
        } catch (Exception e){
        }

       return address;

   }

   private static String GetMacAddress(InetAddress ip){
       String address = null;
        try {

            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));        
            }
            address = sb.toString();

        } catch (SocketException e) {
        }

       return address;
   }
    
}

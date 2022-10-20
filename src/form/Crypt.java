package form;

/**
 *
 * @author ews
 */

import java.io.UnsupportedEncodingException;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Crypt
{
    private static final String ALGORITHM = "AES";
    private static final String KEY = "3YBL-143DAF-GL78";
    private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();
    
    public static String cek_key() throws Exception
    {
       return KEY; 
    }
    
    public static String encrypt(String value) throws Exception
    {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(Crypt.ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte [] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
        String encryptedValue64 = new BASE64Encoder().encode(encryptedByteValue);
        String encryptedValue64toHex = stringToHex(encryptedValue64);
        return encryptedValue64toHex;
               
    }
    
    public static String decrypt(String value) throws Exception
    {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(Crypt.ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        String valuetoHex = hexToString(value);
        byte [] decryptedValue64 = new BASE64Decoder().decodeBuffer(valuetoHex);
        byte [] decryptedByteValue = cipher.doFinal(decryptedValue64);
        String decryptedValue = new String(decryptedByteValue,"utf-8");
        return decryptedValue;
                
    }
    
    private static Key generateKey() throws Exception 
    {
        Key key = new SecretKeySpec(Crypt.KEY.getBytes(),Crypt.ALGORITHM);
        return key;
    }
    
    public static String stringToHex(String input) throws UnsupportedEncodingException
    {
        if (input == null) throw new NullPointerException();
        return asHex(input.getBytes());
    }
 
    public static String hexToString(String txtInHex)
    {
        byte [] txtInByte = new byte [txtInHex.length() / 2];
        int j = 0;
        for (int i = 0; i < txtInHex.length(); i += 2)
        {
                txtInByte[j++] = Byte.parseByte(txtInHex.substring(i, i + 2), 16);
        }
        return new String(txtInByte);
    }
    
    private static String asHex(byte[] buf)
    {
        char[] chars = new char[2 * buf.length];
        for (int i = 0; i < buf.length; ++i)
        {
            chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
        }
        return new String(chars);
    }
}
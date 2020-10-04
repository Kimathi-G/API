/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;
import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
/**
 *
 * @author User
 */
public class Utilities {
 
       public static StringWriter stringifyStackTrace(Exception ex){
        StringWriter stringWriter = new StringWriter();
            ex.printStackTrace(new PrintWriter(stringWriter));
            return stringWriter;
    }
       
    public static String GenarateTransactionID(){
         Random rand = new Random();   
        // Generate random integers in range 0 to 999 
        return "L"+rand.nextInt(10000); 
    }
    
    
    
   public static byte[] HmacSHA256(String data) throws Exception {
        String algorithm="HmacSHA256";
        String key = "KimGat";
        Mac mac = Mac.getInstance(algorithm);    
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), algorithm); 
        mac.init(secretKey);
        return mac.doFinal(data.getBytes("UTF8"));
}
   
   
    
}

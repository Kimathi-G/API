/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.io.PrintWriter;
import java.io.StringWriter;

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
}

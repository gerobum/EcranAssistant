/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uses;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maillot
 */
public class Usefull {
    public static String conversionEncoding(String o, String co, String cd) {
        try {
            return Charset.forName(cd).decode(ByteBuffer.wrap(o.getBytes(co))).toString();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Usefull.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
                       
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Scanner;
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

    public static void changeTo(String name) throws FileNotFoundException {
        String adresse, passwd;
        try (Scanner in = new Scanner(new File(".adresse-"+name))) {
            adresse = in.nextLine();
        }
        try (Scanner in = new Scanner(new File(".passwd-"+name))) {
            passwd = in.nextLine();
        }
        
        try (PrintWriter out = new PrintWriter(".adresse")) {
            out.println(adresse);
        }
        try (PrintWriter out = new PrintWriter(".passwd")) {
            out.println(passwd);
        }
   }
}

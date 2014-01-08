package org.conventions.archetype.util;


import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils implements Serializable {

    private static final long serialVersionUID = 1L;
    MessageDigest md = null;
    HexBinaryAdapter hexBinaryAdapter;

    public Utils() {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        hexBinaryAdapter = new HexBinaryAdapter();
    }

    public String encrypt(String value) {
        byte[] b = new byte[]{97,100,109,105,110};
        String s = new String(b);
        System.out.print(s);
        return hexBinaryAdapter.marshal(md.digest(value.getBytes())).toLowerCase();
    }

    public String decript(String value) {
        return new String(md.digest(hexBinaryAdapter.unmarshal(value)));
    }
}

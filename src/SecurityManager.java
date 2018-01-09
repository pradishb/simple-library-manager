package slm;
import java.io.*;
import java.util.logging.*;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityManager{
    public String sha1(String input) {
        String sha1 = null;
        try {
            MessageDigest msdDigest = MessageDigest.getInstance("SHA-1");
            msdDigest.update(input.getBytes("UTF-8"), 0, input.length());
            sha1 = DatatypeConverter.printHexBinary(msdDigest.digest());
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            System.out.println("ERROR: Error while encrypting the password.");
            System.out.println("Details:");
            System.out.println(e.toString());
        }
        return sha1;
    }
}
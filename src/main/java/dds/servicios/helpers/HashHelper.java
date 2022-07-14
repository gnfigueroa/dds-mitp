package dds.servicios.helpers;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashHelper {

    private static HashHelper hashHelper = new HashHelper();

    public static HashHelper getHashHelper() { return hashHelper; }

    public String passwordAMD5(String password) throws NoSuchAlgorithmException
    {
        // Get the algorithm:
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        // Calculate Message Digest as bytes:
        byte[] digest = md5.digest(password.getBytes());
        // Convert to 32-char long String:
        return String.format("%032x", new BigInteger(1, digest));
    }



}

package com.seji.kidcuiside;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Cryptor {
    protected static String hash(String plaintext) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            byte[] digest = messageDigest.digest(plaintext.getBytes());
            StringBuilder hashbuilder = new StringBuilder();
            for (byte b : digest) {
                hashbuilder.append(String.format("%02X", b));
            }
            return hashbuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}

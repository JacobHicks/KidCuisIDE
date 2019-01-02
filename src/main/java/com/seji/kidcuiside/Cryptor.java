package com.seji.kidcuiside;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Cryptor {
    private static String encrypt(String plaintext) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            byte[] digest = messageDigest.digest(plaintext.getBytes());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "hol up";
    }
}

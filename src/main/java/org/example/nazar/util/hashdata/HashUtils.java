package org.example.nazar.util.hashdata;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {

    /**
     * Generates a hash using the specified algorithm.
     *
     * @param input the input string to hash
     * @param algorithm the name of the algorithm to use (e.g., "SHA-256", "SHA-1", "MD5")
     * @return the generated hash as a hexadecimal string
     * @throws IllegalArgumentException if the specified algorithm is not found
     */
    public static String generateHash(String input, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Error generating hash: Algorithm " + algorithm + " not found", e);
        }
    }

    /**
     * Converts a byte array to a hexadecimal string.
     *
     * @param hash the byte array to convert
     * @return the hexadecimal string representation of the byte array
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Main method for testing purposes.
     * Generates and prints hashes for different algorithms.
     */
    public static void main(String[] args) {
        String input = "Hello, World!";

        System.out.println("SHA-256: " + generateHash(input, "SHA-256"));
        System.out.println("SHA-1: " + generateHash(input, "SHA-1"));
        System.out.println("MD5: " + generateHash(input, "MD5"));
    }
}
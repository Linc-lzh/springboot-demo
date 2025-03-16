package com.xs.encry;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5BruteForce {

    public static void main(String[] args) {
        String targetHash = "fb90dfc3d397561d3c1a9b27fb3b35d0"; // MD5 for "xushu"
        String charset = "abcdefghijklmnopqrstuvwxyz";
        int maxLength = 7; // 设定最大长度

        String result = bruteForceMD5(targetHash, charset, maxLength);
        if (result != null) {
            System.out.println("Found matching string: " + result);
        } else {
            System.out.println("No matching string found.");
        }
    }

    private static String bruteForceMD5(String targetHash, String charset, int maxLength) {
        for (int length = 1; length <= maxLength; length++) {
            char[] currentAttempt = new char[length];
            String result = bruteForceMD5Recursive(targetHash, charset, currentAttempt, 0);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private static String bruteForceMD5Recursive(String targetHash, String charset, char[] currentAttempt, int position) {
        if (position == currentAttempt.length) {
            return null; // Reached the end of current length without finding a match
        }

        for (int i = 0; i < charset.length(); i++) {
            currentAttempt[position] = charset.charAt(i);
            String attemptString = new String(currentAttempt, 0, position + 1);
            String attemptHash = DigestUtils.md5Hex(attemptString);

            if (attemptHash.equals(targetHash)) {
                return attemptString;
            }

            String result = bruteForceMD5Recursive(targetHash, charset, currentAttempt, position + 1);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
}
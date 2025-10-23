package org.example.sistemalogin.Utils;

public class PasswordUtils {
    public static String encryptPassword(String password) {
        return Hash.generarHash(password);
    }

    public static boolean verifyPassword(String inputPassword, String storedHash) {
        String inputHash = encryptPassword(inputPassword);
        return inputHash.equals(storedHash);
    }

    public static boolean isPasswordStrong(String password) {
        if (password == null ||password.length() < 8) return false;

        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if ("!@#$%^&*()-_+=<>?/{}~|".indexOf(c) >= 0) hasSpecial = true;
        }
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
}

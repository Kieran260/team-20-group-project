package com.iamin.data.validation;

import org.jsoup.Jsoup;

public class Validation {

    // Checks a character is alphanumeric
    public static boolean isAlphaNumeric(char char1) {
        return (char1 >= 'a' && char1 <= 'z') || (char1 >= 'A' && char1 <= 'Z') || (char1 >= '0' && char1 <= '9');
    }
    
    // Check that username does not already exist in table
    // Check that length is 8 characters and alphanumeric
    public static boolean userNameValidation(String username) {
        // TODO: check username not already in table 

        // Check length
        if (username.length() != 8) {
            return false;
        }
        // Check for SQL injection
        if (isSqlInjection(username)) {
            return false;
        }
        // Check alphanumeric
        else {
            for (int i = 0; i < username.length(); i++) {
                if (!isAlphaNumeric(username.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    // Check password is 8+ characters and contains one number
    // Check confirmPassword is the same as password
    public static boolean passwordValidation(String password, String confirmPassword) {
        
        if (password.length() < 8) {
            return false;
        } else {
            for (int i = 0; i < password.length(); i++) {
                if (Character.isDigit(password.charAt(i))) {
                    if (password.equals(confirmPassword)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
    
    // Checks for SQL injections
    public static boolean isSqlInjection(String message) {
        // Common SQL injection keywords
        String[] sqlKeywords = {"SELECT", "INSERT", "UPDATE", "DELETE", "CREATE", "DROP", "ALTER", "EXECUTE", "TRUNCATE"};
            
        // Loop through the message and check for SQL injection keywords
        for (String keyword : sqlKeywords) {
            if (message.toUpperCase().contains(keyword)) {
                return true;
            }
        }
            
        // No SQL injection keywords found
        return false;
    }

    // Function to sanitize inputs
    public static String sanitizeInput(String input) {
        // Remove any HTML tags and attributes from the input using Jsoup
        String sanitized = Jsoup.clean(input,null);
        // Return the sanitized input
        return sanitized;
    }
}

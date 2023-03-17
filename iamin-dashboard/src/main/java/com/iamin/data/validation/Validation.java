package com.iamin.data.validation;

public class Validation {

    // Checks a character is alphanumeric
    public static boolean isAlphaNumeric(char char1) {
        return (char1 >= 'a' && char1 <= 'z') || (char1 >= 'A' && char1 <= 'Z') || (char1 >= '0' && char1 <= '9');
    }
    
    // Check that username does not already exist in table
    // Check that length is 8 characters and alphanumeric
    public static boolean userNameValidation(String username) {

        int usernameLength = username.length();

        // TODO: check username not already in table 

        // Check length
        if (usernameLength != 8) {
            return false;
        }

        // Check alphanumeric
        else {
            for (int i = 0; i < usernameLength; i++) {
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

        int passwordLength = password.length();
        
        if (passwordLength < 8) {
            return false;
        }
        else {
            for (int i = 0; i < passwordLength; i++) {
                if (Character.isDigit(password.charAt(i))) {
                    if (password.equals(confirmPassword)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}

package com.iamin.data.validation;

import org.jsoup.Jsoup;
import com.iamin.data.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.jsoup.safety.Safelist;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Validation {
    private final LoginService loginService;

    @Autowired
    public Validation(LoginService loginService) {
        this.loginService = loginService;
    }

    // Checks a character is alphanumeric
    public boolean isAlphaNumeric(char char1) {
        return (char1 >= 'a' && char1 <= 'z') || (char1 >= 'A' && char1 <= 'Z') || (char1 >= '0' && char1 <= '9');
    }



    public boolean usernameExists(String username) {
        return loginService.checkIfUsernameExists(username);
    }

    // Check that username does not already exist in table and no SQL injection
    // Check that length is 8 characters and alphanumeric
    public String usernameValidation(String username) {

        // Check username does not already exist
        if (usernameExists(username)) {
            return "Username is taken";
        }

        // Check length
        if (username.length() != 8) {
            return "Username must be exactly 8 characters in length";
        }
        // Check for SQL injection
        if (isSqlInjection(username)) {
            return "Username contains a forbidden keyword";
        }
        // Check alphabetic
        for (int i = 0; i < username.length(); i++) {
            if (!Character.isAlphabetic(username.charAt(i))) {
                return "Username must contain alphabetic characters only";
            }
        }

        return "";
    }

    // Check password is 8+ characters and contains one number
    // Check confirmPassword is the same as password
    public String passwordValidation(String password, String confirmPassword) {

        boolean containsDigit = false;
        boolean containsAlpha = false;

        // check length
        if (password.length() < 8 || password.length() > 20) {
            return "Password must be between 8 and 20 characters long";
        }
        // check = confirmPassword
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match, please try again";
        }

        // Check for both letters and numbers
        for (int i = 0; i < password.length(); i++) {

            if (!containsDigit && Character.isDigit(password.charAt(i))) {
                containsDigit = true;
            } else if (!containsAlpha && Character.isAlphabetic(password.charAt(i))) {
                containsAlpha = true;
            }
        }

        // Returns error message if password does not contain both letters and numbers
        if (!(containsAlpha && containsDigit)) {
            return "Password must contain at least one letter and one number";
        }
        return "";
    }

    // Check address line 1 and 2 contain only alphanumeric, space, (-,'.&'), up to
    // 100 chars
    // Check city contains letters and spaces only, up to 50 chars
    // Check postcode in format AB1 0AA, EH10 4BF, W1A 0AX, M1 1AE etc
    public boolean addressValidation(String addressLine1, String addressLine2, String city, String postcode) {

        String ADDRESS_REGEX = "^[\\w\\s\\-,'/\\.&]{1,100}$";
        String CITY_REGEX = "^[A-Za-z\\s]{1,50}$";
        String POSTCODE_REGEX = "^[A-Za-z]{1,2}\\d{1,2}[A-Za-z]?\\s*\\d{1}[A-Za-z]{2}$";

        Pattern patternAddressLine = Pattern.compile(ADDRESS_REGEX);
        Pattern patternCity = Pattern.compile(CITY_REGEX);
        Pattern patternPostcode = Pattern.compile(POSTCODE_REGEX);

        Matcher matcherAddressLine1 = patternAddressLine.matcher(addressLine1);
        Matcher matcherAddressLine2 = patternAddressLine.matcher(addressLine2);
        Matcher matcherCity = patternCity.matcher(city);
        Matcher matcherPostcode = patternPostcode.matcher(postcode);

        return (matcherAddressLine1.matches() && matcherAddressLine2.matches()
                && matcherCity.matches() && matcherPostcode.matches());

    }

    // Checks phone number is in one of the below formats:
    // (123) 456-7890, (123)-456-7890, 123-456-7890, 123.456.7890, 1234567890, 01234
    // 435467
    public boolean phoneValidation(String phoneNumber) {

        String PHONE_REGEX = "^(\\(?\\d{3}\\)?[\\s.-]?){1,2}\\d{3}[\\s.-]?\\d{4}$";

        Pattern pattern = Pattern.compile(PHONE_REGEX);
        Matcher matcher = pattern.matcher(phoneNumber);

        return matcher.matches();

    }

    // Checks for SQL injections
    public boolean isSqlInjection(String message) {
        // Common SQL injection keywords
        String[] sqlKeywords = { "SELECT", "INSERT", "UPDATE", "DELETE", "CREATE", "DROP", "ALTER", "EXECUTE",
                "TRUNCATE" };

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
    public String sanitizeInput(String input) {
        // Remove any HTML tags and attributes from the input using Jsoup
        String sanitized = Jsoup.clean(input, Safelist.none());
        // Return the sanitized input
        return sanitized;
    }

}

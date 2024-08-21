package com.samcore.leafdisease.components;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationLogic {
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return true;
        } else {
            //android Regex to check the email address Validation
            return !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }
    public static boolean isValidPassword( String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return !matcher.matches();

    }
    // International Phone Numbers
    public boolean isValidPhoneNumber(String phoneNumber) {
        // Regex to check valid phone number
        String regex = "^[+]{1}(?:[0-9\\-\\(\\)\\/\\.]\\s?){6,15}[0-9]{1}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);
        // If the phone number
        // is empty return false
        if (phoneNumber == null) {
            return false;
        }
        // Pattern class contains matcher()
        // method to find matching between
        // given phone number  using regex
        Matcher m = p.matcher(phoneNumber);
        // Return if the phone number
        // matched the ReGex
        return m.matches();
    }
}

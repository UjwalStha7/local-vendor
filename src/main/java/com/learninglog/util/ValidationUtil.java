package com.learninglog.util;

public class ValidationUtil {

//    user name validation

    public static boolean isValidUsername(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }

// first character name must be a characters
        if (!Character.isLetter(name.charAt(0))) {
            return false;
        }

// except char firstcharacter other can be  letter,digit,or underscore
        for (int i = 1; i < name.length(); i++) {
            char ch = name.charAt(i);

            if (!Character.isLetterOrDigit(ch) && ch != '_') {
                return false;
            }
        }
        return true;
    }


//    validate email
    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
//    validating password
    public static String isValidatePassword(String password) {
        if (password == null || password.isBlank()) {
            return "Password cannot be empty.";
        }

        if (password.length() < 8) {
            return "Password must be at least 8 characters long.";
        }

        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (Character.isWhitespace(ch)) {
                return "Password must not contain spaces.";
            }
        }
        return null;
    }

//    Validate Password isconfimres
    public static boolean doPasswordMatch(String password,String confirmedPassword){

        if(!(password !=null && password.equals(confirmedPassword))){
            return false;
        }
        return true;
    }

//    validate phone number
public static boolean isValidPhoneNumber(String phone) {
    if (phone == null || phone.length() != 10) {
        return false;
    }
    for (int i = 0; i < phone.length(); i++) {
        char ch = phone.charAt(i);
        if (!Character.isDigit(ch)) {
            return false;
        }
    }
    return true;
}
}

package com.learninglog.util;
import org.mindrot.jbcrypt.BCrypt;
public class PasswordUtil {

    private static final  int COST = 10;

//    Hashing password
    public static String getHashpassword(String password){
        String salt = BCrypt.gensalt(COST);
        return BCrypt.hashpw(password,salt);
    }

//    To compare the hash dfor login
    public static  boolean checkpassword(String actualpassword ,String haspassword){
        return BCrypt.checkpw(actualpassword,haspassword);
    }
}

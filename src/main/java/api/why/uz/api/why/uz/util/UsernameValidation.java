package api.why.uz.api.why.uz.util;

public class UsernameValidation {

    public static boolean isEmail(String username){
        return username.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public static boolean isPhone(String username) {
        // Phone number Uzb
        return username.matches("^\\+998\\d{9}$");
    }
}

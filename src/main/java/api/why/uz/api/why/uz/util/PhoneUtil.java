package api.why.uz.api.why.uz.util;

public final class PhoneUtil {

    private PhoneUtil() {}

    public static String toLocalPhone(String phone) {
        if (phone == null) {
            return null;
        }
        phone = phone.trim();
        if (phone.startsWith("+")) {
            return phone;
        } else {
            return "+" + phone;
        }
    }
}

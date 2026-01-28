package api.why.uz.api.why.uz.util;

public class PageUtil {
    public static int page(int value){
        return value <= 0?1 : value-1;
    }
}

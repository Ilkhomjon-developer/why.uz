package api.why.uz.api.why.uz.util;

public final class PageUtil {

    private PageUtil(){}

    public static int page(int value){
        return value <= 0?1 : value-1;
    }
}

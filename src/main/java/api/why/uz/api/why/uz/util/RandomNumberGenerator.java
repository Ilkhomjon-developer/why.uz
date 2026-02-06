package api.why.uz.api.why.uz.util;

import java.util.Random;

public final class RandomNumberGenerator {

    private static final Random random = new Random();

    private RandomNumberGenerator(){}

    public static int generate(){

       return random.nextInt(10000, 99999);
    }

}
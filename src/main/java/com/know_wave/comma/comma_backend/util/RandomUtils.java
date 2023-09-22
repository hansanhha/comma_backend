package com.know_wave.comma.comma_backend.util;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

    public static int sixDigitRandom() {
        return ThreadLocalRandom.current().nextInt(888888) + 111111;
    }
}

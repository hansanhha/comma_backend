package know_wave.comma.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class GenerateUtils {

    public static int generateSixRandomCode() {
        return ThreadLocalRandom.current().nextInt(888888) + 111111;
    }

    public static String generatedRandomCode() {
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmm");
        String dateString = now.format(formatter);
        return uuid + dateString;
    }
}

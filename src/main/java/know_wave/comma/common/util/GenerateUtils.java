package know_wave.comma.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class GenerateUtils {

    public static String generateRandomCode() {
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmm");
        String dateString = now.format(formatter);
        return uuid + dateString;
    }
}

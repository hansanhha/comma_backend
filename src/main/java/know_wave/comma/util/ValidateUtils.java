package know_wave.comma.util;

import know_wave.comma.util.message.ExceptionMessageSource;
import org.springframework.util.Assert;

import java.util.Optional;

public class ValidateUtils {

    public static void throwIfEmpty(Iterable<?> iterable) {
        if (!iterable.iterator().hasNext()) {
            throw new IllegalArgumentException(ExceptionMessageSource.NOT_FOUND_VALUE);
        }
    }

    public static void throwIfEmpty(Optional<?> optional) {
        Assert.isTrue(optional.isPresent(), ExceptionMessageSource.NOT_FOUND_VALUE);
    }
}

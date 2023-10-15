package com.know_wave.comma.comma_backend.util;

import org.springframework.util.Assert;

import java.util.Optional;

import static com.know_wave.comma.comma_backend.util.message.ExceptionMessageSource.NOT_FOUND_VALUE;

public class ValidateUtils {

    public static void throwIfEmpty(Iterable<?> iterable) {
        if (!iterable.iterator().hasNext()) {
            throw new IllegalArgumentException(NOT_FOUND_VALUE);
        }
    }

    public static void throwIfEmpty(Optional<?> optional) {
        Assert.isTrue(optional.isPresent(), NOT_FOUND_VALUE);
    }
}

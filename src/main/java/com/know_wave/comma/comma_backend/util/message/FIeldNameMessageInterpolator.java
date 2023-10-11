package com.know_wave.comma.comma_backend.util.message;

import jakarta.validation.MessageInterpolator;
import org.hibernate.validator.internal.engine.MessageInterpolatorContext;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.springframework.util.StringUtils;

import java.util.Locale;

public class FIeldNameMessageInterpolator implements MessageInterpolator {

    private final MessageInterpolator defaultInterpolator = new ResourceBundleMessageInterpolator(new PlatformResourceBundleLocator("messages"));

    @Override
    public String interpolate(String messageTemplate, Context context) {
        return interpolate(messageTemplate, context, Locale.getDefault());
    }

    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale) {
        MessageInterpolatorContext interpolatorContext = (MessageInterpolatorContext) context;
        String fieldName = interpolatorContext.getPropertyPath().toString();
        return defaultInterpolator.interpolate(messageTemplate, context, locale).replace("{this.field}", fieldName);
    }
}

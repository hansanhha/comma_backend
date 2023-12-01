package know_wave.comma.alarm.util;

import jakarta.validation.MessageInterpolator;
import org.hibernate.validator.internal.engine.MessageInterpolatorContext;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;

import java.util.Locale;

public class FieldNameMessageInterpolator implements MessageInterpolator {

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

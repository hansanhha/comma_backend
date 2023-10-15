package know_wave.comma.util.message;

import jakarta.validation.MessageInterpolator;
import org.hibernate.validator.internal.engine.MessageInterpolatorContext;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;

import java.util.Locale;


public class ConstraintValueMessageInterpolator implements MessageInterpolator {

    private final MessageInterpolator defaultInterpolator = new ResourceBundleMessageInterpolator(new PlatformResourceBundleLocator("messages"));

    @Override
    public String interpolate(String messageTemplate, Context context) {
        return interpolate(messageTemplate, context, Locale.getDefault());
    }

    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale) {
        MessageInterpolatorContext interpolatorContext = (MessageInterpolatorContext) context;
        String value = interpolatorContext.getConstraintDescriptor().getAttributes().get("value").toString();
        return defaultInterpolator.interpolate(messageTemplate, context, locale).replace("{value}", value);
    }
}

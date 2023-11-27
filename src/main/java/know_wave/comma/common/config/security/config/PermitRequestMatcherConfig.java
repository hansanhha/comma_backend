package know_wave.comma.common.config.security.config;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;
import java.util.stream.Stream;


@Configuration
public class PermitRequestMatcherConfig {

    private List<RequestMatcher> userPermitRequestMatchers;
    private List<RequestMatcher> adminPermitRequestMatchers;
    private List<RequestMatcher> allPermitRequestMatchers;
    private final HandlerMappingIntrospector handlerIntroceptor;

    public PermitRequestMatcherConfig(HandlerMappingIntrospector handlerIntroceptor) {
        this.handlerIntroceptor = handlerIntroceptor;
    }

    @PostConstruct
    private void init() {
        MvcRequestMatcher refreshToken = new MvcRequestMatcher(handlerIntroceptor, "/account/*/refresh");
        MvcRequestMatcher getArduino = new MvcRequestMatcher(handlerIntroceptor, "/arduino/*");
        MvcRequestMatcher getArduinoList = new MvcRequestMatcher(handlerIntroceptor, "/arduinos/**");
        MvcRequestMatcher getPaymentRedirect = new MvcRequestMatcher(handlerIntroceptor, "/api/v1/payment/**");
        MvcRequestMatcher sseConnect = new MvcRequestMatcher(handlerIntroceptor, "/sse/connect");
        MvcRequestMatcher sseDisconnect = new MvcRequestMatcher(handlerIntroceptor, "/sse/disconnect/*");
        MvcRequestMatcher main = new MvcRequestMatcher(handlerIntroceptor, "/");
        MvcRequestMatcher preFlightRequest = new MvcRequestMatcher(handlerIntroceptor, "/**");
        refreshToken.setMethod(HttpMethod.GET);
        getArduino.setMethod(HttpMethod.GET);
        getArduinoList.setMethod(HttpMethod.GET);
        getPaymentRedirect.setMethod(HttpMethod.GET);
        sseConnect.setMethod(HttpMethod.GET);
        sseDisconnect.setMethod(HttpMethod.GET);
        main.setMethod(HttpMethod.GET);
        preFlightRequest.setMethod(HttpMethod.OPTIONS);

        userPermitRequestMatchers = List.of(
                new MvcRequestMatcher(handlerIntroceptor, "/account/signin"),
                new MvcRequestMatcher(handlerIntroceptor, "/account/signup"),
                new MvcRequestMatcher(handlerIntroceptor, "/account/email/r"),
                new MvcRequestMatcher(handlerIntroceptor, "/account/email/verify"),
                refreshToken,
                getArduino,
                getArduinoList,
                getPaymentRedirect,
                sseConnect,
                sseDisconnect,
                main,
                preFlightRequest
        );

        adminPermitRequestMatchers = List.of(
                new MvcRequestMatcher(handlerIntroceptor, "/admin/account/signup")
        );

        allPermitRequestMatchers = Stream.concat(
                userPermitRequestMatchers.stream(),
                adminPermitRequestMatchers.stream()
        ).toList();
    }

    public boolean isPermitRequest(HttpServletRequest request) {
        return allPermitRequestMatchers.stream().anyMatch(permitRequestMatcher -> permitRequestMatcher.matches(request));
    }

    public List<RequestMatcher> getUserPermitRequestMatchers() {
        return userPermitRequestMatchers;
    }

    public List<RequestMatcher> getAdminPermitRequestMatchers() {
        return adminPermitRequestMatchers;
    }

    public List<RequestMatcher> getAllPermitRequestMatchers() {
        return allPermitRequestMatchers;
    }
}

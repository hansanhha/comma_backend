package know_wave.comma.notification.sse.service;

import know_wave.comma.account.service.system.AccountQueryService;
import know_wave.comma.common.util.GenerateUtils;
import know_wave.comma.notification.alarm.util.ExceptionMessageSource;
import jakarta.servlet.http.HttpServletRequest;
import know_wave.comma.notification.sse.exception.SseEmitterSendException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SseEmitterService {

    private final AccountQueryService accountQueryService;
    private final Long DEFAULT_TIME_OUT = 60L * 1000 * 60;
    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    public SseEmitter create() {
        String accountId = accountQueryService.getAuthenticatedId();

        if (isConnected(accountId)) {
            throw new SseEmitterSendException(ExceptionMessageSource.SSE_EMITTER_ALREADY_EXIST);
        }

        SseEmitter emitter = new SseEmitter(DEFAULT_TIME_OUT);
        emitterMap.put(accountId, emitter);

        emitter.onCompletion(() -> {
                emitterMap.get(accountId).complete();
                emitterMap.remove(accountId);
        });
        emitter.onTimeout(() -> {
                emitterMap.get(accountId).complete();
                emitterMap.remove(accountId);
        });

        sendEvent(accountId, "connect", "EventSource connected");

        return emitter;
    }

    public void sendEvent(String id, String eventName, String data) {
        SseEmitter sseEmitter = emitterMap.get(id);

        try {
            sseEmitter.send(SseEmitter.event().name(eventName).data(data));
        } catch (IOException e) {
            throw new SseEmitterSendException(ExceptionMessageSource.SSE_EMITTER_SEND_ERROR);
        }
    }

    private boolean isConnected(String id) {
        return emitterMap.containsKey(id);
    }

    public void disconnect() {
        String accountId = accountQueryService.getAuthenticatedId();

        emitterMap.get(accountId).complete();
        emitterMap.remove(accountId);
    }
}

package know_wave.comma.notification.realtime.sse.service;

import know_wave.comma.account.service.system.AccountQueryService;
import know_wave.comma.common.entity.ExceptionMessageSource;
import know_wave.comma.notification.realtime.sse.exception.SseEmitterSendException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SseEmitterService {

    private final AccountQueryService accountQueryService;
    private static final Long DEFAULT_TIME_OUT = 60L * 1000 * 60;
    private final static String CONNECT_EVENT_NAME = "connect";
    private final static String CONNECT_EVENT_DATA = "EventSource connected";
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

        sendEvent(accountId, CONNECT_EVENT_NAME, CONNECT_EVENT_DATA);

        return emitter;
    }

    public void sendEvent(String id, String eventName, String data) {
        SseEmitter sseEmitter = emitterMap.get(id);

        try {
            sseEmitter.send(SseEmitter.event()
                    .name(eventName)
                    .data(data));
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

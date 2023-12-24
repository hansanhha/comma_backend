package know_wave.comma.common.notification.realtime.sse.service;

import jakarta.annotation.PostConstruct;
import know_wave.comma.account.service.system.AccountQueryService;
import know_wave.comma.common.entity.ExceptionMessageSource;
import know_wave.comma.common.notification.realtime.sse.entity.SseConnectType;
import know_wave.comma.common.notification.realtime.sse.exception.SseEmitterSendException;
import know_wave.comma.common.notification.realtime.sse.repositofy.SseEmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SseEmitterService {

    private final AccountQueryService accountQueryService;
    private final SseEmitterRepository sseEmitterRepository;

    private static final Map<SseConnectType, Long> TIMEOUT_MAP = new HashMap<>();
    private final static String CONNECT_EVENT_NAME = "connect";
    private final static String CONNECT_EVENT_DATA = "EventSource connected";

    @PostConstruct
    public void init() {
        TIMEOUT_MAP.put(SseConnectType.ARDUINO_ORDER, 15L * 60L * 1000L);
    }

    public SseEmitter create(SseConnectType connectType) {
        String accountId = accountQueryService.getAuthenticatedId();

        if (isConnected(accountId)) {
            throw new SseEmitterSendException(ExceptionMessageSource.SSE_EMITTER_ALREADY_EXIST);
        }

        SseEmitter emitter = new SseEmitter(TIMEOUT_MAP.get(connectType));
        sseEmitterRepository.save(accountId, emitter);

        emitter.onCompletion(() -> {
            sseEmitterRepository.findByAccountId(accountId).complete();
            sseEmitterRepository.remove(accountId);
        });

        emitter.onTimeout(() -> {
            sseEmitterRepository.findByAccountId(accountId).complete();
            sseEmitterRepository.remove(accountId);
        });

        send(accountId, CONNECT_EVENT_NAME, CONNECT_EVENT_DATA);

        return emitter;
    }

    public void send(String accountId, String eventName, String data) {
        SseEmitter sseEmitter = sseEmitterRepository.findByAccountId(accountId);

        try {
            sseEmitter.send(SseEmitter.event()
                    .name(eventName)
                    .data(data));
        } catch (IOException e) {
            throw new SseEmitterSendException(ExceptionMessageSource.SSE_EMITTER_SEND_ERROR);
        }
    }

    public boolean isConnected(String accountId) {
        return sseEmitterRepository.isContain(accountId);
    }

    public void disconnect() {
        String accountId = accountQueryService.getAuthenticatedId();

        sseEmitterRepository.findByAccountId(accountId).complete();
        sseEmitterRepository.remove(accountId);
    }
}

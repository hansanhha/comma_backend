package know_wave.comma.common.sse;

import know_wave.comma.common.util.GenerateUtils;
import know_wave.comma.alarm.util.ExceptionMessageSource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SseEmitterService {

    private final Map<String, Map<String, SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter create(HttpServletRequest request) {
        String remoteHost = request.getRemoteHost();

        if (alreadyConnectedHost(remoteHost)) {
            throw new SseEmitterSendException(ExceptionMessageSource.SSE_EMITTER_ALREADY_EXIST);
        }

        String emitterId = GenerateUtils.generateRandomCode();
        SseEmitter emitter = new SseEmitter(60L * 1000 * 60);
        emitters.put(remoteHost, Map.of(emitterId, emitter));

        emitter.onCompletion(() -> {
                log.info("SseEmitter complete. emitters size: {}", emitters.size());
                emitters.get(remoteHost).get(emitterId).complete();
                emitters.remove(remoteHost);
        });
        emitter.onTimeout(() -> {
                log.info("SseEmitter timeout. emitters size: {}", emitters.size());
                emitters.get(remoteHost).get(emitterId).complete();
                emitters.remove(remoteHost);
        });

        sendEvent(remoteHost, emitterId, "connect", emitterId);

        log.info("SseEmitter created. emitters size: {}", emitters.size());

        return emitter;
    }

    public void sendEvent(String remoteHost, String emitterId, String eventName, String data) {

        if (!alreadyConnectedHost(remoteHost)) {
            throw new SseEmitterSendException(ExceptionMessageSource.SSE_EMITTER_NOT_EXIST);
        }

        SseEmitter sseEmitter = emitters.get(remoteHost).get(emitterId);

        try {
            sseEmitter.send(SseEmitter.event().name(eventName).data(data));
        } catch (IOException e) {
            log.error("SseEmitter send error. id: {}, eventName: {}, data: {}", emitterId, eventName, data);
            throw new SseEmitterSendException(ExceptionMessageSource.SSE_EMITTER_SEND_ERROR);
        }
    }

    private boolean alreadyConnectedHost(String remoteHost) {
        return emitters.containsKey(remoteHost);
    }

    public void disconnect(HttpServletRequest request, String sseId) {

        if (Objects.isNull(request) || Objects.isNull(sseId)) {
            return;
        }

        String remoteHost = request.getRemoteHost();

        if (alreadyConnectedHost(remoteHost)) {
            emitters.get(remoteHost).get(sseId).complete();
            emitters.get(remoteHost).remove(sseId);
        }
    }
}

package com.know_wave.comma.comma_backend.common.sse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.know_wave.comma.comma_backend.util.GenerateUtils;
import com.know_wave.comma.comma_backend.util.message.ExceptionMessageSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SseEmitterService {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter create() {
        SseEmitter emitter = new SseEmitter(60L * 1000 * 60);

        String id = GenerateUtils.generatedRandomCode();
        emitters.put(id, emitter);

        emitter.onCompletion(() -> {
                log.info("SseEmitter complete. emitters size: {}", emitters.size());
                emitters.get(id).complete();
                emitters.remove(id);
        });
        emitter.onTimeout(() -> {
                log.info("SseEmitter timeout. emitters size: {}", emitters.size());
                emitters.get(id).complete();
                emitters.remove(id);
        });

        Map<String, Object> data = new HashMap<>();
        data.put("msg", "Comma EventStream Connected.");
        data.put("id", id);

        sendEvent(id, "connect", data);

        log.info("SseEmitter created. emitters size: {}", emitters.size());

        return emitter;
    }

    public void remove(String id) {
        emitters.get(id).complete();
        emitters.remove(id);
    }

    public void sendEvent(String id, String eventName, Map<String, Object> data) {
        SseEmitter sseEmitter = emitters.get(id);

        try {
            String jsonData = mapToJson(data);

            sseEmitter.send(SseEmitter.event().name(eventName).data(jsonData));

        } catch (JsonProcessingException je) {
            log.error("SseEmitter json error. id: {}, eventName: {}, data: {}", id, eventName, data);
            throw new SseEmitterSendException(ExceptionMessageSource.SSE_EMITTER_JSON_ERROR);

        } catch (IOException e) {
            log.error("SseEmitter send error. id: {}, eventName: {}, data: {}", id, eventName, data);
            throw new SseEmitterSendException(ExceptionMessageSource.SSE_EMITTER_SEND_ERROR);
        }
    }

    private String mapToJson(Map<String, Object> data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(data);
    }

}

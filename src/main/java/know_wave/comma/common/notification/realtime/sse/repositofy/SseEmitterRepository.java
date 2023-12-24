package know_wave.comma.common.notification.realtime.sse.repositofy;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseEmitterRepository {

    private static final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    public SseEmitter findByAccountId(String accountId) {
        return emitterMap.get(accountId);
    }

    public void save(String accountId, SseEmitter emitter) {
        emitterMap.put(accountId, emitter);
    }

    public void remove(String accountId) {
        emitterMap.remove(accountId);
    }


    public boolean isContain(String accountId) {
        return emitterMap.containsKey(accountId);
    }
}

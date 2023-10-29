package know_wave.comma.common.sse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class SseController {

    private final SseEmitterService sseEmitterService;

    @GetMapping(value = "/sse/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connectSse(HttpServletRequest request) {
        return sseEmitterService.create(request);
    }

    @GetMapping("/sse/disconnect/{sseId}")
    public void disconnectSse(@PathVariable("sseId") String sseId, HttpServletRequest request) {
        sseEmitterService.disconnect(request, sseId);
    }
}

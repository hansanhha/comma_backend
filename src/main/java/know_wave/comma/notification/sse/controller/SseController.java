package know_wave.comma.notification.sse.controller;

import jakarta.servlet.http.HttpServletRequest;
import know_wave.comma.notification.sse.service.SseEmitterService;
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
    public SseEmitter connectSse() {
        return sseEmitterService.create();
    }

    @GetMapping("/sse/disconnect")
    public void disconnectSse() {
        sseEmitterService.disconnect();
    }
}

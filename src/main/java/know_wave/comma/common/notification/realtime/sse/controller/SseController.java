package know_wave.comma.common.notification.realtime.sse.controller;

import know_wave.comma.common.notification.realtime.sse.entity.SseConnectType;
import know_wave.comma.common.notification.realtime.sse.service.SseEmitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
class SseController {

    private final SseEmitterService sseEmitterService;

    @GetMapping(value = "/sse/connect/arduino-order", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connectSse() {
        return sseEmitterService.create(SseConnectType.ARDUINO_ORDER);
    }

    @GetMapping("/sse/disconnect")
    public void disconnectSse() {
        sseEmitterService.disconnect();
    }
}

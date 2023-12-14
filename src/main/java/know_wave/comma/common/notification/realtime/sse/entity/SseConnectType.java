package know_wave.comma.common.notification.realtime.sse.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SseConnectType {

    ARDUINO_ORDER("arduinoOrder");

    private final String type;

}

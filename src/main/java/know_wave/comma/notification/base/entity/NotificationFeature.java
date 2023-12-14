package know_wave.comma.notification.base.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum NotificationFeature {

    ACCOUNT_AUTH_CODE("accountAuthCode"),
    ACCOUNT("account"),
    ARDUINO_ORDER("arduinoOrder"),
    ARDUINO_RESTOCK("arduinoRestock"),
    ARDUINO_COMMENT("arduinoComment"),
    COMMUNITY("community");

    final String feature;
}

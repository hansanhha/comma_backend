package know_wave.comma.alarm.service;

import java.util.Map;

public interface AlarmSender {

    default void send(String title, String message){}
    default void send(String dest, String title, String message){}
    default void send(String title, String message, Map<String, Object> param){}
    default void send(String dest, String title, String message, Map<String, Object> param){}
}

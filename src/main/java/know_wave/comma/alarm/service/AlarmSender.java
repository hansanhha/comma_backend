package know_wave.comma.alarm.service;

public interface AlarmSender {

    void send(String dest, String title, String message);
}

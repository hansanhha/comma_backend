package know_wave.comma.alarm.dto;

public record AlarmSendDto(String dest, String title, String content, AlarmFeature alarmFeature) {
}

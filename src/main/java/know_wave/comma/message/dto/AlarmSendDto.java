package know_wave.comma.message.dto;

public record AlarmSendDto(String dest, String title, String content, AlarmFeature alarmFeature) {
}

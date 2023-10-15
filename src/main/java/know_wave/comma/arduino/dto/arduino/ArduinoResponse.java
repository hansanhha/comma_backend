package know_wave.comma.arduino.dto.arduino;

import know_wave.comma.arduino.entity.Arduino;

import java.util.List;

public class ArduinoResponse {

    public static List<ArduinoResponse> ofList(List<Arduino> arduinos) {
        return arduinos.stream().map(ArduinoResponse::of).toList();
    }

    public static ArduinoResponse of(Arduino arduino) {
        return new ArduinoResponse(
                arduino.getId(),
                arduino.getName(),
                arduino.getCount(),
                arduino.getLikeCount(),
                arduino.getDescription(),
                arduino.getCategories());
    }

    public static ArduinoResponse ofWithoutLike(Arduino arduino) {
        return new ArduinoResponse(
                arduino.getId(),
                arduino.getName(),
                arduino.getCount(),
                0,
                arduino.getDescription(),
                arduino.getCategories()
        );
    }

    private ArduinoResponse(Long arduinoId, String arduinoName, int remainingCount, int likeCount, String arduinoDescription, List<String> arduinoCategories) {
        this.arduinoId = arduinoId;
        this.arduinoName = arduinoName;
        this.remainingCount = remainingCount;
        this.likeCount = likeCount;
        this.arduinoDescription = arduinoDescription;
        this.arduinoCategories = arduinoCategories;
    }

    private final Long arduinoId;

    private final String arduinoName;

    private final int remainingCount;
    private final int likeCount;

    private final String arduinoDescription;

    private final List<String> arduinoCategories;

    public Long getArduinoId() {
        return arduinoId;
    }

    public String getArduinoName() {
        return arduinoName;
    }

    public int getRemainingCount() {
        return remainingCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public String getArduinoDescription() {
        return arduinoDescription;
    }

    public List<String> getArduinoCategories() {
        return arduinoCategories;
    }
}

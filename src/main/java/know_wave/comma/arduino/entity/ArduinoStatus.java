package know_wave.comma.arduino.entity;

public enum ArduinoStatus {

    ENOUGH,
    LESS_THAN_10,
    SOLDOUT;

    public ArduinoStatus getArduinoStatus(int count) {
        return switch (count) {
            case 0 -> SOLDOUT;
            case 1, 2, 3, 4, 5, 6, 7, 8, 9 -> LESS_THAN_10;
            default -> ENOUGH;
        };
    }

}

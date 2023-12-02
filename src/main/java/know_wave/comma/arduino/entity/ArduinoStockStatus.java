package know_wave.comma.arduino.entity;

public enum ArduinoStockStatus {

    LESS_THAN_10,
    MORE_THAN_10,
    SOLDOUT;

    public static ArduinoStockStatus getArduinoStatus(int count) {
        return switch (count) {
            case 1,2,3,4,5,6,7,8,9 -> LESS_THAN_10;
            case 0 -> SOLDOUT;
            default -> MORE_THAN_10;
        };
    }

}

package know_wave.comma.arduino.component.dto;

import know_wave.comma.arduino.component.entity.ArduinoCategory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ArduinoCategoriesResponse {

    public static ArduinoCategoriesResponse of(List<ArduinoCategory> arduinoCategoryList) {
        return new ArduinoCategoriesResponse(
                arduinoCategoryList.stream()
                .map(ArduinoCategoryResponse::of)
                .toList());
    }

    private final List<ArduinoCategoryResponse> categories;

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class ArduinoCategoryResponse {
        private final Long id;
        private final String name;

        private static ArduinoCategoryResponse of(ArduinoCategory arduinoCategory) {
            return new ArduinoCategoryResponse(arduinoCategory.getId(), arduinoCategory.getName());
        }
    }

}

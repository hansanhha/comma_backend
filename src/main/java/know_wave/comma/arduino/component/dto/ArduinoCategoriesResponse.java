package know_wave.comma.arduino.component.dto;

import know_wave.comma.arduino.component.entity.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ArduinoCategoriesResponse {

    public static ArduinoCategoriesResponse to(List<Category> arduinoCategoryList) {
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

        private static ArduinoCategoryResponse of(Category arduinoCategory) {
            return new ArduinoCategoryResponse(arduinoCategory.getId(), arduinoCategory.getName());
        }
    }

}

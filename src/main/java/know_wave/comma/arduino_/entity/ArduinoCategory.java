package know_wave.comma.arduino_.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
public class ArduinoCategory {

    protected ArduinoCategory() {}

    public ArduinoCategory(Arduino arduino, Category category) {
        this.arduino = arduino;
        this.category = category;
    }

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arduino_id")
    private Arduino arduino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public static Map<Arduino, List<ArduinoCategory>> groupingByArduino(List<ArduinoCategory> categories) {
        return categories.stream()
                .collect(Collectors.groupingBy(ArduinoCategory::getArduino));
    }

    public Category getCategory() {
        return category;
    }

    public Arduino getArduino() {
        return arduino;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}

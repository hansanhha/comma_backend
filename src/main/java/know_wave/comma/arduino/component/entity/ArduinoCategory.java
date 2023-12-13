package know_wave.comma.arduino.component.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArduinoCategory {

    public ArduinoCategory(Long id) {
        this.id = id;
    }

    public ArduinoCategory(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue
    @Column(name = "arduino_category_id")
    private Long id;

    @Setter
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "categories")
    private List<Arduino> arduino;
}

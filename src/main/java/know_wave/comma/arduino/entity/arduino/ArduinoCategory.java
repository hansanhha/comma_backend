package know_wave.comma.arduino.entity.arduino;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class ArduinoCategory {

    @Id
    @GeneratedValue
    @Column(name = "arduino_category_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "categories")
    private List<Arduino> arduino;
}

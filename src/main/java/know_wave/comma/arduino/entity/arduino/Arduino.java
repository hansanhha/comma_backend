package know_wave.comma.arduino.entity.arduino;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import know_wave.comma.common.entity.BaseTimeEntity;

import java.util.List;

@Entity
public class Arduino extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "arduino_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    @Min(value = 0, message="{Min.count}")
    private int count;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private ArduinoStockStatus stockStatus;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "arduino_category",
            joinColumns = @JoinColumn(name = "arduino_id"),
            inverseJoinColumns = @JoinColumn(name = "arduino_category_id"))
    private List<ArduinoCategory> categories;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "arduino_photo",
            joinColumns = @JoinColumn(name = "arduino_id"),
            inverseJoinColumns = @JoinColumn(name = "arduino_photo_id"))
    private List<ArduinoPhoto> photos;
}

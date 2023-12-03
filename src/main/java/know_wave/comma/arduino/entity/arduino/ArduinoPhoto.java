package know_wave.comma.arduino.entity.arduino;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class ArduinoPhoto {

    @Id
    @GeneratedValue
    @Column(name = "arduino_photo_id")
    private Long id;

    private String url;

    private String fileName;

    private String fileUrl;

    private String fileType;

    private Long size;

    @ManyToMany(mappedBy = "photos")
    private List<Arduino> arduino;
}

package know_wave.comma.arduino.component.entity;

import jakarta.persistence.*;
import know_wave.comma.common.entity.CreateTimeEntity;
import know_wave.comma.common.entity.DeleteEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArduinoPhoto extends CreateTimeEntity {

    public static ArduinoPhoto of(String fileUuid, String fileName, String filePath, Long size, Arduino arduino) {
        return new ArduinoPhoto(fileUuid, fileName, filePath, size, arduino);
    }

    private ArduinoPhoto(String fileUuid, String fileName, String filePath, Long size, Arduino arduino) {
        this.fileUuid = fileUuid;
        this.fileName = fileName;
        this.filePath = filePath;
        this.size = size;
        this.arduino = arduino;
    }

    @Id
    @GeneratedValue
    @Column(name = "arduino_photo_id")
    private Long id;

    private String fileUuid;

    private String fileName;

    private String filePath;

    private Long size;

    @Embedded
    private DeleteEntity delete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arduino_id")
    private Arduino arduino;

    public static void delete(ArduinoPhoto arduinoPhoto) {
        arduinoPhoto.delete = DeleteEntity.delete();
    }

    public static void deleteList(List<ArduinoPhoto> arduinoPhotos) {
        arduinoPhotos.forEach(ArduinoPhoto::delete);
    }
}

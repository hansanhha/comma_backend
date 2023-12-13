package know_wave.comma.arduino.component.repository;

import know_wave.comma.arduino.component.entity.ArduinoPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArduinoPhotoRepository extends JpaRepository<ArduinoPhoto, Long> {

    @Query("select p from ArduinoPhoto p where p.fileUuid in :deletedPhotoFiles")
    List<ArduinoPhoto> findAllByUuid(List<String> deletedPhotoFiles);
}

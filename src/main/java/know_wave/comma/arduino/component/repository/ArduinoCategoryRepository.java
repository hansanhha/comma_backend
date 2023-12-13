package know_wave.comma.arduino.component.repository;

import know_wave.comma.arduino.component.entity.ArduinoCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ArduinoCategoryRepository extends CrudRepository<ArduinoCategory, Long> {
    Optional<ArduinoCategory> findByName(String categoryName);
}

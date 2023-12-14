package know_wave.comma.arduino.component.repository;

import know_wave.comma.arduino.component.entity.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ArduinoCategoryRepository extends CrudRepository<Category, Long> {
    Optional<Category> findByName(String categoryName);
}

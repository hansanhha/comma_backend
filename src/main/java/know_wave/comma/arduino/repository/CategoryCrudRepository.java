package know_wave.comma.arduino.repository;

import know_wave.comma.arduino.entity.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoryCrudRepository extends CrudRepository<Category, Long> {

    Optional<Category> findByName(String name);

}

package know_wave.comma.arduino_.repository;

import know_wave.comma.arduino_.entity.Arduino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ArduinoRepository extends JpaRepository<Arduino, Long> {

    Optional<Arduino> findByName(String name);

    @Query("select a " +
            "from Arduino a " +
            "join fetch a.categories ac " +
            "join fetch ac.category " +
            "where a.id = :id")
    Optional<Arduino> findFetchCategoriesById(Long id);
}

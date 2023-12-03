package know_wave.comma.arduino_.repository;

import know_wave.comma.arduino_.entity.Arduino;
import know_wave.comma.arduino_.entity.ArduinoCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArduinoCategoryRepository extends CrudRepository<ArduinoCategory, Long> {

    @Query("select ac " +
            "from ArduinoCategory ac " +
            "join fetch ac.category " +
            "join fetch ac.arduino " +
            "where ac.arduino = :arduino")
    List<ArduinoCategory> findAllFetchByArduino(Arduino arduino);

    @Query("select ac " +
            "from ArduinoCategory ac " +
            "join fetch ac.category " +
            "join fetch ac.arduino " +
            "where ac.arduino in :arduinos")
    List<ArduinoCategory> findALlFetchByArduinos(List<Arduino> arduinos);
}

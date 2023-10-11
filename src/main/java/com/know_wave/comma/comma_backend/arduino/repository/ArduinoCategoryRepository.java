package com.know_wave.comma.comma_backend.arduino.repository;

import com.know_wave.comma.comma_backend.arduino.entity.Arduino;
import com.know_wave.comma.comma_backend.arduino.entity.ArduinoCategory;
import jakarta.persistence.Column;
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

package know_wave.comma.arduino.component.repository;

import know_wave.comma.arduino.component.entity.Arduino;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArduinoRepository extends JpaRepository<Arduino, Long> {

}

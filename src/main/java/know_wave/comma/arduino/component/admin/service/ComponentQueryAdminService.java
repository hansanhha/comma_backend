package know_wave.comma.arduino.component.admin.service;

import jakarta.persistence.EntityNotFoundException;
import know_wave.comma.arduino.component.dto.ArduinoDetailResponse;
import know_wave.comma.arduino.component.entity.Arduino;
import know_wave.comma.arduino.component.repository.ArduinoRepository;
import know_wave.comma.common.entity.ExceptionMessageSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ComponentQueryAdminService {

    private final ArduinoRepository arduinoRepository;

    public ArduinoDetailResponse getArduinoDetail(Long id) {
        Arduino arduino = arduinoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ExceptionMessageSource.NOT_FOUND_VALUE));
        return ArduinoDetailResponse.to(arduino);
    }

}

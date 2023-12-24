package know_wave.comma.arduino.component.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class ArduinoCreateForms {

    @JsonProperty("arduino_create_forms")
    private List<ArduinoCreateForm> arduinoCreateForms;
}

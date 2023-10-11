package com.know_wave.comma.comma_backend.arduino.dto.arduino;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class ArduinoCreateFormList {

    public ArduinoCreateFormList() {
    }

    public ArduinoCreateFormList(List<ArduinoCreateForm> arduinoCreateForms) {
        this.arduinoCreateForms = arduinoCreateForms;
    }

    @NotEmpty(message = "{Required}")
    private List<ArduinoCreateForm> arduinoCreateForms;

    public List<ArduinoCreateForm> getArduinoCreateForms() {
        return arduinoCreateForms;
    }

    public void setArduinoCreateForms(List<ArduinoCreateForm> arduinoCreateForms) {
        this.arduinoCreateForms = arduinoCreateForms;
    }
}

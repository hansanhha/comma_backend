package com.know_wave.comma.comma_backend.order.dto;

import com.know_wave.comma.comma_backend.arduino.entity.Arduino;
import com.know_wave.comma.comma_backend.arduino.entity.ArduinoCategory;
import com.know_wave.comma.comma_backend.order.entity.Order;

import java.util.List;
import java.util.Map;

public class OrderArduino {

    public static OrderArduino of(Order order, Map<Arduino, List<ArduinoCategory>> categoryMap) {
        return new OrderArduino(order.getArduino().getId(),
                order.getArduino().getName(),
                order.getOrderArduinoCount(),
                categoryMap.get(order.getArduino())
                        .stream()
                        .map(arduinoCategory -> arduinoCategory.getCategory().getName())
                        .toList()
        );
    }

    public static OrderArduino of(Order order) {
        return new OrderArduino(order.getArduino().getId(),
                order.getArduino().getName(),
                order.getOrderArduinoCount(),
                null
        );
    }

    private OrderArduino(Long arduinoId, String arduinoName, int orderCount, List<String> categories) {
        this.arduinoId = arduinoId;
        this.arduinoName = arduinoName;
        this.orderCount = orderCount;
        this.categories = categories;
    }
    private final Long arduinoId;
    private final String arduinoName;
    private final int orderCount;

    private final List<String> categories;

    public Long getArduinoId() {
        return arduinoId;
    }

    public String getArduinoName() {
        return arduinoName;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public List<String> getCategories() {
        return categories;
    }
}

package com.know_wave.comma.comma_backend.payment.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class PaymentEventService {

    private

    public SseEmitter subscribe(Long id) {
        SseEmitter emitter = createEmitter(id);

        sendEvent(id, "Comma EventStream Connected. id : {}");
        return emitter;
    }
    
    public void sendEvent(Long id, Object event) {

    }

    private SseEmitter createEmitter(Long id) {
        return null;
    }
}

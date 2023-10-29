package know_wave.comma.payment.dto;

import org.springframework.http.*;
import org.springframework.util.MultiValueMap;

import java.net.URI;

public record WebEntityCreator() {

    public static HttpEntity<MultiValueMap<String, Object>> toPaymentEntity(MultiValueMap<String, Object> value) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return new HttpEntity<>(value, httpHeaders);
    }

    public static ResponseEntity<HttpHeaders> toRedirectEntity(URI uri, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uri);

        return new ResponseEntity<>(headers, status);
    }
}

package know_wave.comma.common.util;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    @GetMapping("/")
    public String statusCheck() {
        return "Comma Backend is running";
    }
}

package know_wave.comma.payment.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class CommaArduinoDepositPolicy {

    @Value("${payment.amount}")
    private int amount;

    @Value("${payment.tax-free-amount}")
    private int taxFreeAmount;

    @Value("${payment.product-name}")
    private String productName;

}

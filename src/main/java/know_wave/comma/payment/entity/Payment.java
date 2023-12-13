package know_wave.comma.payment.entity;

import jakarta.persistence.*;
import know_wave.comma.account.entity.Account;
import lombok.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Payment {

    @Id
    @GeneratedValue
    @Column(name = "payment_id")
    private Long id;

    @Column(updatable = false, nullable = false)
    private String paymentRequestId;

    @Setter
    @Column(updatable = false)
    private String externalApiTransactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private PaymentFeature paymentFeature;

    @Setter
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(updatable = false, nullable = false)
    private int amount;

    @Column(updatable = false, nullable = false)
    private int quantity;

    public static Payment of(String paymentRequestId, PaymentType paymentType, String transactionId,PaymentFeature paymentFeature, int amount, Account account, int quantity) {
        return new Payment(null, paymentRequestId, transactionId, account, paymentType, paymentFeature, PaymentStatus.REQUEST, amount, quantity);
    }

    public static String generatePaymentRequestId(String accountId, String orderNumber) {
        try {
            String originalString = accountId + orderNumber;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashbytes = digest.digest(originalString.getBytes(StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : hashbytes) {
                stringBuilder.append(String.format("%02x", b));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not generate hash from String", e);
        }
    }

    public boolean isPaymentComplete() {
        return paymentStatus == PaymentStatus.COMPLETE;
    }
}

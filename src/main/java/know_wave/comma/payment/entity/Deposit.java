package know_wave.comma.payment.entity;

import know_wave.comma.account.entity.Account;
import know_wave.comma.order.entity.OrderInfo;
import know_wave.comma.util.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Deposit extends BaseTimeEntity {

    public static Deposit of(PaymentType paymentType, String paymentRequestId, String transactionId, Account account, int amount, String productName, boolean paymentTermsAgreement, boolean personalInfoTermsAgreement) {
        return new Deposit(account,
                paymentRequestId,
                transactionId,
                DepositStatus.NONE,
                PaymentStatus.REQUEST,
                paymentType,
                null,
                amount,
                productName,
                paymentTermsAgreement,
                personalInfoTermsAgreement
        );
    }

    public Deposit(Account account, String paymentRequestId, String paymentTransactionId, DepositStatus depositStatus, PaymentStatus paymentStatus, PaymentType paymentType, PaymentMethodType paymentMethodType, int totalAmount, String itemName, boolean paymentTermsAgreement, boolean personalInfoTermsAgreement) {
        this.account = account;
        this.paymentRequestId = paymentRequestId;
        this.paymentTransactionId = paymentTransactionId;
        this.depositStatus = depositStatus;
        this.paymentStatus = paymentStatus;
        this.paymentType = paymentType;
        this.paymentMethodType = paymentMethodType;
        this.totalAmount = totalAmount;
        this.itemName = itemName;
        this.paymentTermsAgreement = paymentTermsAgreement;
        this.personalInfoTermsAgreement = personalInfoTermsAgreement;
    }

    @Id
    @GeneratedValue
    @Column(name = "deposit_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_info_id")
    private OrderInfo orderInfo;

    private String paymentRequestId;

    private String paymentTransactionId;

    @Enumerated(EnumType.STRING)
    private DepositStatus depositStatus;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private PaymentMethodType paymentMethodType;

    private int totalAmount;

    private String itemName;

    private LocalDateTime refundedDate;

    private boolean paymentTermsAgreement;

    private boolean personalInfoTermsAgreement;

    public void setDepositStatus(DepositStatus depositStatus) {
        this.depositStatus = depositStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setRefundedDate(LocalDateTime refundedDate) {
        this.refundedDate = refundedDate;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }
}

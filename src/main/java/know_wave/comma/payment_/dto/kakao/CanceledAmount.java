package know_wave.comma.payment_.dto.kakao;

public record CanceledAmount(
        int total,
        int tax_free,
        int vat,
        int point,
        int discount,
        int green_deposit
) {
}

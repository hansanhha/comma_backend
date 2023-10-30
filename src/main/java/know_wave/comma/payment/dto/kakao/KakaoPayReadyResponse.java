package know_wave.comma.payment.dto.kakao;

public record KakaoPayReadyResponse(String tid,
                                    String next_redirect_app_url,
                                    String next_redirect_mobile_url,
                                    String next_redirect_pc_url,
                                    String android_app_scheme,
                                    String ios_app_scheme,
                                    String created_at) {
}

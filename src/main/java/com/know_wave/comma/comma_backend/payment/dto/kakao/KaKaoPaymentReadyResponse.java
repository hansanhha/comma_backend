package com.know_wave.comma.comma_backend.payment.dto.kakao;

public record KaKaoPaymentReadyResponse(String tid,
                                        String next_redirect_app_url,
                                        String next_redirect_mobile_url,
                                        String next_redirect_pc_url,
                                        String android_app_scheme,
                                        String ios_app_scheme,
                                        String created_at) {
}

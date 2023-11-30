package know_wave.comma.message.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Feature {

    ACCOUNT("account"),
    ARDUINO("arduino"),
    COMMUNITY("community");

    final String name;
}

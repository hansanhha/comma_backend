package know_wave.comma.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Embeddable
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeleteEntity {

    private LocalDateTime deletedDate = null;
    private boolean deleted = false;

    public static DeleteEntity delete() {
        return new DeleteEntity(LocalDateTime.now(), true);
    }
}

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

    public static DeleteEntity notDelete() {
        return new DeleteEntity(null, false);
    }

    private LocalDateTime deletedDate;

    private boolean deleted;

    public static DeleteEntity delete() {
        return new DeleteEntity(LocalDateTime.now(), true);
    }
}

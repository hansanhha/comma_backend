package know_wave.comma.common.idempotency.repository;

import know_wave.comma.common.idempotency.entity.Idempotent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IdempotentRepository extends CrudRepository<Idempotent, String> {

}

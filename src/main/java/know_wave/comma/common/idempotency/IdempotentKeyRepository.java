package know_wave.comma.common.idempotency;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IdempotentKeyRepository extends CrudRepository<Idempotent, Long> {

    @Query("select i " +
            "from Idempotent i " +
            "where i.idempotentKey = :idempotentKey " +
            "and i.httpMethod = :httpMethod " +
            "and i.apiPath = :apiPath")
    Optional<Idempotent> findByIdempotentInfo(String idempotentKey, String httpMethod, String apiPath);
}

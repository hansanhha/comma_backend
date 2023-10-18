package com.know_wave.comma.comma_backend.common.idempotency;

import org.springframework.data.repository.CrudRepository;

public interface IdempotentKeyRepository extends CrudRepository<Idempotent, String> {
}

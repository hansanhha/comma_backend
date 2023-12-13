package know_wave.comma.common.idempotency.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import know_wave.comma.notification.alarm.util.ExceptionMessageSource;
import know_wave.comma.common.idempotency.dto.IdempotentRequest;
import know_wave.comma.common.idempotency.dto.IdempotentResponse;
import know_wave.comma.common.idempotency.dto.IdempotentSaveDto;
import know_wave.comma.common.idempotency.entity.HttpMethod;
import know_wave.comma.common.idempotency.entity.Idempotent;
import know_wave.comma.common.idempotency.exception.IdempotencyJacksonException;
import know_wave.comma.common.idempotency.exception.IdempotencyUnprocessableException;
import know_wave.comma.common.idempotency.repository.IdempotentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class IdempotencyService {

    private final IdempotentRepository idempotencyRepository;

    public boolean isIdempotent(IdempotentRequest request) {

        Optional<Idempotent> optionalIdempotent = idempotencyRepository.findById(request.getIdempotentKey());

        if (optionalIdempotent.isPresent()) {
            Idempotent idempotent = optionalIdempotent.get();

            if (!idempotent.getPayload().equals(request.getPayload())) {
                throw new IdempotencyUnprocessableException(ExceptionMessageSource.IDEMPOTENCY_UNPROCESSABLE_EXCEPTION);
            }

            return idempotent.equals(
                    request.getIdempotentKey(),
                    HttpMethod.valueOf(request.getHttpMethod()),
                    request.getApiPath());
        }

        return false;
    }

    public <T> Optional<IdempotentResponse<T>> get(String idempotentKey, Class<T> type) throws IdempotencyJacksonException {
        Optional<Idempotent> optionalIdempotent = idempotencyRepository.findById(idempotentKey);

        if (optionalIdempotent.isPresent()) {
            Idempotent idempotent = optionalIdempotent.get();
            ObjectMapper mapper = new ObjectMapper();
            try {
                T response = mapper.readValue(idempotent.getResponse(), type);
                return Optional.of(IdempotentResponse.of(idempotent.getResponseStatus(), response));
            } catch (JsonProcessingException e) {
                throw new IdempotencyJacksonException(ExceptionMessageSource.INVALID_IDEMPOENT_RESPONSE);
            }
        }

        return Optional.empty();
    }

    public void create(IdempotentSaveDto<?> idempotentDto) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String response = mapper.writeValueAsString(idempotentDto.getResponse());

            Idempotent idempotent = Idempotent.of(
                    idempotentDto.getIdempotentKey(),
                    HttpMethod.valueOf(idempotentDto.getHttpMethod()),
                    idempotentDto.getApiPath(),
                    idempotentDto.getPayload(),
                    idempotentDto.getResponseStatus(),
                    response);

            idempotencyRepository.save(idempotent);

        } catch (JsonProcessingException e) {
            throw new IdempotencyJacksonException(ExceptionMessageSource.INVALID_IDEMPOENT_RESPONSE);
        }

    }

    public void delete(String idempotentKey) {
        Optional<Idempotent> optionalIdempotent = idempotencyRepository.findById(idempotentKey);
        optionalIdempotent.ifPresent(idempotencyRepository::delete);
    }


}

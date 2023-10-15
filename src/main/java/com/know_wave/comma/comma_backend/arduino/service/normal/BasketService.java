package com.know_wave.comma.comma_backend.arduino.service.normal;

import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.account.service.normal.AccountQueryService;
import com.know_wave.comma.comma_backend.arduino.dto.basket.BasketDeleteRequest;
import com.know_wave.comma.comma_backend.arduino.dto.basket.BasketRequest;
import com.know_wave.comma.comma_backend.arduino.dto.basket.BasketResponse;
import com.know_wave.comma.comma_backend.arduino.entity.Arduino;
import com.know_wave.comma.comma_backend.arduino.entity.Basket;
import com.know_wave.comma.comma_backend.arduino.repository.BasketRepository;
import com.know_wave.comma.comma_backend.util.exception.EntityAlreadyExistException;
import com.know_wave.comma.comma_backend.util.ValidateUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.know_wave.comma.comma_backend.account.service.normal.AccountQueryService.getAuthenticatedId;
import static com.know_wave.comma.comma_backend.util.message.ExceptionMessageSource.*;

@Service
@Transactional
public class BasketService {

    private final AccountQueryService accountQueryService;
    private final ArduinoService arduinoService;
    private final BasketRepository basketRepository;

    public BasketService(AccountQueryService accountQueryService, ArduinoService arduinoService, BasketRepository basketRepository) {
        this.accountQueryService = accountQueryService;
        this.arduinoService = arduinoService;
        this.basketRepository = basketRepository;
    }

    public List<BasketResponse> getBasket() {

        Account account = accountQueryService.findAccount(getAuthenticatedId());
        List<Basket> baskets = getBasketsByAccount(account);

        ValidateUtils.throwIfEmpty(baskets);

        return BasketResponse.of(baskets);
    }

    public void addArduinoToBasket(BasketRequest request) {

        Account account = accountQueryService.findAccount(getAuthenticatedId());
        Arduino arduino = arduinoService.getArduino(request.getArduinoId());

        int orderCount = request.getContainedCount();

        if (arduino.isNotEnoughCount(orderCount)) {
            throw new IllegalArgumentException(NOT_ACCEPTABLE_REQUEST);
        }

        basketRepository.findByAccountAndArduino(account, arduino)
                .ifPresentOrElse(basket ->
                        {throw new EntityAlreadyExistException(ALREADY_IN_BASKET);},
                        () -> basketRepository.save(new Basket(account, arduino, orderCount)));
    }

    public void deleteArduinoFromBasket(BasketDeleteRequest request) {

        Account account = accountQueryService.findAccount(getAuthenticatedId());
        Arduino arduino = arduinoService.getArduino(request.getArduinoId());

        basketRepository.findByAccountAndArduino(account, arduino)
                .ifPresentOrElse(basketRepository::delete,
                        () -> {throw new EntityNotFoundException(NOT_FOUND_VALUE);});
    }

    public void updateArduinoFromBasket(BasketRequest request) {

        Account account = accountQueryService.findAccount(getAuthenticatedId());
        Arduino arduino = arduinoService.getArduino(request.getArduinoId());

        if (arduino.isNotEnoughCount(request.getContainedCount())) {
            throw new IllegalArgumentException(NOT_ACCEPTABLE_REQUEST);
        }

        basketRepository.findByAccountAndArduino(account, arduino)
                .ifPresentOrElse(basket ->
                                basket.setStoredArduinoCount(request.getContainedCount()),
                        () -> {throw new EntityNotFoundException(NOT_FOUND_VALUE);});
    }

    public void emptyBasket() {
        Account account = accountQueryService.findAccount(getAuthenticatedId());
        List<Basket> basket = getBasketsByAccount(account);

        basketRepository.deleteAll(basket);
    }

    public List<Basket> getBasketsByAccount(Account account) {
        List<Basket> arduinoList = basketRepository.findAllFetchArduinoByAccount(account);

        ValidateUtils.throwIfEmpty(arduinoList);

        return arduinoList;
    }
}

package know_wave.comma.arduino_.service.normal;

import jakarta.persistence.EntityNotFoundException;
import know_wave.comma.account.entity.Account;
import know_wave.comma.account.service.normal.AccountQueryService;
import know_wave.comma.arduino_.dto.basket.BasketDeleteRequest;
import know_wave.comma.arduino_.dto.basket.BasketRequest;
import know_wave.comma.arduino_.dto.basket.BasketResponse;
import know_wave.comma.arduino_.entity.Arduino;
import know_wave.comma.arduino_.entity.Basket;
import know_wave.comma.arduino_.repository.BasketRepository;
import know_wave.comma.alarm.exception.EntityAlreadyExistException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static know_wave.comma.alarm.util.ExceptionMessageSource.*;

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

        Account account = accountQueryService.findAccount(accountQueryService.getAuthenticatedId());
        List<Basket> baskets = getBasketsByAccount(account);

        return BasketResponse.of(baskets);
    }

    public void addArduinoToBasket(BasketRequest request) {

        Account account = accountQueryService.findAccount(accountQueryService.getAuthenticatedId());
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

        Account account = accountQueryService.findAccount(accountQueryService.getAuthenticatedId());
        Arduino arduino = arduinoService.getArduino(request.getArduinoId());

        basketRepository.findByAccountAndArduino(account, arduino)
                .ifPresentOrElse(basketRepository::delete,
                        () -> {throw new EntityNotFoundException(NOT_FOUND_VALUE);});
    }

    public void updateArduinoFromBasket(BasketRequest request) {

        Account account = accountQueryService.findAccount(accountQueryService.getAuthenticatedId());
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
        Account account = accountQueryService.findAccount(accountQueryService.getAuthenticatedId());
        List<Basket> basket = getBasketsByAccount(account);

        basketRepository.deleteAll(basket);
    }

    public List<Basket> getBasketsByAccount(Account account) {
        return basketRepository.findAllFetchArduinoByAccount(account);
    }
}

package know_wave.comma.arduino.cart.service;

import know_wave.comma.account.entity.Account;
import know_wave.comma.account.service.system.AccountQueryService;
import know_wave.comma.arduino.cart.dto.CartAddRequest;
import know_wave.comma.arduino.cart.dto.CartResponse;
import know_wave.comma.arduino.cart.dto.CartUpdateRequest;
import know_wave.comma.arduino.cart.dto.CartValidateStatus;
import know_wave.comma.arduino.cart.entity.Cart;
import know_wave.comma.arduino.cart.exception.BadArduinoStockStatusException;
import know_wave.comma.arduino.cart.exception.CartException;
import know_wave.comma.arduino.cart.exception.NotEnoughArduinoException;
import know_wave.comma.arduino.cart.exception.OverMaxQuantityException;
import know_wave.comma.arduino.cart.repository.CartRepository;
import know_wave.comma.arduino.component.entity.Arduino;
import know_wave.comma.arduino.component.service.ComponentQueryService;
import know_wave.comma.common.entity.ExceptionMessageSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final AccountQueryService accountQueryService;
    private final ComponentQueryService componentQueryService;
    private final CartRepository cartRepository;

    @Value("${arduino.max-order-quantity}")
    private int BASKET_MAX_QUANTITY;

    {
        BASKET_MAX_QUANTITY = 4;
    }

    public CartResponse getCart() {
        Account account = accountQueryService.findAccount();
        List<Cart> cartList = cartRepository.findAllByAccount(account);

        return CartResponse.to(cartList);
    }

    public List<Cart> findCartList() {
        Account account = accountQueryService.findAccount();
        return cartRepository.findAllByAccount(account);
    }

    public void addArduino(Long arduinoId, CartAddRequest addRequest) {
        Account account = accountQueryService.findAccount();
        Arduino arduino = componentQueryService.getArduino(arduinoId);
        final int containCount = addRequest.getCount();

        if (isAlreadyInBasket(arduino, account)) {
            throw new CartException(ExceptionMessageSource.ALREADY_IN_BASKET);
        }

        CartValidateStatus cartValidateStatus = Cart.validate(arduino, containCount, BASKET_MAX_QUANTITY);

        if (cartValidateStatus.isNotValid()) {
            throw inValidCartException(cartValidateStatus);
        }

        Cart cart = Cart.create(account, arduino, containCount);

        cartRepository.save(cart);
    }

    public void updateContainCount(Long basketId, CartUpdateRequest updateRequest) {
        Cart cart = findBasket(basketId);
        Arduino arduino = cart.getArduino();
        final int updatedCount = updateRequest.getUpdatedCount();

        CartValidateStatus cartValidateStatus = Cart.validate(arduino, updatedCount, BASKET_MAX_QUANTITY);

        if (cartValidateStatus.isNotValid()) {
            throw inValidCartException(cartValidateStatus);
        }

        cart.update(updatedCount);
    }

    private CartException inValidCartException(CartValidateStatus cartValidateStatus) {
        return switch (cartValidateStatus) {
            case BAD_ARDUINO_STATUS -> new BadArduinoStockStatusException(ExceptionMessageSource.BAD_ARDUINO_STOCK_STATUS);
            case OVER_MAX_QUANTITY -> new OverMaxQuantityException(ExceptionMessageSource.OVER_MAX_ARDUINO_QUANTITY);
            case NOT_ENOUGH_ARDUINO_STOCK -> new NotEnoughArduinoException(ExceptionMessageSource.NOT_ENOUGH_ARDUINO_STOCK);
            default -> new CartException(ExceptionMessageSource.UNABLE_TO_BASKET);
        };
    }

    public void deleteArduino(Long basketId) {
        Cart cart = findBasket(basketId);
        cartRepository.delete(cart);
    }

    public void deleteAllArduino() {
        Account account = accountQueryService.findAccount();
        List<Cart> cartList = cartRepository.findAllByAccount(account);

        if (!cartList.isEmpty()) {
            cartRepository.deleteAll(cartList);
        }
    }

    private boolean isAlreadyInBasket(Arduino arduino, Account account) {
        Optional<Cart> optionalBasket = cartRepository.findByArduinoAndAccount(arduino, account);

        return optionalBasket.isPresent();
    }

    private Cart findBasket(Long basketId) throws CartException {
        Optional<Cart> optionalBasket = cartRepository.findFetchArduinoById(basketId);
        return optionalBasket.orElseThrow(() -> new CartException(ExceptionMessageSource.NOT_FOUND_VALUE));
    }

}

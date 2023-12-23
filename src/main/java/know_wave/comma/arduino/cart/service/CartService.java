package know_wave.comma.arduino.cart.service;

import know_wave.comma.account.entity.Account;
import know_wave.comma.account.service.system.AccountQueryService;
import know_wave.comma.arduino.cart.dto.CartAddRequest;
import know_wave.comma.arduino.cart.dto.CartResponse;
import know_wave.comma.arduino.cart.dto.CartUpdateRequest;
import know_wave.comma.arduino.cart.dto.CartValidateStatus;
import know_wave.comma.arduino.cart.entity.Cart;
import know_wave.comma.arduino.cart.exception.CartException;
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
    private final ComponentQueryService arduinoInfoService;
    private final CartRepository cartRepository;

    @Value("${arduino.max-order-quantity}")
    private int BASKET_MAX_QUANTITY;

    public CartResponse getBasket() {
        Account account = accountQueryService.findAccount();
        List<Cart> cartList = cartRepository.findAllByAccount(account);

        return CartResponse.to(cartList);
    }

    public List<Cart> findBasketList() {
        Account account = accountQueryService.findAccount();
        return cartRepository.findAllByAccount(account);
    }

    public void addArduino(Long arduinoId, CartAddRequest addRequest) {
        Account account = accountQueryService.findAccount();
        Arduino arduino = arduinoInfoService.getArduino(arduinoId);
        final int containCount = addRequest.getCount();

        if (isAlreadyInBasket(arduino, account)) {
            throw new CartException(ExceptionMessageSource.ALREADY_IN_BASKET);
        }

        CartValidateStatus cartValidateStatus = Cart.validate(arduino, containCount, BASKET_MAX_QUANTITY);

        if (cartValidateStatus.isNotValid()) {
            throw new CartException(ExceptionMessageSource.UNABLE_TO_BASKET);
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
            throw new CartException(ExceptionMessageSource.UNABLE_TO_BASKET);
        }

        cart.update(updatedCount);
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

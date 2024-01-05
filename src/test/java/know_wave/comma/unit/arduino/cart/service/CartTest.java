package know_wave.comma.unit.arduino.cart.service;

import know_wave.comma.account.entity.AcademicMajor;
import know_wave.comma.account.entity.Account;
import know_wave.comma.account.service.system.AccountQueryService;
import know_wave.comma.arduino.cart.dto.CartAddRequest;
import know_wave.comma.arduino.cart.dto.CartResponse;
import know_wave.comma.arduino.cart.dto.CartUpdateRequest;
import know_wave.comma.arduino.cart.entity.Cart;
import know_wave.comma.arduino.cart.exception.BadArduinoStockStatusException;
import know_wave.comma.arduino.cart.exception.CartException;
import know_wave.comma.arduino.cart.exception.NotEnoughArduinoException;
import know_wave.comma.arduino.cart.exception.OverMaxQuantityException;
import know_wave.comma.arduino.cart.repository.CartRepository;
import know_wave.comma.arduino.cart.service.CartService;
import know_wave.comma.arduino.component.entity.Arduino;
import know_wave.comma.arduino.component.entity.Category;
import know_wave.comma.arduino.component.service.ComponentQueryService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@DisplayName("유닛 테스트(서비스) : 아두이노(장바구니)")
@ExtendWith(MockitoExtension.class)
public class CartTest {

    @Mock
    private AccountQueryService accountQueryService;

    @Mock
    private ComponentQueryService componentQueryService;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    private static final int TEST_DATA_COUNT = 10;
    private static final int ARDUINO_COUNT = 10;
    private static final List<Arduino> ARDUINOS = new ArrayList<>(TEST_DATA_COUNT);
    private static final List<Cart> CARTS = new ArrayList<>(TEST_DATA_COUNT);
    private static Account account;

    @BeforeEach
    void arduinoSetup() {

        ARDUINOS.clear();
        CARTS.clear();

        String arduinoName;
        String arduinoDescription;

        account = Account.create("test-user", "test@m365.dongyang.ac.kr", "test-name", "test-password", "01012345678", "20181818", AcademicMajor.AIEngineering);

        // 아두이노 엔티티 생성
        for (int i = 0; i < TEST_DATA_COUNT; i++) {
            arduinoName = "test-arduino" + i;
            arduinoDescription = "test-description" + i;

            Arduino arduino = Arduino.create(arduinoName, ARDUINO_COUNT, arduinoDescription, List.of(Category.createById(1L), Category.createById(2L)));
            ARDUINOS.add(arduino);

            Cart cart = Cart.create(account, arduino, 1);
            CARTS.add(cart);
        }

    }

    @DisplayName("장바구니 조회")
    @Test
    void whenGetCart_thenCart() {
        //given
        when(accountQueryService.findAccount()).thenReturn(account);
        when(cartRepository.findAllByAccount(ArgumentMatchers.any(Account.class))).thenReturn(CARTS);

        //when
        CartResponse cartResponse = cartService.getCart();

        //then
        assertThat(cartResponse.getCarts().size()).isEqualTo(TEST_DATA_COUNT);
    }

    @DisplayName("장바구니 담기")
    @Test
    void givenArduino_whenAddCart_thenSuccess() {
        //given
        Arduino newArduino = Arduino.create("test-arduino", 10, "test-description", List.of(Category.createById(1L), Category.createById(2L)));
        CartAddRequest cartAddRequest = CartAddRequest.create(3);

        when(componentQueryService.getArduino(anyLong())).thenReturn(newArduino);
        when(accountQueryService.findAccount()).thenReturn(account);

        //when
        cartService.addArduino(1L, cartAddRequest);

        //then
        verify(cartRepository, times(1)).save(any(Cart.class));
    }


    @DisplayName("장바구니 담기 실패(이미 담김)")
    @Test
    void givenAlreadyContainedArduino_whenAddCart_thenThrow() {
        //given
        CartAddRequest cartAddRequest = CartAddRequest.create(3);

        when(cartRepository.findByArduinoAndAccount(any(Arduino.class),any(Account.class))).thenReturn(Optional.of(CARTS.getFirst()));
        when(accountQueryService.findAccount()).thenReturn(account);
        when(componentQueryService.getArduino(anyLong())).thenReturn(ARDUINOS.getFirst());

        //when & then
        assertThatThrownBy(() -> cartService.addArduino(1L, cartAddRequest)).isInstanceOf(CartException.class);
    }

    @DisplayName("장바구니 담기 실패(잔여 수량 부족)")
    @Test
    void givenNotEnoughArduino_whenAddCart_thenThrow() {
        //given
        Arduino notEnoughArduino = Arduino.create("test-arduino", 0, "test-description", List.of(Category.createById(1L), Category.createById(2L)));
        CartAddRequest cartAddRequest = CartAddRequest.create(3);

        when(componentQueryService.getArduino(anyLong())).thenReturn(notEnoughArduino);
        when(accountQueryService.findAccount()).thenReturn(account);

        //when & then
        assertThatThrownBy(() -> cartService.addArduino(1L, cartAddRequest)).isInstanceOf(BadArduinoStockStatusException.class);
    }

    @DisplayName("장바구니 담기 실패(최대 수량 초과)")
    @Test
    void givenOverMaxArduinoQuantity_whenAddCart_thenThrow() {
        //given
        Arduino newArduino = Arduino.create("test-arduino", 10, "test-description", List.of(Category.createById(1L), Category.createById(2L)));
        CartAddRequest cartAddRequest = CartAddRequest.create(100);

        when(componentQueryService.getArduino(anyLong())).thenReturn(newArduino);
        when(accountQueryService.findAccount()).thenReturn(account);

        //when & then
        assertThatThrownBy(() -> cartService.addArduino(1L, cartAddRequest)).isInstanceOf(OverMaxQuantityException.class);
    }

    @DisplayName("장바구니 아두이노 수량 변경")
    @Test
    void givenUpdate_whenUpdateCart_thenSuccess() {
        //given
        Cart cart = CARTS.getFirst();
        when(cartRepository.findFetchArduinoById(anyLong())).thenReturn(Optional.of(cart));
        CartUpdateRequest cartUpdateRequest = CartUpdateRequest.create(3);

        //when
        cartService.updateContainCount(1L, cartUpdateRequest);

        //then
        assertThat(cart.getStoredCount()).isEqualTo(3);
    }

    @DisplayName("장바구니 아두이노 수량 변경 실패(최대 수량 초과)")
    @Test
    void givenOverMaxUpdate_whenUpdateCart_thenThrow() {
        //given
        Cart cart = CARTS.getFirst();
        when(cartRepository.findFetchArduinoById(anyLong())).thenReturn(Optional.of(cart));
        CartUpdateRequest cartUpdateRequest = CartUpdateRequest.create(10);

        //when & then
        assertThatThrownBy(() -> cartService.updateContainCount(1L, cartUpdateRequest)).isInstanceOf(OverMaxQuantityException.class);
    }

    @DisplayName("장바구니 아두이노 수량 변경 실패(잔여 수량 부족)")
    @Test
    void givenNotEnoughUpdate_whenUpdateCart_thenThrow() {
        //given
        Cart cart = CARTS.getFirst();
        Arduino arduino = cart.getArduino();
        arduino.update("test-arduino", 0, "test-description", List.of(Category.createById(1L), Category.createById(2L)));

        CartUpdateRequest cartUpdateRequest = CartUpdateRequest.create(3);

        when(cartRepository.findFetchArduinoById(anyLong())).thenReturn(Optional.of(cart));

        //when & then
        assertThatThrownBy(() -> cartService.updateContainCount(1L, cartUpdateRequest)).isInstanceOf(NotEnoughArduinoException.class);
    }

    @DisplayName("장바구니 아두이노 삭제")
    @Test
    void givenDelete_whenDeleteCart_thenSuccess() {
        //given
        Cart cart = CARTS.getFirst();
        when(cartRepository.findFetchArduinoById(anyLong())).thenReturn(Optional.of(cart));

        //when
        cartService.deleteArduino(1L);

        //then
        verify(cartRepository, times(1)).delete(any(Cart.class));
    }

    @DisplayName("장바구니 비우기")
    @Test
    void givenEmpty_whenEmptyCart_thenSuccess() {
        //given
        when(accountQueryService.findAccount()).thenReturn(account);
        when(cartRepository.findAllByAccount(any(Account.class))).thenReturn(CARTS);

        //when
        cartService.deleteAllArduino();

        //then
        verify(cartRepository, times(1)).deleteAll(anyList());
    }

}

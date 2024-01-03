package know_wave.comma.unit.arduino.component;

import know_wave.comma.account.service.system.AccountQueryService;
import know_wave.comma.arduino.comment.repository.CommentLikeRepository;
import know_wave.comma.arduino.comment.repository.CommentRepository;
import know_wave.comma.arduino.component.repository.ArduinoRepository;
import know_wave.comma.arduino.component.service.ComponentQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@DisplayName("유닛 테스트(서비스) : 아두이노(사용자)")
@ExtendWith(MockitoExtension.class)
public class ComponentUserServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentLikeRepository commentLikeRepository;

    @Mock
    private ArduinoRepository arduinoRepository;

    @Mock
    private AccountQueryService accountQueryService;

    @InjectMocks
    private ComponentQueryService componentQueryService;

}

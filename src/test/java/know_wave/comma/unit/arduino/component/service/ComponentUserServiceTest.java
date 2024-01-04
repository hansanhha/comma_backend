package know_wave.comma.unit.arduino.component.service;

import know_wave.comma.account.entity.AcademicMajor;
import know_wave.comma.account.entity.Account;
import know_wave.comma.account.service.system.AccountQueryService;
import know_wave.comma.arduino.comment.dto.*;
import know_wave.comma.arduino.comment.entity.Comment;
import know_wave.comma.arduino.comment.repository.CommentLikeRepository;
import know_wave.comma.arduino.comment.repository.CommentRepository;
import know_wave.comma.arduino.comment.service.CommentCommandService;
import know_wave.comma.arduino.comment.service.CommentQueryService;
import know_wave.comma.arduino.component.dto.ArduinoDetailResponse;
import know_wave.comma.arduino.component.dto.ArduinoPageResponse;
import know_wave.comma.arduino.component.entity.Arduino;
import know_wave.comma.arduino.component.entity.ArduinoPhoto;
import know_wave.comma.arduino.component.entity.Category;
import know_wave.comma.arduino.component.repository.ArduinoRepository;
import know_wave.comma.arduino.component.service.ComponentQueryService;
import know_wave.comma.community.entity.ContentStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@DisplayName("유닛 테스트(서비스) : 아두이노(사용자-컴포넌트, 커뮤니티)")
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

    @InjectMocks
    private CommentQueryService commentQueryService;

    private CommentCommandService commentCommandService;

    private static final int TEST_DATA_COUNT = 100;

    private static final List<Arduino> ARDUINOS = new ArrayList<>(TEST_DATA_COUNT);

    private static final List<ArduinoPhoto> ARDUINO_PHOTOS = new ArrayList<>(TEST_DATA_COUNT);

    private static final List<Account> ACCOUNTS = new ArrayList<>(TEST_DATA_COUNT);

    private static final Map<Arduino, List<Comment>> COMMENT_MAP = new HashMap<>(TEST_DATA_COUNT);

    private static final int ARDUINO_COUNT = 10;

    @BeforeEach
    void setUp() {
        commentCommandService = new CommentCommandService(accountQueryService, componentQueryService, commentRepository, commentLikeRepository);
    }

    @BeforeAll
    static void arduinoSetup() {
        String arduinoName;
        String arduinoDescription;
        String uploadFilePath = "arduino/image";
        String originalFileName;
        String accountId;

        // 아두이노 엔티티, 아두이노 사진 엔티티, 계정 엔티티
        for (int i = 0; i < TEST_DATA_COUNT; i++) {
            arduinoName = "test-arduino" + i;
            arduinoDescription = "test-description" + i;
            originalFileName = "test-file" + i;
            accountId = "test-user" + i;

            Arduino arduino = Arduino.create(arduinoName, ARDUINO_COUNT, arduinoDescription, List.of(Category.createById(1L), Category.createById(2L)));
            ARDUINOS.add(arduino);

            ArduinoPhoto arduinoPhoto = ArduinoPhoto.create(UUID.randomUUID().toString(), originalFileName, uploadFilePath, 100L, arduino);
            ARDUINO_PHOTOS.add(arduinoPhoto);

            Account account = Account.create(accountId, "test-password", "test-email", "test-nickname", "test-phone", "test-address", AcademicMajor.AIEngineering);
            ACCOUNTS.add(account);
        }

        // 댓글 엔티티 생성(아두이노 당 100개 댓글, 100 * 100 = 10000개)
        for (int i=0; i < TEST_DATA_COUNT; i++) {
            Arduino arduino = ARDUINOS.get(i);
            Account account = ACCOUNTS.get(i);

            ArrayList<Comment> comments = new ArrayList<>(TEST_DATA_COUNT);

            for (int j = 1; j <= TEST_DATA_COUNT; j++) {
                Comment comment = Comment.create(arduino, account, "test-comment" + "-" + i+  "-" + j);
                comments.add(comment);
            }

            COMMENT_MAP.put(arduino, comments);
        }

        // 대댓글 엔티티 생성(댓글당 10개 대댓글, 100 * 100 * 10 = 100000개)
        for (int i=0; i < TEST_DATA_COUNT; i++) {
            Arduino arduino = ARDUINOS.get(i);
            Account account = ACCOUNTS.get(i);

            List<Comment> comments = COMMENT_MAP.get(arduino);

            Comment parent = comments.get(i);

            for (int j = 1; j <= 10; j++) {
                Comment reply = Comment.createReply(arduino, account, parent,"test-reply-comment" + "-" + i+  "-" + j);
                comments.add(reply);
            }
        }
    }

    @DisplayName("아두이노 리스트 조회")
    @Test
    void givenArduinoId_whenGetArduino_thenArduino() {
        // given
        int index = 0, offset = 20;
        List<Arduino> foundArduino = ARDUINOS.subList(index, offset);

        when(arduinoRepository.findAll(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(new PageImpl<>(foundArduino, PageRequest.of(index, offset), ARDUINOS.size()));

        // when
        ArduinoPageResponse arduinoPage = componentQueryService.getArduinoPage(PageRequest.of(index, offset));

        // then
        assertThat(arduinoPage.getArduinoList().size()).isEqualTo(offset);
        assertThat(arduinoPage.getArduinoList().size()).isEqualTo(foundArduino.size());
        assertThat(arduinoPage.getIsFirst()).isTrue();
        assertThat(arduinoPage.getHasNext()).isTrue();
        assertThat(arduinoPage.getIsLast()).isFalse();
    }

    @DisplayName("아두이노 상세 조회(댓글 미포함)")
    @Test
    void givenArduinoId_whenGetArduinoDetail_thenArduinoDetail() {
        // given
        Arduino arduino = ARDUINOS.getFirst();

        when(arduinoRepository.findById(anyLong())).thenReturn(Optional.of(arduino));

        when(commentRepository.findAllByArduino(ArgumentMatchers.any(Arduino.class), ArgumentMatchers.any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0));

        // when
        ArduinoDetailResponse arduinoDetailResponse = componentQueryService.getArduinoDetailWithComments(1L);

        // then
        assertThat(arduinoDetailResponse.getName()).isEqualTo(arduino.getName());
        assertThat(arduinoDetailResponse.getCount()).isEqualTo(arduino.getCount());
        assertThat(arduinoDetailResponse.getComments()).isNull();
        assertThat(arduinoDetailResponse.getPhotos()).isNull();
    }

    @DisplayName("아두이노 상세 조회(댓글 포함)")
    @Test
    void givenArduinoId_whenGetArduinoDetail_thenArduinoDetailWithComment() {
        // given
        int index = 0, offset = 10;
        Arduino arduino = ARDUINOS.getFirst();
        List<Comment> arduinoComments = COMMENT_MAP.get(arduino);

        when(arduinoRepository.findById(anyLong())).thenReturn(Optional.of(arduino));

        when(commentRepository.findAllByArduino(ArgumentMatchers.any(Arduino.class), ArgumentMatchers.any(Pageable.class)))
                .thenReturn(new PageImpl<>(arduinoComments.subList(index, offset), PageRequest.of(index, offset), arduinoComments.size()));

        // when
        ArduinoDetailResponse arduinoDetailResponse = componentQueryService.getArduinoDetailWithComments(1L);

        // then
        assertThat(arduinoDetailResponse.getName()).isEqualTo(arduino.getName());
        assertThat(arduinoDetailResponse.getCount()).isEqualTo(arduino.getCount());
        assertThat(arduinoDetailResponse.getComments().getComments().size()).isEqualTo(offset);
        assertThat(arduinoDetailResponse.getComments().isHasNext()).isTrue();
        assertThat(arduinoDetailResponse.getPhotos()).isNull();
    }


    @DisplayName("아두이노 댓글 조회")
    @Test
    void givenArduinoId_whenGetComment_thenComments() {
        // given
        int index = 1, offset = 10;
        Arduino arduino = ARDUINOS.getFirst();

        List<Comment> totalComments = COMMENT_MAP.get(arduino);
        List<Comment> comments = totalComments.subList(index-1+10, offset+10); // 댓글 페이징 처리
        PageRequest pageRequest = PageRequest.of(index, offset);

        when(arduinoRepository.findById(anyLong())).thenReturn(Optional.of(arduino));
        when(commentRepository.findAllByArduino(ArgumentMatchers.any(Arduino.class), ArgumentMatchers.any(Pageable.class)))
                .thenReturn(new PageImpl<>(comments, pageRequest, totalComments.size()));

        // when
        CommentPageResponse commentPageResponse = commentQueryService.getComments(1L, pageRequest);

        // then
        assertThat(commentPageResponse.getComments().size()).isEqualTo(10);
        assertThat(commentPageResponse.isLast()).isFalse();
        assertThat(commentPageResponse.isFirst()).isFalse();
        assertThat(commentPageResponse.isHasNext()).isTrue();
    }

    @DisplayName("아두이노 대댓글 조회")
    @Test
    void givenCommentId_whenGetReplyComment_thenReplyComments() {
        // given
        int index = 0, offset = 10;

        Arduino arduino = ARDUINOS.getFirst();
        List<Comment> totalComments = COMMENT_MAP.get(arduino);
        List<Comment> replyComments = totalComments.stream().filter(comment -> comment.getParent() != null).toList();
        Comment parent = totalComments.getFirst();

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(parent));
        when(commentRepository.findAllReplyById(ArgumentMatchers.any(Comment.class), ArgumentMatchers.any(Pageable.class)))
                .thenReturn(new PageImpl<>(replyComments, PageRequest.of(index, offset), replyComments.size()));

        // when
        ReplyCommentPageResponse replyCommentResponse = commentQueryService.getReplyComments(1L, PageRequest.of(index, offset));

        // then
        assertThat(replyCommentResponse.getComments().size()).isEqualTo(offset);
        assertThat(replyCommentResponse.isFirst()).isTrue();
        assertThat(replyCommentResponse.isLast()).isTrue();
        assertThat(replyCommentResponse.isHasNext()).isFalse();
    }

    @DisplayName("아두이노 댓글 달기")
    @Test
    void givenComment_whenCreateComment_thenSuccess() {
        // given
        when(arduinoRepository.findById(anyLong())).thenReturn(Optional.of(ARDUINOS.getFirst()));
        when(accountQueryService.findAccount()).thenReturn(ACCOUNTS.getFirst());

        CommentWriteRequest commentWriteRequest = CommentWriteRequest.create("테스트 댓글");

        // when
        commentCommandService.writeComment(1L, commentWriteRequest);

        // then
        verify(commentRepository, times(1)).save(ArgumentMatchers.any(Comment.class));
    }

    @DisplayName("아두이노 대댓글 달기")
    @Test
    void givenComment_whenCreateReplyComment_thenSuccess() {
        // given
        Arduino arduino = ARDUINOS.getFirst();
        List<Comment> totalComments = COMMENT_MAP.get(arduino);
        Comment parent = totalComments.getFirst();

        when(arduinoRepository.findById(anyLong())).thenReturn(Optional.of(arduino));
        when(accountQueryService.findAccount()).thenReturn(ACCOUNTS.getFirst());
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(parent));

        ReplyCommentWriteRequest replyCommentWriteRequest = ReplyCommentWriteRequest.create("테스트 대댓글");

        // when
        commentCommandService.writeReplyComment(1L, 1L, replyCommentWriteRequest);

        // then
        verify(commentRepository, times(1)).save(ArgumentMatchers.any(Comment.class));
    }

    @DisplayName("아두이노 댓글 수정")
    @Test
    void givenUpdateComment_whenUpdateComment_thenSuccess() {
        // given
        Arduino arduino = ARDUINOS.getFirst();
        List<Comment> totalComments = COMMENT_MAP.get(arduino);
        Comment comment = totalComments.getFirst();

        when(accountQueryService.findAccount()).thenReturn(ACCOUNTS.getFirst());
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        String updatedContent = "테스트 댓글 수정";

        CommentUpdateRequest commentUpdateRequest = CommentUpdateRequest.create(updatedContent);

        // when
        commentCommandService.updateComment(1L, commentUpdateRequest);

        // then
        assertThat(comment.getContent()).isEqualTo(updatedContent);
    }

    @DisplayName("아두이노 대댓글 수정")
    @Test
    void givenUpdateComment_whenUpdateReplyComment_thenSuccess() {
        // given
        Arduino arduino = ARDUINOS.getFirst();
        List<Comment> totalComments = COMMENT_MAP.get(arduino);
        Comment comment = totalComments.getFirst();

        when(accountQueryService.findAccount()).thenReturn(ACCOUNTS.getFirst());
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        String updatedContent = "테스트 대댓글 수정";

        ReplyCommentUpdateRequest replyCommentUpdateRequest = ReplyCommentUpdateRequest.create(updatedContent);

        // when
        commentCommandService.updateReplyComment(1L, replyCommentUpdateRequest);

        // then
        assertThat(comment.getContent()).isEqualTo(updatedContent);
    }

    @DisplayName("아두이노 댓글 삭제")
    @Test
    void givenCommentId_whenDeleteComment_thenSuccess() {
        // given
        Comment comment = COMMENT_MAP.get(ARDUINOS.getFirst()).getFirst();

        when(accountQueryService.findAccount()).thenReturn(ACCOUNTS.getFirst());
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        // when
        commentCommandService.deleteComment(1L);

        // then
        assertThat(comment.getContentStatus()).isEqualTo(ContentStatus.DELETED);
    }

    @DisplayName("아두이노 대댓글 삭제")
    @Test
    void givenCommentId_whenDeleteReplyComment_thenSuccess() {
        // given
        Comment comment = COMMENT_MAP.get(ARDUINOS.getFirst()).getFirst();

        when(accountQueryService.findAccount()).thenReturn(ACCOUNTS.getFirst());
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        // when
        commentCommandService.deleteReplyComment(1L);

        // then
        assertThat(comment.getContentStatus()).isEqualTo(ContentStatus.DELETED);
    }

}

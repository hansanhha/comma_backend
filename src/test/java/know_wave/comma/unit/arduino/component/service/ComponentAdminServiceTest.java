package know_wave.comma.unit.arduino.component.service;

import jakarta.persistence.EntityNotFoundException;
import know_wave.comma.arduino.component.admin.dto.ArduinoCreateForm;
import know_wave.comma.arduino.component.admin.dto.ArduinoUpdateForm;
import know_wave.comma.arduino.component.admin.service.CategoryAdminService;
import know_wave.comma.arduino.component.admin.service.ComponentCommandAdminService;
import know_wave.comma.arduino.component.admin.service.ComponentQueryAdminService;
import know_wave.comma.arduino.component.dto.CategoriesResponse;
import know_wave.comma.arduino.component.entity.Arduino;
import know_wave.comma.arduino.component.entity.ArduinoPhoto;
import know_wave.comma.arduino.component.entity.ArduinoStockStatus;
import know_wave.comma.arduino.component.entity.Category;
import know_wave.comma.arduino.component.repository.ArduinoCategoryRepository;
import know_wave.comma.arduino.component.repository.ArduinoPhotoRepository;
import know_wave.comma.arduino.component.repository.ArduinoRepository;
import know_wave.comma.common.file.dto.FileDto;
import know_wave.comma.common.file.service.FileService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.setRemoveAssertJRelatedElementsFromStackTrace;
import static org.mockito.Mockito.*;


@DisplayName("유닛 테스트(서비스) : 아두이노(관리자)")
@ExtendWith(MockitoExtension.class)
public class ComponentAdminServiceTest {

    @Mock
    private ArduinoRepository arduinoRepository;

    @Mock
    private ArduinoCategoryRepository categoryRepository;

    @Mock
    private ArduinoPhotoRepository arduinoPhotoRepository;

    @Spy
    private FileService fileService;

    @InjectMocks
    private CategoryAdminService categoryAdminService;

    @InjectMocks
    private ComponentCommandAdminService commandAdminService;

    @InjectMocks
    private ComponentQueryAdminService QueryAdminService;

    private static final List<MultipartFile> arduinoImageFiles = new ArrayList<>(3);

    private static final List<ArduinoCreateForm> arduinoCreateForms = new ArrayList<>(3);

    private static final List<Arduino> arduinos = new ArrayList<>(3);

    private static final List<ArduinoPhoto> arduinoPhotos = new ArrayList<>(3);

    private static Answer<FileDto> fileDtoAnswer;

    private static final int ARDUINO_COUNT = 10;

    @BeforeAll
    static void arduinoSetup() {
        String arduinoName;
        String arduinoDescription;

        // 아두이노 이미지 파일, 아두이노 생성 Form, 아두이노 엔티티, 아두이노 사진 엔티티 생성
        for (int i = 0; i < 3; i++) {
            MockMultipartFile arduinoImageFile = new MockMultipartFile("arduino-image" + i, "arduino-image" + i + ".png", MimeTypeUtils.IMAGE_PNG_VALUE, ("test-arduino-image" + i).getBytes(StandardCharsets.UTF_8));
            arduinoImageFiles.add(arduinoImageFile);

            arduinoName = "test-arduino" + i;
            arduinoDescription = "test-description" + i;

            ArduinoCreateForm arduinoCreateForm = ArduinoCreateForm.create(arduinoName, ARDUINO_COUNT, arduinoDescription, List.of(1L, 2L));
            arduinoCreateForms.add(arduinoCreateForm);

            Arduino arduino = Arduino.create(arduinoName, ARDUINO_COUNT, arduinoDescription, List.of(Category.createById(1L), Category.createById(2L)));
            arduinos.add(arduino);

            ArduinoPhoto arduinoPhoto = ArduinoPhoto.create(UUID.randomUUID().toString(), arduinoImageFile.getOriginalFilename(), arduinoImageFile.getOriginalFilename(), arduinoImageFile.getSize(), arduino);
            arduinoPhotos.add(arduinoPhoto);
        }

        // 실제로 이미지 파일을 저장하지 않고, FileDto만 반환하는 Answer
        fileDtoAnswer = invocation -> {
            MultipartFile file = invocation.getArgument(0);
            String uploadPath = invocation.getArgument(1);
            String uuid = UUID.randomUUID().toString();
            String filename = file.getOriginalFilename();
            String filePath = Paths.get(uploadPath, uuid, filename).toString();
            long size = file.getSize();
            String contentType = file.getContentType();

            return new FileDto(uuid, filename, filePath, contentType, size);
        };
    }

    @AfterEach
    void tearDown() {
        for (int i = 0; i < 3; i++) {
            arduinos.get(i).update("test-arduino" + i, ARDUINO_COUNT, "test-description" + i, List.of(Category.createById(1L), Category.createById(2L)));
        }
    }

    @DisplayName("카테고리 생성 성공")
    @Test
    void givenCategory_whenCreate_thenSuccess() {
        //given
        String categoryName = "test-category";
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(categoryRepository.save(ArgumentMatchers.any(Category.class))).thenReturn(Category.createByName(categoryName));

        //when
        String savedCategoryName = categoryAdminService.createCategory("test-category");

        //then
        verify(categoryRepository, times(1)).findByName(anyString());
        verify(categoryRepository, times(1)).save(ArgumentMatchers.any(Category.class));
        assertThat(savedCategoryName).isEqualTo(categoryName);
    }

    @DisplayName("카테고리 수정 성공")
    @Test
    void givenValidCategory_whenUpdate_thenSuccess(){
        //given
        String categoryName = "test-category";
        String updateCategoryName = "update-test-category";

        Category category = Category.createByName(categoryName);

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());

        //when
        categoryAdminService.updateCategory(1L, updateCategoryName);

        //then
        verify(categoryRepository, times(1)).findById(anyLong());
        verify(categoryRepository, times(1)).findByName(anyString());
        assertThat(category.getName()).isEqualTo(updateCategoryName);
    }

    @DisplayName("카테고리 삭제 성공")
    @Test
    void givenValidCategory_whenDelete_thenSuccess(){
        //given
        Long id = 1L;

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(Category.createById(id)));
        doNothing().when(categoryRepository).delete(ArgumentMatchers.any(Category.class));

        //when
        categoryAdminService.deleteCategory(id);

        //then
        verify(categoryRepository, times(1)).findById(anyLong());
        verify(categoryRepository, times(1)).delete(ArgumentMatchers.any(Category.class));
    }

    @DisplayName("카테고리 전체 조회 성공")
    @Test
    void whenReadAll_thenSuccess(){
        //given
        List<Category> categories = IntStream.range(0, 5)
                .mapToObj(i -> Category.createByName("test-category" + i))
                .toList();

        when(categoryRepository.findAll()).thenReturn(categories);

        //when
        CategoriesResponse foundCategories = categoryAdminService.getCategories();

        //then
        List<String> foundCategoryNames = foundCategories.getCategories()
                .stream()
                .map(CategoriesResponse.CategoryResponse::getCategoryName)
                .toList();

        verify(categoryRepository, times(1)).findAll();
        assertThat(foundCategories.getCategories().size()).isEqualTo(5);
        assertThat(foundCategoryNames).containsExactly("test-category0", "test-category1", "test-category2", "test-category3", "test-category4");
    }

    @DisplayName("단일 아두이노(사진 포함) 생성 성공")
    @Test
    void givenArduinoWithPhoto_whenCreate_thenSucess(){
        //given
        doAnswer(fileDtoAnswer).when(fileService).save(any(MultipartFile.class), anyString());


        ArduinoCreateForm arduinoCreateForm = ArduinoCreateForm.create("test-arduino", 10, "test-description", List.of(1L, 2L));

        //when
        commandAdminService.createArduino(arduinoCreateForm, arduinoImageFiles);

        //then
        verify(fileService, times(3)).save(ArgumentMatchers.any(MultipartFile.class), anyString());
        verify(arduinoRepository, times(1)).save(ArgumentMatchers.any(Arduino.class));
        verify(arduinoPhotoRepository, times(1)).saveAll(ArgumentMatchers.anyList());
    }

    @DisplayName("다중 아두이노 생성 성공")
    @Test
    void givenArduinoListWithoutPhoto_whenCreate_thenSuccess(){
        //given
        ArduinoCreateForm arduinoCreateForm1 = ArduinoCreateForm.create("test-arduino1", 10, "test-description1", List.of(1L, 2L));
        ArduinoCreateForm arduinoCreateForm2 = ArduinoCreateForm.create("test-arduino2", 10, "test-description2", List.of(1L, 2L));
        ArduinoCreateForm arduinoCreateForm3 = ArduinoCreateForm.create("test-arduino3", 10, "test-description3", List.of(1L, 2L));

        List<ArduinoCreateForm> forms = List.of(arduinoCreateForm1, arduinoCreateForm2, arduinoCreateForm3);

        //when
        commandAdminService.createArduinoList(forms);

        //then
        verify(arduinoRepository, times(3)).save(ArgumentMatchers.any(Arduino.class));

    }

    @DisplayName("아두이노 수정 성공")
    @Test
    void givenValidUpdateArduino_whenUpdate_thenSuccess(){
        //given
        ArduinoUpdateForm arduinoUpdateForm = ArduinoUpdateForm.create("update-test-arduino", 10, "update-test-description", List.of(1L, 2L));

        String deleteFileUuid = arduinoPhotos.getFirst().getFileUuid();

        MockMultipartFile addImageFile = new MockMultipartFile("add-arduino-image", "add-arduino-image.png", MimeTypeUtils.IMAGE_PNG_VALUE, ("add-arduino-image").getBytes(StandardCharsets.UTF_8));

        when(arduinoRepository.findById(anyLong())).thenReturn(Optional.of(arduinos.getFirst()));
        when(arduinoPhotoRepository.findAllByUuid(anyList())).thenReturn(List.of(arduinoPhotos.getFirst()));
        doAnswer(fileDtoAnswer).when(fileService).save(any(MultipartFile.class), anyString());

        //when
        commandAdminService.updateArduino(1L, arduinoUpdateForm, List.of(deleteFileUuid), List.of(addImageFile));

        //then
        verify(arduinoRepository, times(1)).findById(anyLong());
        verify(arduinoPhotoRepository, times(1)).findAllByUuid(anyList());
        verify(arduinoPhotoRepository, times(1)).saveAll(anyList());
        verify(fileService, times(1)).save(any(MultipartFile.class), anyString());
    }

    @DisplayName("아두이노 잔여수량 추가 성공")
    @Test
    void givenValidUpdateCount_whenUpdateCount_thenSuccess(){
        //given
        when(arduinoRepository.findById(anyLong())).thenReturn(Optional.of(arduinos.getFirst()));

        //when
        commandAdminService.addArduinoCount(1L, 100);

        //then
        assertThat(arduinos.getFirst().getCount()).isGreaterThanOrEqualTo(100);
        assertThat(arduinos.getFirst().getStockStatus()).isEqualTo(ArduinoStockStatus.MORE_THAN_10);
    }

    @DisplayName("아두이노 재고상태 변경 성공")
    @Test
    void givenValidStockStatus_whenUpdateStockStatus_thenSuccess(){
        //given
        when(arduinoRepository.findById(anyLong())).thenReturn(Optional.of(arduinos.getFirst()));

        //when
        commandAdminService.updateArduinoStockStatus(1L, "NONE");

        //then
        assertThat(arduinos.getFirst().getStockStatus()).isEqualTo(ArduinoStockStatus.NONE);
    }

    @DisplayName("아두이노 삭제 성공")
    @Test
    void givenArduino_whenDelete_thenSuccess(){
        //given
        when(arduinoRepository.findById(anyLong())).thenReturn(Optional.of(arduinos.getFirst()));

        //when
        commandAdminService.deleteArduino(1L);

        //then
        assertThat(arduinos.getFirst().getDelete().isDeleted()).isTrue();
    }

}

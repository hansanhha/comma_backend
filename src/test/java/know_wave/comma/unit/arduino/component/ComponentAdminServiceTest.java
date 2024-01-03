package know_wave.comma.unit.arduino.component;

import know_wave.comma.arduino.component.admin.dto.CategoryCreateRequest;
import know_wave.comma.arduino.component.admin.service.CategoryAdminService;
import know_wave.comma.arduino.component.admin.service.ComponentCommandAdminService;
import know_wave.comma.arduino.component.admin.service.ComponentQueryAdminService;
import know_wave.comma.arduino.component.dto.CategoriesResponse;
import know_wave.comma.arduino.component.entity.Category;
import know_wave.comma.arduino.component.repository.ArduinoCategoryRepository;
import know_wave.comma.arduino.component.repository.ArduinoPhotoRepository;
import know_wave.comma.arduino.component.repository.ArduinoRepository;
import know_wave.comma.common.file.service.FileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



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

}

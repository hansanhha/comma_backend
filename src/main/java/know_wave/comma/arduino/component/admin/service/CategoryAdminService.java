package know_wave.comma.arduino.component.admin.service;

import jakarta.persistence.EntityNotFoundException;
import know_wave.comma.arduino.component.dto.CategoriesResponse;
import know_wave.comma.arduino.component.admin.exception.AlreadyCategoryException;
import know_wave.comma.arduino.component.admin.exception.NotFoundCategoryException;
import know_wave.comma.arduino.component.entity.Category;
import know_wave.comma.arduino.component.repository.ArduinoCategoryRepository;
import know_wave.comma.arduino.component.repository.ArduinoRepository;
import know_wave.comma.common.entity.ExceptionMessageSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryAdminService {

    private final ArduinoRepository arduinoRepository;
    private final ArduinoCategoryRepository categoryRepository;


    public CategoriesResponse getCategories() {
        var categories = (List<Category>) categoryRepository.findAll();

        return CategoriesResponse.to(categories);
    }

    public void registerCategory(String name) {
        Optional<Category> optionalCategory = categoryRepository.findByName(name);

        if (optionalCategory.isPresent()) {
            throw new AlreadyCategoryException(ExceptionMessageSource.ALREADY_EXIST_VALUE);
        }

        categoryRepository.save(Category.createByName(name));
    }

    public void updateCategory(Long id, String dest) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        Optional<Category> optionalDestCategory = categoryRepository.findByName(dest);

        if (optionalCategory.isEmpty()) {
            throw new EntityNotFoundException(ExceptionMessageSource.NOT_FOUND_VALUE);
        }

        if (optionalDestCategory.isPresent()) {
            throw new AlreadyCategoryException(ExceptionMessageSource.ALREADY_EXIST_VALUE);
        }

        Category category = optionalCategory.get();
        category.setName(dest);
    }

    public void deleteCategory(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isEmpty()) {
            throw new NotFoundCategoryException(ExceptionMessageSource.NOT_FOUND_VALUE);
        }

        categoryRepository.delete(optionalCategory.get());
    }
}

package know_wave.comma.arduino.service.normal;

import know_wave.comma.arduino.dto.arduino.ArduinoDetailResponse;
import know_wave.comma.arduino.dto.arduino.ArduinoResponse;
import know_wave.comma.arduino.dto.category.CategoryDto;
import know_wave.comma.arduino.entity.Arduino;
import know_wave.comma.arduino.entity.ArduinoCategory;
import know_wave.comma.arduino.entity.Category;
import know_wave.comma.arduino.entity.Comment;
import know_wave.comma.arduino.repository.ArduinoCategoryRepository;
import know_wave.comma.arduino.repository.ArduinoRepository;
import know_wave.comma.arduino.repository.CategoryCrudRepository;
import know_wave.comma.arduino.repository.CommentRepository;
import know_wave.comma.message.util.ExceptionMessageSource;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ArduinoService {

    private final ArduinoRepository arduinoRepository;
    private final CategoryCrudRepository categoryRepository;
    private final ArduinoCategoryRepository arduinoCategoryRepository;
    private final CommentRepository commentRepository;

    public ArduinoService(ArduinoRepository arduinoRepository, CategoryCrudRepository categoryRepository, ArduinoCategoryRepository arduinoCategoryRepository, CommentRepository commentRepository) {
        this.arduinoRepository = arduinoRepository;
        this.categoryRepository = categoryRepository;
        this.arduinoCategoryRepository = arduinoCategoryRepository;
        this.commentRepository = commentRepository;
    }

    public List<CategoryDto> getAllCategories() {
        var categories = (List<Category>) categoryRepository.findAll();

        return categories.stream()
                .map(CategoryDto::of)
                .sorted(Comparator.comparing(CategoryDto::getCategoryName))
                .toList();
    }

    public ArduinoDetailResponse getArduinoDetails(Long id) {
        return getArduinoDetails(id, PageRequest.of(0, 10));
    }

    public ArduinoDetailResponse getArduinoDetails(Long id, Pageable pageable) {
        Arduino arduino = getArduino(id);

        List<Comment> comments = commentRepository.findAllPagingByArduino(arduino, pageable);

        return ArduinoDetailResponse.of(arduino, comments);
    }


//    public Optional<Page<ArduinoResponse>> getFirstPage(int size) {
//        return getPage(Pageable.ofSize(size));
//    }
    public Page<ArduinoResponse> getPage(Pageable pageable) {
        Page<Arduino> findArduino = arduinoRepository.findAll(pageable);

        return findArduino.map(arduino -> {
                Hibernate.initialize(arduino.getCategories());
                return ArduinoResponse.of(arduino);
            }
        );
    }


    public Arduino getArduino(Long id) {
        Optional<Arduino> arduinoOptional = arduinoRepository.findFetchCategoriesById(id);

        throwIfEmpty(arduinoOptional);

        return arduinoOptional.get();
    }

    public List<ArduinoCategory> getArduinoCategoreisByArduinos(List<Arduino> arduinos) {
        List<ArduinoCategory> arduinoCategories = arduinoCategoryRepository.findALlFetchByArduinos(arduinos);

        throwIfEmpty(arduinoCategories);

        return arduinoCategories;
    }

    private void throwIfEmpty(Iterable<?> iterable) {
        if (!iterable.iterator().hasNext()) {
            throw new IllegalArgumentException(ExceptionMessageSource.NOT_FOUND_VALUE);
        }
    }

    private void throwIfEmpty(Optional<?> optional) {
        Assert.isTrue(optional.isPresent(), ExceptionMessageSource.NOT_FOUND_VALUE);
    }
}

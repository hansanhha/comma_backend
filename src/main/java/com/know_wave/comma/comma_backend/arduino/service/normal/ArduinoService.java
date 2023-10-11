package com.know_wave.comma.comma_backend.arduino.service.normal;

import com.know_wave.comma.comma_backend.arduino.dto.arduino.ArduinoDetailResponse;
import com.know_wave.comma.comma_backend.arduino.dto.arduino.ArduinoResponse;
import com.know_wave.comma.comma_backend.arduino.dto.category.CategoryDto;
import com.know_wave.comma.comma_backend.arduino.entity.Arduino;
import com.know_wave.comma.comma_backend.arduino.entity.ArduinoCategory;
import com.know_wave.comma.comma_backend.arduino.entity.Category;
import com.know_wave.comma.comma_backend.arduino.entity.Comment;
import com.know_wave.comma.comma_backend.arduino.repository.ArduinoCategoryRepository;
import com.know_wave.comma.comma_backend.arduino.repository.ArduinoRepository;
import com.know_wave.comma.comma_backend.arduino.repository.CategoryCrudRepository;
import com.know_wave.comma.comma_backend.arduino.repository.CommentRepository;
import com.know_wave.comma.comma_backend.util.ValidateUtils;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        ValidateUtils.throwIfOptionalEmpty(arduinoOptional);

        return arduinoOptional.get();
    }

    public List<ArduinoCategory> getArduinoCategoreisByArduinos(List<Arduino> arduinos) {
        List<ArduinoCategory> arduinoCategories = arduinoCategoryRepository.findALlFetchByArduinos(arduinos);

        ValidateUtils.throwIfEmpty(arduinoCategories);

        return arduinoCategories;
    }
}

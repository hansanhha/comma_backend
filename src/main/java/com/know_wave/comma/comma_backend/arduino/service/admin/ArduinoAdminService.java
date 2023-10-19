package com.know_wave.comma.comma_backend.arduino.service.admin;

import com.know_wave.comma.comma_backend.arduino.dto.arduino.ArduinoCreateForm;
import com.know_wave.comma.comma_backend.arduino.dto.arduino.ArduinoCreateFormList;
import com.know_wave.comma.comma_backend.arduino.dto.arduino.ArduinoUpdateRequest;
import com.know_wave.comma.comma_backend.arduino.entity.Arduino;
import com.know_wave.comma.comma_backend.arduino.entity.ArduinoCategory;
import com.know_wave.comma.comma_backend.arduino.entity.Category;
import com.know_wave.comma.comma_backend.arduino.repository.*;
import com.know_wave.comma.comma_backend.util.mail.exception.EntityAlreadyExistException;
import com.know_wave.comma.comma_backend.security.annotation.PermissionProtection;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.know_wave.comma.comma_backend.util.message.ExceptionMessageSource.*;

@Service
@Transactional
@PermissionProtection
public class ArduinoAdminService {

    private final ArduinoRepository arduinoRepository;
    private final CategoryCrudRepository categoryCrudRepository;
    private final ArduinoCategoryRepository arduinoCategoryRepository;

    public ArduinoAdminService(ArduinoRepository arduinoRepository, CategoryCrudRepository categoryCrudRepository, ArduinoCategoryRepository arduinoCategoryRepository) {
        this.arduinoRepository = arduinoRepository;
        this.categoryCrudRepository = categoryCrudRepository;
        this.arduinoCategoryRepository = arduinoCategoryRepository;
    }

    public void registerCategory(String categoryName) {

        if (categoryCrudRepository.findByName(categoryName).isPresent()) {
            throw new EntityAlreadyExistException(ALREADY_EXIST_VALUE);
        }

        categoryCrudRepository.save(new Category(categoryName));
    }

    public void updateCategory(Long id, String dest) {

        categoryCrudRepository.findById(id).ifPresentOrElse(category ->
                categoryCrudRepository.findByName(dest).ifPresentOrElse(findCategory ->
                        {throw new EntityAlreadyExistException(ALREADY_EXIST_VALUE);},
                        () -> category.setName(dest)
                ),
                () -> {throw new EntityNotFoundException(NOT_FOUND_VALUE);}
        );
    }

    public void deleteCategory(Long id) {

        categoryCrudRepository.findById(id).ifPresentOrElse(
                categoryCrudRepository::delete,
                () -> {throw new EntityNotFoundException(NOT_FOUND_VALUE);}
        );
    }

    public void registerArduino(ArduinoCreateForm form) {

        arduinoRepository.findByName(form.getArduinoName()).ifPresentOrElse(arduino -> {
                throw new EntityExistsException(ALREADY_EXIST_ARDUINO);
            },

            () -> {

                var categories = (List<Category>) categoryCrudRepository.findAllById(form.getCategories());
                if (Category.isUnMatchCategory(categories, form.getCategories())) {
                    throw new EntityNotFoundException(NOT_EXIST_CATEGORY);
                }

                Arduino savedArduino = arduinoRepository.save(form.toEntity());
                var requestCategories = categoryCrudRepository.findAllById(form.getCategories());
                requestCategories.forEach(requestCategory -> arduinoCategoryRepository.save(new ArduinoCategory(savedArduino, requestCategory)));
            });
    }

    public void registerArduinoList(ArduinoCreateFormList forms) {
        forms.getArduinoCreateForms().forEach(this::registerArduino);
    }

    public void updateArduino(Long id, ArduinoUpdateRequest request) {

        arduinoRepository.findById(id).ifPresentOrElse(arduino -> {

                arduino.update(request.getModifiedArduinoName(), request.getModifiedArduinoCount(), request.getModifiedArduinoOriginalCount(), request.getModifiedArduinoDescription());

                // category update logic
                // compare request category, own category
                // then save new request category if nothing matches
                var RequestCategories = categoryCrudRepository.findAllById(request.getModifiedArduinoCategories());
                var OwnCategories = arduinoCategoryRepository.findAllFetchByArduino(arduino);

                RequestCategories.forEach(requestCategory -> {
                            if (OwnCategories.stream()
                                    .noneMatch(ownCategory -> ownCategory.getCategory().getId().equals(requestCategory.getId()))) {
                                arduinoCategoryRepository.save(new ArduinoCategory(arduino, requestCategory));
                            }
                        }
                );
            },

            () -> {throw new EntityNotFoundException(NOT_FOUND_ARDUINO);}
        );
    }

    public void deleteArduino(Long id) {

        arduinoRepository.findById(id).ifPresentOrElse(
                arduinoRepository::delete,
                () -> {throw new EntityNotFoundException(NOT_FOUND_ARDUINO);});
    }
}

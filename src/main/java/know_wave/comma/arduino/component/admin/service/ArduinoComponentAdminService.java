package know_wave.comma.arduino.component.admin.service;

import jakarta.persistence.EntityNotFoundException;
import know_wave.comma.arduino.component.admin.dto.ArduinoCreateForm;
import know_wave.comma.arduino.component.admin.dto.ArduinoUpdateForm;
import know_wave.comma.arduino.component.admin.exception.AlreadyCategoryException;
import know_wave.comma.arduino.component.dto.ArduinoDetailResponse;
import know_wave.comma.arduino.component.entity.Arduino;
import know_wave.comma.arduino.component.entity.ArduinoPhoto;
import know_wave.comma.arduino.component.entity.ArduinoStockStatus;
import know_wave.comma.arduino.component.entity.Category;
import know_wave.comma.arduino.component.repository.ArduinoCategoryRepository;
import know_wave.comma.arduino.component.repository.ArduinoPhotoRepository;
import know_wave.comma.arduino.component.repository.ArduinoRepository;
import know_wave.comma.common.entity.ExceptionMessageSource;
import know_wave.comma.common.file.dto.FileListDto;
import know_wave.comma.common.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class ArduinoComponentAdminService {

    private final FileService fileService;
    private final ArduinoRepository arduinoRepository;
    private final ArduinoCategoryRepository categoryRepository;
    private final ArduinoPhotoRepository arduinoPhotoRepository;
    private final String ARDUINO_IMAGE_UPLOAD_PATH = "arduino/image";

    public void registerCategory(String categoryName) {
        categoryRepository.findByName(categoryName).ifPresentOrElse(
                category -> {
                    throw new AlreadyCategoryException(ExceptionMessageSource.ALREADY_EXIST_VALUE);
                },

                () -> categoryRepository.save(new Category(categoryName)));
    }

    public void updateCategory(Long id, String dest) {
        categoryRepository.findById(id).ifPresentOrElse(
                category -> {
                    category.setName(dest);
                },

                () -> {
                    throw new EntityNotFoundException(ExceptionMessageSource.NOT_FOUND_VALUE);
                }
        );
    }

    public void deleteCategory(Long id) {
        categoryRepository.findById(id).ifPresentOrElse(
                category -> {
                    categoryRepository.delete(category);
                },

                () -> {
                    throw new EntityNotFoundException(ExceptionMessageSource.NOT_FOUND_VALUE);
                }
        );
    }

    public ArduinoDetailResponse getArduinoDetail(Long id) {
        Arduino arduino = arduinoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ExceptionMessageSource.NOT_FOUND_VALUE));
        return ArduinoDetailResponse.to(arduino);
    }

    public void registerArduino(ArduinoCreateForm form) {
        Arduino arduino = ArduinoCreateForm.to(form);
        arduinoRepository.save(arduino);

        if (isPresent(form.getPhotoFiles())) {
            FileListDto fileListDto = fileService.saveAllImage(form.getPhotoFiles(), ARDUINO_IMAGE_UPLOAD_PATH);
            List<ArduinoPhoto> uploadPhotos = toArduinoPhotoList(fileListDto, arduino);
            arduinoPhotoRepository.saveAll(uploadPhotos);
        }
    }

    public void registerArduinoList(List<ArduinoCreateForm> forms) {
        forms.forEach(this::registerArduino);
    }

    public void updateArduino(ArduinoUpdateForm form) {
        Arduino arduino = arduinoRepository.findById(form.getUpdatedArduinoId()).orElseThrow(() -> new EntityNotFoundException(ExceptionMessageSource.NOT_FOUND_VALUE));
        arduino.update(form.getUpdatedArduinoName(), form.getUpdatedCount(), form.getUpdatedDescription(), form.getUpdatedCategories().stream().map(Category::new).toList());

        if (isPresent(form.getDeletedPhotoFiles())) {
            List<ArduinoPhoto> photos = arduinoPhotoRepository.findAllByUuid(form.getDeletedPhotoFiles());
            ArduinoPhoto.deleteList(photos);
        }

        if (isPresent(form.getAddedPhotoFiles())) {
            FileListDto fileListDto = fileService.saveAllImage(form.getAddedPhotoFiles(), ARDUINO_IMAGE_UPLOAD_PATH);
            List<ArduinoPhoto> uploadPhotos = toArduinoPhotoList(fileListDto, arduino);
            arduinoPhotoRepository.saveAll(uploadPhotos);
        }
    }

    public void deleteArduino(Long id) {
        Arduino arduino = arduinoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ExceptionMessageSource.NOT_FOUND_VALUE));
        Arduino.delete(arduino);

        if (isPresent(arduino.getPhotos())) {
            ArduinoPhoto.deleteList(arduino.getPhotos());
        }
    }

    public void addArduinoCount(Long id, int count) {
        Arduino arduino = arduinoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ExceptionMessageSource.NOT_FOUND_VALUE));
        arduino.addCount(count);
    }

    public void updateArduinoStockStatus(Long id, String stockStatus) {
        Arduino arduino = arduinoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ExceptionMessageSource.NOT_FOUND_VALUE));
        try {
            ArduinoStockStatus updateStatus = ArduinoStockStatus.valueOf(stockStatus);
            arduino.updateStockStatus(updateStatus);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(ExceptionMessageSource.NOT_SUPPORTED_ARGUMENT);
        }
    }

    private static List<ArduinoPhoto> toArduinoPhotoList(FileListDto photos, Arduino arduino) {
        return photos.getFiles().stream()
                .map(photo -> ArduinoPhoto.of(photo.getUuid(), photo.getFileName(), photo.getFilePath(), photo.getSize(), arduino))
                .toList();
    }

    public static boolean isPresent(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    public static boolean isPresent(Map<?, ?> map) {
        return map != null && !map.isEmpty();
    }
}

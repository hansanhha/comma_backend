package know_wave.comma.arduino.component.admin.service;

import jakarta.persistence.EntityNotFoundException;
import know_wave.comma.arduino.component.admin.dto.ArduinoCreateForm;
import know_wave.comma.arduino.component.admin.dto.ArduinoUpdateForm;
import know_wave.comma.arduino.component.entity.Arduino;
import know_wave.comma.arduino.component.entity.ArduinoPhoto;
import know_wave.comma.arduino.component.entity.ArduinoStockStatus;
import know_wave.comma.arduino.component.entity.Category;
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

@Service
@Transactional
@RequiredArgsConstructor
public class ComponentCommandAdminService {

    private final FileService fileService;
    private final ArduinoRepository arduinoRepository;
    private final ArduinoPhotoRepository arduinoPhotoRepository;

    private final String ARDUINO_IMAGE_UPLOAD_PATH = "arduino/image";

    public void registerArduino(ArduinoCreateForm form) {
        Arduino arduino = Arduino.create(form.getArduinoName(), form.getCount(), form.getDescription(),
                form.getCategories().stream().map(Category::createById).toList());

        arduinoRepository.save(arduino);

        if (isPresent(form.getPhotoFiles())) {
            FileListDto fileListDto = fileService.saveAllImage(form.getPhotoFiles(), ARDUINO_IMAGE_UPLOAD_PATH);
            List<ArduinoPhoto> uploadPhotos = toArduinoPhoto(fileListDto, arduino);
            arduinoPhotoRepository.saveAll(uploadPhotos);
        }
    }

    public void registerArduinoList(List<ArduinoCreateForm> forms) {
        forms.forEach(this::registerArduino);
    }

    public void updateArduino(Long id, ArduinoUpdateForm form) {
        Arduino arduino = arduinoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ExceptionMessageSource.NOT_FOUND_VALUE));
        arduino.update(form.getUpdatedArduinoName(), form.getUpdatedCount(), form.getUpdatedDescription(), form.getUpdatedCategories().stream().map(Category::createById).toList());

        if (isPresent(form.getDeletedPhotoFiles())) {
            List<ArduinoPhoto> photos = arduinoPhotoRepository.findAllByUuid(form.getDeletedPhotoFiles());
            ArduinoPhoto.deleteList(photos);
        }

        if (isPresent(form.getAddedPhotoFiles())) {
            FileListDto fileListDto = fileService.saveAllImage(form.getAddedPhotoFiles(), ARDUINO_IMAGE_UPLOAD_PATH);
            List<ArduinoPhoto> uploadPhotos = toArduinoPhoto(fileListDto, arduino);
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

    private static List<ArduinoPhoto> toArduinoPhoto(FileListDto photos, Arduino arduino) {
        return photos.getFiles().stream()
                .map(photo -> ArduinoPhoto.create(photo.getUuid(), photo.getFileName(), photo.getFilePath(), photo.getSize(), arduino))
                .toList();
    }

    private static boolean isPresent(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
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
}

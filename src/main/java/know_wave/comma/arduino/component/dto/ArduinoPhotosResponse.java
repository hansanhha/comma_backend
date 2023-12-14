package know_wave.comma.arduino.component.dto;

import know_wave.comma.arduino.component.entity.ArduinoPhoto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ArduinoPhotosResponse {

    public static ArduinoPhotosResponse to(List<ArduinoPhoto> photos) {
        return new ArduinoPhotosResponse(photos.stream()
                .map(photo -> ArduinoPhotoResponse.of(photo.getFileUuid(), photo.getFileName(), photo.getFilePath(), photo.getSize(), photo.getArduino().getId()))
                .toList());
    }

    private final List<ArduinoPhotoResponse> photos;

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ArduinoPhotoResponse {

        private static ArduinoPhotoResponse of(String fileUuid, String fileName, String filePath, Long fileSize, Long arduinoId){
            return new ArduinoPhotoResponse(fileUuid, fileName, filePath, fileSize, arduinoId);
        }

        private final String fileUuid;
        private final String fileName;
        private final String filePath;
        private final Long fileSize;
        private final Long arduinoId;

    }

}

package know_wave.comma.common.file.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FileDto {

    public static FileDto create(String uuid, String fileName, String filePath, String fileType, long size) {
        return new FileDto(uuid, fileName, filePath, fileType, size);
    }

    private final String uuid;
    private final String fileName;
    private final String filePath;
    private final String fileType;
    private final long size;

}

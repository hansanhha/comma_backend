package know_wave.comma.common.file.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class FileListDto {

    public static FileListDto to(List<FileDto> files) {
        return new FileListDto(files);
    }

    private final List<FileDto> files;
}

package know_wave.comma.common.file.service;

import know_wave.comma.common.file.dto.FileDto;
import know_wave.comma.common.file.dto.FileListDto;
import know_wave.comma.common.file.exception.NotSupportedTypeException;
import know_wave.comma.common.file.exception.UploadFileException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class FileService {

    private final Set<String> SUPPORT_IMAGE_FILE_TYPES = Set.of("image/png", "image/jpeg", "image/gif", "image/svg+xml", "image/webp", "image/jpg");

    public FileDto save(MultipartFile file, String uploadPath) throws UploadFileException {
        String uuid = getUuid();
        String filename = file.getOriginalFilename();
        String filePath = getFilePath(uploadPath, uuid, filename);

        Path savePath = Paths.get(filePath);

        try {
            file.transferTo(savePath);
            return FileDto.create(uuid, filename, filePath, file.getContentType(), file.getSize());
        } catch (IOException e) {
            throw new UploadFileException("파일 업로드에 실패했습니다.");
        }

    }

    public FileListDto saveAll(List<MultipartFile> files, String uploadPath) {
        return FileListDto.to(files.stream()
                .map(file -> save(file, uploadPath))
                .toList());
    }

    public FileDto saveImage(MultipartFile file, String uploadPath) throws NotSupportedTypeException {
        boolean notSupportedType = SUPPORT_IMAGE_FILE_TYPES.stream()
                                            .noneMatch(fileType -> fileType.equals(file.getContentType()));

        if (notSupportedType) {
            throw new NotSupportedTypeException("지원하지 않는 파일 형식입니다.");
        }

        return save(file, uploadPath);
    }

    public FileListDto saveAllImage(List<MultipartFile> files, String uploadPath) {
        return FileListDto.to(files.stream()
                .map(file -> saveImage(file, uploadPath))
                .toList());
    }

    public void delete(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    public void deleteAll(List<String> filePaths) {
        filePaths.forEach(this::delete);
    }

    private String getUuid() {
        return UUID.randomUUID().toString();
    }

    private String getFilePath(String uploadPath, String uuid, String originalFilename) {
        return uploadPath + File.separator + uuid + "_" + originalFilename;
    }
}

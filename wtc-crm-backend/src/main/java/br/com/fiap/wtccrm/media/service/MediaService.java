package br.com.fiap.wtccrm.media.service;

import br.com.fiap.wtccrm.exception.BusinessException;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MediaService {
    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png", "image/webp");
    private static final long MAX_SIZE = 5L * 1024L * 1024L;

    public String uploadFile(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("File is required");
        }
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new BusinessException("Unsupported file type");
        }
        if (file.getSize() > MAX_SIZE) {
            throw new BusinessException("File size must be <= 5MB");
        }
        String extension = extractExtension(file.getOriginalFilename());
        String generated = UUID.randomUUID() + extension;
        return "https://storage.googleapis.com/wtc-crm/" + folder + "/" + generated;
    }

    public void deleteFile(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            throw new BusinessException("fileName is required");
        }
    }

    private String extractExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return ".jpg";
        }
        return fileName.substring(fileName.lastIndexOf('.'));
    }
}

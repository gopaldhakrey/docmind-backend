package in.strikes.docmind_backend.service;

import in.strikes.docmind_backend.entity.User;
import in.strikes.docmind_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfilePhotoService {

    private final UserRepository userRepository;

    @Value("${app.profile-upload-dir:uploads/profile}")
    private String uploadDirectory;

    public String uploadPhoto(User user, MultipartFile file) {

        validateFile(file);

        try {
            Path uploadPath = Paths.get(uploadDirectory)
                    .toAbsolutePath()
                    .normalize();

            Files.createDirectories(uploadPath);

            String extension = getExtension(file.getOriginalFilename());

            String filename = "user-" + user.getId() + "-" +
                    UUID.randomUUID() + extension;

            Path targetPath = uploadPath.resolve(filename).normalize();

            if (!targetPath.startsWith(uploadPath)) {
                throw new IllegalArgumentException("Invalid file path");
            }

            Files.copy(
                    file.getInputStream(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            String oldPhoto = user.getProfilePhotoUrl();

            user.setProfilePhotoUrl(
                    "/api/v1/profile/photo/" + filename
            );

            userRepository.save(user);

            deleteOldPhoto(oldPhoto);

            return user.getProfilePhotoUrl();

        } catch (IOException e) {
            throw new RuntimeException("Failed to store profile photo", e);
        }
    }

    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Profile photo is required");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException(
                    "Profile photo must be smaller than 5 MB"
            );
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                (!contentType.equals("image/jpeg") &&
                        !contentType.equals("image/png") &&
                        !contentType.equals("image/webp"))) {

            throw new IllegalArgumentException(
                    "Only JPG, PNG and WEBP images are supported"
            );
        }
    }

    private String getExtension(String filename) {

        if (filename == null || !filename.contains(".")) {
            throw new IllegalArgumentException("Invalid image filename");
        }

        return filename.substring(
                filename.lastIndexOf(".")
        ).toLowerCase();
    }

    private void deleteOldPhoto(String photoUrl) {

        if (photoUrl == null || photoUrl.isBlank()) {
            return;
        }

        try {
            String filename = Paths.get(photoUrl)
                    .getFileName()
                    .toString();

            Path path = Paths.get(uploadDirectory)
                    .toAbsolutePath()
                    .normalize()
                    .resolve(filename)
                    .normalize();

            if (path.startsWith(
                    Paths.get(uploadDirectory)
                            .toAbsolutePath()
                            .normalize()
            )) {
                Files.deleteIfExists(path);
            }

        } catch (Exception ignored) {
            // Old photo cleanup should not break a successful upload.
        }
    }
    public void removePhoto(User user) {

        String photoUrl = user.getProfilePhotoUrl();

        if (photoUrl == null || photoUrl.isBlank()) {
            return;
        }

        deleteOldPhoto(photoUrl);

        user.setProfilePhotoUrl(null);

        userRepository.save(user);
    }
}
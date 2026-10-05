
package in.strikes.docmind_backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import in.strikes.docmind_backend.entity.User;
import in.strikes.docmind_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProfilePhotoService {

    private final UserRepository userRepository;
    private final Cloudinary cloudinary;

    public String uploadPhoto(User user, MultipartFile file) {

        validateFile(file);

        try {
            String oldPhotoUrl = user.getProfilePhotoUrl();

            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "docmind/profile",
                            "public_id", "user_" + user.getId(),
                            "overwrite", true,
                            "resource_type", "image"
                    )
            );

            String photoUrl = (String) uploadResult.get("secure_url");

            user.setProfilePhotoUrl(photoUrl);
            userRepository.save(user);

            // Old image is automatically replaced because
            // the same public_id is used for this user.

            return photoUrl;

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to upload profile photo to Cloudinary",
                    e
            );
        }
    }

    public void removePhoto(User user) {

        String photoUrl = user.getProfilePhotoUrl();

        if (photoUrl == null || photoUrl.isBlank()) {
            return;
        }

        try {
            cloudinary.uploader().destroy(
                    "docmind/profile/user_" + user.getId(),
                    ObjectUtils.asMap("resource_type", "image")
            );
        } catch (Exception e) {
            // Do not fail profile update if Cloudinary cleanup fails.
        }

        user.setProfilePhotoUrl(null);
        userRepository.save(user);
    }

    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Profile photo is required"
            );
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
}

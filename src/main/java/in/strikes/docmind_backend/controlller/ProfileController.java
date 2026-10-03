package in.strikes.docmind_backend.controlller;

import in.strikes.docmind_backend.entity.User;
import in.strikes.docmind_backend.service.ProfilePhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfilePhotoService profilePhotoService;

    @GetMapping
    public ResponseEntity<?> getProfile(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                Map.of(
                        "id", user.getId(),
                        "username", user.getUsername(),
                        "email", user.getEmail(),
                        "role", user.getRole(),
                        "profilePhotoUrl",
                        user.getProfilePhotoUrl() == null
                                ? ""
                                : user.getProfilePhotoUrl()
                )
        );
    }

    @PostMapping(
            value = "/photo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadProfilePhoto(
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) {

        User user = (User) authentication.getPrincipal();

        String photoUrl =
                profilePhotoService.uploadPhoto(user, file);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Profile photo uploaded successfully",
                        "profilePhotoUrl", photoUrl
                )
        );
    }
    @GetMapping("/photo/{filename}")
    public ResponseEntity<Resource> getProfilePhoto(
            @PathVariable String filename
    ) throws MalformedURLException {

        Path uploadPath = Paths.get("uploads/profile")
                .toAbsolutePath()
                .normalize();

        Path filePath = uploadPath
                .resolve(filename)
                .normalize();

        if (!filePath.startsWith(uploadPath)) {
            return ResponseEntity.badRequest().build();
        }

        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(
                        "Content-Type",
                        "image/" + getExtension(filename)
                )
                .body(resource);
    }

    private String getExtension(String filename) {

        int dotIndex = filename.lastIndexOf('.');

        if (dotIndex == -1) {
            return "jpeg";
        }

        String extension = filename
                .substring(dotIndex + 1)
                .toLowerCase();

        if (extension.equals("jpg")) {
            return "jpeg";
        }

        return extension;
    }
    @DeleteMapping("/photo")
    public ResponseEntity<?> removeProfilePhoto(
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();

        profilePhotoService.removePhoto(user);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Profile photo removed successfully"
                )
        );
    }
}
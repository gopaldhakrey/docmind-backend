package in.strikes.docmind_backend.controlller;

import in.strikes.docmind_backend.dto.AdminUserDto;
import in.strikes.docmind_backend.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import in.strikes.docmind_backend.dto.UpdateUserRoleRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import in.strikes.docmind_backend.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<List<AdminUserDto>> getAllUsers() {

        return ResponseEntity.ok(
                adminUserService.getAllUsers()
        );
    }
    @PatchMapping("/{userId}/role")
    public ResponseEntity<AdminUserDto> updateUserRole(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserRoleRequest request
    ) {
        return ResponseEntity.ok(
                adminUserService.updateUserRole(
                        userId,
                        request.getRole()
                )
        );
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long userId,
            Authentication authentication
    ) {
        User adminUser = (User) authentication.getPrincipal();

        adminUserService.deleteUser(
                userId,
                adminUser.getId()
        );

        return ResponseEntity.noContent().build();
    }
}
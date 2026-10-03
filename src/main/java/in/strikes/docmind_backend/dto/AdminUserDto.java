package in.strikes.docmind_backend.dto;

import in.strikes.docmind_backend.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminUserDto {

    private Long id;
    private String username;
    private String email;
    private Role role;
}
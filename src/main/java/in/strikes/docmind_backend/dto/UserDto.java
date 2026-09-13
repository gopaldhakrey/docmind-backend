package in.strikes.docmind_backend.dto;

import in.strikes.docmind_backend.entity.Role;

public record UserDto(Long id,
                      String username,
                      String email,
                      Role role) {
}

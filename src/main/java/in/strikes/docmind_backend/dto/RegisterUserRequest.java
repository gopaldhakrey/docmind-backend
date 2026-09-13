package in.strikes.docmind_backend.dto;

public record RegisterUserRequest( String username,
                                   String email,
                                   String password
) {
}


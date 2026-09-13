package in.strikes.docmind_backend.dto;

public record LoginResponse (
String accessToken,
        UserDto user
) {
}



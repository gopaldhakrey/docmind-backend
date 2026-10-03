package in.strikes.docmind_backend.controlller;
import in.strikes.docmind_backend.dto.ForgotPasswordRequest;
import in.strikes.docmind_backend.dto.ResetPasswordRequest;
import in.strikes.docmind_backend.service.PasswordResetService;
import in.strikes.docmind_backend.dto.LoginRequest;
import  in.strikes.docmind_backend.dto.LoginResponse;
import  in.strikes.docmind_backend.dto.RegisterUserRequest;
import in.strikes.docmind_backend.dto.UserDto;
import in.strikes.docmind_backend.entity.User;
import in.strikes.docmind_backend.repository.UserRepository;
import in.strikes.docmind_backend.service.CustomUserDetail;
import in.strikes.docmind_backend.service.JwtService;
import in.strikes.docmind_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final PasswordResetService passwordResetService;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest loginRequest
    ) {

        //authenticate and return the token and user
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginRequest.username(),
                loginRequest.password()
        );
        Authentication authenticated = authenticationManager.authenticate(authentication);
        User user = userRepository.findByUsername(loginRequest.username()).orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(new CustomUserDetail(user));
        var response = new LoginResponse(token, new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(
            @Valid @RequestBody RegisterUserRequest registerUserRequest
    ) {
        UserDto userDto = userService.registerUser(registerUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);

    }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {
        passwordResetService.createResetToken(request.getEmail());

        return ResponseEntity.ok(
                "If an account exists with this email, a password reset link has been sent."
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        passwordResetService.resetPassword(
                request.getToken(),
                request.getNewPassword()
        );

        return ResponseEntity.ok("Password reset successfully");
    }



}

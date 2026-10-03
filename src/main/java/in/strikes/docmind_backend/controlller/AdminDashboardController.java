package in.strikes.docmind_backend.controlller;

import in.strikes.docmind_backend.dto.AdminDashboardDto;
import in.strikes.docmind_backend.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardDto> getDashboard() {

        return ResponseEntity.ok(
                adminDashboardService.getDashboardStats()
        );
    }
}
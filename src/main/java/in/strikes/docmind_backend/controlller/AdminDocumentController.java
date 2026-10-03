package in.strikes.docmind_backend.controlller;

import in.strikes.docmind_backend.dto.AdminDocumentDto;
import in.strikes.docmind_backend.service.AdminDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.UUID;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/documents")
@RequiredArgsConstructor
public class AdminDocumentController {

    private final AdminDocumentService adminDocumentService;

    @GetMapping
    public ResponseEntity<List<AdminDocumentDto>> getAllDocuments() {

        return ResponseEntity.ok(
                adminDocumentService.getAllDocuments()
        );
    }
    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable UUID documentId
    ) {
        adminDocumentService.deleteDocument(documentId);
        return ResponseEntity.noContent().build();
    }
}
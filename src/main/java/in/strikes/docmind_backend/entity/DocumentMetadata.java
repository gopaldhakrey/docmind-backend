package in.strikes.docmind_backend.entity;


import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;


@Entity
@Table(name = "document_metadata")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String contentType;
    private Long fileSize;
    private Integer totalPages;
    private Integer totalChunks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentStatus status;

    @Column(length = 1000)
    private String errorMessage;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // User who uploaded this document
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;



}

package in.strikes.docmind_backend.repository;

import org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig;
import org.springframework.data.jpa.repository.JpaRepository;
import in.strikes.docmind_backend.entity.DocumentMetadata;
import in.strikes.docmind_backend.entity.DocumentStatus;
import in.strikes.docmind_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentMetadataRepo extends JpaRepository<DocumentMetadata, UUID> {

    List<DocumentMetadata> findByStatus(DocumentStatus status);

    List<DocumentMetadata> findAllByOrderByCreatedAtDesc();


    List<DocumentMetadata> findByUserOrderByCreatedAtDesc(User user);

    Optional<DocumentMetadata> findByIdAndUser(UUID id, User user);

    List<DocumentMetadata> findByUser(User user);




}

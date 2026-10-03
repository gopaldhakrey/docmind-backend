package in.strikes.docmind_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminDashboardDto {

    private long totalUsers;
    private long totalDocuments;
    private long totalConversations;
    private long totalMessages;
}
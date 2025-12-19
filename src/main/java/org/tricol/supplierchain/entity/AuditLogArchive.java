package org.tricol.supplierchain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs_archive", indexes = {
        @Index(name = "idx_archive_user_id", columnList = "user_id"),
        @Index(name = "idx_archive_timestamp", columnList = "action_timestamp"),
        @Index(name = "idx_archive_resource", columnList = "resource"),
        @Index(name = "idx_archived_at", columnList = "archived_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogArchive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String action;

    @Column(nullable = false, length = 100)
    private String resource;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "action_timestamp", nullable = false)
    private LocalDateTime actionTimestamp;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "archived_at", nullable = false)
    private LocalDateTime archivedAt;

    @PrePersist
    protected void onArchive() {
        if (archivedAt == null) {
            archivedAt = LocalDateTime.now();
        }
    }
}

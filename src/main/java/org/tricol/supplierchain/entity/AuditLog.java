package org.tricol.supplierchain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_action_timestamp", columnList = "action_timestamp"),
        @Index(name = "idx_resource", columnList = "resource"),
        @Index(name = "idx_created_at", columnList = "created_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

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

    @Column(name = "action_timestamp", nullable = false)
    @CreationTimestamp
    private LocalDateTime actionTimestamp;


//    @PrePersist
//    protected void onCreate() {
//        createdAt = LocalDateTime.now();
//        if (actionTimestamp == null) {
//            actionTimestamp = LocalDateTime.now();
//        }
//    }
}

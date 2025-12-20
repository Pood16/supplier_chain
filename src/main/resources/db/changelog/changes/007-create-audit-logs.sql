-- liquibase formatted sql

-- changeset admin:007-create-audit-logs
-- comment: Create audit logs and archive tables for security tracking

CREATE TABLE audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    username VARCHAR(50),
    action VARCHAR(100) NOT NULL,
    resource VARCHAR(100) NOT NULL,
    details TEXT,
    ip_address VARCHAR(45),
    action_timestamp TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_action_timestamp (action_timestamp),
    INDEX idx_resource (resource),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE audit_logs_archive (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    username VARCHAR(50),
    action VARCHAR(100) NOT NULL,
    resource VARCHAR(100) NOT NULL,
    details TEXT,
    ip_address VARCHAR(45),
    action_timestamp TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    archived_at TIMESTAMP NOT NULL,
    INDEX idx_archive_user_id (user_id),
    INDEX idx_archive_timestamp (action_timestamp),
    INDEX idx_archive_resource (resource),
    INDEX idx_archived_at (archived_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- rollback DROP TABLE audit_logs_archive;
-- rollback DROP TABLE audit_logs;

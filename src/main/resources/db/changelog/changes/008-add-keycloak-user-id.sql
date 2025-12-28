-- liquibase formatted sql

-- changeset admin:008-add-keycloak-user-id
-- comment: Add keycloak_user_id column to support Keycloak authentication

ALTER TABLE users 
    ADD COLUMN keycloak_user_id VARCHAR(255) UNIQUE,
    MODIFY COLUMN password VARCHAR(255) NULL;

CREATE INDEX idx_keycloak_user_id ON users(keycloak_user_id);

-- rollback ALTER TABLE users DROP COLUMN keycloak_user_id;
-- rollback ALTER TABLE users MODIFY COLUMN password VARCHAR(255) NOT NULL;
-- rollback DROP INDEX idx_keycloak_user_id ON users;

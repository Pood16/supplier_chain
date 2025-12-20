-- liquibase formatted sql

-- changeset admin:102-insert-security-data
-- comment: Insert permissions, role permissions, and initial users based on TRICOL permissions matrix

-- ==========================================
-- INSERT PERMISSIONS (based on permissions_matrix_md.md)
-- ==========================================

-- FOURNISSEUR Permissions
INSERT INTO permissions (name, description, resource, action) VALUES
('FOURNISSEUR_CREATE', 'Créer/Modifier/Supprimer fournisseur', 'FOURNISSEUR', 'WRITE'),
('FOURNISSEUR_READ', 'Consulter fournisseur', 'FOURNISSEUR', 'READ');

-- PRODUIT Permissions
INSERT INTO permissions (name, description, resource, action) VALUES
('PRODUIT_CREATE', 'Créer/Modifier/Supprimer produit', 'PRODUIT', 'WRITE'),
('PRODUIT_READ', 'Consulter produit', 'PRODUIT', 'READ'),
('PRODUIT_CONFIGURE_ALERT', 'Configurer seuils d''alerte', 'PRODUIT', 'CONFIGURE');

-- COMMANDE FOURNISSEUR Permissions
INSERT INTO permissions (name, description, resource, action) VALUES
('COMMANDE_CREATE', 'Créer/Modifier commande fournisseur', 'COMMANDE', 'WRITE'),
('COMMANDE_VALIDATE', 'Valider commande fournisseur', 'COMMANDE', 'VALIDATE'),
('COMMANDE_CANCEL', 'Annuler commande fournisseur', 'COMMANDE', 'CANCEL'),
('COMMANDE_RECEIVE', 'Réceptionner commande fournisseur', 'COMMANDE', 'RECEIVE'),
('COMMANDE_READ', 'Consulter commande fournisseur', 'COMMANDE', 'READ');

-- STOCK & LOTS Permissions
INSERT INTO permissions (name, description, resource, action) VALUES
('STOCK_READ', 'Consulter stock/lots', 'STOCK', 'READ'),
('STOCK_VALUATION_READ', 'Voir valorisation FIFO', 'STOCK', 'VALUATION'),
('STOCK_HISTORY_READ', 'Consulter historique mouvements', 'STOCK', 'HISTORY');

-- BON DE SORTIE Permissions
INSERT INTO permissions (name, description, resource, action) VALUES
('BON_SORTIE_CREATE', 'Créer bon de sortie (brouillon)', 'BON_SORTIE', 'WRITE'),
('BON_SORTIE_VALIDATE', 'Valider bon de sortie', 'BON_SORTIE', 'VALIDATE'),
('BON_SORTIE_CANCEL', 'Annuler bon de sortie', 'BON_SORTIE', 'CANCEL'),
('BON_SORTIE_READ', 'Consulter bon de sortie', 'BON_SORTIE', 'READ');

-- ADMINISTRATION Permissions
INSERT INTO permissions (name, description, resource, action) VALUES
('USER_MANAGE', 'Gérer utilisateurs', 'USER', 'MANAGE'),
('AUDIT_VIEW', 'Voir logs d''audit', 'AUDIT', 'VIEW');

-- ==========================================
-- ASSIGN ROLE PERMISSIONS (Based on permissions_matrix_md.md)
-- ==========================================

-- ADMIN - Full permissions on everything
INSERT INTO role_permissions (role, permission_id)
SELECT 'ADMIN', id FROM permissions;

-- RESPONSABLE_ACHATS (Purchase Manager) - Based on matrix
INSERT INTO role_permissions (role, permission_id)
SELECT 'RESPONSABLE_ACHATS', id FROM permissions WHERE name IN (
    -- FOURNISSEUR: Créer/Modifier/Supprimer + Consulter
    'FOURNISSEUR_CREATE', 'FOURNISSEUR_READ',
    -- PRODUIT: Créer/Modifier/Supprimer + Consulter + Configurer seuils
    'PRODUIT_CREATE', 'PRODUIT_READ', 'PRODUIT_CONFIGURE_ALERT',
    -- COMMANDE: Créer/Modifier + Valider + Annuler + Consulter
    'COMMANDE_CREATE', 'COMMANDE_VALIDATE', 'COMMANDE_CANCEL', 'COMMANDE_READ',
    -- STOCK: Consulter stock/lots + Voir valorisation FIFO + Consulter historique
    'STOCK_READ', 'STOCK_VALUATION_READ', 'STOCK_HISTORY_READ',
    -- BON SORTIE: Consulter only
    'BON_SORTIE_READ'
);

-- MAGASINIER (Warehouse Manager) - Based on matrix
INSERT INTO role_permissions (role, permission_id)
SELECT 'MAGASINIER', id FROM permissions WHERE name IN (
    -- FOURNISSEUR: Consulter only
    'FOURNISSEUR_READ',
    -- PRODUIT: Consulter only
    'PRODUIT_READ',
    -- COMMANDE: Réceptionner + Consulter
    'COMMANDE_RECEIVE', 'COMMANDE_READ',
    -- STOCK: Consulter stock/lots + Voir valorisation FIFO + Consulter historique
    'STOCK_READ', 'STOCK_VALUATION_READ', 'STOCK_HISTORY_READ',
    -- BON SORTIE: Créer (brouillon) + Valider + Annuler + Consulter
    'BON_SORTIE_CREATE', 'BON_SORTIE_VALIDATE', 'BON_SORTIE_CANCEL', 'BON_SORTIE_READ'
);

-- CHEF_ATELIER (Workshop Manager) - Based on matrix
INSERT INTO role_permissions (role, permission_id)
SELECT 'CHEF_ATELIER', id FROM permissions WHERE name IN (
    -- PRODUIT: Consulter only
    'PRODUIT_READ',
    -- STOCK: Consulter stock/lots + Consulter historique (NO valorisation FIFO)
    'STOCK_READ', 'STOCK_HISTORY_READ',
    -- BON SORTIE: Créer (brouillon) + Consulter
    'BON_SORTIE_CREATE', 'BON_SORTIE_READ'
);

-- ==========================================
-- CREATE SAMPLE USERS FOR EACH ROLE
-- Password for all: Tricol@2024 (BCrypt encoded with strength 12)
-- ** CHANGE THESE PASSWORDS AFTER FIRST LOGIN **
-- ==========================================

-- BCrypt hash for 'Tricol@2024' with strength 12
-- $2a$12$mF8LPmVVqZPqKvLTJXVJB.Py6x3YQ4ZJZmWHGKJZQvKxRxYgPfJiC

-- Admin User
INSERT INTO users (username, email, password, role, enabled)
VALUES ('admin', 'admin@tricol.com', '$2a$12$mF8LPmVVqZPqKvLTJXVJB.Py6x3YQ4ZJZmWHGKJZQvKxRxYgPfJiC', 'ADMIN', TRUE);

-- Purchase Manager
INSERT INTO users (username, email, password, role, enabled)
VALUES ('resp_achats', 'achats@tricol.com', '$2a$12$mF8LPmVVqZPqKvLTJXVJB.Py6x3YQ4ZJZmWHGKJZQvKxRxYgPfJiC', 'RESPONSABLE_ACHATS', TRUE);

-- Warehouse Manager
INSERT INTO users (username, email, password, role, enabled)
VALUES ('magasinier', 'magasin@tricol.com', '$2a$12$mF8LPmVVqZPqKvLTJXVJB.Py6x3YQ4ZJZmWHGKJZQvKxRxYgPfJiC', 'MAGASINIER', TRUE);

-- Workshop Manager
INSERT INTO users (username, email, password, role, enabled)
VALUES ('chef_atelier', 'atelier@tricol.com', '$2a$12$mF8LPmVVqZPqKvLTJXVJB.Py6x3YQ4ZJZmWHGKJZQvKxRxYgPfJiC', 'CHEF_ATELIER', TRUE);

-- ==========================================
-- DEFAULT USER CREDENTIALS
-- ==========================================
-- Username: admin          | Email: admin@tricol.com     | Password: Tricol@2024 | Role: ADMIN
-- Username: resp_achats    | Email: achats@tricol.com    | Password: Tricol@2024 | Role: RESPONSABLE_ACHATS
-- Username: magasinier     | Email: magasin@tricol.com   | Password: Tricol@2024 | Role: MAGASINIER
-- Username: chef_atelier   | Email: atelier@tricol.com   | Password: Tricol@2024 | Role: CHEF_ATELIER
--
-- ** CHANGE ALL PASSWORDS IMMEDIATELY AFTER FIRST LOGIN **
-- ==========================================

-- rollback DELETE FROM role_permissions;
-- rollback DELETE FROM permissions;
-- rollback DELETE FROM users WHERE username IN ('admin', 'resp_achats', 'magasinier', 'chef_atelier');

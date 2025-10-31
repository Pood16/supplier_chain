
-- liquibase formatted sql

-- changeset lahcen:001-create-produit-table
CREATE TABLE produits (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         reference VARCHAR(50) NOT NULL UNIQUE,
                         nom VARCHAR(255) NOT NULL,
                         description TEXT,
                         prix_unitaire DECIMAL(10,2) NOT NULL,
                         stock_actuel INT NOT NULL,
                         point_commande INT NOT NULL,
                         unite_mesure VARCHAR(50) NOT NULL,
                         categorie VARCHAR(100) NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- changeset lahcen:002-create-fournisseur-table
CREATE TABLE fournisseurs (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             raison_sociale VARCHAR(255) NOT NULL,
                             adresse VARCHAR(500) NOT NULL,
                             ville VARCHAR(100) NOT NULL,
                             personne_contact VARCHAR(255) NOT NULL,
                             email VARCHAR(255) NOT NULL,
                             telephone VARCHAR(50) NOT NULL,
                             ice VARCHAR(255) NOT NULL UNIQUE,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- changeset lahcen:003-create-commande-fournisseur-table
CREATE TABLE commande_fournisseur (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      numero_commande VARCHAR(255) NOT NULL UNIQUE,
                                      fournisseur_id BIGINT NOT NULL,
                                      date_commande TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      date_livraison_prevue DATE,
                                      date_livraison_effective DATE,
                                      statut VARCHAR(20) NOT NULL DEFAULT 'EN_ATTENTE',
                                      montant_total DECIMAL(12,2) NOT NULL DEFAULT 0,
                                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                      CONSTRAINT fk_commande_fournisseur FOREIGN KEY (fournisseur_id) REFERENCES fournisseurs(id)
);

-- changeset lahcen:004-create-lignes-commande-table
CREATE TABLE lignes_commande (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                commande_id BIGINT NOT NULL,
                                produit_id BIGINT NOT NULL,
                                quantite DECIMAL(10,2) NOT NULL,
                                prix_unitaire DECIMAL(10,2) NOT NULL,
                                montant_total DECIMAL(12,2) NOT NULL DEFAULT 0,
                                CONSTRAINT fk_ligne_commande FOREIGN KEY (commande_id) REFERENCES commande_fournisseur(id),
                                CONSTRAINT fk_ligne_produit FOREIGN KEY (produit_id) REFERENCES produits(id)
);

-- changeset lahcen:005-add-indexes
CREATE INDEX idx_commande_fournisseur ON commande_fournisseur(fournisseur_id);
CREATE INDEX idx_ligne_commande ON lignes_commande(commande_id);
CREATE INDEX idx_ligne_produit ON lignes_commande(produit_id);
CREATE INDEX idx_produit_categorie ON produits(categorie);
CREATE INDEX idx_commande_statut ON commande_fournisseur(statut);
CREATE INDEX idx_commande_date ON commande_fournisseur(date_commande);
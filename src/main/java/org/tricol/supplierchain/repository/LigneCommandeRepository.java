package org.tricol.supplierchain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tricol.supplierchain.entity.LigneCommande;

public interface LigneCommandeRepository extends JpaRepository<LigneCommande, Long> {
}

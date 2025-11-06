package org.tricol.supplierchain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tricol.supplierchain.entity.BonSortie;


@Repository
public interface BonSortieRepository extends JpaRepository<BonSortie, Integer> {

}

package org.tricol.supplierchain.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;
import org.tricol.supplierchain.dto.request.MouvementStockSearchCriteria;
import org.tricol.supplierchain.entity.LotStock;
import org.tricol.supplierchain.entity.MouvementStock;
import org.tricol.supplierchain.entity.Produit;
import org.tricol.supplierchain.enums.TypeMouvement;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MouvementStockSpecifications {

    public static Specification<MouvementStock> betweenDates(LocalDate dateDebut, LocalDate dateFin) {
        return (root, query, cb) -> {
            if (dateDebut == null && dateFin == null) {
                return cb.conjunction();
            }
            
            Path<LocalDateTime> datePath = root.get("dateMouvement");
            
            if (dateDebut != null && dateFin != null) {
                return cb.between(datePath, 
                    dateDebut.atStartOfDay(), 
                    dateFin.atTime(23, 59, 59));
            } else if (dateDebut != null) {
                return cb.greaterThanOrEqualTo(datePath, dateDebut.atStartOfDay());
            } else {
                return cb.lessThanOrEqualTo(datePath, dateFin.atTime(23, 59, 59));
            }
        };
    }

}
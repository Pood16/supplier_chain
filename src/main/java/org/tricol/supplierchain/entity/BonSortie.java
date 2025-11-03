package org.tricol.supplierchain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tricol.supplierchain.enums.MotifBonSortie;
import org.tricol.supplierchain.enums.StatutBonSortie;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BonSortie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "numero_bon", nullable = false, unique = true)
    private String numeroBon;
    @Column(name = "date_sortie", nullable = false)
    private LocalDate dateSortie;
    @Enumerated
    private StatutBonSortie statut;
    @Enumerated
    private MotifBonSortie motif;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime dateCreation;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime dateModification;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.dateCreation = now;
        this.dateModification = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.dateModification = LocalDateTime.now();
    }


}

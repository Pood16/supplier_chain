package org.tricol.supplierchain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Fournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "raison_social")
    private String raisonSociale;
    private String adresse;
    private String ville;
    @Column(name = "personne_contact")
    private String personneContact;
    private String email;
    private String telephone;
    @Column(unique = true , length = 15)
    private String ice;
    @Column(name = "created_at")
    private LocalDateTime dateCreation ;
    @Column(name = "updated_at")
    private LocalDateTime dateModification;


    @PrePersist
    public void prePersist(){
        this.dateCreation = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        this.dateModification = LocalDateTime.now();
    }
}

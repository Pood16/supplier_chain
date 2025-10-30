package org.tricol.supplierchain.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

@Data
@Entity
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reference;
    private String nom;
    private String description;
    private Double prixUnitarie;
    private int stockActuel;
    private int pointCommande;
    private String uniteMesure;
    private String categorie ;
    @Column(name = "created_at")
    private LocalDateTime dateCreation;
    @Column(name = "updated_at")
    private LocalDateTime dateModification;



}

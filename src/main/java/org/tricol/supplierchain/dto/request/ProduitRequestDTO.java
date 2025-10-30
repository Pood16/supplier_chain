package org.tricol.supplierchain.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProduitRequestDTO {

    @NotBlank(message = "la référence est obligatoire")
    private String reference;

    @NotBlank(message = "le nom est obligatoire")
    private String nom;

    @NotBlank(message = "la description est obligatoire")
    private String description;

    @NotNull(message = "le prix unitaire est obligatoire")
    private Double prixUnitaire;

    @NotNull(message = "le stock actuel est obligatoire")
    private int stockActuel;

    @NotNull(message = "le point de commande est obligatoire")
    private int pointCommande;

    @NotBlank(message = "l'unité de mesure est obligatoire")
    private String uniteMesure;

    @NotBlank(message = "la catégorie est obligatoire")
    private String categorie;


    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;
}

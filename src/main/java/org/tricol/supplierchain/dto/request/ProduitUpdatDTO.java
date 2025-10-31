package org.tricol.supplierchain.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;


@Data
public class ProduitUpdatDTO {


    private String nom;

    private String description;

    @Min(value = 0, message = "le prix unitaire doit être positif")
    private Double prixUnitaire;

    @Min(value = 0, message = "le stock actuel doit être positif")
    private int stockActuel;

    @Min(value = 0, message = "le point de commande doit être positif")
    private int pointCommande;

    private String uniteMesure;

    private String categorie;
}

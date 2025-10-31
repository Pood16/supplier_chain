package org.tricol.supplierchain.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tricol.supplierchain.enums.StatutCommande;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeFournisseurCreateDTO {

    @NotNull(message = "L'identifiant du fournisseur est obligatoire")
    private Long fournisseurId;

    @NotBlank
    @Size(max = 100)
    @NotNull(message = "Numero de commande est obligatoire")
    private String numeroCommande;

    @NotNull(message = "La date de livraison prévue est obligatoire")
    private LocalDate dateLivraisonPrevue;

    @Valid
    @Size(min = 1, message = "La commande doit contenir au moins une ligne.")
    private List<LigneCommandeRequestDTO> lignes;

}

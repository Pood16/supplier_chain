package org.tricol.supplierchain.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeFournisseurUpdateDTO {

    @NotNull(message = "La date de livraison prévue est obligatoire")
    private LocalDate dateLivraisonPrevue;

    @Valid
    @NotNull(message = "Les lignes de commande sont obligatoires")
    @Size(min = 1, message = "La commande doit contenir au moins une ligne")
    private List<LigneCommandeRequestDTO> lignes;
}

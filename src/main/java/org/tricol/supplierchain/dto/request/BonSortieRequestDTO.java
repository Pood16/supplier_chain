package org.tricol.supplierchain.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.tricol.supplierchain.enums.MotifBonSortie;
import org.tricol.supplierchain.enums.StatutBonSortie;

import java.time.LocalDate;
import java.util.List;

@Data
public class BonSortieRequestDTO {


    @NotNull(message = "La date de sortie est obligatoire")
    private LocalDate dateSortie;

    @NotNull(message = "Le motif est obligatoire")
    private MotifBonSortie motif;

    @NotEmpty(message = "La liste des lignes de bon de sortie ne peut pas être vide")
    @Valid
    private List<LigneBonSortieRequestDTO> ligneBonSorties;
}
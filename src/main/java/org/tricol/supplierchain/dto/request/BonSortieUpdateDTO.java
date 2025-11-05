package org.tricol.supplierchain.dto.request;

import jakarta.validation.Valid;
import lombok.Data;
import org.tricol.supplierchain.enums.MotifBonSortie;
import org.tricol.supplierchain.enums.StatutBonSortie;

import java.time.LocalDate;
import java.util.List;

@Data
public class BonSortieUpdateDTO {

    private String numeroBon;

    private LocalDate dateSortie;

    private StatutBonSortie statut;

    private MotifBonSortie motif;

    @Valid
    private List<LigneBonSortieRequestDTO> ligneBonSorties;
}


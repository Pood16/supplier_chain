package org.tricol.supplierchain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tricol.supplierchain.enums.StatutCommande;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeFournisseurResponseDTO {
    
    private Long id;
    private String numeroCommande;
    private String fournisseurNom;
    private LocalDateTime dateCommande;
    private LocalDate dateLivraisonPrevue;
    private LocalDate dateLivraisonEffective;
    private StatutCommande statut;
    private BigDecimal montantTotal;
    private List<LigneCommandeResponseDTO> lignesCommande = new ArrayList<>();
    private LocalDateTime updatedAt;
}
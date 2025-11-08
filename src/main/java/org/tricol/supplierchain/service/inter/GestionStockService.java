package org.tricol.supplierchain.service.inter;

import org.tricol.supplierchain.dto.response.*;
import org.tricol.supplierchain.entity.Fournisseur;
import org.tricol.supplierchain.entity.Produit;

import java.math.BigDecimal;
import java.util.List;


public interface GestionStockService {

    StockGlobalResponseDTO getStockGlobal();
    StockProduitResponseDTO getStockByProduit(Long produitId);
    List<MouvementStockResponseDTO> getHistoriqueMouvements();
    List<MouvementStockResponseDTO> getMouvementsByProduit(Long produitId);
    CommandeFournisseurResponseDTO createCommandeFournisseurEnCasUrgente(Produit produit, Fournisseur fournisseur);
    ValorisationStockResponseDTO getValorisationTotale();

    boolean isStockSuffisant(Long produitId, BigDecimal quantiteRequise);
    BigDecimal getQuantiteDisponible(Long produitId);




}

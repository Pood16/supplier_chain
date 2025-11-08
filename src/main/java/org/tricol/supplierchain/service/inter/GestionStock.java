package org.tricol.supplierchain.service.inter;

import org.springframework.stereotype.Service;
import org.tricol.supplierchain.dto.response.*;

import java.math.BigDecimal;
import java.util.List;


public interface GestionStock {

    StockGlobalResponseDTO getStockGlobal();
    StockProduitResponseDTO getStockByProduit(Long produitId);
    List<MouvementStockResponseDTO> getHistoriqueMouvements();
    List<MouvementStockResponseDTO> getMouvementsByProduit(Long produitId);
    List<AlerteStockResponseDTO> getAllAlerts();
    ValorisationStockResponseDTO getValorisationTotale();

    boolean isStockSuffisant(Long produitId, BigDecimal quantiteRequise);
    BigDecimal getQuantiteDisponible(Long produitId);




}

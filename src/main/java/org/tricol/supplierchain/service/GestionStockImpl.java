package org.tricol.supplierchain.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tricol.supplierchain.dto.response.*;
import org.tricol.supplierchain.entity.LotStock;
import org.tricol.supplierchain.entity.Produit;
import org.tricol.supplierchain.enums.StatutLot;
import org.tricol.supplierchain.exception.ResourceNotFoundException;
import org.tricol.supplierchain.mapper.LotStockMapper;
import org.tricol.supplierchain.mapper.MouvementStockMapper;
import org.tricol.supplierchain.mapper.StockMapper;
import org.tricol.supplierchain.repository.LotStockRepository;
import org.tricol.supplierchain.repository.MouvementStockRepository;
import org.tricol.supplierchain.repository.ProduitRepository;
import org.tricol.supplierchain.service.inter.GestionStock;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class GestionStockImpl implements GestionStock {


    private final StockMapper stockMapper;
    private final LotStockMapper lotStockMapper;
    private final MouvementStockMapper mouvementStockMapper;
    private final ProduitRepository produitRepository;
    private final LotStockRepository lotStockRepository;
    private final MouvementStockRepository mouvementStockRepository;


    @Override
    @Transactional(readOnly = true)
    public StockGlobalResponseDTO getStockGlobal() {

        List<Produit> produits = produitRepository.findAll();
        Long nombreProduitEnAlerte = produits
                .stream()
                .filter(this::isEnAlerte)
                .count();

        List<StockProduitResponseDTO> stockProduits = produits
                .stream()
                .map(this::buildStockProduitResponse)
                .toList();

       StockGlobalResponseDTO globaleResponse = StockGlobalResponseDTO
               .builder()
               .produits(stockProduits)
               .valorisationTotale(calculerValorisationTotal())
               .nombreProduitsTotal(stockProduits.size())
               .nombreProduitsEnAlerte(nombreProduitEnAlerte)
               .nombreLotsActifs(lotStockRepository.countLotStockByStatut(StatutLot.ACTIF))
               .build();

        return globaleResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public StockProduitResponseDTO getStockByProduit(Long produitId) {
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(()->new ResourceNotFoundException("Pas de produit avec cette ID: "+ produitId));
        return buildStockProduitResponse(produit);
    }

    @Override
    public List<MouvementStockResponseDTO> getHistoriqueMouvements() {

        return mouvementStockRepository.findAll()
                .stream()
                .map(mouvementStockMapper::toResponseDTO)
                .sorted(Comparator.comparing(MouvementStockResponseDTO::getDateMouvement))
                .toList();
    }

    @Override
    public List<MouvementStockResponseDTO> getMouvementsByProduit(Long produitId) {
        return mouvementStockRepository.findAll()
                .stream()
                .map(mouvementStockMapper::toResponseDTO)
                .filter(m->m.getProduitId().equals(produitId))
                .sorted(Comparator.comparing(MouvementStockResponseDTO::getDateMouvement))
                .toList();
    }

    @Override
    public List<AlerteStockResponseDTO> getAllAlerts() {
        return List.of();
    }

    @Override
    public ValorisationStockResponseDTO getValorisationTotale() {
        return null;
    }

    @Override
    public boolean isStockSuffisant(Long produitId, BigDecimal quantiteRequise) {
        return false;
    }

    @Override
    public BigDecimal getQuantiteDisponible(Long produitId) {
        return null;
    }

    private StockProduitResponseDTO buildStockProduitResponse(Produit produit){

        StockProduitResponseDTO produitResponse = stockMapper.toStockProduitDto(produit);

        List<LotStock> actifsLots = lotStockRepository.findByProduitIdAndStatutOrderByDateEntreeAsc(produit.getId(), StatutLot.ACTIF);
        System.err.println(actifsLots);

        produitResponse.setLots(actifsLots
                .stream()
                .map(lotStockMapper::toResponseDTO)
                .toList()
        );

        BigDecimal valorisation = actifsLots
                .stream()
                .map(lot -> lot.getQuantiteRestante().multiply(lot.getPrixUnitaireAchat()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        produitResponse.setValorisationTotale(valorisation);
        produitResponse.setAlerteSeuil(isEnAlerte(produit));


        return produitResponse;
    }

    private boolean isEnAlerte(Produit produit){
        return produit.getStockActuel().compareTo(produit.getPointCommande()) <= 0;
    }

    private BigDecimal calculerValorisationTotal(){
        return lotStockRepository.findByStatut(StatutLot.ACTIF)
                .stream()
                .map(lot->lot.getQuantiteRestante().multiply(lot.getPrixUnitaireAchat()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

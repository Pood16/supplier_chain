package org.tricol.supplierchain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tricol.supplierchain.dto.request.BonSortieRequestDTO;
import org.tricol.supplierchain.dto.request.LigneBonSortieRequestDTO;
import org.tricol.supplierchain.dto.response.BonSortieResponseDTO;
import org.tricol.supplierchain.entity.BonSortie;
import org.tricol.supplierchain.entity.LigneBonSortie;
import org.tricol.supplierchain.entity.Produit;
import org.tricol.supplierchain.enums.StatutBonSortie;
import org.tricol.supplierchain.exception.ResourceNotFoundException;
import org.tricol.supplierchain.mapper.BonSortieMapper;
import org.tricol.supplierchain.repository.BonSortieRepository;
import org.tricol.supplierchain.repository.ProduitRepository;
import org.tricol.supplierchain.service.inter.BonSortieService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BonSortieServiceImpl implements BonSortieService {

    private final BonSortieRepository bonSortieRepository;
    private final ProduitRepository produitRepository;
    private final BonSortieMapper bonSortieMapper;


    @Override
    public BonSortieResponseDTO createBonSortie(BonSortieRequestDTO requestDTO) {
        BonSortie bonSortie = bonSortieMapper.toEntity(requestDTO);
        bonSortie.setNumeroBon(UUID.randomUUID().toString());

        List<LigneBonSortie> ligneBonSortie = new ArrayList<>();
        for(LigneBonSortieRequestDTO lineDto : requestDTO.getLigneBonSorties()) {
            Produit produit = produitRepository.findById(lineDto.getProduitId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec l'id " + lineDto.getProduitId()));
            LigneBonSortie ligne = LigneBonSortie.builder()
                    .produit(produit)
                    .quantite(lineDto.getQuantite())
                    .bonSortie(bonSortie)
                    .build();

            ligneBonSortie.add(ligne);
        }
        bonSortie.setLigneBonSorties(ligneBonSortie);
        bonSortie.setStatut(StatutBonSortie.BROUILLON);
        bonSortie.setMotif(requestDTO.getMotif());

        BonSortie savedBonSortie = bonSortieRepository.save(bonSortie);

        return  bonSortieMapper.toResponseDTO(savedBonSortie);

    }
}

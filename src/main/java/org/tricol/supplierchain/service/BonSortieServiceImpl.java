package org.tricol.supplierchain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tricol.supplierchain.dto.request.BonSortieRequestDTO;
import org.tricol.supplierchain.dto.request.BonSortieUpdateDTO;
import org.tricol.supplierchain.dto.request.LigneBonSortieRequestDTO;
import org.tricol.supplierchain.dto.response.BonSortieResponseDTO;
import org.tricol.supplierchain.entity.BonSortie;
import org.tricol.supplierchain.entity.LigneBonSortie;
import org.tricol.supplierchain.entity.Produit;
import org.tricol.supplierchain.enums.Atelier;
import org.tricol.supplierchain.enums.StatutBonSortie;
import org.tricol.supplierchain.exception.BusinessException;
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
@Transactional
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
        bonSortie.setAtelier(requestDTO.getAtelier());

        BonSortie savedBonSortie = bonSortieRepository.save(bonSortie);

        return  bonSortieMapper.toResponseDTO(savedBonSortie);

    }

    @Override
    public List<BonSortieResponseDTO> getBonSorties() {
        List<BonSortieResponseDTO> Bons = bonSortieRepository.findAll()
                .stream()
                .map(bonSortieMapper::toResponseDTO)
                .toList();
        if (Bons.isEmpty()) {
            throw new ResourceNotFoundException("Aucun bon de sortie trouvé.");
        }
        return Bons;
    }

    @Override
    public BonSortieResponseDTO getBonSortieById(Long id) {
        BonSortie bonSortie = bonSortieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bon de sortie non trouvé avec l'id " + id));
        return bonSortieMapper.toResponseDTO(bonSortie);
    }

    @Override
    public void deleteBonSortie(Long id) {
        BonSortie bonSortie = bonSortieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bon de sortie non trouvé avec l'id " + id));
        if(bonSortie.getStatut() != StatutBonSortie.BROUILLON) {
            throw new BusinessException("Seul les bons de sortie en statut BROUILLON peuvent être supprimés.");
        }
        bonSortieRepository.delete(bonSortie);
    }

    @Override
    public List<BonSortieResponseDTO> getBonSortiesByAtelier(Atelier atelier) {
        List<BonSortieResponseDTO> Bons = bonSortieRepository.findByAtelier(atelier)
                .stream()
                .map(bonSortieMapper::toResponseDTO)
                .toList();
        if (Bons.isEmpty()) {
            throw new ResourceNotFoundException("Aucun bon de sortie trouvé pour l'atelier " + atelier);
        }

        return Bons;
    }

    @Override
    public BonSortieResponseDTO updateBonSortie(Long id, BonSortieUpdateDTO requestDTO) {
        BonSortie existingBonSortie = bonSortieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bon de sortie non trouvé avec l'id " + id));

        if(existingBonSortie.getStatut() != StatutBonSortie.BROUILLON) {
            throw new BusinessException("Seul les bons de sortie en statut BROUILLON peuvent être modifiés.");
        }
        
        if (requestDTO.getLigneBonSorties() != null && !requestDTO.getLigneBonSorties().isEmpty()) {
            existingBonSortie.getLigneBonSorties().clear();

            for (LigneBonSortieRequestDTO lineDto : requestDTO.getLigneBonSorties()) {
                Produit produit = produitRepository.findById(lineDto.getProduitId())
                        .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec l'id " + lineDto.getProduitId()));
                LigneBonSortie ligne = LigneBonSortie.builder()
                        .produit(produit)
                        .quantite(lineDto.getQuantite())
                        .bonSortie(existingBonSortie)
                        .build();
                existingBonSortie.getLigneBonSorties().add(ligne);
            }
        }
        bonSortieMapper.updateEntityFromDto(requestDTO, existingBonSortie);

        BonSortie savedBonSortie = bonSortieRepository.save(existingBonSortie);

        return bonSortieMapper.toResponseDTO(savedBonSortie);
    }

    @Override
    public void annulationBonSortie(Long id) {
        BonSortie bonSortie = bonSortieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bon de sortie non trouvé avec l'id " + id));
        if(bonSortie.getStatut() != StatutBonSortie.BROUILLON) {
            throw new BusinessException("Seul les bons de sortie en statut BROUILLON peuvent être annulés.");
        }
        bonSortie.setStatut(StatutBonSortie.ANNULE);
        bonSortieRepository.save(bonSortie);
    }
}

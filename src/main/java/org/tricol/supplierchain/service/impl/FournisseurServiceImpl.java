package org.tricol.supplierchain.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tricol.supplierchain.dto.request.FournisseurRequestDTO;
import org.tricol.supplierchain.dto.response.FournisseurResponseDTO;
import org.tricol.supplierchain.entity.Fournisseur;
import org.tricol.supplierchain.exception.DuplicateResourceException;
import org.tricol.supplierchain.mapper.FournisseurMapper;
import org.tricol.supplierchain.repository.FournisseurRepository;
import org.tricol.supplierchain.service.FournisseurService;


@Service
@RequiredArgsConstructor
public class FournisseurServiceImpl implements FournisseurService {


    private final FournisseurMapper fournisseurMapper;
    private final FournisseurRepository fournisseurRepository;

    public FournisseurResponseDTO crerateFournisseur(FournisseurRequestDTO fournisseurRequest) {
        Fournisseur fournisseur = fournisseurMapper.toEntity(fournisseurRequest);
        if(fournisseurRepository.existsByEmail(fournisseur.getEmail())) {
            throw new DuplicateResourceException("Fournisseur with Email " + fournisseur.getEmail() + " already exists.");
        }
        if(fournisseurRepository.existsByIce(fournisseur.getIce())) {
            throw new DuplicateResourceException("Fournisseur with ICE " + fournisseur.getIce() + " already exists.");
        }
        return fournisseurMapper.toResponseDTO(
                fournisseurRepository.save(fournisseur)
        );
    }
}

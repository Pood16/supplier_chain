package org.tricol.supplierchain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tricol.supplierchain.dto.request.CommandeFournisseurCreateDTO;
import org.tricol.supplierchain.dto.request.CommandeFournisseurUpdateDTO;
import org.tricol.supplierchain.dto.response.CommandeFournisseurResponseDTO;
import org.tricol.supplierchain.entity.CommandeFournisseur;
import org.tricol.supplierchain.entity.Fournisseur;
import org.tricol.supplierchain.enums.StatutCommande;
import org.tricol.supplierchain.exception.BusinessException;
import org.tricol.supplierchain.exception.ResourceNotFoundException;
import org.tricol.supplierchain.mapper.CommandeFournisseurMapper;
import org.tricol.supplierchain.mapper.LigneCommandeMapper;
import org.tricol.supplierchain.repository.CommandeFournisseurRepository;
import org.tricol.supplierchain.repository.FournisseurRepository;
import org.tricol.supplierchain.repository.LigneCommandeRepository;
import org.tricol.supplierchain.repository.ProduitRepository;
import org.tricol.supplierchain.service.inter.CommandeFournisseurService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CommandeFournisseurServiceimpl implements CommandeFournisseurService {


    private final CommandeFournisseurRepository commandeFournisseurRepository;
    private final FournisseurRepository fournisseurRepository;
    private final LigneCommandeRepository ligneCommandeRepository;
    private final ProduitRepository produitRepository;
    private final CommandeFournisseurMapper commandeFournisseurMapper;
    private final LigneCommandeMapper ligneCommandeMapper;
    private final CommandeFournisseur commandeFournisseur;


    public CommandeFournisseurResponseDTO createCommande(CommandeFournisseurCreateDTO requestDTO){
        Fournisseur fournisseur = fournisseurRepository
                .findById(requestDTO.getFournisseurId())
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur not found with ID: "+requestDTO.getFournisseurId()));

        CommandeFournisseur commande = commandeFournisseurMapper.toEntity(requestDTO);

        commande.setNumeroCommande(UUID.randomUUID().toString());

        commande.calculerMontantTotal();

        CommandeFournisseur saved = commandeFournisseurRepository.save(commande);

        return commandeFournisseurMapper.toResponseDto(saved);
    }

    public CommandeFournisseurResponseDTO updateCommande(Long id, CommandeFournisseurUpdateDTO requestDTO){

        CommandeFournisseur commande = commandeFournisseurRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Commande Found with ID: " + id));

        if (commande.getStatut() != StatutCommande.EN_ATTENTE){
            throw new BusinessException("Can not modify commande with status: " + commande.getStatut());
        }

        commandeFournisseurMapper.updateEntityFromDto(requestDTO, commande);

        commande.calculerMontantTotal();

        CommandeFournisseur updated = commandeFournisseurRepository.save(commande);

        return commandeFournisseurMapper.toResponseDto(updated);
    }


}

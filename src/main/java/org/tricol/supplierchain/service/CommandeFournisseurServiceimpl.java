package org.tricol.supplierchain.service;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tricol.supplierchain.dto.request.CommandeFournisseurCreateDTO;
import org.tricol.supplierchain.dto.request.CommandeFournisseurUpdateDTO;
import org.tricol.supplierchain.dto.request.LigneCommandeRequestDTO;
import org.tricol.supplierchain.dto.response.CommandeFournisseurResponseDTO;
import org.tricol.supplierchain.entity.*;
import org.tricol.supplierchain.enums.StatutCommande;
import org.tricol.supplierchain.enums.StatutLot;
import org.tricol.supplierchain.enums.TypeMouvement;
import org.tricol.supplierchain.exception.BusinessException;
import org.tricol.supplierchain.exception.OperationNotAllowedException;
import org.tricol.supplierchain.exception.ResourceNotFoundException;
import org.tricol.supplierchain.mapper.CommandeFournisseurMapper;
import org.tricol.supplierchain.mapper.LigneCommandeMapper;
import org.tricol.supplierchain.repository.*;
import org.tricol.supplierchain.service.inter.CommandeFournisseurService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Data
@RequiredArgsConstructor
@Builder
@Transactional
public class CommandeFournisseurServiceimpl implements CommandeFournisseurService {


    private final CommandeFournisseurRepository commandeFournisseurRepository;
    private final FournisseurRepository fournisseurRepository;
    private final LigneCommandeRepository ligneCommandeRepository;
    private final ProduitRepository produitRepository;
    private final LotStockRepository lotStockRepository;
    private final MouvementStockRepository mouvementStockRepository;
    private final CommandeFournisseurMapper commandeFournisseurMapper;
    private final LigneCommandeMapper ligneCommandeMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CommandeFournisseurResponseDTO> getAllCommandes(){
        return commandeFournisseurRepository
                .findAll()
                .stream()
                .map(commandeFournisseurMapper::toResponseDto)
                .toList();
    }

    @Override
    public CommandeFournisseurResponseDTO createCommande(CommandeFournisseurCreateDTO requestDTO){
        Fournisseur fournisseur = fournisseurRepository
                .findById(requestDTO.getFournisseurId())
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur not found with ID: "+requestDTO.getFournisseurId()));

        CommandeFournisseur commande = commandeFournisseurMapper.toEntity(requestDTO);

        commande.setFournisseur(fournisseur);

        commande.setNumeroCommande(UUID.randomUUID().toString());

        List<LigneCommande> lignesCommandes = new ArrayList<>();
        for (LigneCommandeRequestDTO ligneDTO : requestDTO.getLignes()){
            Produit produit = produitRepository
                    .findById(ligneDTO.getProduitId())
                    .orElseThrow(() -> new ResourceNotFoundException("Pas de produit avec ID: " + ligneDTO.getProduitId()));
            LigneCommande ligne = LigneCommande.builder()
                    .produit(produit)
                    .commande(commande)
                    .quantite(ligneDTO.getQuantite())
                    .prixUnitaire(ligneDTO.getPrixUnitaire())
                    .build();
            ligne.calculerMontantTotal();
            lignesCommandes.add(ligne);
        }

        commande.setLignesCommande(lignesCommandes);

        commande.calculerMontantTotal();

        CommandeFournisseur saved = commandeFournisseurRepository.save(commande);

        return commandeFournisseurMapper.toResponseDto(saved);
    }

    @Override
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

    @Override
    public CommandeFournisseurResponseDTO getCommandeById(Long id){

        CommandeFournisseur commande = commandeFournisseurRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pas de Commande avec ID: " + id));
        return commandeFournisseurMapper.toResponseDto(commande);

    }

    @Override
    public void deleteCommande(Long id){
        CommandeFournisseur commande = commandeFournisseurRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pas de commande avec ID: " + id));
        if (commande.getStatut() == StatutCommande.LIVREE || commande.getStatut() == StatutCommande.VALIDEE) {
            throw new OperationNotAllowedException("Impossible de supprime un commande Livree ou Validee");
        }
        commandeFournisseurRepository.deleteById(id);
    }

    @Override
    public List<CommandeFournisseurResponseDTO> getCommandesBySupplierId(Long id){
        Fournisseur fournisseur = fournisseurRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pas de Fournisseur avec ID: " + id));
        return commandeFournisseurRepository
                .findBySupplierIdWithRelationship(fournisseur.getId())
                .stream()
                .map(commandeFournisseurMapper::toResponseDto)
                .toList();
    }

    @Override
    public CommandeFournisseurResponseDTO receiveCommande(Long id){

        CommandeFournisseur commande = commandeFournisseurRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pas de Commande Fournisseur avec cette ID: " + id));


        if (commande.getStatut() != StatutCommande.VALIDEE){
            throw new BusinessException("Seules les commandes validees peuvent etre receptionnees. Statut actuel : " + commande.getStatut());
        }

        if (commande.getStatut() == StatutCommande.LIVREE){
            throw new BusinessException("Commande est deja livree.");
        }

        LocalDateTime now = LocalDateTime.now();

        for (LigneCommande ligne: commande.getLignesCommande()){
            Produit produit = ligne.getProduit();

            LotStock lot = LotStock.builder()
                    .numeroLot("LOT-"+UUID.randomUUID())
                    .produit(produit)
                    .commande(commande)
                    .quantiteInitiale(ligne.getQuantite())
                    .quantiteRestante(ligne.getQuantite())
                    .prixUnitaireAchat(ligne.getPrixUnitaire())
                    .statut(StatutLot.ACTIF)
                    .dateEntree(now)
                    .build();
            lotStockRepository.save(lot);

            MouvementStock mouvement = MouvementStock.builder()
                    .lotStock(lot)
                    .dateMouvement(now)
                    .motif("RECEPTION_COMMANDE")
                    .typeMouvement(TypeMouvement.ENTREE)
                    .produit(produit)
                    .quantite(ligne.getQuantite())
                    .reference(commande.getNumeroCommande())
                    .build();
            mouvementStockRepository.save(mouvement);


            produit.setStockActuel(produit.getStockActuel().add(ligne.getQuantite()));

            produitRepository.save(produit);

        }

        commande.setStatut(StatutCommande.LIVREE);
        commande.setDateLivraisonEffective(LocalDate.now());

        CommandeFournisseur receivedCommande = commandeFournisseurRepository.save(commande);

        return commandeFournisseurMapper.toResponseDto(receivedCommande);
    }


}

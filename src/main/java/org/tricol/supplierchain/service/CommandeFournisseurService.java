package org.tricol.supplierchain.service;

import org.tricol.supplierchain.dto.request.CommandeFournisseurRequestDTO;
import org.tricol.supplierchain.dto.response.CommandeFournisseurResponseDTO;

import java.util.List;

public interface CommandeFournisseurService {

    CommandeFournisseurResponseDTO createCommande(CommandeFournisseurRequestDTO requestDTO);

//    List<CommandeFournisseurRequestDTO> getAllCommandes();
//
//    CommandeFournisseurResponseDTO getCommandeById(Long id);
//
//    CommandeFournisseurResponseDTO updateCommande(Long id, CommandeFournisseurRequestDTO requestDTO);
//
//    void deleteCommande(Long id);
//
//    List<CommandeFournisseurResponseDTO> getCommandesBySupplier(Long fournisseurId);
//
//    CommandeFournisseurResponseDTO receiveCommande(Long id);


}

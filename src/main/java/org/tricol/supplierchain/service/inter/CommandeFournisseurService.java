package org.tricol.supplierchain.service.inter;

import org.tricol.supplierchain.dto.request.CommandeFournisseurCreateDTO;
import org.tricol.supplierchain.dto.request.CommandeFournisseurUpdateDTO;
import org.tricol.supplierchain.dto.response.CommandeFournisseurResponseDTO;

public interface CommandeFournisseurService {

    CommandeFournisseurResponseDTO createCommande(CommandeFournisseurCreateDTO requestDTO);

    CommandeFournisseurResponseDTO updateCommande(Long id, CommandeFournisseurUpdateDTO requestDTO);



}

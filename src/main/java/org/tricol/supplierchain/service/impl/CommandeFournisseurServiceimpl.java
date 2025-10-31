package org.tricol.supplierchain.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tricol.supplierchain.dto.request.CommandeFournisseurRequestDTO;
import org.tricol.supplierchain.dto.response.CommandeFournisseurResponseDTO;
import org.tricol.supplierchain.repository.CommandeFournisseurRepository;
import org.tricol.supplierchain.service.CommandeFournisseurService;

@Service
@RequiredArgsConstructor
@Transactional
public class CommandeFournisseurServiceimpl implements CommandeFournisseurService {


    private final CommandeFournisseurRepository commandeFournisseurRepository;



    public CommandeFournisseurResponseDTO createCommande(CommandeFournisseurRequestDTO requestDTO){



        return null;
    }
}

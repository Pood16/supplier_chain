package org.tricol.supplierchain.service;


import org.tricol.supplierchain.dto.request.FournisseurRequestDTO;
import org.tricol.supplierchain.dto.response.FournisseurResponseDTO;

public interface FournisseurService {

    FournisseurResponseDTO crerateFournisseur(FournisseurRequestDTO fournisseurRequest);
}

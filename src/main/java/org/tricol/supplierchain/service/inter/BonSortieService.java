package org.tricol.supplierchain.service.inter;

import org.tricol.supplierchain.dto.request.BonSortieRequestDTO;
import org.tricol.supplierchain.dto.response.BonSortieResponseDTO;

import java.util.List;

public interface BonSortieService {

    BonSortieResponseDTO createBonSortie(BonSortieRequestDTO requestDTO);

    List<BonSortieResponseDTO> getBonSorties();

    BonSortieResponseDTO getBonSortieById(Long id);
}

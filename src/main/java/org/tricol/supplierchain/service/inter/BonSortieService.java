package org.tricol.supplierchain.service.inter;

import org.tricol.supplierchain.dto.request.BonSortieRequestDTO;
import org.tricol.supplierchain.dto.response.BonSortieResponseDTO;

public interface BonSortieService {

    BonSortieResponseDTO createBonSortie(BonSortieRequestDTO requestDTO);
}

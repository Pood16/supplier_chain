package org.tricol.supplierchain.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tricol.supplierchain.dto.request.BonSortieRequestDTO;
import org.tricol.supplierchain.dto.response.BonSortieResponseDTO;
import org.tricol.supplierchain.service.inter.BonSortieService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/bonSorties")
public class BonSortieController {

    private final BonSortieService bonSortieService;

    @PostMapping()
    public ResponseEntity<BonSortieResponseDTO> createBonSortie(@RequestBody @Valid BonSortieRequestDTO bonSortieRequestDTO) {

        BonSortieResponseDTO responseDTO = bonSortieService.createBonSortie(bonSortieRequestDTO);
        return ResponseEntity.ok(responseDTO);

    }
}

package org.tricol.supplierchain.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tricol.supplierchain.dto.request.BonSortieRequestDTO;
import org.tricol.supplierchain.dto.response.BonSortieResponseDTO;
import org.tricol.supplierchain.service.inter.BonSortieService;

import java.util.List;

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


    @GetMapping()
    public ResponseEntity<List<BonSortieResponseDTO>> getBonSorties() {
        List<BonSortieResponseDTO> bonSorties = bonSortieService.getBonSorties();
        return ResponseEntity.ok(bonSorties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BonSortieResponseDTO> getBonSortieById(@PathVariable Long id) {
        BonSortieResponseDTO bonSortie = bonSortieService.getBonSortieById(id);
        return ResponseEntity.ok(bonSortie);
    }
}


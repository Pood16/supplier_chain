package org.tricol.supplierchain.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tricol.supplierchain.dto.request.CommandeFournisseurCreateDTO;
import org.tricol.supplierchain.dto.response.CommandeFournisseurResponseDTO;
import org.tricol.supplierchain.service.inter.CommandeFournisseurService;

@RestController
@RequestMapping("/api/v1/commandes")
@RequiredArgsConstructor
public class CommandeFournisseurController {

    private final CommandeFournisseurService commandeFournisseurService;

    @PostMapping
    public ResponseEntity<CommandeFournisseurResponseDTO> createCommande(@Valid
                                                                         @RequestBody
                                                                         CommandeFournisseurCreateDTO createDTO){
        CommandeFournisseurResponseDTO response = commandeFournisseurService.createCommande(createDTO);
        return ResponseEntity.ok(response);
    }
}

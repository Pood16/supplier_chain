package org.tricol.supplierchain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tricol.supplierchain.dto.request.ProduitRequestDTO;
import org.tricol.supplierchain.dto.request.ProduitUpdatDTO;
import org.tricol.supplierchain.dto.response.ProduitResponseDTO;
import org.tricol.supplierchain.entity.Produit;
import org.tricol.supplierchain.exception.DuplicateResourceException;
import org.tricol.supplierchain.exception.ResourceNotFoundException;
import org.tricol.supplierchain.mapper.ProduitMapper;
import org.tricol.supplierchain.repository.ProduitRepository;
import org.tricol.supplierchain.service.inter.Produitservice;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProduitServiceImpl implements Produitservice {

    private final ProduitRepository produitRepository;
    private final ProduitMapper produitMapper;

    @Override
    public ProduitResponseDTO createProduit(ProduitRequestDTO produitRequestDTO) {
        Produit produit = produitMapper.toEntity(produitRequestDTO);
        if(produitRepository.existsByReference(produit.getReference())){
            throw new DuplicateResourceException("Produit avec reference "+ produit.getReference()+" existe déjà");
        }
        return produitMapper.toResponseDTO(produitRepository.save(produit));
    }

    @Override
    public ProduitResponseDTO modifierProduit(Long id, ProduitUpdatDTO produitUpdatDTO) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit avec id " + id + " n'existe pas"));
        produitMapper.updateEntityFromDto(produitUpdatDTO, produit);
        return produitMapper.toResponseDTO(produitRepository.save(produit));
    }

    @Override
    public ProduitResponseDTO getProduitById(Long id) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit avec id " + id + " n'existe pas"));
        return produitMapper.toResponseDTO(produit);
    }

    @Override
    public void deleteProduit(Long id) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit avec id " + id + " n'existe pas"));
        produitRepository.delete(produit);
    }

    @Override
    public List<ProduitResponseDTO> getAllProduits() {
        List<ProduitResponseDTO> produits = produitRepository.findAll().stream()
                .map(produitMapper::toResponseDTO)
                .toList();
        return produits;
    }
}

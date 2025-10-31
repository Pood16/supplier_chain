package org.tricol.supplierchain.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.tricol.supplierchain.dto.request.ProduitRequestDTO;
import org.tricol.supplierchain.dto.response.ProduitResponseDTO;
import org.tricol.supplierchain.entity.Produit;

@Mapper(componentModel = "spring")
public interface ProduitMapper {

    Produit toEntity(ProduitRequestDTO produitRequestDTO);
    ProduitResponseDTO toResponseDTO(Produit produit);

}

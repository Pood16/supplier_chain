package org.tricol.supplierchain.mapper;

import org.mapstruct.Mapper;
import org.tricol.supplierchain.dto.request.CommandeFournisseurRequestDTO;
import org.tricol.supplierchain.dto.response.CommandeFournisseurResponseDTO;
import org.tricol.supplierchain.entity.CommandeFournisseur;

@Mapper(componentModel = "spring", uses = {LigneCommandeMapper.class})
public interface CommandeFournisseurMapper {

    CommandeFournisseur toEntity(CommandeFournisseurRequestDTO dto);

    CommandeFournisseurResponseDTO toDto(CommandeFournisseur entity);

}
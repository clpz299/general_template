package com.general.template.core.assembler;

import org.mapstruct.MappingTarget;

import java.util.List;

public interface StandardAssembler<PO, DTO> {

    DTO toDTO(PO po);

    PO toPO(DTO dto);

    List<DTO> toDTOs(List<PO> po);

    List<PO> toPOs(List<DTO> dto);

    PO update(DTO dto, @MappingTarget PO po);

}

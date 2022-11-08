package com.general.template.assembler;

import com.general.template.auth.dto.SysOrganizationDTO;
import com.general.template.core.assembler.StandardAssembler;
import com.general.template.entity.SysOrganization;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysOrganizationAssembler extends StandardAssembler<SysOrganization, SysOrganizationDTO> {

}

package com.general.template.assembler;

import com.general.template.auth.dto.SysRoleDTO;
import com.general.template.core.assembler.StandardAssembler;
import com.general.template.entity.SysRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysRoleAssembler extends StandardAssembler<SysRole, SysRoleDTO> {

}

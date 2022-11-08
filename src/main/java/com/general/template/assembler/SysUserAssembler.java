package com.general.template.assembler;

import com.general.template.auth.dto.SysUserDTO;
import com.general.template.core.assembler.StandardAssembler;
import com.general.template.entity.SysUser;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface SysUserAssembler extends StandardAssembler<SysUser, SysUserDTO> {

}

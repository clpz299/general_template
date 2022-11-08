package com.general.template.assembler;

import com.general.template.auth.dto.SysMenuDTO;
import com.general.template.core.assembler.StandardAssembler;
import com.general.template.entity.SysMenu;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysMenuAssembler extends StandardAssembler<SysMenu, SysMenuDTO> {

}




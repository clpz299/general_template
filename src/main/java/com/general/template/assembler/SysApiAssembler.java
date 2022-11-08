package com.general.template.assembler;


import com.general.template.auth.dto.SysApiDTO;
import com.general.template.core.assembler.StandardAssembler;
import com.general.template.entity.SysApi;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysApiAssembler extends StandardAssembler<SysApi, SysApiDTO> {

}

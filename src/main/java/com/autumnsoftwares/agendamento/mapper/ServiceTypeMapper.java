package com.autumnsoftwares.agendamento.mapper;

import com.autumnsoftwares.agendamento.domain.services.ServiceType;
import com.autumnsoftwares.agendamento.domain.services.dto.ServiceTypeCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.services.dto.ServiceTypeResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceTypeMapper {

    @Mapping(target = "id", ignore = true)
    ServiceType toEntity(ServiceTypeCreateRequestDTO createRequestDTO);

    ServiceTypeResponseDTO toResponseDTO(ServiceType serviceType);
}

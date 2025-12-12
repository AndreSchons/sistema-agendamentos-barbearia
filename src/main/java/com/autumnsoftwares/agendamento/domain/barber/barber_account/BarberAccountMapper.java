package com.autumnsoftwares.agendamento.domain.barber.barber_account;

import com.autumnsoftwares.agendamento.domain.barber.barber_account.dto.BarberAccountCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.barber.barber_account.dto.BarberAccountResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BarberAccountMapper {

    BarberAccountResponseDTO toResponseDTO(BarberAccount account);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", source = "password")
    @Mapping(target = "role", ignore = true)    
    @Mapping(target = "authorities", ignore = true)
    BarberAccount toEntity(BarberAccountCreateRequestDTO createRequestDTO);
} 
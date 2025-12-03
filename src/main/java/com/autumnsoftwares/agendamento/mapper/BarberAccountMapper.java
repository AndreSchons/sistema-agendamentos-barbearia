package com.autumnsoftwares.agendamento.mapper;

import com.autumnsoftwares.agendamento.domain.barber.barber_account.BarberAccount;
import com.autumnsoftwares.agendamento.domain.barber.barber_account.dto.BarberAccountCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.barber.barber_account.dto.BarberAccountResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BarberAccountMapper {

    BarberAccountResponseDTO toResponseDTO(BarberAccount account);

    @Mapping(target = "passwordHash", source = "password")
    BarberAccount toEntity(BarberAccountCreateRequestDTO createRequestDTO);
} 
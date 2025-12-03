package com.autumnsoftwares.agendamento.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.autumnsoftwares.agendamento.domain.barber.dto.BarberCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.barber.Barber;
import com.autumnsoftwares.agendamento.domain.barber.dto.BarberResponseDTO;
import com.autumnsoftwares.agendamento.domain.barber.dto.BarberUpdateRequestDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BarberMapper {

    @Mapping(source = "account.email", target = "email")
    BarberResponseDTO toResponseDTO(Barber barber);

    Barber toEntity(BarberCreateRequestDTO requestDTO);

    List<BarberResponseDTO> toDTOList(List<Barber> barbers);

    Barber toEntity(BarberUpdateRequestDTO updateDTO);
}

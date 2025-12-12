package com.autumnsoftwares.agendamento.domain.barber;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.autumnsoftwares.agendamento.domain.barber.dto.BarberCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.barber.dto.BarberResponseDTO;
import com.autumnsoftwares.agendamento.domain.barber.dto.BarberUpdateRequestDTO;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BarberMapper {

    @Mapping(source = "account.email", target = "email")
    BarberResponseDTO toResponseDTO(Barber barber);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "barberShop", ignore = true)
    Barber toEntity(BarberCreateRequestDTO requestDTO);

    List<BarberResponseDTO> toDTOList(List<Barber> barbers);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "barberShop", ignore = true)
    Barber toEntity(BarberUpdateRequestDTO updateDTO);
}

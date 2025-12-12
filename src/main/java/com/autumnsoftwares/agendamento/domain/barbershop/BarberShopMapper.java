package com.autumnsoftwares.agendamento.domain.barbershop;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.autumnsoftwares.agendamento.domain.barber.BarberMapper;
import com.autumnsoftwares.agendamento.domain.barbershop.dto.BarberShopCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.barbershop.dto.BarberShopResponseDTO;

@Mapper(componentModel = "spring", uses = {BarberMapper.class})
public interface BarberShopMapper {

    BarberShopResponseDTO toResponseDTO(BarberShop barberShop);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "barbers", ignore = true)
    BarberShop toEntity(BarberShopCreateRequestDTO barberShopCreateRequestDTO);

    List<BarberShopResponseDTO> toResponseDTOList(List<BarberShop> barberShops);
}
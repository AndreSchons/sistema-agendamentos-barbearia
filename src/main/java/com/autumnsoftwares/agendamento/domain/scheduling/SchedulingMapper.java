package com.autumnsoftwares.agendamento.domain.scheduling;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.autumnsoftwares.agendamento.domain.barber.BarberMapper;
import com.autumnsoftwares.agendamento.domain.customer.CustomerMapper;
import com.autumnsoftwares.agendamento.domain.scheduling.dto.SchedulingCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.scheduling.dto.SchedulingResponseDTO;

@Mapper(componentModel = "spring", uses = {BarberMapper.class, CustomerMapper.class})
public interface SchedulingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "barber", ignore = true)
    @Mapping(target = "service", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "endTime", ignore = true)
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "status", ignore = true)
    Scheduling toEntity(SchedulingCreateRequestDTO schedulingCreateRequestDTO);

    @Mapping(source = "barber.name", target =  "barberName")
    @Mapping(source = "customer.name", target = "customerName")
    @Mapping(source = "service.name", target = "serviceName")
    SchedulingResponseDTO toResponseDTO(Scheduling scheduling);    

}

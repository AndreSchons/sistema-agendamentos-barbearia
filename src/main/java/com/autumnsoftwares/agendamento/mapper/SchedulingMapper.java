package com.autumnsoftwares.agendamento.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.autumnsoftwares.agendamento.domain.scheduling.Scheduling;
import com.autumnsoftwares.agendamento.domain.scheduling.dto.SchedulingCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.scheduling.dto.SchedulingResponseDTO;

@Mapper(componentModel = "spring")
public interface SchedulingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "barber", ignore = true)
    @Mapping(target = "service", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "endTime", ignore = true)
    @Mapping(target = "price", ignore = true)
    Scheduling toEntity(SchedulingCreateRequestDTO schedulingCreateRequestDTO);

    @Mapping(source = "barber.id", target = "barberId")
    @Mapping(source = "service.id", target = "serviceId")
    @Mapping(source = "customer.id", target = "customerId")
    SchedulingResponseDTO toResponseDTO(Scheduling scheduling);    

}

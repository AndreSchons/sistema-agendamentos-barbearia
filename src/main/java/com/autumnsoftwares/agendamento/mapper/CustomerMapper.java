package com.autumnsoftwares.agendamento.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.autumnsoftwares.agendamento.domain.customer.Customer;
import com.autumnsoftwares.agendamento.domain.customer.dto.CustomerCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.customer.dto.CustomerResponseDTO;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "schedulings", ignore = true)
    Customer toEntity(CustomerCreateRequestDTO customerCreateRequestDTO);
    
    CustomerResponseDTO toResponseDTO(Customer customer);
}

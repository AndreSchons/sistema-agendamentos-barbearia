package com.autumnsoftwares.agendamento.domain.customer.dto;

import com.autumnsoftwares.agendamento.domain.scheduling.Scheduling;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CustomerResponseDTO {

    private Integer id;
    private String name;
    private String phone;
    private List<Scheduling> schedulings = new ArrayList<>();

    public CustomerResponseDTO(Integer id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }
}

package com.autumnsoftwares.agendamento.domain.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CustomerUpdateRequestDTO {

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @NotBlank
    @Size(min = 11, max = 14)
    private String phone;

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}

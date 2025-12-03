package com.autumnsoftwares.agendamento.domain.barber.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BarberUpdateRequestDTO {
    
    @NotBlank
    @Size(min = 2, max = 100)
    private String name; 

    @NotBlank
    @Size(min = 11, max = 14)
    private String phone;

    public BarberUpdateRequestDTO(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    

}

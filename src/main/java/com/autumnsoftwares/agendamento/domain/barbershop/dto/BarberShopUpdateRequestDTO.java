package com.autumnsoftwares.agendamento.domain.barbershop.dto;

import jakarta.validation.constraints.NotBlank;

public class BarberShopUpdateRequestDTO {
    
    @NotBlank
    private String address;

    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    public BarberShopUpdateRequestDTO(String address, String name, String phone) {
        this.address = address;
        this.name = name;
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

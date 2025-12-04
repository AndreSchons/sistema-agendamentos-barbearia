package com.autumnsoftwares.agendamento.domain.barbershop.dto;


public class BarberShopResponseDTO {
    
    private Integer id;
    private String address;
    private String name;
    private String phone;

    public BarberShopResponseDTO(Integer id, String address, String name, String phone) {
         this.id = id;
         this.address = address;
         this.name = name;
         this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    
}

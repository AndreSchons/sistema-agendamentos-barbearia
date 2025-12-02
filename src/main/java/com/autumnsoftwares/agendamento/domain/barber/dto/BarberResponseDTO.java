package com.autumnsoftwares.agendamento.domain.barber.dto;

import com.autumnsoftwares.agendamento.domain.barbershop.BarberShop;

public class BarberResponseDTO {
    
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private BarberShop barberShop;

    public BarberResponseDTO(Integer id, String name, String email, String phone, BarberShop barberShop) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.barberShop = barberShop;
    }

    public Integer getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public BarberShop getBarberShop() {return barberShop;}
}

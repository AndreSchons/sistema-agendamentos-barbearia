package com.autumnsoftwares.agendamento.domain.barber.barber_account.dto;

public class BarberAccountResponseDTO {

    private Integer id;
    private String email;

    public BarberAccountResponseDTO(Integer id, String email) {
        this.id = id;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
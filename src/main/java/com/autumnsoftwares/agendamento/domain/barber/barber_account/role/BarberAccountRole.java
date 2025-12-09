package com.autumnsoftwares.agendamento.domain.barber.barber_account.role;

import lombok.Getter;

@Getter
public enum BarberAccountRole {

    ADMIN("admin"),
    USER("user");

    private final String role;

    BarberAccountRole(String role) {
        this.role = role;
    }
}

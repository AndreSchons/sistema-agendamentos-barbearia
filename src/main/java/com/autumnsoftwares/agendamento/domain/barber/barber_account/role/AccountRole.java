package com.autumnsoftwares.agendamento.domain.barber.barber_account.role;

import lombok.Getter;

@Getter
public enum AccountRole {

    ADMIN("admin"),
    USER("user");

    private final String role;

    AccountRole(String role) {
        this.role = role;
    }
}

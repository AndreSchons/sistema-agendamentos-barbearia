package com.autumnsoftwares.agendamento.domain.barbershop.dto;

import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BarberShopCreateRequestDTO {
    
    @NotBlank
    private String address;

    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    private LocalTime startTime;

    private LocalTime endTime;
}

package com.autumnsoftwares.agendamento.domain.barbershop;

import java.util.List;

import com.autumnsoftwares.agendamento.domain.barber.Barber;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "barber_shops")
public class BarberShop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(nullable = false)
    private String address;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String phone;

    @OneToMany(mappedBy = "barberShop")
    private List<Barber> barbers;

    public BarberShop(){}

    public BarberShop(String address, String name, String phone){
        this.address = address;
        this.name = name;
        this.phone = phone;
    }



}

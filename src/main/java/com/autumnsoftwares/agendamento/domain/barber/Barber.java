package com.autumnsoftwares.agendamento.domain.barber;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import com.autumnsoftwares.agendamento.domain.barber.barber_account.BarberAccount;
import jakarta.persistence.CascadeType;
import com.autumnsoftwares.agendamento.domain.barbershop.BarberShop;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "barbers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Barber{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(nullable = false)
    @Size(min = 2, max = 100)
    private String name;

    @NotBlank
    @Column(nullable = false, unique = true)
    @Size(min = 11, max = 14)
    private String phone;

    @OneToOne(optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "barber_account_id")
    private BarberAccount account;

    @ManyToOne(optional = false)
    @JsonBackReference
    @JoinColumn(name = "barber_shop_id")
    private BarberShop barberShop;
}

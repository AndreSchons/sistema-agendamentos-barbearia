package com.autumnsoftwares.agendamento.domain.barber;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.autumnsoftwares.agendamento.domain.barber.barber_account.BarberAccount;
import com.autumnsoftwares.agendamento.domain.barbershop.BarberShop;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne; // OneToOne é usado para a conta
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "barbers")
public class Barber {
    
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

    @OneToOne(optional = false)
    @JoinColumn(name = "barber_account_id")
    private BarberAccount account;

    @ManyToOne(optional = false)
    @JsonBackReference
    @JoinColumn(name = "barber_shop_id")
    private BarberShop barberShop;

    public  Barber(){}

    public Barber(String name, String phone, BarberAccount account, BarberShop barberShop){
        this.name = name;
        this.phone = phone;
        this.account = account;
        this.barberShop = barberShop;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public BarberAccount getAccount() {
        return account;
    }

    public void setAccount(BarberAccount account) {
        this.account = account;
    }

    public BarberShop getBarberShop() {
        return barberShop;
    }

    public void setBarberShop(BarberShop barberShop) {
        this.barberShop = barberShop;
    }
}

package com.autumnsoftwares.agendamento.domain.barbershop;

import java.time.LocalTime;
import java.util.List;
import com.autumnsoftwares.agendamento.domain.barber.Barber;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "barber_shops")
@NoArgsConstructor
@Getter
@Setter
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

    @NotNull
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @JsonManagedReference
    @OneToMany(mappedBy = "barberShop")
    private List<Barber> barbers;

    public BarberShop(String address, String name, String phone){
        this.address = address;
        this.name = name;
        this.phone = phone;
    }
}

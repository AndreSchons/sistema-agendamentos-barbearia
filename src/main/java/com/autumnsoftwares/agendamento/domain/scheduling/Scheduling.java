package com.autumnsoftwares.agendamento.domain.scheduling;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.autumnsoftwares.agendamento.domain.customer.Customer;
import com.autumnsoftwares.agendamento.domain.barber.Barber;
import com.autumnsoftwares.agendamento.domain.services_type.ServiceType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Scheduling {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "barber_id", nullable = false)
    private Barber barber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceType service;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SchedulingStatus status;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    public Scheduling(
        Barber barber,
        ServiceType serviceType,
        Customer customer,
        LocalDateTime startTime
        ){

        if(barber == null) throw new IllegalArgumentException("Barber cannot be null");
        if(serviceType == null) throw new IllegalArgumentException("ServiceType cannot be null");
        if(customer == null) throw new IllegalArgumentException("Customer cannot be null");
        if(startTime == null) throw new IllegalArgumentException("StartTime cannot be null");

        this.barber = barber;
        this.service = serviceType;
        this.price = serviceType.getPrice();
        this.customer = customer;
        this.status = SchedulingStatus.SCHEDULED;
        this.startTime = startTime;
        this.endTime = startTime.plusMinutes(serviceType.getDurationInMinutes());
    }
}
